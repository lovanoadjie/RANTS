package com.example.rants.model

data class RegisterRequest(
    val nama: String,
    val nohp: String,
    val email: String,
    val password: String
)