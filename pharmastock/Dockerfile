# Étape 1 : Build de l'application avec Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copie d'abord uniquement le pom.xml pour mettre en cache les dépendances
COPY pom.xml .
# Copie le dossier src contenant ton code Java
COPY src ./src

# Lance la compilation
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]