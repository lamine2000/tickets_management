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

    stage('unit tests') {
        sh "./mvnw -ntp -Dskip.installnodenpm -Dskip.npm verify --batch-mode -Dlogging.level.ROOT=OFF -Dlogging.level.org.zalando=OFF -Dlogging.level.tech.jhipster=OFF -Dlogging.level.sn.trivial.ticket=OFF -Dlogging.level.org.springframework=OFF -Dlogging.level.org.springframework.web=OFF -Dlogging.level.org.springframework.security=OFF"
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
        dockerImage = docker.build("localhost:5000/tickets_management:${dockertag}", 'target/docker')
    }


    stage('Publish docker') {
        docker.withRegistry('http://localhost:5000', 'lamine-dockerhub') {
            dockerImage.push dockertag
            sh "docker rmi localhost:5000/tickets_management:${dockertag}"
        }
    }

}