pipeline {
    agent any

    environment {
        SONARQUBE = 'sonarqube'
        SONARQUBE_URL = 'http://sonarqube:9000'
        SONAR_PROJECT_KEY='DadesAdventures'
        SONAR_TOKEN = credentials('sonar-token')
        IMAGE_NAME = 'dadesadventures'
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github-token', branch: 'main', url: 'https://github.com/ibtissamelhani/tourism-platform.git'
            }
        }
        stage('Check Maven') {
    steps {
        sh 'mvn -v'
    }
}
        stage('Build and Test') {
            steps {
                script {
                    // Run Maven tests
                    sh 'mvn test --batch-mode'
                }
            }
            post {
                        always {
                            junit '**/target/surefire-reports/*.xml'
                            jacoco(
                                execPattern: '**/target/*.exec',
                                classPattern: '**/target/classes',
                                sourcePattern: '**/src/main/java'
                            )
                        }
                    }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    try {
                         withSonarQubeEnv('sonarqube') {
                             sh '''
                                 mvn clean verify sonar:sonar \
                                     -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                                     -Dsonar.projectName="DadesAdventures" \
                                     -Dsonar.host.url=$SONARQUBE_URL \
                                     -Dsonar.login=$SONAR_TOKEN
                             '''
                         }
                    } catch (Exception e) {
                        error "SonarQube analysis failed: ${e.message}"
                    }
                }
            }
        }
        stage('Quality Gate') {
    steps {
        script {
            timeout(time: 3, unit: 'MINUTES') {  // Attendre que l'analyse soit terminée
                def qualityGate = waitForQualityGate()
                if (qualityGate.status != 'OK') {
                    error "Quality Gate failed: ${qualityGate.status}"
                }
            }
        }
    }
}

stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${IMAGE_NAME}:latest .'
                }
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
