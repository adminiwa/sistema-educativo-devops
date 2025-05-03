FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/usuarios-servicio-0.0.1-SNAPSHOT.jar app.jar

# Añadir configuraciones específicas para resolver el problema de dependencia circular
ENV SPRING_MAIN_ALLOW_CIRCULAR_REFERENCES=true
ENV SPRING_MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING=true

# Establecer un nivel de log más detallado para depuración
ENV LOGGING_LEVEL_ROOT=INFO
ENV LOGGING_LEVEL_ORG_SPRINGFRAMEWORK=DEBUG

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]