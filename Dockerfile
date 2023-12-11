# Use a base image with Maven to build the project
FROM maven:3.6.0-jdk-11-slim AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package


# Create a new image with just the JRE and the JAR file
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/Project-0.0.1-SNAPSHOT.jar /usr/local/lib/Project-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/Project-0.0.1-SNAPSHOT.jar"]

