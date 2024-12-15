package com.example.rants.api

import com.example.rants.model.AuthResponse
import com.example.rants.model.Calendar
import com.example.rants.model.Gallery
import com.example.rants.model.GalleryResponse
import com.example.rants.model.LoginRequest
import com.example.rants.model.MakeupResponse
import com.example.rants.model.ProductResponse
import com.example.rants.model.TariResponse
import com.example.rants.model.kosta
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.*

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    @GET("acara/{tanggal}")
    fun getCalendars(@Path("tanggal") tanggal: String): Call<List<Calendar>>

    @GET("tari/all")
    fun getTari(): Call<TariResponse>

    @GET("makeup/all")
    fun getMakeup(): Call<MakeupResponse>

    @GET("kostum/all")
    fun getProducts(): Call<ProductResponse>

    @GET("galleries")
    fun getGalleries(): Call<GalleryResponse>

<<<<<<< HEAD
    // Mendapatkan semua kostum
    @GET("kostum/all")
    fun getKostum(): Call<List<Product>>

    // Mendapatkan detail kostum berdasarkan ID
    @GET("kostum/{id}")
    fun getKostumDetail(@Path("id") id: Int): Call<Product>

    // Menambahkan kostum baru
    @GET("kosta")
    fun addKostum(@Body kostum: Product): Call<Product>

    // Mengupdate kostum
    @PUT("kosta/{id}")
    fun updateKosta(@Path("id") id: Int, @Body kostum: Product): Call<Product>

    // Menghapus kostum
    @DELETE("kosta/{id}")
    fun deleteKosta(@Path("id") id: Int): Call<Void>

    // Mendapatkan semua kostum
    @GET("kosta")
    fun getKosta(): Call<List<Product>>

    // Mendapatkan detail kostum berdasarkan ID
    @GET("kosta/{id}")
    fun getKostaDetail(@Path("id") id: Int): Call<Product>

    // Menambahkan kostum baru
    @POST("kosta")
    fun addKosta(@Body kostum: Product): Call<Product>

    // Mengupdate kostum
    @PUT("kosta/{id}")
    fun updateKostum(@Path("id") id: Int, @Body kostum: Product): Call<Product>

    // Menghapus kostum
    @DELETE("kostum/{id}")
    fun deleteKostum(@Path("id") id: Int): Call<Void>
=======
>>>>>>> 20ddc8895f9f437224f304221c13169af316b00a
}
