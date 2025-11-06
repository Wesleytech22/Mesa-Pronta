package com.mesapronta.app.model

data class Restaurant(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val rating: Float,
    val deliveryTime: String,
    val imageUrl: String,
    val address: String,
    // --- NOVAS PROPRIEDADES ---
    val menu: List<String>, // Adicione o menu
    val availableTimes: List<String>, // Adicione os horários (já usados no DetailScreen)
    val isOpen: Boolean = true // Adicione a flag de status (já usada no DetailScreen)
    // -------------------------
)
