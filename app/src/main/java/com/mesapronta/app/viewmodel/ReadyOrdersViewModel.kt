// ReadyOrdersViewModel.kt
package com.mesapronta.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesapronta.app.model.ReadyOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReadyOrdersViewModel : ViewModel() {
    private val _readyOrders = MutableStateFlow<List<ReadyOrder>>(emptyList())
    val readyOrders: StateFlow<List<ReadyOrder>> = _readyOrders.asStateFlow()

    private val _hasNewReadyOrders = MutableStateFlow(false)
    val hasNewReadyOrders: StateFlow<Boolean> = _hasNewReadyOrders.asStateFlow()

    init {
        // Simular alguns pedidos prontos para demonstração
        loadSampleReadyOrders()
    }

    private fun loadSampleReadyOrders() {
        viewModelScope.launch {
            val sampleOrders = listOf(
                ReadyOrder(
                    id = "ORD-001",
                    restaurantName = "Pizzaria do DED",
                    reservationTime = "19:00",
                    readyTime = getCurrentTime(),
                    tableNumber = 5,
                    items = listOf("Pizza Margherita", "Coca-Cola 2L")
                ),
                ReadyOrder(
                    id = "ORD-002",
                    restaurantName = "Sushi House",
                    reservationTime = "20:30",
                    readyTime = "20:45",
                    tableNumber = 3,
                    items = listOf("Combinado Salmão", "Temaki")
                )
            )
            _readyOrders.value = sampleOrders
            _hasNewReadyOrders.value = sampleOrders.any { !it.isCollected }
        }
    }

    fun addReadyOrder(order: ReadyOrder) {
        viewModelScope.launch {
            _readyOrders.value = _readyOrders.value + order
            _hasNewReadyOrders.value = true
        }
    }

    fun markAsCollected(orderId: String) {
        viewModelScope.launch {
            _readyOrders.value = _readyOrders.value.map { order ->
                if (order.id == orderId) order.copy(isCollected = true) else order
            }
            _hasNewReadyOrders.value = _readyOrders.value.any { !it.isCollected }
        }
    }

    fun clearAllCollected() {
        viewModelScope.launch {
            _readyOrders.value = _readyOrders.value.filter { !it.isCollected }
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}