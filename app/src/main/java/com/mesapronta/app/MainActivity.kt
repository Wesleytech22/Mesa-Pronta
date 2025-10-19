package com.mesapronta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.mesapronta.app.model.CartItem
import com.mesapronta.app.model.Order
import com.mesapronta.app.ui.screen.*
import com.mesapronta.app.ui.theme.MesaProntaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MesaProntaAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Login) }
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var selectedTable by remember { mutableStateOf<Int?>(null) }
    var currentOrder by remember { mutableStateOf<Order?>(null) }

    val cartItemCount = cartItems.sumOf { it.quantity }

    when (currentScreen) {
        AppScreen.Login -> {
            LoginScreen(
                onLoginSuccess = { currentScreen = AppScreen.Home }
            )
        }
        AppScreen.Home -> {
            HomeScreen(
                onDishSelect = { dish, restaurant ->
                    // Add to cart
                    val existingItem = cartItems.find { it.dish.id == dish.id && it.restaurant.id == restaurant.id }
                    if (existingItem != null) {
                        cartItems = cartItems.map {
                            if (it.dish.id == dish.id && it.restaurant.id == restaurant.id) {
                                it.copy(quantity = it.quantity + 1)
                            } else {
                                it
                            }
                        }
                    } else {
                        cartItems = cartItems + CartItem(dish, 1, restaurant)
                    }
                },
                onLogout = {
                    currentScreen = AppScreen.Login
                    cartItems = emptyList()
                    selectedTable = null
                },
                onCartClick = { currentScreen = AppScreen.Cart },
                onPromotionsClick = { /* TODO: Implement promotions screen */ },
                cartItemCount = cartItemCount
            )
        }
        AppScreen.Cart -> {
            CartScreen(
                cartItems = cartItems,
                onBackClick = { currentScreen = AppScreen.Home },
                onUpdateQuantity = { cartItem, newQuantity ->
                    cartItems = if (newQuantity > 0) {
                        cartItems.map {
                            if (it.dish.id == cartItem.dish.id && it.restaurant.id == cartItem.restaurant.id) {
                                it.copy(quantity = newQuantity)
                            } else {
                                it
                            }
                        }
                    } else {
                        cartItems.filterNot {
                            it.dish.id == cartItem.dish.id && it.restaurant.id == cartItem.restaurant.id
                        }
                    }
                },
                onRemoveItem = { cartItem ->
                    cartItems = cartItems.filterNot {
                        it.dish.id == cartItem.dish.id && it.restaurant.id == cartItem.restaurant.id
                    }
                },
                onSelectTable = {
                    // TODO: Implement table selection
                    selectedTable = 5 // Example table number
                },
                onAddMoreItems = { currentScreen = AppScreen.Home },
                onProceedToPayment = {
                    val total = cartItems.sumOf { it.dish.price * it.quantity }
                    currentOrder = Order(
                        id = System.currentTimeMillis().toString(),
                        items = cartItems,
                        selectedTable = selectedTable,
                        subtotal = total,
                        total = total
                    )
                    currentScreen = AppScreen.Payment
                },
                selectedTable = selectedTable
            )
        }
        AppScreen.Payment -> {
            val order = currentOrder
            if (order != null) {
                PaymentScreen(
                    order = order,
                    onBackClick = { currentScreen = AppScreen.Cart },
                    onPaymentSuccess = {
                        currentScreen = AppScreen.Home
                        cartItems = emptyList()
                        selectedTable = null
                        currentOrder = null
                    }
                )
            }
        }
    }
}