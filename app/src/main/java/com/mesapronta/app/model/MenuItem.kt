// Adicione este arquivo: MenuItem.kt
package com.mesapronta.app.model

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String = ""
)

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int
) {
    val totalPrice: Double get() = menuItem.price * quantity
}