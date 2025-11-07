// Adicione ao seu package de models
package com.mesapronta.app.model

import java.util.*

data class ReadyOrder(
    val id: String,
    val restaurantName: String,
    val reservationTime: String,
    val readyTime: String,
    val tableNumber: Int,
    val items: List<String>,
    val isCollected: Boolean = false
)

// Enum para status do pedido
enum class OrderStatus {
    CONFIRMED, PREPARING, READY, DELIVERED, COLLECTED
}