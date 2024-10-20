pipeline {
    agent any

    environment {
        // Nome do servidor configurado no Jenkins (Etapa 2)
        SONARQUBE_SERVER = 'SonarQube'
    }

    stages {
        stage('Build') {
            steps {
                // Compilar o projeto (utilizando Maven)
                sh 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(SONARQUBE_SERVER) {
                    // Executa a análise do SonarQube
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    // Aguarda o resultado do Quality Gate do SonarQube
                    timeout(time: 1, unit: 'HOURS') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }
    }
}
