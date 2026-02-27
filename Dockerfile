# Етап 1: Збірка (використовуємо JDK 21)
FROM eclipse-temurin:21-jdk-jammy AS build
COPY . /app
WORKDIR /app

# Даємо права на виконання
RUN chmod +x gradlew

# Збірка з лімітом пам'яті для Gradle (щоб вписатися в ліміти Render)
RUN ./gradlew shadowJar --no-daemon -Dorg.gradle.jvmargs="-Xmx384m -XX:MaxMetaspaceSize=128m"

# Етап 2: Запуск (використовуємо JRE 21)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Оскільки rootProject.name = "EcomDiplomaKtor", копіюємо результат
COPY --from=build /app/build/libs/EcomDiplomaKtor-1.0-SNAPSHOT-all.jar /app/app.jar

EXPOSE 8080

# Запуск з лімітом пам'яті
ENTRYPOINT ["java", "-Xmx384m", "-jar", "/app/app.jar"]