FROM openjdk:17
ARG JAR_FILE=./target/execute-sql-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} shop.jar
ENTRYPOINT ["java","-jar","/shop.jar"]