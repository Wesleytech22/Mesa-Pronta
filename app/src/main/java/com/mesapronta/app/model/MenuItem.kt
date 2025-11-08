package com.mesapronta.app.model

data class MenuItem(
    val id: String = "",
    val name: String,
    val description: String = "",
    val price: Double,
    val category: String = "",
    val imageUrl: String = ""
)