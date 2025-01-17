package com.example.rants.model

data class user(
    val id: Int,
    val name: String,
    val nohp: String,
    val email: String,
    val password: String
)
data class AuthResponse(
    val status: String,
    val message: String,
    val data: AuthData  // Menyimpan data token dan user
)

data class AuthData(
    val user: user,  // Gunakan User dengan huruf kapital
    val token: String  // Token berada di dalam data
)
data class LogoutResponse(
    val status: String,
    val message: String
)
