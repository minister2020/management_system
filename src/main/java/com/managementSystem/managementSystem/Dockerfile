# Use a base image with JDK
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar file from target folder (assumes you have already built the jar)
COPY target/management-system-0.0.1-SNAPSHOT.jar app.jar

# Expose port (optional, for local dev)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
