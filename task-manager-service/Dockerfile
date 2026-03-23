FROM maven:3.9.9-eclipse-temurin-21-alpine AS maven
WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn clean package

FROM amazoncorretto:21.0.10-al2023
WORKDIR /app
COPY --from=maven /app/target/task-manager-0.0.1-SNAPSHOT.jar /app/task-manager-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/app/task-manager-0.0.1-SNAPSHOT.jar"]