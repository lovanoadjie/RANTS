package com.example.rants.model

import com.google.gson.annotations.SerializedName

enum class Kategory {
    @SerializedName("SD") SD,
    @SerializedName("SMP") SMP,
    @SerializedName("SMA") SMA,
    @SerializedName("Umum") Umum;

    fun getCategoryName(): String {
        return when (this) {
            SD -> "Sekolah Dasar"
            SMP -> "Sekolah Menengah Pertama"
            SMA -> "Sekolah Menengah Atas"
            Umum -> "Umum"
        }
    }
}

class Makeup(
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String,
    @SerializedName("Kategory") val kategory: Kategory,
    @SerializedName("harga") val harga: Int
)

data class MakeupResponse(
    val data: List<Makeup> // List of Makeup objects
)

data class MakeupDetailResponse(
    val data: Makeup
)

data class PesananRequest(
    val make_ups_id: Int,
    val Users_id: Int,
    val tanggal_pesanan: String,
    val lokasi_pemesanan: String,
    val alamat: String,
    val latitude: Double?,
    val longitude: Double?,
    val total_harga: Int,
    val status_pesanan: String
)

data class PesananResponse(
    val success: Boolean,
    val message: String,
    val data: PesananData?
)

data class PesananData(
    val id: Int,
    val make_ups_id: Int,
    val Users_id: Int,
    val tanggal_pesanan: String,
    val lokasi_pemesanan: String,
    val alamat: String,
    val latitude: Double?,
    val longitude: Double?,
    val total_harga: Int,
    val status_pesanan: String,
    val created_at: String
)


