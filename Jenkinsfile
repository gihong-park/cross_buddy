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
        withGradle() {
          echo 'Test Running'
          sh './gradlew test'
        }

      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          app = docker.build("hongpark/checkoutservice")
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

  }
}
