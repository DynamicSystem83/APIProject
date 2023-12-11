# Use a base image with Maven to build the project
FROM maven:3.8.4-openjdk-11-slim AS build

# Working directory
WORKDIR /

# Copy the Maven project file and download dependencies
COPY ./pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and resources
COPY ./src src
COPY ./target target

# Build clean package
RUN mvn clean package -DskipTests

# Create a new image with just the JRE and the JAR file
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory
WORKDIR /app

# Copy the JAR file from the previous build stage
COPY --from=build ./target/Project-0.0.1-SNAPSHOT.jar Project-0.0.1-SNAPSHOT.jar

# Expose the port 8080 which the movie booking service will run on
EXPOSE 8080

# Command to run Spring Boot application
CMD ["java", "-jar", "Project-0.0.1-SNAPSHOT.jar"]
