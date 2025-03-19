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
            timeout(time: 10, unit: 'MINUTES') {
              waitForQualityGate true
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
                    sh '''
                    # Check if the container is already running and stop/remove it
                    if [ $(docker ps -q -f name=dadesadventures-container) ]; then
                        docker stop dadesadventures-container
                        docker rm dadesadventures-container
                    fi

                    # Check if an old container exists but is stopped, and remove it
                    if [ $(docker ps -aq -f name=dadesadventures-container) ]; then
                        docker rm dadesadventures-container
                    fi
                    '''

                    // Run the new container
                    sh 'docker run -d --name dadesadventures-container -p 8089:8080 dadesadventures:latest'
                }
            }
        }

        stage('done') {
            steps {
                script {
                    echo 'This Jenkins pipeline is done'
                }
            }
        }
    }
}
