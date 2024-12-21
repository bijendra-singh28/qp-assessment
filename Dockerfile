# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build artifact (your JAR file) into the container
COPY target/grocery-app-0.0.1-SNAPSHOT.jar /app/grocery-app.jar

# Expose the port the app will run on
EXPOSE 8080

# Define the command to run your JAR
ENTRYPOINT ["java", "-jar", "grocery-app.jar"]
