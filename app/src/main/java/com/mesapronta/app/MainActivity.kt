package com.mesapronta.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mesapronta.app.model.CartItem
import com.mesapronta.app.model.ReservationDetails
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.ui.screen.*
import com.mesapronta.app.ui.theme.MesaProntaAppTheme
import com.mesapronta.app.viewmodel.AuthViewModel
import com.mesapronta.app.viewmodel.ReadyOrdersViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val readyOrdersViewModel: ReadyOrdersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MesaProntaAppTheme {
                val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

                // Estados de navegação
                var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }
                var currentReservation by remember { mutableStateOf<ReservationDetails?>(null) }
                var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }

                var currentScreen by remember { mutableStateOf("home") }
                var isCheckingIn by remember { mutableStateOf(false) }
                var isPaying by remember { mutableStateOf(false) }

                // NOVO: Estado para simular o pagamento da taxa (ou a necessidade dele)
                var needsFeePayment by remember { mutableStateOf(false) }
                val lateFeeAmount = 50.0

                when {
                    isLoggedIn -> {
                        LoginScreen(
                            onLoginSuccess = {
                                authViewModel.login("admin", "1234")
                            },
                            onRegisterClicked = {
                                Toast.makeText(this, "Navegar para Tela de Cadastro!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    // --- Lógica de Check-in (Atualizada) ---
                    isCheckingIn && currentReservation != null -> {
                        CheckInScreen(
                            reservation = currentReservation!!,
                            onCheckInComplete = { isConsumptionInPlace -> // Recebe true se for consumir no local
                                isCheckingIn = false
                                if (isConsumptionInPlace) {
                                    Toast.makeText(this, "Check-in realizado para consumo no local!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Check-in realizado para retirada!", Toast.LENGTH_SHORT).show()
                                }
                                currentScreen = "home"
                            },
                            onBack = { isCheckingIn = false }
                        )
                    }

                    // --- Simulação de Pagamento de Taxa (Pode ser integrado ao PaymentScreen real) ---
                    needsFeePayment && currentReservation != null -> {
                        PaymentScreen(
                            reservation = currentReservation!!.copy(
                                totalAmount = currentReservation!!.totalAmount + lateFeeAmount // Adiciona a taxa
                            ),
                            cartItems = cartItems,
                            couvertAmount = 0.0,
                            onReservationConfirmed = {
                                Toast.makeText(this, "Taxa paga! Check-in para consumo no local liberado.", Toast.LENGTH_SHORT).show()
                                needsFeePayment = false
                                isCheckingIn = true // Volta para a tela de check-in, agora sem o alerta de atraso
                            },
                            onBack = {
                                needsFeePayment = false
                                isCheckingIn = false
                            }
                        )
                    }

                    // --- Lógica de Pagamento de Reserva (Mantida) ---
                    isPaying && currentReservation != null -> {
                        PaymentScreen(
                            reservation = currentReservation!!,
                            cartItems = cartItems,
                            couvertAmount = 10.0,
                            onReservationConfirmed = {
                                Toast.makeText(this, "Reserva confirmada!", Toast.LENGTH_SHORT).show()
                                isPaying = false
                                currentScreen = "home"
                                selectedRestaurant = null
                                currentReservation = null
                                cartItems = emptyList()
                            },
                            onBack = { isPaying = false }
                        )
                    }

                    // --- Lógica de Detalhe de Restaurante (Mantida) ---
                    selectedRestaurant != null -> {
                        RestaurantDetailScreen(
                            restaurant = selectedRestaurant!!,
                            onBack = { selectedRestaurant = null },
                            onReserveClicked = { restaurant, time, peopleCount ->
                                // Simulação de criação de ReservationDetails
                                currentReservation = ReservationDetails(
                                    id = System.currentTimeMillis().toString(),
                                    restaurantName = restaurant.name,
                                    tableNumber = 1,
                                    reservationTime = time,
                                    numberOfPeople = peopleCount,
                                    totalAmount = 0.0 // O total será calculado no PaymentScreen
                                )
                                isPaying = true
                            }
                        )
                    }

                    // --- Navegação Principal (Mantida) ---
                    else -> {
                        MainScreenWithBottomNav(
                            selectedScreen = currentScreen,
                            onScreenSelected = { screen -> currentScreen = screen },
                            onLogout = { authViewModel.logout() },
                            onRestaurantSelected = { restaurant ->
                                selectedRestaurant = restaurant
                            },
                            onCheckInSelected = { reservation ->
                                currentReservation = reservation
                                isCheckingIn = true
                            },
                            readyOrdersViewModel = readyOrdersViewModel
                        )
                    }
                }
            }
        }
    }
}