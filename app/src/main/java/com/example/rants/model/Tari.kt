package com.example.rants.model

class Tari (
    val id: Int,
    val jenis_tarian: String,
    val image: String,
    val deskripsi_acara: String,
    val jumlah_penari: Int,
    val harga: Double
)

data class TariResponse(
    val data: List<Tari> // List of Gallery objects
)