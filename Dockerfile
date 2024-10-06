FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]