package com.mesapronta.app.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Reserva @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: String = UUID.randomUUID().toString(),
    val nomeCliente: String = "",
    val emailCliente: String = "",
    val telefoneCliente: String = "",
    val numeroPessoas: Int = 1,
    val dataReserva: LocalDate,
    val horarioReserva: LocalTime,
    val mesaId: String = "",
    val status: StatusReserva = StatusReserva.PENDENTE,
    val observacoes: String = "",
    val dataCriacao: LocalDate,
    val checkInTime: LocalTime? = null // NOVO: Hora que o cliente fez o check-in
)

enum class StatusReserva {
    PENDENTE, CONFIRMADA, CANCELADA, FINALIZADA, CHECKED_IN // NOVO: Adiciona status de check-in
}

fun StatusReserva.getColor(): String {
    return when (this) {
        StatusReserva.PENDENTE -> "#FFA500"
        StatusReserva.CONFIRMADA -> "#008000"
        StatusReserva.CANCELADA -> "#FF0000"
        StatusReserva.FINALIZADA -> "#0000FF"
        StatusReserva.CHECKED_IN -> "#9C27B0" // Roxo para check-in
    }
}