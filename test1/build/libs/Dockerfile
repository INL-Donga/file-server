# 1. 빌드 단계 (Gradle을 사용하여 프로젝트 빌드)
FROM gradle:7.6.0-jdk17 AS builder
WORKDIR /app

# 2. Gradle Wrapper 파일에 실행 권한 부여 및 프로젝트 소스를 복사
COPY --chown=gradle:gradle . /app

# Gradle Wrapper 실행 권한 설정
RUN chmod +x ./gradlew

# 3. Gradle 의존성 캐시를 활용하기 위해 빌드 전에 의존성만 먼저 다운로드
RUN ./gradlew dependencies --no-daemon

# 4. Gradle을 사용하여 프로젝트를 빌드하고 JAR 생성 (테스트 생략)
RUN ./gradlew build -x test --no-daemon

# 5. 런타임 단계 (최종 이미지를 위한 단계)
FROM openjdk:17-jdk-slim
WORKDIR /app

# 6. 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=builder /app/build/libs/test1-1.0-SNAPSHOT.jar /app/test1-1.0-SNAPSHOT.jar

# 7. 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app/test1-1.0-SNAPSHOT.jar"]

# 8. 서버는 8080 포트를 사용
EXPOSE 8080
