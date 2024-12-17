package com.example.rants.model

import com.google.gson.annotations.SerializedName
enum class Kategory{
    SD,SMP,SMA,Umum
}

class Makeup (
    @SerializedName ("id") val id: Int,
    @SerializedName ("image") val image: String,
    @SerializedName ("Kategory") val Kategory: Kategory,
    @SerializedName ("harga") val harga: Double
)

data class MakeupResponse(
    val data: List<Makeup> // List of Gallery objects
)
data class MakeupDetailResponse(
    val data: Makeup // List of Gallery objects
)