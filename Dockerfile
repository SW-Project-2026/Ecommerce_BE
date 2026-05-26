FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x gradlew && ./gradlew build -x test

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", \
  "-Xms128m", "-Xmx350m", \
  "-XX:+UseG1GC", \
  "-XX:MaxMetaspaceSize=128m", \
  "-Dfile.encoding=UTF-8", \
  "-jar", "app.jar"]
