FROM openjdk:21-jdk-slim
MAINTAINER github.com/Sakerini

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
