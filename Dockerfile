FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw -DskipTests clean package
ENV PORT 8080
EXPOSE 8080
CMD ["sh", "-c", "java -jar -Dspring.profiles.active=prod target/*.jar"]
