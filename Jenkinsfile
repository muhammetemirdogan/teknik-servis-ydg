pipeline {
    agent any

    environment {
        // Jenkins'te oluşturduğun GitHub PAT credentials ID
        GIT_CREDENTIALS = 'github-ydg-token'
    }

    stages {
        stage('1- Checkout from GitHub') {
            steps {
                // Senin GitHub reposunu Jenkins'e çektiriyoruz
                git branch: 'main',
                    url: 'https://github.com/muhammetemirdogan/teknik-servis-proje.git',
                    credentialsId: env.GIT_CREDENTIALS
            }
        }

        stage('2- Build') {
            steps {
                // Testleri şimdilik atlayarak jar üret
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('3- Unit Tests') {
            steps {
                // Sadece *UnitTest sınıflarını çalıştır
                bat 'mvn -B test -DskipUnitTests=false -Dtest=*UnitTest'
            }
            post {
                always {
                    // JUnit raporlarını Jenkins’e yükle
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                // İleride integration test eklersek buradan çalışır (failsafe vs.)
                bat 'mvn -B verify -DskipUnitTests=true'
            }
            post {
                always {
                    // Failsafe raporları (varsa)
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }

        stage('5- Docker Build & Run') {
            steps {
                // Docker imajını üret
                bat 'docker build -t teknik-servis-app .'
                // Container'ı 8081 portunda ayağa kaldır
                bat 'docker run -d --rm -p 8081:8081 --name teknik-servis-container teknik-servis-app'
            }
        }

        stage('6- Selenium System Tests') {
            steps {
                // Çalışan sisteme karşı Selenium senaryolarını çalıştır
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
            // İş bittiğinde container ayakta kalmasın
            bat 'docker stop teknik-servis-container || echo "Container zaten kapaliydi"'
        }
    }
}
