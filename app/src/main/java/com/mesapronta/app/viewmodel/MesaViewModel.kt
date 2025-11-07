//package com.mesapronta.app.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.mesapronta.app.data.model.Mesa
//import com.mesapronta.app.data.repository.MesaRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//
//class MesaViewModel : ViewModel() {  // ← SEM @HiltViewModel
//
//    private val mesaRepository = MesaRepository()  // ← Instância direta
//
//    private val _mesasState = MutableStateFlow<List<Mesa>>(emptyList())
//    val mesasState: StateFlow<List<Mesa>> = _mesasState.asStateFlow()
//
//    private val _loadingState = MutableStateFlow(false)
//    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()
//
//    init {
//        carregarMesas()
//    }
//
//    fun carregarMesas() {
//        viewModelScope.launch {
//            _loadingState.value = true
//            try {
//                mesaRepository.obterMesas().collect { mesas ->
//                    _mesasState.value = mesas
//                }
//            } catch (e: Exception) {
//                // Tratar erro
//            } finally {
//                _loadingState.value = false
//            }
//        }
//    }
//}