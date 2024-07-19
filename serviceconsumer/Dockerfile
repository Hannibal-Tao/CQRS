FROM maven:latest AS builder
WORKDIR /serviceconsumer
COPY pom.xml /serviceconsumer/pom.xml
RUN mvn verify clean --fail-never
#RUN apt-get -y update && apt-get install -y mongodb-clients
COPY src /serviceconsumer/src
RUN mvn clean package -DskipTests


FROM openjdk:21-jdk
WORKDIR /serviceconsumer
COPY --from=builder /serviceconsumer/target/*.jar app.jar
RUN echo $(ls -al .)
EXPOSE 8080
ENV MONGO_HOST=localhost
ENV MONGO_PORT=27017
ENV MONGO_DATABASE=events
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","app.jar"]