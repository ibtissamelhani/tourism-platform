pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-username/your-repository.git'
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'This is a simple Jenkins pipeline test'
                }
            }
        }
    }
}
