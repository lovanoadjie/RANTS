package com.example.rants.model

import com.google.gson.annotations.SerializedName

class Tari (
    @SerializedName ("id") val id: Int,
    @SerializedName ("jenis_tarian") val jenis_tarian: String,
    @SerializedName ("image") val image: String,
    @SerializedName ("deskripsi_acara") val deskripsi_acara: String,
    @SerializedName ("jumlah_penari") val jumlah_penari: Int,
    @SerializedName ("harga") val harga: Double
)

data class TariResponse(
    val data: List<Tari> // List of Gallery objects
)

data class TariDetailResponse(
    val data: Tari
)