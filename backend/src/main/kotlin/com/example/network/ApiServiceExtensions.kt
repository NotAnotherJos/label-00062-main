package com.example.network

import com.example.core.validation.InputValidator
import com.example.network.model.Post

/**
 * ApiService 扩展函数
 * 提供带输入校验的 API 调用方法
 */
suspend fun ApiService.getPostWithValidation(id: Int): Post {
    // 校验输入参数
    InputValidator.validateId(id, "id")
    return getPost(id)
}
