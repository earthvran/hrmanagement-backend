# ใช้ JDK 17 บน Debian Slim
FROM openjdk:17-jdk-slim

# ทำงานในโฟลเดอร์ /app
WORKDIR /app

# copy ทั้งโปรเจคเข้า container
COPY . .

# build JAR โดยไม่รัน test
RUN ./mvnw -DskipTests clean package

# default fallback port
ENV PORT 8080

# เปิดพอร์ตใน container (Render จะ map มาเป็น $PORT เอง)
EXPOSE 8080

# บังคับ Spring Boot ใช้ PORT จาก Render
CMD ["sh", "-c", "echo Running on PORT=$PORT && java -jar target/*.jar --spring.profiles.active=prod --server.port=$PORT"]

