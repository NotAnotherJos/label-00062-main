# Kotlin 网络模块封装 (Ktorfit + Koin)

这是一个生产就绪的 Kotlin 网络模块，封装了 **Ktorfit** 和 **Koin**，提供类型安全的 HTTP 客户端、健壮的错误处理和灵活的配置。

## 题目要求

```
帮我用ktorfit网络框架封装一下，包含参数拦截，响应拦截，都基于ktorfit实现，并用koin注入
```

## How to run

使用 Docker Compose 一键启动所有服务：

```bash
# 构建并启动所有服务
docker-compose up -d --build

# 查看服务运行状态
docker-compose ps

# 查看服务日志
docker-compose logs -f

# 停止所有服务
docker-compose down

# 停止并删除数据卷（会清除数据库数据）
docker-compose down -v
```


## 特性

- **类型安全 API**: 使用 Ktorfit (Retrofit for Kotlin) 定义接口。
- **依赖注入**: 预配置的 Koin 模块 (`networkModule`)。
- **高级拦截**:
    - **AuthInterceptor**: 自动注入认证头。
    - **SafeApiCall**: 内置错误处理，将 HTTP 状态码映射为领域异常 (`ClientException`, `ServerException`)。
- **健壮性**:
    - 自动重试机制 (Exponential Backoff)。
    - 连接/请求超时配置。
    - `NetworkResult<T>` 封装 (Loading/Success/Error)。
    - `safeApiCall` 帮助函数，将挂起函数转换为 Flow。
- **测试**: 包含单元测试示例。

## 项目结构

```text
com.example
├── config          # 全局配置 (BaseUrl, API Key)
├── core            # 核心基础组件 (与业务无关)
│   ├── exception   # 自定义异常体系 (NetworkException, NoInternetException, etc.)
│   └── network
│       ├── adapter # 适配器 (SafeApiCall)
│       └── result  # 结果封装 (NetworkResult)
├── di              # Koin 依赖注入模块 (NetworkModule)
├── network         # 网络层具体实现
│   ├── interceptor # Ktor 拦截器 (AuthInterceptor)
│   ├── model       # 数据模型 (Post, ApiResponse)
│   └── ApiService  # Ktorfit 接口定义
└── Main.kt         # 演示入口
```

## 快速开始

### 1. 引入模块

将此模块包含在您的 Gradle 项目中。确保已添加 Ktorfit 插件。

### 2. 初始化 Koin

在应用启动时加载 `networkModule`：

```kotlin
startKoin {
    modules(networkModule)
}
```

### 3. 定义 API

```kotlin
interface ApiService {
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Post
}
```

### 4. 调用 API

推荐使用 `safeApiCall` 来处理加载状态和错误：

```kotlin
val apiService = get<ApiService>()

safeApiCall { 
    apiService.getPost(1) 
}.collect { result ->
    when (result) {
        is NetworkResult.Loading -> showLoading()
        is NetworkResult.Success -> showData(result.data)
        is NetworkResult.Error -> showError(result.message)
    }
}
```

## 配置

### 超时与重试
在 `NetworkModule.kt` 中修改：
- `HttpTimeout`: 默认连接 10s，请求 15s。
- `HttpRequestRetry`: 默认重试 3 次 (指数退避)。

### 错误处理
所有非 2xx 响应以及网络异常都会被 `safeApiCall` 自动捕获并映射为相应的领域异常（如 `ClientException`, `NoInternetException` 等），最终包装为 `NetworkResult.Error` 返回。

## 测试

运行单元测试：
```bash
./gradlew test
```
