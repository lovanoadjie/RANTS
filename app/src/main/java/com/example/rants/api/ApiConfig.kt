package com.example.rants.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "http://192.168.208.91:8000/api/"
    private const val GALLERY_BASE_URL = "http://192.168.208.91:8000/api/"
    private const val PRODUCT_BASE_URL = "https://fakestoreapi.com/"


    fun getRetrofitInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.208.91:8000/api/")
            .client(client)  // Menambahkan client dengan interceptor
            .addConverterFactory(GsonConverterFactory.create())  // Converter JSON ke objek Kotlin
            .build()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getProducts(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PRODUCT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getGalleries(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GALLERY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



}
