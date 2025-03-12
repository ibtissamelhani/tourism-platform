pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://sonarqube:9000'
        SONAR_PROJECT_KEY='DadesAdventures'
        SONAR_TOKEN = credentials('sonar_token')
        IMAGE_NAME = 'DadesAdventures'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url:'https://github.com/ibtissamelhani/tourism-platform.git'
            }
        }

        stage('Build') {
            steps {
                 sh '''
                    mvn clean package -DskipTests --batch-mode --errors -Dmaven.repo.local=.m2/repository
                 '''
            }
        }

        stage('Unit Tests') {
                    steps {
                        sh 'mvn test --batch-mode'
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
                          def qualityGate = sh(
                              script: """
                              curl -s -u "$SONAR_TOKEN:" \
                              "$SONARQUBE_URL/api/qualitygates/project_status?projectKey=$SONAR_PROJECT_KEY" \
                              | jq -r '.projectStatus.status'
                              """,
                              returnStdout: true
                          ).trim()
                          if (qualityGate != "OK") {
                              error "Quality Gate failed! Stopping the build."
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
         stage('Run Docker Container') {
                    steps {
                        script {
                            // Stop and remove any existing container with the same name
                            sh '''
                            if [ $(docker ps -aq -f name=${IMAGE_NAME}_container) ]; then
                                docker stop ${IMAGE_NAME}_container
                                docker rm ${IMAGE_NAME}_container
                            fi
                            '''
                            // Run the container
                            sh '''
                            docker run -d --name ${IMAGE_NAME}_container -p 8080:8080 ${IMAGE_NAME}:latest
                            '''
                        }
                    }
         }

    }
}