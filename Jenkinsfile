pipeline {
  agent any
  stages {
    stage('prepare') {
      steps {
        echo 'Clonning Repository'
        checkout scm
      }
    }

    stage('test') {
      steps {
        echo 'Test'
        sh './gradlew clean test'
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          app = docker.build("hongpark/cross-buddy")
        }

      }
    }

    stage('Push Docker Image') {
      steps {
        echo 'Push Image'
        script {
          docker.withRegistry('https://registry.hub.docker.com', 'docker-credential') {
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
          }
        }

      }
    }

    stage('Push Manifest File') {
      steps {
        echo 'Push Manifest File'
        git credentialsId: '$git-credential',
            url: '$GIT_REPO_URL',
            branch: '$BRANCH'

        withCredentials([gitUsernamePassword(credentialsId: 'git-credential', gitToolName: 'git-tool')]) {
            sh "helm template crossfit . --set images.tag=${env.BUILD_NUMBER} > ./kubernetes-manifests/kubernetes-manifests.yaml"
            sh 'kubernetes-manifests/kubernetes-manifests.yaml'
            sh "git commit -m '[UPDATE] checkoutservice ${env.BUILD_NUMBER} image versioning'"
            sh 'git push origin main'
        }
      }
    }

  }
}