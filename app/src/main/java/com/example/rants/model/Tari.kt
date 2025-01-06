package com.example.rants.model

import com.google.gson.annotations.SerializedName

class Tari (
    @SerializedName ("id") val id: Int,
    @SerializedName ("jenis_tarian") val jenis_tarian: String,
    @SerializedName ("image") val image: String,
    @SerializedName ("deskripsi_acara") val deskripsi_acara: String,
    @SerializedName ("jumlah_penari") val jumlah_penari: Int,
    @SerializedName ("harga") val harga: Int
)

data class TariResponse(
    val status: String,
    val data: List<Tari>
)

data class TariDetailResponse(
    val data: Tari
)

data class PesananTariRequest(
    val penyewaan_jasa_taris_id: Int,
    val Users_id: Int,
    val tanggal: String,
    val jam_pemakaian: String,
    val alamat: String,
    val latitude: Double?,
    val longitude: Double?,
    val total_harga: Int,
    val status_pesanan: String
)

data class PesananTariResponse(
    val success: Boolean,
    val message: String,
    val data: PesananData?
)


data class PesananTariData(
    val penyewaan_jasa_taris_id: Int,
    val UsersId: Int,
    val tanggal: String,
    val jam_pemakaian: Int,
    val alamat: String,
    val latitude: Double?,
    val longitude: Double?,
    val totalHarga: Int,
    val statusPesanan: String,
    val created_at: String,
    val update_at: String
)