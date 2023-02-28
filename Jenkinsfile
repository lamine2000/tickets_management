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
        sh "pwd"
        sh "ls"
        //build docker image
        //sh "docker image build -t custom-jenkins-docker ./target/docker/"
        dockerImage = docker.build("lamine2000/tickets_management:${dockertag}", 'target/docker')
    }


    stage('Publish docker') {
        docker.withRegistry('https://registry.hub.docker.com', 'lamine-dockerhub') {
            dockerImage.push dockertag
            sh "docker rmi guiltech/marketplace:${dockertag}"
        }
    }

}