package com.example.rants.api

import com.example.rants.model.AuthResponse
import com.example.rants.model.Calendar
import com.example.rants.model.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @GET("calendars") // Endpoint Laravel untuk mendapatkan semua data kalender
    fun getCalendars(): Call<List<Calendar>>

//    @GET
}
