# Sistema Educativo con Microservicios y DevOps

Este proyecto implementa un sistema educativo distribuido mediante una arquitectura de microservicios con enfoque DevOps, que permite gestionar usuarios, asignaturas y matrículas.

## Arquitectura

El sistema se compone de los siguientes microservicios:

- **usuarios-servicio**: Gestión de usuarios con autenticación JWT
- **asignaturas-servicio**: Administración del catálogo de asignaturas
- **matriculas-servicio**: Gestión de matrículas con integración Feign
- **eureka-server**: Servicio de descubrimiento para registro y localización de microservicios
- **config-server**: Centralización de la configuración de todos los microservicios
- **Prometheus y Grafana**: Monitorización del sistema

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.2.1
- Spring Cloud
- MongoDB
- Docker y Docker Compose
- JWT para seguridad
- GitHub Actions para CI/CD
- Prometheus y Grafana para monitorización

## Prerequisitos

- Java 17 o superior
- Maven 3.6 o superior
- Docker y Docker Compose
- Git

## Estructura del Proyecto
sistema-educativo-devops/
├── README.md
├── usuarios-servicio/
├── asignaturas-servicio/
├── matriculas-servicio/
├── eureka-server/
├── config-server/
├── prometheus/
│   └── prometheus.yml
├── docker-compose.yml
└── .github/
└── workflows/
└── test.yml

## Instalación y Ejecución

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/adminiwa/sistema-educativo-devops.git
   cd sistema-educativo-devops

Compilar los microservicios:
bashcd usuarios-servicio && mvn clean package -DskipTests
cd ../asignaturas-servicio && mvn clean package -DskipTests
cd ../matriculas-servicio && mvn clean package -DskipTests
cd ../eureka-server && mvn clean package -DskipTests
cd ../config-server && mvn clean package -DskipTests
cd ..

Ejecutar con Docker Compose:
bashdocker-compose up -d

Acceder a los servicios:

Eureka: http://localhost:8761
API de Usuarios: http://localhost:8081
API de Asignaturas: http://localhost:8082
API de Matrículas: http://localhost:8083
Prometheus: http://localhost:9090
Grafana: http://localhost:3000 (usuario: admin, contraseña: admin)



Endpoints principales
Servicio de Usuarios

POST /api/auth/registro - Registrar nuevo usuario
POST /api/auth/login - Iniciar sesión y obtener token JWT
GET /api/usuarios - Listar usuarios (requiere autenticación)

Servicio de Asignaturas

GET /api/asignaturas - Listar todas las asignaturas
POST /api/asignaturas - Crear una nueva asignatura
GET /api/asignaturas/{id} - Obtener asignatura por ID
GET /api/asignaturas/nivel/{nivel} - Filtrar por nivel

Servicio de Matrículas

GET /api/matriculas - Listar todas las matrículas
POST /api/matriculas - Crear una nueva matrícula
GET /api/matriculas/estudiante/{estudianteId} - Filtrar por estudiante
GET /api/matriculas/asignatura/{asignaturaId} - Filtrar por asignatura

CI/CD con GitHub Actions
El proyecto incluye un pipeline de CI/CD configurado en GitHub Actions que ejecuta pruebas automáticas cuando se realiza un push al repositorio.
Monitorización

Prometheus: Recolecta métricas de todos los microservicios a través de Spring Boot Actuator.
Grafana: Visualiza las métricas recolectadas por Prometheus mediante dashboards personalizables.

Limitaciones conocidas

El servicio de usuarios presenta un problema de dependencia circular entre componentes que puede afectar a su funcionamiento.
Para obtener más información, consulta la sección "Problemas y desafíos" en la documentación del proyecto.

Contribución

Fork del repositorio
Crear una rama (git checkout -b feature/nueva-caracteristica)
Commit de los cambios (git commit -am 'Añadir nueva característica')
Push a la rama (git push origin feature/nueva-caracteristica)
Crear un Pull Request

Licencia
Este proyecto está licenciado bajo la Licencia MIT - ver el archivo LICENSE para más detalles.