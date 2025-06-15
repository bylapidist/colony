FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./gradlew :server:build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app .
ENTRYPOINT ["./gradlew", ":server:run", "--no-daemon"]
