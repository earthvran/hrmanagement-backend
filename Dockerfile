FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw -DskipTests clean package
ENV PORT 8080
CMD ["sh", "-c", "java -jar -Dserver.port=$PORT target/*.jar"]
