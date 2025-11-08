package com.mesapronta.app.model

// NOVO MODELO: Define as características especiais da mesa
data class CaracteristicaMesa(
    val id: String,
    val nome: String, // Ex: "Vista para o Mar", "Mesa para Cadeirante", "Sofá"
    val iconeUrl: String = ""
)