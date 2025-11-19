//package com.mesapronta.app.data.repository
//
//import com.mesapronta.app.model.Mesa
//import com.mesapronta.app.model.LocalizacaoMesa
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flowOf
//
//class MesaRepository {  // ← SEM @Inject, SEM @Singleton
//
//    private val mesas = listOf(
//        Mesa(id = "1", numero = 1, capacidade = 4, disponivel = true, localizacao = LocalizacaoMesa.INTERNA),
//        Mesa(id = "2", numero = 2, capacidade = 6, disponivel = true, localizacao = LocalizacaoMesa.EXTERNA),
//        Mesa(id = "3", numero = 3, capacidade = 2, disponivel = true, localizacao = LocalizacaoMesa.VARANDA),
//        Mesa(id = "4", numero = 4, capacidade = 8, disponivel = false, localizacao = LocalizacaoMesa.VIP),
//        Mesa(id = "5", numero = 5, capacidade = 4, disponivel = true, localizacao = LocalizacaoMesa.INTERNA),
//        Mesa(id = "6", numero = 6, capacidade = 6, disponivel = true, localizacao = LocalizacaoMesa.EXTERNA),
//    )
//
//    fun obterMesas(): Flow<List<Mesa>> = flowOf(mesas)
//
//    suspend fun obterMesaPorId(id: String): Mesa? {
//        return mesas.firstOrNull { it.id == id }
//    }
//
//    fun obterMesasDisponiveis(capacidade: Int): Flow<List<Mesa>> {
//        val mesasFiltradas = mesas.filter {
//            it.disponivel && it.capacidade >= capacidade
//        }
//        return flowOf(mesasFiltradas)
//    }
//}