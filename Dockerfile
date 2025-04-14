# Etapa de build
FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /app

# Copia o código do backend
COPY . .

# Executa a build da aplicação Spring Boot, ignorando os testes
RUN mvn clean package -DskipTests

# Etapa final de produção
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o arquivo gerado da etapa de build
COPY --from=build /app/target/*.jar /app/app.jar

# Exposição da porta 8090
EXPOSE 8090

# Comando para iniciar a aplicação Spring Boot
CMD ["java", "-jar", "app.jar"]