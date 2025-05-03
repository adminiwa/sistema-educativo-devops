FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/matriculas-servicio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]