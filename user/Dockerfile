# Build Stage
FROM gradle:8.1.1-jdk17 AS build

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle 캐시를 활용하여 빌드 속도 개선
RUN gradle clean build --no-daemon -x test

# Run Stage
FROM openjdk:17-jdk-slim

VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

# 컨테이너 실행 시 사용할 포트 설정
EXPOSE 19092