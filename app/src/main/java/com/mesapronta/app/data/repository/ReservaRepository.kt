package com.mesapronta.app.data.repository

import com.mesapronta.app.model.Reserva
import com.mesapronta.app.model.StatusReserva
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class ReservaRepository {  // ← SEM @Inject, SEM @Singleton

    private val _reservas = MutableStateFlow<List<Reserva>>(emptyList())
    val reservas: Flow<List<Reserva>> = _reservas.asStateFlow()

    suspend fun salvarReserva(reserva: Reserva): Boolean {
        return try {
            val currentList = _reservas.value.toMutableList()
            currentList.add(reserva)
            _reservas.value = currentList
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obterReservaPorId(id: String): Reserva? {
        return _reservas.value.find { it.id == id }
    }

    suspend fun atualizarStatusReserva(id: String, status: StatusReserva): Boolean {
        val currentList = _reservas.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        return if (index != -1) {
            val reserva = currentList[index]
            currentList[index] = reserva.copy(status = status)
            _reservas.value = currentList
            true
        } else {
            false
        }
    }

    suspend fun obterReservasPorData(data: LocalDate): List<Reserva> {
        return _reservas.value.filter { it.dataReserva == data }
    }
}