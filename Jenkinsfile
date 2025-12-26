pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        DOCKER_IMAGE       = "teknik-servis-image"
        DOCKER_CONTAINER   = "teknik-servis-container"
        APP_PORT           = "8081"
        BASE_URL           = "http://localhost:8081"
        SELENIUM_HEADLESS  = "true"
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
                if not exist Dockerfile (
                  echo Dockerfile bulunamadi!
                  dir
                  exit /b 1
                )

                docker rm -f %DOCKER_CONTAINER% >NUL 2>NUL || echo No previous container

                netstat -aon | findstr :%APP_PORT% | findstr LISTENING >NUL
                if %ERRORLEVEL%==0 (
                  echo PORT %APP_PORT% dolu. Bosalt ve tekrar dene.
                  netstat -aon | findstr :%APP_PORT% | findstr LISTENING
                  exit /b 1
                )

                docker build -t %DOCKER_IMAGE% .

                docker run -d --rm -p %APP_PORT%:8081 --name %DOCKER_CONTAINER% %DOCKER_IMAGE%

                REM ---- HEALTHCHECK (PowerShell - stabil) ----
                powershell -NoProfile -ExecutionPolicy Bypass -Command ^
                  "$ErrorActionPreference='SilentlyContinue';" ^
                  "$urls=@('%BASE_URL%/actuator/health','%BASE_URL%/api/servis-kayitlari');" ^
                  "for($i=1;$i -le 40;$i++){" ^
                  "  foreach($u in $urls){" ^
                  "    try{ $r=Invoke-WebRequest -UseBasicParsing -Uri $u -TimeoutSec 2; if($r.StatusCode -ge 200 -and $r.StatusCode -lt 500){ Write-Host 'HEALTH OK:' $u; exit 0 } } catch {}" ^
                  "  }" ^
                  "  Start-Sleep -Seconds 2" ^
                  "}" ^
                  "exit 1"

                if %ERRORLEVEL% NEQ 0 (
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

        stage('6- Selenium (All Scenarios)') {
            steps {
                bat '''
                mvnw -B ^
                  -Dtest=com.example.teknikservis.selenium.Senaryo1SeleniumTest,com.example.teknikservis.selenium.Senaryo2SeleniumTest,com.example.teknikservis.selenium.Senaryo3SeleniumTest,com.example.teknikservis.selenium.Senaryo4SeleniumTest,com.example.teknikservis.selenium.Senaryo5SeleniumTest,com.example.teknikservis.selenium.Senaryo6SeleniumTest,com.example.teknikservis.selenium.Senaryo7SeleniumTest,com.example.teknikservis.selenium.Senaryo8SeleniumTest,com.example.teknikservis.selenium.Senaryo9SeleniumTest,com.example.teknikservis.selenium.Senaryo10SeleniumTest ^
                  -Dsurefire.reportsDirectory=target/surefire-reports-selenium ^
                  -DbaseUrl=%BASE_URL% ^
                  -Dheadless=%SELENIUM_HEADLESS% ^
                  test
                '''
            }
        }
    }

    post {
        always {
            // Raporlari garanti yakala
            junit testResults: 'target/**/TEST-*.xml',
                  allowEmptyResults: true

            archiveArtifacts artifacts: 'target/**/*.xml', allowEmptyArchive: true

            // log al -> sonra kapat
            bat '''
            echo ---- docker ps ----
            docker ps -a
            echo ---- docker logs ----
            docker logs %DOCKER_CONTAINER% 2>NUL || echo No container logs
            docker rm -f %DOCKER_CONTAINER% >NUL 2>NUL || exit /b 0
            '''
        }
    }
}
