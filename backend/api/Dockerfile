FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY ../gradlew ../build.gradle.kts ../gradle.properties ../settings.gradle.kts ./
COPY ../gradle ./gradle

COPY ../api ./api
COPY ../common ./common

RUN chmod +x ./gradlew

RUN ./gradlew :api:build --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/api/build/libs/api-all.jar /app

EXPOSE 8080 8083

CMD ["java", "-jar", "api-all.jar"]