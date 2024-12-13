package com.example.rants.model


data class GalleryResponse(
    val data: List<Gallery> // List of Gallery objects
)

data  class Gallery (
    val id : Int,
    val image : String,
    val description : String
    )
