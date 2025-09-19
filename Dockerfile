FROM ubuntu:22.04 AS build

# Define diretório de trabalho
WORKDIR /app

# Instala dependências
RUN apt-get update && apt-get install -y openjdk-17-jdk maven

# Copia apenas os arquivos necessários
COPY . .

# Build do projeto
RUN mvn clean install -DskipTests

# -----------------------------
FROM openjdk:17-jdk-slim

WORKDIR /app

EXPOSE 8080

# Copia apenas o JAR gerado
COPY --from=build /app/target/notasapi-0.0.1-SNAPSHOT.jar notasapi.jar

ENTRYPOINT ["java", "-jar", "notasapi.jar"]
