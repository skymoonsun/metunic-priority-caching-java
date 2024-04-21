FROM openjdk:17 AS builder

COPY CacheContents.java /app/CacheContents.java

WORKDIR /app

RUN javac CacheContents.java

FROM openjdk:17

COPY --from=builder /app/CacheContents.class /app/CacheContents.class

WORKDIR /app

CMD ["java", "CacheContents"]
