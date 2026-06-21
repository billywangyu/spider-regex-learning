# 第一阶段：构建阶段（使用 Maven + JDK 11）
FROM maven:3.8.4-openjdk-11-slim AS build

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 并下载依赖（利用 Docker 缓存）
COPY pom.xml .
RUN mvn dependency:go-offline

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行阶段（仅使用 JRE）
FROM openjdk:11-jre-slim

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 jar 包
COPY --from=build /app/target/*.jar app.jar

# 暴露端口（Render 会通过环境变量 PORT 覆盖）
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]