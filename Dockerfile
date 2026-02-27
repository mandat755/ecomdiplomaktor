# Використовуємо JDK 17
FROM eclipse-temurin:17-jdk-jammy AS build
COPY . /app
WORKDIR /app

# Даємо права та збираємо, ігноруючи можливі помилки скриптів
RUN chmod +x gradlew
RUN ./gradlew shadowJar --no-daemon --stacktrace

# Етап запуску
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Використовуємо більш точний шлях до результату збірки
COPY --from=build /app/build/libs/*-all.jar /app/app.jar
EXPOSE 8080
# Додаємо параметр порту для Netty
ENTRYPOINT ["java", "-Dktor.deployment.port=8080", "-jar", "/app/app.jar"]