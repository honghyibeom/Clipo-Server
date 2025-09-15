# 1단계: 빌드 (Gradle Wrapper 사용)
FROM gradle:jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 빌드 실행
RUN ./gradlew build --no-daemon

# 2단계: 실행
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
