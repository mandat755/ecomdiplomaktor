# Етап 1: Збірка проекту
FROM eclipse-temurin:17-jdk-jammy AS build
COPY . /app
WORKDIR /app
# Даємо права на виконання грайдлу і збираємо "товстий" JAR
RUN chmod +x gradlew
RUN ./gradlew shadowJar --no-daemon

# Етап 2: Запуск
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Копіюємо JAR файл із першого етапу (назва має збігатися з твоєю)
COPY --from=build /app/build/libs/*-all.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
