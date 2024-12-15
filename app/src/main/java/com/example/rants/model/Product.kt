package com.example.rants.model

data class kosta(
    val id: Int,
    val nama_kostum: String?,
    val image: String?,
    val jumlah: Int,
    val warna: String?,
    val ukuran: String?,
    val harga: Int,
)

data class ProductResponse(
    val status: String,
    val data: List<kosta>
)
//data class ProductResponse(
//    val data: List<kosta> // or whatever field name is used for the list
//)



//data class DetailResponse(
//    val data: List<kosta> // or whatever field name is used for the list
//)
