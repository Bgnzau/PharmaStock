# Étape 1 : Build de l'application avec Maven
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# On build en sautant les tests pour aller plus vite lors du déploiement Docker
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/pharmastock-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]