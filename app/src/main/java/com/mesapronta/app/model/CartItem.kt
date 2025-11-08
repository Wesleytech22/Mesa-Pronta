package com.mesapronta.app.model

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
) {
    val totalPrice: Double
        get() = menuItem.price * quantity
}