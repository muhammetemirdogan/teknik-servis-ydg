pipeline {
    agent any

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
                    ]]
                ])
            }
        }

        stage('2- Build') {
            steps {
                bat """
                mvnw -B -DskipTests clean package
                """
            }
        }

        stage('3- Unit Tests') {
            steps {
                bat """
                mvnw -B test
                """
            }
        }

        stage('4- Integration Tests') {
            when {
                // Şimdilik pasif, ileride aktif edeceğiz
                expression { return false }
            }
            steps {
                echo 'Buraya Integration test komutları gelecek (ileri aşama için)'
            }
        }

       stage('5- Docker Build & Run') {
           steps {
               bat """
               docker build -t %DOCKER_IMAGE% .
               docker run -d --rm -p 8080:8080 --name %DOCKER_CONTAINER% %DOCKER_IMAGE%
               """
           }
       }


        stage('6- Selenium System Tests') {
            when {
                // Selenium testleri de şimdilik pasif
                expression { return false }
            }
            steps {
                echo 'Buraya Selenium test komutları gelecek (ileri aşama için)'
            }
        }
    }

    post {
        always {
            // 1) Test raporlarını HER YERDEN ara
            // 2) Eğer nedense rapor bulamazsa bile build’i FAIL yapma
            junit allowEmptyResults: true, testResults: '**/surefire-reports/*.xml'

            // 3) Container çalışıyorsa durdur, yoksa hata verme
            bat """
            docker stop teknik-servis-container || exit /b 0
            """
        }
    }
}
