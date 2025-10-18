package com.mesapronta.app.model

data class Mesa(
    val id: String,
    val numero: Int,
    val capacidade: Int = 4,
    val disponivel: Boolean = true,
    val localizacao: LocalizacaoMesa = LocalizacaoMesa.INTERNA,
    val caracteristicas: List<CaracteristicaMesa> = emptyList()
)