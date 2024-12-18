package com.example.rants.model

data class AuthResponse(
    val message: String,
    val user: user, // Tambahkan model User jika Anda perlu data user
    val token: String
)