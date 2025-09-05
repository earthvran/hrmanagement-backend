# ใช้ OpenJDK 17 แบบ slim (เบาและเร็ว)
FROM openjdk:17-jdk-slim

# ตั้ง working directory ใน container
WORKDIR /app

# copy โปรเจกต์ทั้งหมดเข้า container
COPY . .

# สร้าง JAR ด้วย Maven Wrapper
RUN ./mvnw -DskipTests clean package

# Render จะกำหนด PORT ให้ container ใช้
ENV PORT 8080

# รัน Spring Boot JAR (ใช้ profile prod)
CMD ["sh", "-c", "java -jar -Dspring.profiles.active=prod target/*.jar"]
