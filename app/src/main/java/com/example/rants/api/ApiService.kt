package com.example.rants.api

import com.example.rants.model.AuthResponse
import com.example.rants.model.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>
}
