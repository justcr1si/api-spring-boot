FROM openjdk
WORKDIR /app
COPY target/project-0.0.1-SNAPSHOT.jar /app/backend.jar
LABEL author="Daniil Moskalenko"
ENTRYPOINT ["java", "jar", "backend.jar"]
