package com.mesapronta.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mesapronta.app.model.CartItem
import com.mesapronta.app.model.ReservationDetails
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.ui.screen.FoodSelectionScreen
import com.mesapronta.app.ui.screen.LoginScreen
import com.mesapronta.app.ui.screen.MainScreenWithBottomNav
import com.mesapronta.app.ui.screen.PaymentScreen
import com.mesapronta.app.ui.screen.RegisterScreen
import com.mesapronta.app.ui.screen.TableSelectionScreen
import com.mesapronta.app.ui.screens.*
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
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                val currentUser by authViewModel.currentUser.collectAsState()

                // Variáveis de Estado de Dados
                var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }
                var selectedTimeForReservation by remember { mutableStateOf<String?>(null) }
                var currentReservationDetails by remember { mutableStateOf<ReservationDetails?>(null) }
                var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }

                // Controles de estado de navegação
                var isReserving by remember { mutableStateOf(false) }
                var isPaying by remember { mutableStateOf(false) }
                var isAddingFood by remember { mutableStateOf(false) }
                var isTrackingOrder by remember { mutableStateOf(false) } // NOVO: Estado para acompanhar pedido
                var showRegisterScreen by remember { mutableStateOf(false) } // NOVO: Estado para tela de cadastro

                // Estado para Bottom Navigation
                var selectedScreen by remember { mutableStateOf("home") }

                when {
                    // 🔐 1. Tela de Cadastro (NOVO)
                    showRegisterScreen -> {
                        RegisterScreen(
                            onRegisterSuccess = {
                                showRegisterScreen = false
                                Toast.makeText(this@MainActivity, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            },
                            onNavigateToLogin = {
                                showRegisterScreen = false
                            },
                            onRegister = { fullName, username, email, password ->
                                authViewModel.register(fullName, username, email, password)
                            }
                        )
                    }

                    // 🔐 2. Tela de Login
                    !isLoggedIn -> {
                        LoginScreen(
                            onLoginSuccess = { username, password ->
                                val success = authViewModel.login(username, password)
                                if (!success) {
                                    Toast.makeText(this@MainActivity, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onNavigateToRegister = {
                                showRegisterScreen = true
                            }
                        )
                    }

                    // 📦 3. Tela de Acompanhamento de Pedido (NOVO)
                    isTrackingOrder -> {
                        OrderTrackingScreen(
                            onNavigateToReadyOrders = {
                                // Quando finaliza o pedido, volta para home e mostra pedidos prontos
                                isTrackingOrder = false
                                selectedScreen = "home"
                                // Adiciona um pedido pronto de exemplo
                                readyOrdersViewModel.addReadyOrder(
                                    com.mesapronta.app.model.ReadyOrder(
                                        id = "ORD-${System.currentTimeMillis().toString().takeLast(6)}",
                                        restaurantName = "Pizzaria do DED",
                                        reservationTime = "19:00",
                                        readyTime = "Agora",
                                        tableNumber = 5,
                                        items = listOf("Pizza Margherita", "Refrigerante", "Brownie"),
                                        isCollected = false
                                    )
                                )
                                Toast.makeText(
                                    this@MainActivity,
                                    "Pedido finalizado! Verifique seus pedidos prontos.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }

                    // 🍽 4. Tela de Adicionar Comidas
                    isAddingFood && selectedRestaurant != null -> {
                        FoodSelectionScreen(
                            menuItems = selectedRestaurant!!.menu,
                            onBack = {
                                isAddingFood = false
                            },
                            onContinueToPayment = { selectedCartItems ->
                                cartItems = selectedCartItems
                                isAddingFood = false
                                isPaying = true
                            }
                        )
                    }

                    // 💳 5. Tela de Pagamento
                    isPaying && currentReservationDetails != null -> {
                        PaymentScreen(
                            reservation = currentReservationDetails!!,
                            cartItems = cartItems,
                            onReservationConfirmed = {
                                // Limpa todos os estados para voltar à Home após a confirmação
                                currentReservationDetails = null
                                selectedRestaurant = null
                                selectedTimeForReservation = null
                                isReserving = false
                                isPaying = false
                                isAddingFood = false
                                cartItems = emptyList()
                                selectedScreen = "home"

                                // Simular pedido pronto após confirmação (para demonstração)
                                readyOrdersViewModel.addReadyOrder(
                                    com.mesapronta.app.model.ReadyOrder(
                                        id = "ORD-${System.currentTimeMillis().toString().takeLast(6)}",
                                        restaurantName = selectedRestaurant?.name ?: "Restaurante",
                                        reservationTime = selectedTimeForReservation ?: "19:00",
                                        readyTime = "Agora",
                                        tableNumber = currentReservationDetails?.tableNumber ?: 1,
                                        items = cartItems.map { "${it.quantity}x ${it.menuItem.name}" },
                                        isCollected = false
                                    )
                                )

                                Toast.makeText(this@MainActivity, "Reserva confirmada com sucesso!", Toast.LENGTH_SHORT).show()
                            },
                            onBack = {
                                isPaying = false
                            }
                        )
                    }

                    // 🪑 6. Tela de Seleção de Mesa
                    isReserving && selectedRestaurant != null && selectedTimeForReservation != null -> {
                        TableSelectionScreen(
                            restaurant = selectedRestaurant!!,
                            selectedTime = selectedTimeForReservation!!,
                            onBack = {
                                isReserving = false
                            },
                            onContinueToPayment = { reservation ->
                                currentReservationDetails = reservation
                                cartItems = emptyList()
                                isPaying = true
                            },
                            onAddMoreFood = {
                                isAddingFood = true
                            }
                        )
                    }

                    // 📋 7. Tela de Detalhes do Restaurante
                    selectedRestaurant != null -> {
                        RestaurantDetailScreen(
                            restaurant = selectedRestaurant!!,
                            onBack = {
                                selectedRestaurant = null
                                isReserving = false
                                selectedTimeForReservation = null
                            },
                            onReserveClicked = { restaurant, time ->
                                selectedRestaurant = restaurant
                                selectedTimeForReservation = time
                                isReserving = true
                            }
                        )
                    }

                    // 🏠 8. Tela Principal com Bottom Navigation
                    else -> {
                        MainScreenWithBottomNav(
                            selectedScreen = selectedScreen,
                            onScreenSelected = { screen -> selectedScreen = screen },
                            onLogout = { authViewModel.logout() },
                            onRestaurantSelected = { restaurant ->
                                selectedRestaurant = restaurant
                                isReserving = false
                                isPaying = false
                                isAddingFood = false
                                isTrackingOrder = false
                                selectedTimeForReservation = null
                                currentReservationDetails = null
                                cartItems = emptyList()
                            },
                            onNavigateToOrderTracking = { // NOVO: Callback para navegar para acompanhamento
                                isTrackingOrder = true
                            },
                            readyOrdersViewModel = readyOrdersViewModel,
                            currentUserName = currentUser?.fullName // NOVO: Passa o nome do usuário
                        )
                    }
                }
            }
        }
    }
}