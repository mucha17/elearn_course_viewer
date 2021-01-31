FROM maven:3.6-amazoncorretto-15 as builder
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src /build/src
RUN mvn clean package -Dactive.profile=prod

FROM openjdk:15-jdk-alpine
RUN addgroup -S apprunners && adduser -S apprunner -G apprunners
USER apprunner:apprunners

COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]