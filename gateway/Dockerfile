# Build Stage
FROM gradle:8.11.1-jdk17 AS build

WORKDIR /tmp

COPY . .

RUN gradle clean build --no-daemon -x test --stacktrace

# Run Stage
FROM openjdk:17-jdk-slim

VOLUME /tmp

ARG JAR_FILE=/tmp/build/libs/*.jar

COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 19091
