package com.mesapronta.app.model

// Import necessário para o tipo Restaurant
// import com.mesapronta.app.model.Restaurant

data class ReservationDetails(
    // Detalhes essenciais para a reserva
    val id: String = "", // Deve ter valor padrão se não for fornecido
    val restaurantName: String,
    val tableNumber: Int,
    val reservationTime: String,
    val numberOfPeople: Int,

    // Detalhes financeiros (pode ter valor inicial zero)
    val totalAmount: Double = 0.0,

    // Opcionais:
    val restaurant: Restaurant? = null // O objeto completo pode ser útil
)