# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app/products-service

# Copy and build
COPY ./pom.xml /app
COPY ./products-service/.mvn ./.mvn
COPY ./products-service/mvnw .
COPY ./products-service/pom.xml .
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline -B
COPY ./products-service/src ./src
RUN ./mvnw clean package -DskipTests -B
#COPY . .

# Stage 2: Runtime
# Without to create non-root user layer
#FROM gcr.io/distroless/java17-debian12
# Needs to create a non-root user layer
FROM bellsoft/liberica-runtime-container:jre-17-musl
#FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user layer
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copy the built JAR from the builder stage
COPY --from=builder /app/products-service/target/*.jar app.jar

# Expose the port the app runs on
#EXPOSE 800x

# Command to run the application
ENTRYPOINT ["java", "-XX:+ExitOnOutOfMemoryError", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
#ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
