pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/ibtissamelhani/tourism-platform.git'
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
