FROM openjdk:17
ADD target/doctor-service-0.0.1-SNAPSHOT.jar doctor-service-0.0.1-SNAPSHOT.jar
EXPOSE 8002
ENTRYPOINT ["java" ,"-jar", "doctor-service-0.0.1-SNAPSHOT.jar"]