# Uso de imagen base con Java 17 (Coincide con <java.version>17</java.version> del pom)
FROM eclipse-temurin:17-jre

# Directorio donde se colocará la aplicación en el contenedor
WORKDIR /app

# Copiar el archivo jar del proyecto.
# IMPORTANTE: El nombre viene de artifactId (Cambridge-API) + version (0.0.1-SNAPSHOT)
COPY target/Cambridge-API-0.0.1-SNAPSHOT.jar /app/cambridge-api.jar

# Exponer el puerto estándar de Spring Boot (8080).
# Si configuras server.port en application.properties, cambia este número.
EXPOSE 8093

# Comando para ejecutar aplicación
CMD ["java","-jar","/app/cambridge-api.jar"]