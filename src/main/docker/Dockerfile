FROM adoptopenjdk/openjdk11:jre-11.0.16.1_1-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=10 \
    JAVA_OPTS=""

ADD *.war /app.war

EXPOSE 8081
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war
