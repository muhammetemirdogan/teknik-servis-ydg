pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        DOCKER_IMAGE     = "teknik-servis-image"
        DOCKER_CONTAINER = "teknik-servis-container"
        APP_PORT         = "8081"
        BASE_URL         = "http://localhost:8081"
        // Selenium tarafında okumak için (ister env'den ister -D ile)
        SELENIUM_HEADLESS = "true"
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
                bat '''
                mvnw -B -DskipTests clean package
                '''
            }
        }

        stage('3- Unit Tests') {
            steps {
                bat '''
                mvnw -B ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-unit ^
                  test
                '''
            }
        }

        stage('4- Integration Tests') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=*IT ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-it ^
                  -Dspring.sql.init.mode=never ^
                  -Dspring.jpa.hibernate.ddl-auto=create-drop ^
                  test
                '''
            }
        }

        stage('5- Docker Build & Run') {
            steps {
                bat '''
                REM Dockerfile var mi kontrol et
                if not exist Dockerfile (
                  echo Dockerfile bulunamadi! repo root'a ekleyip commit/push yap.
                  dir
                  exit /b 1
                )

                REM Eski container varsa sil
                docker rm -f %DOCKER_CONTAINER% >NUL 2>NUL || echo No previous container

                REM Port kullaniliyor mu kontrol et (OLDURME YOK)
                netstat -aon | findstr :%APP_PORT% | findstr LISTENING >NUL
                if %ERRORLEVEL%==0 (
                  echo PORT %APP_PORT% dolu gorunuyor. Jenkins node uzerinde bu portu bosalt.
                  netstat -aon | findstr :%APP_PORT% | findstr LISTENING
                  exit /b 1
                )

                REM Image build
                docker build -t %DOCKER_IMAGE% .

                REM Container run (container icinde 8081)
                docker run -d --rm -p %APP_PORT%:8081 --name %DOCKER_CONTAINER% %DOCKER_IMAGE%

                REM Uygulama ayaga kalkti mi kontrol (20 deneme)
                set HEALTH_OK=0

                for /L %%i in (1,1,20) do (
                  REM once actuator varsa onu dene
                  curl.exe -s -f "%BASE_URL%/actuator/health" >NUL 2>NUL && (
                    set HEALTH_OK=1
                    goto :healthdone
                  )
                  REM actuator yoksa API endpoint dene
                  curl.exe -s -f "%BASE_URL%/api/servis-kayitlari" >NUL 2>NUL && (
                    set HEALTH_OK=1
                    goto :healthdone
                  )
                  timeout /t 2 >NUL
                )

                :healthdone
                if "%HEALTH_OK%"=="0" (
                  echo Uygulama ayaga kalkmadi! (healthcheck fail)
                  echo ---- docker ps ----
                  docker ps -a
                  echo ---- docker logs ----
                  docker logs %DOCKER_CONTAINER%
                  exit /b 1
                )

                echo Uygulama ayakta: %BASE_URL%
                '''
            }
        }

        // ---- Selenium Senaryolari (max 10'a kadar puan) ----

        stage('6- Selenium Senaryo 1') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo1SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-s1 ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }

        stage('7- Selenium Senaryo 2') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo2SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-s2 ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }

        stage('8- Selenium Senaryo 3') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo3SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-s3 ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }

        stage('9- Selenium Senaryo 4') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo4SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-s4 ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }

        stage('10- Selenium Senaryo 5') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo5SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-s5 ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }

        stage('11- Selenium Senaryo 6') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo6SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-s6 ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }

        stage('12- Selenium Senaryo 7') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo7SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-s7 ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }
    }

    post {
        always {
            // Unit + IT + Selenium raporlari (hepsi target/surefire-reports-... icine gidiyor)
            junit testResults: 'target/surefire-reports-*/TEST-*.xml,target/failsafe-reports/*.xml',
                  allowEmptyResults: true

            // (Opsiyonel) rapor xml'lerini artifact olarak da sakla
            archiveArtifacts artifacts: 'target/surefire-reports-*/**/*.xml,target/failsafe-reports/**/*.xml', allowEmptyArchive: true

            // Container'i kapat (varsa)
            bat '''
            docker rm -f %DOCKER_CONTAINER% >NUL 2>NUL || exit /b 0
            '''
        }

        failure {
            // Fail olursa log basmak çok işe yarıyor
            bat '''
            echo ---- FAIL DEBUG: docker ps ----
            docker ps -a
            echo ---- FAIL DEBUG: docker logs ----
            docker logs %DOCKER_CONTAINER% 2>NUL || echo "No container logs"
            '''
        }
    }
}
