package com.mesapronta.app.model

// Importe sua classe Restaurant se não estiver no mesmo pacote
data class ReservationDetails(
    val restaurant: Restaurant,
    val time: String,
    val people: Int,
    val tableNumber: Int
)