FROM ubuntu:latest AS build

WORKDIR /build

RUN apt-get update && \
    apt-get install -y openjdk-25-jdk maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY . .

RUN mvn clean install 

FROM openjdk:25-jdk-slim

WORKDIR /app

EXPOSE 8080

COPY --from=build /target/equipamentos-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]