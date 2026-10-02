pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 20, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    environment {
        IMAGE = 'dogtrack-backend'
    }

    stages {
        stage('Récupération du code') {
            steps {
                checkout scm
            }
        }

        stage('Compilation et tests') {
            steps {
                // *Test, *Tests et *IT : les tests d'intégration (AuthControllerIT) sont inclus
                sh 'mvn -B clean verify -Dtest="*Test,*Tests,*IT"'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Archivage du livrable') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Image Docker') {
            steps {
                sh 'docker build -t ${IMAGE}:${BUILD_NUMBER} -t ${IMAGE}:latest .'
            }
        }
    }

    post {
        success { echo 'Pipeline réussi : tests validés, archive et image Docker produites.' }
        failure { echo 'Pipeline en échec : consulter la sortie console et les rapports de tests.' }
    }
}
