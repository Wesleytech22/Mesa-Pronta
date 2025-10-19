//package com.mesapronta.app.model
//
//import com.mesapronta.app.ui.screen.Dish
//
//data class Order(
//    val id: Int,
//    val dish: Dish,
//    val quantity: Int,
//    val consumptionType: ConsumptionType,
//    val selectedTable: Int? = null,
//    val deliveryAddress: String? = null,
//    val subtotal: Double,
//    val additionalFee: Double,
//    val total: Double
//)
//
//enum class ConsumptionType {
//    DINE_IN,      // Reservar mesa
//    TAKEAWAY,     // Retirada
//    DELIVERY      // Entrega via motoboy
//}
//
//data class PaymentInfo(
//    val paymentMethod: PaymentMethod,
//    val cardNumber: String = "",
//    val cardHolder: String = "",
//    val expiryDate: String = "",
//    val cvv: String = ""
//)
//
//enum class PaymentMethod {
//    CREDIT_CARD,
//    DEBIT_CARD,
//    MEAL_VOUCHER
//}