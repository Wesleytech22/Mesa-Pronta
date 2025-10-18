package com.mesapronta.app.model

data class Restaurant(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val rating: Float,
    val deliveryTime: String,
    val imageUrl: String? = null
)