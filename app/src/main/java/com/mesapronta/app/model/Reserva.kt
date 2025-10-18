package com.mesapronta.app.model

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Reserva(
    val id: String = UUID.randomUUID().toString(),
    val nomeCliente: String = "",
    val emailCliente: String = "",
    val telefoneCliente: String = "",
    val numeroPessoas: Int = 1,
    val dataReserva: LocalDate = LocalDate.now(),
    val horarioReserva: LocalTime = LocalTime.NOON,
    val mesaId: String = "",
    val status: StatusReserva = StatusReserva.PENDENTE,
    val observacoes: String = "",
    val dataCriacao: LocalDate = LocalDate.now()
)

enum class StatusReserva {
    PENDENTE, CONFIRMADA, CANCELADA, FINALIZADA
}

fun StatusReserva.getColor(): String {
    return when (this) {
        StatusReserva.PENDENTE -> "#FFA500"
        StatusReserva.CONFIRMADA -> "#008000"
        StatusReserva.CANCELADA -> "#FF0000"
        StatusReserva.FINALIZADA -> "#0000FF"
    }
}