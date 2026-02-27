package com.example.network.interceptor

import io.ktor.client.HttpClientConfig
import org.slf4j.LoggerFactory

/**
 * 响应拦截器：用于记录响应信息、检查响应状态、处理响应头等
 * 
 * 在 Ktor 3.0 中，响应拦截主要通过 Logging 插件实现。
 * Logging 插件已经提供了完整的请求和响应日志功能，包括：
 * - 请求方法、URL、头信息
 * - 响应状态码、头信息
 * - 请求和响应体（根据日志级别）
 * 
 * 在 NetworkModule 中，Logging 插件已配置为 HEADERS 级别，
 * 可以记录请求和响应的详细信息，满足响应拦截的需求。
 * 
 * 注意：如果需要更详细的响应拦截（如响应时间统计、自定义响应处理），
 * 可以在 safeApiCall 中实现，或者使用 Ktor 的响应验证功能。
 */
object ResponseInterceptor {
    private val logger = LoggerFactory.getLogger("ResponseInterceptor")
    
    /**
     * 配置响应拦截功能
     * 
     * 注意：实际的响应拦截功能由 NetworkModule 中的 Logging 插件提供。
     * Logging 插件已经配置为记录请求和响应信息，包括状态码、头信息等。
     * 
     * 这个函数保持 API 一致性，实际的响应日志由 Logging 插件处理。
     */
    fun HttpClientConfig<*>.configureResponseInterceptor() {
        // Logging 插件已经在 NetworkModule 中安装并配置
        // 它已经能够记录响应信息，包括：
        // - 响应状态码
        // - 响应头信息
        // - 响应体（如果日志级别设置为 BODY）
        
        // 如果需要额外的响应处理逻辑，可以在这里添加
        // 但基本的响应拦截功能已经由 Logging 插件提供
        logger.debug("Response interceptor configured (using Logging plugin)")
    }
}
