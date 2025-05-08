# 1. Java 17 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. 빌드된 JAR 파일 복사 (Gradle 사용 시 build/libs)
COPY build/libs/vote-backend-0.0.1-SNAPSHOT.jar app.jar

# 4. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar", "--server.address=0.0.0.0"]
