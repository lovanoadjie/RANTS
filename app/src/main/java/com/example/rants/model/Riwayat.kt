package com.example.rants.model

data class RiwayatResponse(
    val status: Boolean,
    val data: List<RiwayatItem>
)

data class RiwayatItem(
    val id: Int,
    val user_id: Int,
    val pesanan_id: Int,
    val jenis_pesanan: String,
    val status: String,
    val created_at: String,  // atau kamu bisa rename ke 'tanggal' nanti di UI
    val updated_at: String,
    val data_pesanan: PesananData? // dari Laravel controller
)

data class RiwayatPesananData(
    val id: Int?,
    val nama_kostum: String?,       // jika jenis_pesanan == "kostum"
    val nama_makeup: String?,       // jika jenis_pesanan == "makeup"
    val jenis_tarian: String?,      // jika jenis_pesanan == "tari"
    val total_harga: Int?,
    val tanggal: String?,
    val jam_pemakaian: String?,
    val status_pesanan: String?
)

data class RiwayatRequest(
    val user_id: Int,
    val pesanan_id: Int,
    val jenis_pesanan: String,
    val status: String
)
