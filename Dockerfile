# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and project files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
COPY . .

# Make gradlew executable
RUN chmod +x gradlew

# Build the app
RUN ./gradlew build -x test

# Expose port
EXPOSE 9898

# Start the app
CMD ["java", "-jar", "build/libs/AuthApp-0.0.1-SNAPSHOT.jar"]
