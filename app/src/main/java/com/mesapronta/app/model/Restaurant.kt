package com.mesapronta.app.model

import com.mesapronta.app.ui.screens.MenuItem

data class Restaurant(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val rating: Float,
    val deliveryTime: String,
    val imageUrl: String,
    val address: String,
    val menu: List<MenuItem>, // Agora é List<MenuItem> em vez de List<String>
    val availableTimes: List<String>,
    val isOpen: Boolean = true
)