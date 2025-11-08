package com.mesapronta.app.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID

data class Order @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: String = UUID.randomUUID().toString(),
    val restaurantId: Int,
    val mesaId: String? = null,
    val userId: String,
    val items: List<CartItem>, // Itens do pedido
    val totalAmount: Double,
    val subTotal: Double,
    val taxAmount: Double = 0.0,
    val status: OrderStatus = OrderStatus.CONFIRMED,
    val orderTime: LocalDateTime = now(),
    val reservationId: String? = null // Link para a Reserva
)