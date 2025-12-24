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
                expression { return false } // ileride açarız
            }
            steps {
                echo 'Buraya integration test komutlari gelecek (ileri asama icin)'
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
                expression { return false } // ileride açarız
            }
            steps {
                echo 'Buraya Selenium test komutlari gelecek (ileri asama icin)'
            }
        }
    }

    post {
        always {
            // Test raporlarini bulursa okusun, bulamazsa da build FAIL yapma
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true

            // Container calisiyorsa durdur, yoksa hata verme
            bat """
            docker stop %DOCKER_CONTAINER% || exit /b 0
            """
        }
    }
}
