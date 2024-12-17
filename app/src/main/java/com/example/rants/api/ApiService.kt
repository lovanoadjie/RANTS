package com.example.rants.api

import com.example.rants.model.AuthResponse
import com.example.rants.model.Calendar
//import com.example.rants.model.DetailResponse
import com.example.rants.model.Gallery
import com.example.rants.model.GalleryResponse
import com.example.rants.model.LoginRequest
import com.example.rants.model.Makeup
import com.example.rants.model.MakeupDetailResponse
import com.example.rants.model.MakeupResponse
import com.example.rants.model.ProductDetailResponse
import com.example.rants.model.ProductResponse
import com.example.rants.model.RegisterRequest
import com.example.rants.model.TariDetailResponse
import com.example.rants.model.TariResponse
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

    @GET("tari/all")
    fun getTari(): Call<TariResponse>

    @GET("tari/{id}")
    fun getTariById(@Path("id")tariId: Int): Call<TariDetailResponse>

    @GET("makeup/all")
    fun getMakeup(): Call<MakeupResponse>

    @GET("makeup/{id}")
    fun getMakeupById(@Path("id") makeupId: Int): Call<MakeupDetailResponse>


    @GET("kostum/all")
    fun getProducts(): Call<ProductResponse>

    @GET("kostum/{id}")
    fun getProductById(@Path("id") productId: Int): Call<ProductDetailResponse>

    @GET("galleries")
    fun getGalleries(): Call<GalleryResponse>

    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<AuthResponse>

}

