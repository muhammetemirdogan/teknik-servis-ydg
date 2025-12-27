pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    // ÖNEMLİ: Bu isim, Manage Jenkins -> Tools -> Git installations'da verdiğin isimle birebir aynı olmalı
    tools {
        git 'git-2.48'

    }

    environment {
        DOCKER_IMAGE      = "teknik-servis-image"
        DOCKER_CONTAINER  = "teknik-servis-container"
        APP_PORT          = "8081"
        BASE_URL          = "http://localhost:8081"
        SELENIUM_HEADLESS = "true"
    }

    stages {

        stage('0- Debug Git (Jenkins sees)') {
            steps {
                bat '''
                echo ==== GIT DEBUG ====
                where git
                git --version
                echo ===================
                '''
            }
        }

        stage('1- Checkout from GitHub') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/muhammetemirdogan/teknik-servis-ydg.git',
                        credentialsId: 'github-ydg-token'
                    ]],
                    // Jenkins'in changelog hesaplamak için whatchanged koşturmasını azaltmak için:
                    // (tam garanti değil ama faydalı)
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [
                        [$class: 'CleanBeforeCheckout']
                    ]
                ])
            }
        }

        stage('2- Build') {
            steps {
                bat 'call mvnw -B -DskipTests clean package'
            }
        }

        stage('3- Unit Tests') {
            steps {
                bat 'call mvnw -B -Dsurefire.reportsDirectory=target/surefire-reports-unit test'
            }
        }

        stage('4- Integration Tests') {
            steps {
                bat 'call mvnw -B -Dtest=*IT -Dsurefire.reportsDirectory=target/surefire-reports-it -Dspring.sql.init.mode=never -Dspring.jpa.hibernate.ddl-auto=create-drop test'
            }
        }

        stage('5- Docker Build & Run') {
            steps {
                bat '''
                setlocal EnableExtensions

                if not exist Dockerfile (
                  echo Dockerfile bulunamadi
                  dir
                  exit /b 1
                )

                docker rm -f %DOCKER_CONTAINER% >NUL 2>NUL

                netstat -aon | findstr :%APP_PORT% | findstr LISTENING >NUL
                if %ERRORLEVEL%==0 (
                  echo PORT %APP_PORT% dolu
                  netstat -aon | findstr :%APP_PORT% | findstr LISTENING
                  exit /b 1
                )

                docker build -t %DOCKER_IMAGE% .

                docker run -d --rm -p %APP_PORT%:8081 --name %DOCKER_CONTAINER% %DOCKER_IMAGE%

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
                  echo Uygulama ayaga kalkmadi - healthcheck fail
                  echo ---- docker ps ----
                  docker ps -a
                  echo ---- docker logs ----
                  docker logs %DOCKER_CONTAINER%
                  exit /b 1
                )

                echo Uygulama ayakta: %BASE_URL%
                endlocal
                exit /b 0
                '''
            }
        }

        stage('6- Selenium (All Scenarios)') {
            steps {
                bat 'call mvnw -B -Dtest=com.example.teknikservis.selenium.Senaryo*SeleniumTest -Dsurefire.reportsDirectory=target/surefire-reports-selenium -DbaseUrl=%BASE_URL% -Dheadless=%SELENIUM_HEADLESS% test'
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports-*/TEST-*.xml,target/failsafe-reports/*.xml',
                  allowEmptyResults: true

            archiveArtifacts artifacts: 'target/surefire-reports-*/**/*.xml,target/failsafe-reports/**/*.xml',
                             allowEmptyArchive: true
        }

        failure {
            // Bu blok build'i tekrar fail etmesin
            bat '''
            echo ---- FAIL DEBUG: docker ps ----
            docker ps -a
            echo ---- FAIL DEBUG: docker logs ----
            docker logs %DOCKER_CONTAINER% 2>NUL || echo No container logs
            exit /b 0
            '''
        }

        cleanup {
            bat 'docker rm -f %DOCKER_CONTAINER% >NUL 2>NUL || exit /b 0'
        }
    }
}
