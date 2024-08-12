# Use a imagem do OpenJDK 21
FROM openjdk:21-alpine

# Copie o arquivo JAR para o contêiner
COPY build/libs/*.jar app.jar

# Exponha a porta 8080
EXPOSE 8080

# Defina o ponto de entrada para executar o JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
