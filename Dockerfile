# Étape 1 : Build de l'application avec Maven
FROM maven:3.9-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]