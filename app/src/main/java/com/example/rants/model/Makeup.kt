package com.example.rants.model

enum class kategory{
    SD,SMP,SMA,Umum
}

class Makeup (
    val id: Int,
    val image: String,
    val Kategory: kategory,
    val harga: Double
)

data class MakeupResponse(
    val data: List<Makeup> // List of Gallery objects
)