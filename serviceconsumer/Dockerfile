FROM maven:latest AS builder
WORKDIR /serviceconsumer
COPY src /serviceconsumer/src
COPY pom.xml /serviceconsumer/pom.xml
RUN mvn clean package -P docker


FROM openjdk:17-jdk-slim
WORKDIR /serviceconsumer
COPY --from=builder /serviceconsumer/target/*.jar app.jar
RUN echo $(ls -al .)
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]