FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw -DskipTests clean package

# ค่า fallback
ENV PORT 8080

# บอก container ว่าจะเปิด port นี้
EXPOSE 8080

# ใช้ค่า PORT ที่ Render ส่งมา
CMD ["sh", "-c", "java -jar target/*.jar --server.port=$PORT"]

