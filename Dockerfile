# Usamos la imagen de openjdk para construir la aplicación
FROM openjdk:21-jdk-slim AS build

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos los archivos del proyecto a la imagen
COPY . .

# Instalamos las dependencias y construimos el proyecto con Maven
RUN ./mvnw clean install

# Establecemos la imagen base para ejecutar la aplicación
FROM openjdk:21-jdk-slim

# Establecemos el directorio de trabajo para la ejecución
WORKDIR /app

# Copiamos el archivo JAR desde el contenedor de construcción
COPY --from=build /app/target/*.jar /app/app.jar

# Exponemos el puerto en el que se ejecuta la aplicación (usualmente 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
