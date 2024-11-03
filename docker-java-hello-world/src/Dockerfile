# Základný obraz s nainštalovanou OpenJDK
FROM openjdk:17-alpine
# Nastavte pracovný adresár
WORKDIR /app
# Skopírujte Java súbor do kontajnera
COPY HelloWorld.java /app
# Skontrolujte a skompilujte HelloWorld.java
RUN javac HelloWorld.java
# Definujte príkaz, ktorý sa spustí pri štarte kontajnera
CMD ["java", "HelloWorld"]