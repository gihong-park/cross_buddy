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
        git credentialsId: 'git-credential',
            url: 'https://github.com/gihong-park/cross_buddy_helm.git',
            branch: 'main'

        withCredentials([gitUsernamePassword(credentialsId: 'git-credential', gitToolName: 'Default')]) {
          sh "git config local user.email dev.gihong2012@gmail.com"
          sh "git config local user.name gihong-park"
          sh "helm template crossfit . --set images.tag=${env.BUILD_NUMBER} > ./kubernetes-manifests/kubernetes-manifests.yaml"
          sh 'git add kubernetes-manifests/kubernetes-manifests.yaml'
          sh "git commit -m '[UPDATE] cross-buddy ${env.BUILD_NUMBER} image versioning'"
          sh 'git push origin main'
        }
      }
    }

  }
}