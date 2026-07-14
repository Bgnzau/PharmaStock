# Étape 1 : Build de l'application avec Maven et Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copie d'abord uniquement le pom.xml
COPY pom.xml .
# Copie le dossier src
COPY src ./src

# Lance la compilation
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application avec Java 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]