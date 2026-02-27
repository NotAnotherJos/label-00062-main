package com.example.network

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import com.example.network.model.ApiResponse
import com.example.network.model.Post

interface ApiService {
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Post
}
