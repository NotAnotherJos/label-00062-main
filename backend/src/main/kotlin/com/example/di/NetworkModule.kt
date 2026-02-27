package com.example.di

import com.example.config.AppConfig
import com.example.network.ApiService
import com.example.network.interceptor.configureAuth
import com.example.network.interceptor.ResponseInterceptor.configureResponseInterceptor
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.slf4j.LoggerFactory

val networkModule = module {
    // 1. JSON Configuration
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
            encodeDefaults = true
        }
    }

    // 2. HttpClient Configuration (Core Engine)
    single {
        val logger = LoggerFactory.getLogger("KtorClient")
        
        HttpClient(CIO) {
            expectSuccess = true // Validate 2xx responses

            install(ContentNegotiation) {
                json(get())
            }
            
            install(Logging) {
                // 设置为 HEADERS 级别以记录请求和响应头信息
                // 设置为 BODY 级别可以记录请求和响应体（生产环境建议使用 INFO）
                level = LogLevel.HEADERS
                this.logger = object : Logger {
                    override fun log(message: String) {
                        // 使用 SLF4J 记录日志，符合项目规范
                        logger.info(message)
                    }
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 10000L
                socketTimeoutMillis = 15000L
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            }

            install(DefaultRequest) {
                configureAuth()
            }
            
            // 配置响应拦截器，用于记录响应信息和状态检查
            configureResponseInterceptor()
        }
    }

    // 3. Ktorfit Instance
    single {
        Ktorfit.Builder()
            .httpClient(get<HttpClient>())
            .baseUrl(AppConfig.BASE_URL)
            .build()
    }

    // 4. API Services
    single<ApiService> {
        get<Ktorfit>().create<ApiService>()
    }
}
