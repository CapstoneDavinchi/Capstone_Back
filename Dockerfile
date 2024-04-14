FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} docker-cicd-test-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/docker-cicd-test-0.0.1-SNAPSHOT.jar"]