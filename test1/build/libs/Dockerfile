# 1. 베이스 이미지로 OpenJDK 17을 사용
FROM openjdk:17-jdk-slim

# 2. 애플리케이션 JAR 파일을 컨테이너 내로 복사
WORKDIR /app
COPY ./test1-1.0-SNAPSHOT.jar /app/test1-1.0-SNAPSHOT.jar

# 3. 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app/test1-1.0-SNAPSHOT.jar"]

# 서버는 8080 포트를 사용
EXPOSE 8080