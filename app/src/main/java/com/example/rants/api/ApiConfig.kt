package com.example.rants.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
<<<<<<< HEAD
    private const val BASE_URL = "http://192.168.137.24:8000/api/"
    private const val GALLERY_BASE_URL = "http://192.168.137.24:8000/api/"
    private const val KOSTUM_BASE_URL = "https:/192.168.137.24:8000/api/"
=======
    private const val BASE_URL = "http://192.168.137.128:8000/api/"
    fun  getImageUrl(): String{
>>>>>>> 20ddc8895f9f437224f304221c13169af316b00a

        return "http://192.168.137.128:8000/storage/"
    }    fun getRetrofitInstance(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
<<<<<<< HEAD
            .baseUrl("http://192.168.137.24:8000/api/")
=======
            .baseUrl(BASE_URL)
>>>>>>> 20ddc8895f9f437224f304221c13169af316b00a
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

    fun getMakeup(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getTari(): Retrofit {
        return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }

    fun getProducts(): Retrofit {
        return Retrofit.Builder()
<<<<<<< HEAD
            .baseUrl(KOSTUM_BASE_URL)
=======
            .baseUrl(BASE_URL)
>>>>>>> 20ddc8895f9f437224f304221c13169af316b00a
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getGalleries(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    private const val KOSTUM_BASE_URL = "http://192.168.137.24:8000/api/"

    // Logging Interceptor untuk Debugging
    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }

    // OkHttpClient untuk HTTP Request
    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    // Retrofit Instance untuk API
    val kostaApiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }



}
