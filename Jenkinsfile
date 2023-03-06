#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    stage('check java') {
        sh "java -version"
    }

    def dockertag = 'latest'
    stage("Docker Tag"){
        gittags = sh(returnStdout: true, script: 'git tag -l --contains HEAD').trim()
        if (gittags?.trim()){
            echo " there are one tag : ${gittags}"
            dockerTag = gittags
        }
    }

    stage('clean') {
        sh "chmod +x ./mvnw"
        sh "./mvnw clean"
    }

    stage('packaging') {
        sh "./mvnw verify -Pprod -DskipTests"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }

    stage('build docker') {
        sh "cp -R src/main/docker target/"
        sh "cp target/*.war target/docker/"
        //sh "pwd"
        //sh "ls"
        //build docker image
        //sh "docker image build -t custom-jenkins-docker ./target/docker/"
        dockerImage = docker.build("localhost:5000/tickets_management:${dockertag}", 'target/docker')
    }


    stage('Publish docker') {
        docker.withRegistry('http://localhost:5000', 'lamine-dockerhub') {
            dockerImage.push dockertag
            sh "docker rmi localhost:5000/tickets_management:${dockertag}"
        }
    }

    stage('Deploy with ansible') {
        //sh "ls /usr/local/bin/"
        //sh "find / -type f -name ansible-playbook"
        //sh "whoami"
        //sh "cat /etc/passwd"
        //ansiblePlaybook become: true, disableHostKeyChecking: true, installation: 'ansible', inventory: 'deply/inventory', playbook: 'deply/playbook.yml'
        //sh "ansible-playbook -i deply/inventory.yml deply/playbook.yml"
        ansiblePlaybook installation: 'ansible', inventory: 'deply/inventory', playbook: 'deply/playbook.yml'
        //ansiblePlaybook disableHostKeyChecking: true, installation: 'ansible', inventory: 'deply/inventory', playbook: 'deply/docker-compose.yml'
    }
}
