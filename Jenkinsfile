pipeline {
    agent any

    stages {
        stage('1- Checkout from GitHub') {
            steps {
                // Eğer pipeline'ı 'Pipeline script from SCM' ile açarsan bu satır yeter:
                checkout scm

                // Alternatif:
                // git url: 'https://github.com/KENDI_KULLANICI_ADIN/teknik-servis-proje.git', branch: 'main'
            }
        }

        stage('2- Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('3- Unit Tests') {
            steps {
                sh 'mvn -B test -DskipUnitTests=false -Dtest=*UnitTest'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                sh 'mvn -B verify -DskipUnitTests=true'
            }
            post {
                always {
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }

        stage('5- Docker Build & Run') {
            steps {
                sh 'docker build -t teknik-servis-app .'
                sh 'docker run -d --rm -p 8081:8081 --name teknik-servis-container teknik-servis-app'
            }
        }

        stage('6- Selenium System Tests') {
            steps {
                // Çalışan sisteme karşı 3 senaryo
                sh 'mvn -B test -Dtest=*SeleniumTest'
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
            // Container açık kalmasın
            sh 'docker stop teknik-servis-container || true'
        }
    }
}
