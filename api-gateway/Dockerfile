# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app/api-gateway

# Copy and build
COPY ./pom.xml /app
COPY ./api-gateway/.mvn ./.mvn
COPY ./api-gateway/mvnw .
COPY ./api-gateway/pom.xml .
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline
COPY ./api-gateway/src ./src
RUN ./mvnw clean package -DskipTests
#COPY . .

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copy the built JAR from the builder stage
COPY --from=builder /app/api-gateway/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
