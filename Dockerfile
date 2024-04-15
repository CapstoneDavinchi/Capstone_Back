FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} davinchi-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/davinchi-0.0.1-SNAPSHOT.jar"]