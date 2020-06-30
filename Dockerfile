FROM openjdk:1.8
COPY build/libs/naumen-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8000
CMD java -jar app.jar
