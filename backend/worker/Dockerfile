FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY ../gradlew ../build.gradle.kts ../gradle.properties ../settings.gradle.kts ./
COPY ../gradle ./gradle

COPY ../worker ./worker
COPY ../common ./common

RUN chmod +x ./gradlew

RUN ./gradlew :worker:build --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update && apt-get install -y docker.io && apt-get install docker-buildx

COPY --from=build /app/worker/build/libs/worker-all.jar /app

ENV RABBITMQ_HOST=run-rabbitmq
ENV EXECUTION_SOCKET_HOST=run-api

EXPOSE 8081 8083

CMD ["java", "-jar", "worker-all.jar"]