package com.mesapronta.app.model

data class Dish(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String = ""
)

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val type: String,
    val rating: Float,
    val deliveryTime: String,
    val availableTables: Int,
    val dishes: List<Dish> = emptyList()
)

data class CartItem(
    val dish: Dish,
    var quantity: Int,
    val restaurant: Restaurant
)

data class Order(
    val id: String,
    val items: List<CartItem>,
    val selectedTable: Int? = null,
    val subtotal: Double,
    val additionalFee: Double = 0.0,
    val total: Double
)

data class Promotion(
    val id: String,
    val title: String,
    val description: String,
    val originalPrice: Double,
    val promoPrice: Double,
    val restaurant: String,
    val validHours: String
)

data class User(
    val id: String,
    val name: String,
    val email: String
)

enum class PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    MEAL_VOUCHER
}