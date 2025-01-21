package com.example.rants.model

import com.google.gson.annotations.SerializedName

data class CalendarModel(
    val id: Long,
    val date: String,
    val title: String,
    val description: String
)

data class CalendarAll(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)