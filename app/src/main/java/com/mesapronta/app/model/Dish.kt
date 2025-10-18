package com.mesapronta.app.model

data class Dish(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val restaurantId: Int
)