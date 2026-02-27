package com.example.network.interceptor

import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import com.example.config.AppConfig
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("AuthInterceptor")

/**
 * 配置认证拦截器
 * 为所有请求添加 Authorization header
 */
fun DefaultRequest.DefaultRequestBuilder.configureAuth() {
    logger.debug("Adding Authorization header to request")
    header("Authorization", "Bearer ${AppConfig.API_KEY}")
    // parameter("api_key", AppConfig.API_KEY) // Example: Injecting query param
}
