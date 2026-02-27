# Stage 1: Build the application
# 使用 Gradle 官方镜像进行构建，支持多架构 (AMD64/ARM64)
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# 将项目文件复制到容器中
COPY --chown=gradle:gradle . .

# 执行构建，生成可运行的程序包
# installDist 会在 build/install/ 下生成包含启动脚本和依赖 jar 的目录
RUN gradle installDist --no-daemon

# Stage 2: Run the application
# 使用 Eclipse Temurin JRE 作为运行时基础镜像，体积小且支持多架构
FROM eclipse-temurin:17-jre

WORKDIR /app

# 从构建阶段复制编译产物
# 注意：路径中的 'ktorfit-koin-demo' 对应 settings.gradle.kts 中的 rootProject.name
COPY --from=builder /app/build/install/ktorfit-koin-demo/ .

# 赋予启动脚本执行权限
RUN chmod +x ./bin/ktorfit-koin-demo

# 设置启动命令
ENTRYPOINT ["./bin/ktorfit-koin-demo"]
