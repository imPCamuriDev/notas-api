FROM openjdk:17-jdk-slim

# Instala o Maven dentro do container
RUN apt-get update && apt-get install -y maven

# Se não existir, /app é criado
WORKDIR /app

# Copia todo o projeto para dentro de /app
COPY . .

# Faz a build e executa (instala os dependencias)
RUN mvn clean package -DskipTests

# Verificar se o JAR foi gerado corretamente
RUN ls -l target/

# Expõe o projeto JAVA para a porta 8080
EXPOSE 8080

CMD ["java", "-jar", "target/notasapi-0.0.1-SNAPSHOT.jar"]