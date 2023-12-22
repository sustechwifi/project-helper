FROM openjdk:17-jdk

WORKDIR /app

# 将打包好的 Spring Boot JAR 文件复制到镜像中的工作目录, 视情况删除 target/
COPY ./mainservice-0.0.1-SNAPSHOT.jar ./app.jar

# 设置环境变量指定运行端口（例如 8080）
ENV PORT=8070

# 对外开放运行端口
EXPOSE $PORT

# 启动应用
CMD ["java", "-jar", "app.jar"]
