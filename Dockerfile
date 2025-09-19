FROM ubuntu:22.04 AS build

# Instala o Maven dentro do container
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/notasapi-0.0.1-SNAPSHOT.jar notasapi.jar
ENTRYPOINT [ "java", "-jar", "notasapi.jar" ]
