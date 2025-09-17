# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app/orders-service

# Copy and build
COPY ./pom.xml /app
COPY ./orders-service/.mvn ./.mvn
COPY ./orders-service/mvnw .
COPY ./orders-service/pom.xml .
RUN ./mvnw dependency:go-offline -B
COPY ./orders-service/src ./src
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Runtime
FROM bellsoft/liberica-runtime-container:jre-17-musl

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copy the built JAR from the builder stage
COPY --from=builder /app/orders-service/target/*.jar app.jar

# Expose the port the app runs on
#EXPOSE 800x

# Command to run the application
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
