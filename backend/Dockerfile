# ===== Stage 1
FROM maven:3.8.5-openjdk-17 AS build-stage
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# ===== Stage 2
FROM openjdk:17-jdk-slim AS runtime-stage
WORKDIR /app
COPY --from=build-stage /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]