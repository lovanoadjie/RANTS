package com.example.rants.api

import com.example.rants.ImageData
import com.example.rants.model.AuthResponse
import com.example.rants.model.Calendar
import com.example.rants.model.Gallery
import com.example.rants.model.GalleryResponse
import com.example.rants.model.LoginRequest
import com.example.rants.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @GET("acara/{tanggal}")
    fun getCalendars(@Path("tanggal") tanggal: String): Call<List<Calendar>>

    @GET("products")
    fun getProducts(): Call<List<Product>>

    @GET("galleries")
    fun getGalleries(): Call<GalleryResponse>
}
