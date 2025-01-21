package com.example.rants.model
data class UserProfile(
    val name: String?,
    val email: String?,
    val nohp: String?,
    val image_url: String? // Menambahkan field image_url
)

data class UserResponse(
    val status: String,
    val message: String,
    val data: UserProfile
)
