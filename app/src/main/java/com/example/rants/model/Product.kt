package com.example.rants.model

import com.google.gson.annotations.SerializedName

data class kosta(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_kostum") val nama_kostum: String?,
    @SerializedName("jumlah") val jumlah: Int?,
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
    val status: String,
    val data: kosta
)

data class PesananKostumRequest (
    val kosta_id: Int,
    val Users_id: Int,
    val tanggal_pemakaian_mulai: String,
    val tanggal_pemakaian_selesai: String,
    val total_harga: Int,
    val status_pesanan: String,
    val updated_at: String,
    val created_at: String
)

data class PesananKostumResponse(
    val success: Boolean,
    val message: String,
    val data: List<PesananKostum>
)

data class PesananKostumData(
    val kosta_id: Int,
    val User_id: Int,
    val waktu_pemakaian_mulai: String,
    val waktu_pemakaian_string: String,
    val total_harga: Int,
    val status_pesanan: String,
    val created_at: String,
    val update_at: String
)

data class PesananKostum(
    val kosta_id: Int,
    val User_id: Int,
    val waktu_pemakaian_mulai: String,
    val waktu_pemakaian_string: String,
    val total_harga: Int,
    val status_pesanan: String,
    val created_at: String,
    val update_at: String
)
