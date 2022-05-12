FROM amazoncorretto:17

WORKDIR /app

COPY . .

RUN ./mvnw install

VOLUME /tmp

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/vkstat-0.0.1-SNAPSHOT.jar"]