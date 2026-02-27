package com.example.core.validation

import com.example.core.exception.ClientException

/**
 * 输入校验工具类
 * 提供常用的输入校验方法，确保 API 调用前参数的有效性
 */
object InputValidator {
    
    /**
     * 校验 ID 参数
     * @param id 要校验的 ID
     * @param paramName 参数名称（用于错误消息）
     * @throws ClientException 如果 ID 无效
     */
    fun validateId(id: Int, paramName: String = "id") {
        require(id > 0) { 
            throw ClientException(400, "$paramName must be a positive integer, got: $id")
        }
    }
    
    /**
     * 校验字符串参数不为空
     * @param value 要校验的字符串
     * @param paramName 参数名称（用于错误消息）
     * @throws ClientException 如果字符串为空或空白
     */
    fun validateNotBlank(value: String?, paramName: String = "value") {
        require(!value.isNullOrBlank()) {
            throw ClientException(400, "$paramName cannot be null or blank")
        }
    }
    
    /**
     * 校验字符串长度
     * @param value 要校验的字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @param paramName 参数名称（用于错误消息）
     * @throws ClientException 如果长度不符合要求
     */
    fun validateLength(
        value: String?,
        minLength: Int = 0,
        maxLength: Int = Int.MAX_VALUE,
        paramName: String = "value"
    ) {
        validateNotBlank(value, paramName)
        val length = value!!.length
        require(length >= minLength && length <= maxLength) {
            throw ClientException(
                400,
                "$paramName length must be between $minLength and $maxLength, got: $length"
            )
        }
    }
    
    /**
     * 校验数值范围
     * @param value 要校验的数值
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @param paramName 参数名称（用于错误消息）
     * @throws ClientException 如果值不在范围内
     */
    fun validateRange(value: Int, min: Int, max: Int, paramName: String = "value") {
        require(value >= min && value <= max) {
            throw ClientException(400, "$paramName must be between $min and $max, got: $value")
        }
    }
    
    /**
     * 校验列表不为空
     * @param list 要校验的列表
     * @param paramName 参数名称（用于错误消息）
     * @throws ClientException 如果列表为空
     */
    fun <T> validateNotEmpty(list: Collection<T>?, paramName: String = "list") {
        require(!list.isNullOrEmpty()) {
            throw ClientException(400, "$paramName cannot be null or empty")
        }
    }
    
    /**
     * 校验 URL 格式
     * @param url 要校验的 URL
     * @param paramName 参数名称（用于错误消息）
     * @throws ClientException 如果 URL 格式无效
     */
    fun validateUrl(url: String?, paramName: String = "url") {
        validateNotBlank(url, paramName)
        val urlPattern = Regex("^https?://.+")
        require(urlPattern.matches(url!!)) {
            throw ClientException(400, "$paramName must be a valid URL, got: $url")
        }
    }
}
