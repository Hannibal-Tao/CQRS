# Build stage
FROM maven:latest AS builder
WORKDIR /service-producer
COPY src /service-producer/src
COPY pom.xml /service-producer/pom.xml
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jdk
WORKDIR /service-producer
COPY --from=builder /service-producer/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]