FROM openjdk:17
ARG JAR_FILE=./target/site-0.0.2-SNAPSHOT.jar
COPY ${JAR_FILE} shop.jar
ENTRYPOINT ["java","-jar","/shop.jar"]