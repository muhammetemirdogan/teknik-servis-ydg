pipeline {
    agent any

    stages {
        stage('1- Checkout from GitHub') {
            steps {
                checkout scm
            }
        }

        stage('2- Build') {
            steps {
                bat 'mvnw -B -DskipTests clean package'
            }
        }

        stage('3- Unit Tests') {
            steps {
                bat 'mvnw -B test -DskipUnitTests=false -Dtest=*UnitTest'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                bat 'mvnw -B verify -DskipUnitTests=true'
            }
            post {
                always {
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }

        stage('5- Docker Build & Run') {
            steps {
                bat 'docker build -t teknik-servis-app .'
                bat 'docker run -d --rm -p 8081:8081 --name teknik-servis-container teknik-servis-app'
            }
        }

        stage('6- Selenium System Tests') {
            steps {
                bat 'mvnw -B test -Dtest=*SeleniumTest'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            // Container açık kalmışsa, docker yoksa bile hata kodunu 0’a çekiyoruz
            bat 'docker stop teknik-servis-container || exit /b 0'
        }
    }
}
