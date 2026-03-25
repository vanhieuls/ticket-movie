# syntax=docker/dockerfile:1

# Stage 1: Build stage
FROM maven:3.9.8-amazoncorretto-21 AS build
WORKDIR /app

# Copy pom.xml
COPY pom.xml .
# Copy source code
COPY src ./src
# Build application
RUN mvn clean package -DskipTests

# Stage 2: create Image
# start with Amazon Corretto JDK 21
FROM amazoncorretto:21.0.4
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
