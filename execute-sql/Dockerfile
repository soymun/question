FROM maven:3.9.8-amazoncorretto-17-debian AS build
COPY . .
RUN mvn -DskipTests=true clean package

FROM openjdk:17
COPY --from=build /target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]