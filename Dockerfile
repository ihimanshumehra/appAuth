# Use OpenJDK 17 slim image
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

# Build the project
RUN ./gradlew build -x test

# Expose the port
# Render provides PORT env variable, fallback to 9898
ENV PORT 9898
EXPOSE $PORT

# Set environment variables for MySQL (FreeSQL)
ENV MYSQL_HOST=sql12.freesqldatabase.com
ENV MYSQL_PORT=3306
ENV MYSQL_DB=sql12800149
ENV MYSQL_USER=sql12800149
ENV MYSQL_PASSWORD=PJznT7VhCY

# Start the Spring Boot application
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar build/libs/AuthApp-0.0.1-SNAPSHOT.jar"]
