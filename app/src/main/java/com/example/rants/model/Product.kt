package com.example.rants.model

import com.google.gson.annotations.SerializedName

data class kosta(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_kostum") val nama_kostum: String, // Sesuai JSON
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("image") val image: String,
    @SerializedName("warna") val warna: String,
    @SerializedName("ukuran") val ukuran: String,
    @SerializedName("harga") val harga: Int
)

data class ProductResponse(
    val status: String,
    val data: List<kosta>
)

data class ProductDetailResponse(
    val data: kosta
)