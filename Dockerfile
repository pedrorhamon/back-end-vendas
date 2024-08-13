# Use a imagem do OpenJDK 21
FROM alpine/java:21-jdk

# Copie o arquivo JAR para o contêiner
COPY target/*.jar app.jar

# Exponha a porta 8080
EXPOSE 8080

# Defina o ponto de entrada para executar o JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
