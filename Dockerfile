# Sử dụng image chính thức của Maven làm base image
FROM maven:3.8.4-openjdk-17

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file pom.xml và các file cấu hình Maven khác (nếu có) vào thư mục làm việc
COPY pom.xml ./

# Tải xuống các dependency Maven cần thiết, nhưng không xây dựng ứng dụng
RUN mvn dependency:go-offline

# Sao chép mã nguồn của ứng dụng vào thư mục làm việc
COPY src ./src

# Xây dựng ứng dụng Maven (chạy các lệnh build)
RUN mvn package

# Chỉ định lệnh sẽ chạy khi container được khởi động
# Giả sử ứng dụng của bạn tạo ra file jar trong thư mục target
CMD ["java", "-jar", "target/your-app.jar"]
