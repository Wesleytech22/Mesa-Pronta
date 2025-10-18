//package com.mesapronta.app.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.mesapronta.app.data.model.Reserva
//import com.mesapronta.app.data.model.StatusReserva
//import com.mesapronta.app.data.repository.ReservaRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class ReservaViewModel : ViewModel() {  // ← SEM @HiltViewModel
//
//    private val reservaRepository = ReservaRepository()  // ← Instância direta
//
//    private val _uiState = MutableStateFlow(ReservaUiState())
//    val uiState: StateFlow<ReservaUiState> = _uiState.asStateFlow()
//
//    fun criarReserva(reserva: Reserva) {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true)
//            try {
//                val sucesso = reservaRepository.salvarReserva(reserva)
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    reservaCriada = sucesso,
//                    error = if (!sucesso) "Erro ao criar reserva" else null
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    error = e.message ?: "Erro desconhecido"
//                )
//            }
//        }
//    }
//
//    fun obterReservas() {
//        viewModelScope.launch {
//            reservaRepository.reservas.collect { reservas ->
//                _uiState.value = _uiState.value.copy(
//                    reservas = reservas
//                )
//            }
//        }
//    }
//
//    fun resetState() {
//        _uiState.value = ReservaUiState()
//    }
//}
//
//data class ReservaUiState(
//    val isLoading: Boolean = false,
//    val reservas: List<Reserva> = emptyList(),
//    val reservaCriada: Boolean = false,
//    val error: String? = null
//)