# Use the official OpenJDK base image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app
#VOLUME /hostpipe
# Copy the JAR file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Expose the port that your Spring Boot app runs on
EXPOSE 8080:8080


# Command to run the application
CMD ["java", "-jar", "demo.jar"]
