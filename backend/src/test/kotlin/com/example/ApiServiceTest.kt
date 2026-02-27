package com.example.network

import com.example.network.model.Post
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.internal.KtorfitService
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApiServiceTest {

    @Test
    fun `test getPost success`() = runBlocking {
        // Given
        val mockPost = Post(1, 1, "Test Title", "Test Body")
        val apiService = mockk<ApiService>()
        
        coEvery { apiService.getPost(1) } returns mockPost

        // When
        val result = apiService.getPost(1)

        // Then
        assertEquals(1, result.id)
        assertEquals("Test Title", result.title)
    }
}
