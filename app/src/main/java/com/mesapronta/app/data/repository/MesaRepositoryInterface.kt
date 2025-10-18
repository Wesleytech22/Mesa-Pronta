package com.mesapronta.app.data.repository

import com.mesapronta.app.model.Mesa
import kotlinx.coroutines.flow.Flow

interface MesaRepositoryInterface {
    fun obterMesas(): Flow<List<Mesa>>
    suspend fun obterMesaPorId(id: String): Mesa?
    fun obterMesasDisponiveis(capacidade: Int): Flow<List<Mesa>>
    suspend fun atualizarDisponibilidadeMesa(mesaId: String, disponivel: Boolean)
}