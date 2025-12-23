pipeline {
    agent any

    stages {
        stage('1- Checkout from GitHub') {
            steps {
                // Bu job zaten "Pipeline script from SCM" olduğu için checkout scm yeterli
                checkout scm
            }
        }

        stage('2- Build') {
            steps {
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('3- Unit Tests') {
            steps {
                bat 'mvn -B test -DskipUnitTests=false -Dtest=*UnitTest'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                bat 'mvn -B verify -DskipUnitTests=true'
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
                bat 'mvn -B test -Dtest=*SeleniumTest'
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
            // Container açık kalırsa durdur
            bat 'docker stop teknik-servis-container || exit /b 0'
        }
    }
}
