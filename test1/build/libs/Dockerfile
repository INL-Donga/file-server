# 1. 빌드 단계 (Gradle을 사용하여 프로젝트 빌드)
FROM gradle:7.6.0-jdk17 AS builder
WORKDIR /app

# 2. 프로젝트 소스를 컨테이너 내로 복사
COPY --chown=gradle:gradle . /app

# 3. Gradle을 사용하여 프로젝트를 빌드하고 JAR 생성 (테스트 생략)
RUN gradle build -x test

# 4. 런타임 단계 (최종 이미지를 위한 단계)
FROM openjdk:17-jdk-slim
WORKDIR /app

# 5. 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /app/build/libs/test1-1.0-SNAPSHOT.jar /app/test1-1.0-SNAPSHOT.jar

# 6. 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app/test1-1.0-SNAPSHOT.jar"]

# 7. 서버는 8080 포트를 사용
EXPOSE 8080
