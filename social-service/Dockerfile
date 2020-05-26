FROM adoptopenjdk/openjdk11:alpine-jre

# ${PORT}
VOLUME /tmp
ADD target/*.jar app.jar

EXPOSE 8200
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
