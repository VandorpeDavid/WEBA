FROM maven:3-openjdk-11 as build
WORKDIR /workspace/app

COPY mvnw .
# COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN mvn package
RUN cp target/*.jar compiled.jar

FROM openjdk:11
ARG DEPENDENCY=/workspace/app
ARG SPRING_CONFIG_LOCATION=/app
COPY --from=build ${DEPENDENCY}/compiled.jar /app/compiled.jar
CMD ["java", "-jar", "-Dserver.forward-headers-strategy=native", "-Dspring.profiles.active=production", "/app/compiled.jar"]
