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
          app = docker.build("hongpark/health")
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