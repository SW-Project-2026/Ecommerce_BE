FROM eclipse-temurin:21-jre
WORKDIR /app
COPY app.jar app.jar
ENTRYPOINT ["java", \
  "-Xms128m", "-Xmx350m", \
  "-XX:+UseG1GC", \
  "-XX:MaxMetaspaceSize=128m", \
  "-Dfile.encoding=UTF-8", \
  "-jar", "app.jar"]
