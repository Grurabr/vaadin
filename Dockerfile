FROM eclipse-temurin:21-jdk
VOLUME /tmp
COPY target/my-app-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
