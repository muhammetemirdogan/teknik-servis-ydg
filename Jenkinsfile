pipeline {
    agent any

    options {
        skipDefaultCheckout(true)   // changelog/whatchanged tetiklerini azaltır (pipeline içi)
        timestamps()
    }

    environment {
        DOCKER_IMAGE     = "teknik-servis-image"
        DOCKER_CONTAINER = "teknik-servis-container"
    }

    stages {

        stage('1- Checkout from GitHub') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/muhammetemirdogan/teknik-servis-ydg.git',
                        credentialsId: 'github-ydg-token'
                    ]],
                    changelog: false,
                    poll: false
                ])
            }
        }

        stage('2- Build') {
            steps {
                bat 'mvnw -B -DskipTests clean package'
            }
        }

        stage('3- Unit Tests') {
            steps {
                bat 'mvnw -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('4- Integration Tests') {
            steps {
                // IT koşarken data.sql'yi kapatıyoruz (aşağıda anlatıyorum)
                bat 'mvnw -B -Dtest=*IT -Dspring.sql.init.mode=never -Dspring.jpa.hibernate.ddl-auto=create-drop test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('5- Docker Build & Run') {
            steps {
                bat """
                docker build -t %DOCKER_IMAGE% .

                docker rm -f %DOCKER_CONTAINER% || echo "No previous container"

                docker run -d -p 8081:8081 --name %DOCKER_CONTAINER% %DOCKER_IMAGE%

                REM Uygulama ayaga kalkti mi kontrol (3-5 deneme)
                for /L %%i in (1,1,5) do (
                  curl.exe -s http://localhost:8081/api/servis-kayitlari && exit /b 0
                  timeout /t 2 >nul
                )
                echo "App did not become ready" & exit /b 1
                """
            }
        }

        stage('6- Selenium System Tests') {
            when {
                expression { return false } // ileride açacağız
            }
            steps {
                echo 'Buraya Selenium test komutlari gelecek'
            }
        }
    }

    post {
        always {
            // Container'i durdurup sil (yoksa hata verme)
            bat 'docker rm -f %DOCKER_CONTAINER% || exit /b 0'
        }
    }
}
