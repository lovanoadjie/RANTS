package com.example.rants.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val nohp: String,
    val password: String,
    val password_confirmation: String
)
