FROM openjdk:17

LABEL mentainer="mili.arwa.ma@gmail.com"

WORKDIR /app
COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]