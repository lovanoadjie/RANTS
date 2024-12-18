package com.example.rants.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("nohp") val nohp: String,
    @SerializedName("password") val password: String?,
    @SerializedName("profile_picture") val profilePicture: String? = null
)