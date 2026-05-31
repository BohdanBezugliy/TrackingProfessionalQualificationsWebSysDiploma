# --- ЕТАП 1: Збірка проєкту ---
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /build

RUN apk add --no-cache maven

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# --- ЕТАП 2: Запуск ---
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]