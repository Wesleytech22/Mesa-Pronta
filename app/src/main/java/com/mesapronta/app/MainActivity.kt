package com.mesapronta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mesapronta.app.model.ReservationDetails
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.ui.screen.LoginScreen
import com.mesapronta.app.ui.screen.PaymentScreen
import com.mesapronta.app.ui.screen.TableSelectionScreen
import com.mesapronta.app.ui.screens.HomeScreen
import com.mesapronta.app.ui.screens.RestaurantDetailScreen
import com.mesapronta.app.ui.theme.MesaProntaAppTheme
import com.mesapronta.app.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MesaProntaAppTheme {
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                // Variáveis de Estado de Dados (tipagem explícita para estabilidade)
                var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }
                var selectedTimeForReservation by remember { mutableStateOf<String?>(null) }
                var currentReservationDetails by remember { mutableStateOf<ReservationDetails?>(null) }

                // Controles de estado de navegação
                var isReserving by remember { mutableStateOf(false) } // Detalhes -> Seleção de Mesa
                var isPaying by remember { mutableStateOf(false) } // Seleção de Mesa -> Pagamento

                when {
                    // 🔐 1. Tela de Login
                    !isLoggedIn -> {
                        LoginScreen(
                            onLoginSuccess = { authViewModel.login("admin", "1234") }
                        )
                    }

                    // 💳 2. Tela de Pagamento
                    isPaying && currentReservationDetails != null -> {
                        PaymentScreen(
                            reservation = currentReservationDetails!!,
                            onReservationConfirmed = {
                                // Limpa todos os estados para voltar à Home após a confirmação
                                currentReservationDetails = null
                                selectedRestaurant = null
                                selectedTimeForReservation = null
                                isReserving = false
                                isPaying = false
                            },
                            onBack = {
                                isPaying = false // Volta para a Seleção de Mesa
                            }
                        )
                    }

                    // 🪑 3. Tela de Seleção de Mesa
                    isReserving && selectedRestaurant != null && selectedTimeForReservation != null -> {
                        TableSelectionScreen(
                            restaurant = selectedRestaurant!!,
                            selectedTime = selectedTimeForReservation!!,
                            onBack = {
                                isReserving = false // Volta para Detalhes do Restaurante
                            },
                            onContinueToPayment = { reservation ->
                                currentReservationDetails = reservation // Salva o objeto completo
                                isPaying = true // Navega para Pagamento
                            }
                        )
                    }

                    selectedRestaurant != null -> {
                        RestaurantDetailScreen(
                            restaurant = selectedRestaurant!!,
                            onBack = {
                                selectedRestaurant = null // Volta para Home
                                isReserving = false
                                selectedTimeForReservation = null
                            },
                            onReserveClicked = { restaurant, time ->
                                // Argumentos corretos (Restaurant e String)
                                selectedRestaurant = restaurant
                                selectedTimeForReservation = time
                                isReserving = true // Navega para Seleção de Mesa
                            }
                        )
                    }

                    // 🏠 5. Tela Inicial
                    else -> {
                        HomeScreen(
                            onLogout = { authViewModel.logout() },
                            onRestaurantSelected = { restaurant ->
                                selectedRestaurant = restaurant
                                // Reseta o estado de reserva/pagamento antes de ir para Detalhes
                                isReserving = false
                                isPaying = false
                                selectedTimeForReservation = null
                                currentReservationDetails = null
                            }
                        )
                    }
                }
            }
        }
    }
}