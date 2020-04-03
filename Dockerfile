FROM maven:3.6.3-jdk-8 AS build
COPY . /build
WORKDIR /build
RUN mvn clean install -DskipTests


FROM openjdk:12-jdk-alpine
COPY --from=build /build/target/*.jar ./app.jar
ENTRYPOINT ["java","-jar","/app.jar"]