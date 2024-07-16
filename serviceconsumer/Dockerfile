FROM maven:latest AS builder
WORKDIR /serviceconsumer
COPY pom.xml /serviceconsumer/pom.xml
RUN mvn verify clean --fail-never
COPY src /serviceconsumer/src
RUN mvn clean package -DskipTests


FROM openjdk:21-jdk
WORKDIR /serviceconsumer
COPY --from=builder /serviceconsumer/target/*.jar app.jar
RUN echo $(ls -al .)
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","app.jar"]