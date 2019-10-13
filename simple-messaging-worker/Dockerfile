# Jave base image
FROM openjdk:12-alpine

# Maintainer
MAINTAINER Sidney Simmons <sas.simm@gmail.com>

# Make port 8080 open to outside the container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=build/libs/simple-messaging-worker-0.0.1.jar

# Add the jar to the container
COPY ${JAR_FILE} simple-messaging-worker.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/simple-messaging-worker.jar"]