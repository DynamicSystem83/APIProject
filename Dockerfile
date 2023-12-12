# Use a base image with Maven to build the project
#FROM maven:3.6.0-jdk-11-slim AS build

#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean package


# Create a new image with just the JRE and the JAR file
FROM openjdk:11

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files to the container
COPY pom.xml .
COPY src ./src/

# Build the Maven project
RUN mvn clean install


#COPY --from=build /home/app/target/Project-1.0.0-SNAPSHOT.jar /usr/local/lib/Project-1.0.0-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","target/Project-1.0.0-SNAPSHOT.jar"]

