FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw -DskipTests clean package
EXPOSE 8080
CMD ["sh","-c","java -jar -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prod target/*.jar"]

