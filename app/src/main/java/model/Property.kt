package com.example.house_rental_app.model

data class Property(
    val id: Int,
    val title: String,
    val address: String,
    val price: Double,
    val rooms: Int,
    val imageUrl: String? = null
)
