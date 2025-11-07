package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.ReservationDetails
import com.mesapronta.app.model.CartItem
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    reservation: ReservationDetails,
    cartItems: List<CartItem> = emptyList(), // Novo parâmetro para itens do carrinho
    onReservationConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    // Estados para o pagamento
    var couponCode by remember { mutableStateOf("") }
    var isCouponApplied by remember { mutableStateOf(false) }
    var discountPercentage by remember { mutableStateOf(0) }

    // Estados para informações do cartão
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedCardType by remember { mutableStateOf("Cartão de Crédito") }

    // Cálculo do valor base
    val basePrice = remember(cartItems, reservation.people) {
        val foodTotal = cartItems.sumOf { it.totalPrice }
        val coverCharge = reservation.people * 10.0 // Taxa de couvert
        foodTotal + coverCharge
    }

    val discountAmount = remember(basePrice, discountPercentage) {
        basePrice * (discountPercentage / 100.0)
    }

    val finalPrice = remember(basePrice, discountAmount) {
        basePrice - discountAmount
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagamento") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Voltar") }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // --- SEÇÃO 1: RESUMO DA RESERVA ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumo da Reserva", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("🍽 ${reservation.restaurant.name}")
                        Text("🕒 ${reservation.time}")
                        Text("👥 ${reservation.people} pessoas")
                        Text("🪑 Mesa ${reservation.tableNumber}")
                    }
                }
            }

            // --- SEÇÃO 2: ITENS DO CARRINHO (se houver) ---
            if (cartItems.isNotEmpty()) {
                item {
                    Text(
                        "Itens do Pedido",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(cartItems) { cartItem ->
                    CartItemRow(
                        cartItem = cartItem,
                        currencyFormat = currencyFormat
                    )
                }
            }

            // --- SEÇÃO 3: RESUMO FINANCEIRO ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumo Financeiro", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        if (cartItems.isNotEmpty()) {
                            Text("Itens: ${currencyFormat.format(cartItems.sumOf { it.totalPrice })}")
                        }
                        Text("Taxa de couvert (${reservation.people} pessoas): ${currencyFormat.format(reservation.people * 10.0)}")

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Subtotal: ${currencyFormat.format(basePrice)}")

                        if (isCouponApplied) {
                            Text(
                                "Desconto ($discountPercentage%): -${currencyFormat.format(discountAmount)}",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            "Valor Final: ${currencyFormat.format(finalPrice)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }

            // --- SEÇÃO 4: CUPOM DE DESCONTO ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Discount, contentDescription = "Cupom",
                                tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cupom de Desconto", style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = couponCode,
                                onValueChange = { couponCode = it },
                                label = { Text("Código do cupom") },
                                placeholder = { Text("Digite o código") },
                                modifier = Modifier.weight(1f),
                                enabled = !isCouponApplied
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    when (couponCode.uppercase()) {
                                        "DESC10" -> {
                                            discountPercentage = 10
                                            isCouponApplied = true
                                        }
                                        "DESC20" -> {
                                            discountPercentage = 20
                                            isCouponApplied = true
                                        }
                                        "PRIMEIRA" -> {
                                            discountPercentage = 15
                                            isCouponApplied = true
                                        }
                                        else -> {
                                            // Cupom inválido
                                            discountPercentage = 0
                                            isCouponApplied = false
                                        }
                                    }
                                },
                                enabled = couponCode.isNotBlank() && !isCouponApplied
                            ) {
                                Text("Aplicar")
                            }
                        }

                        if (isCouponApplied) {
                            Text(
                                "✅ Cupom aplicado! $discountPercentage% de desconto",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // --- SEÇÃO 5: FORMAS DE PAGAMENTO ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payment, contentDescription = "Pagamento")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Forma de Pagamento", style = MaterialTheme.typography.titleMedium)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val paymentMethods = listOf(
                            "Cartão de Crédito",
                            "Cartão de Débito",
                            "PIX",
                            "Dinheiro"
                        )

                        Column {
                            paymentMethods.forEach { method ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = method == selectedCardType,
                                        onClick = { selectedCardType = method }
                                    )
                                    Text(
                                        text = method,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }

                        // Formulário do cartão (aparece apenas para cartões)
                        if (selectedCardType.contains("Cartão")) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CreditCard, contentDescription = "Cartão")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Dados do Cartão", style = MaterialTheme.typography.titleSmall)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = {
                                    if (it.length <= 19) cardNumber = it.formatCardNumber()
                                },
                                label = { Text("Número do Cartão") },
                                placeholder = { Text("0000 0000 0000 0000") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = cardHolder,
                                onValueChange = { cardHolder = it },
                                label = { Text("Nome no Cartão") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(
                                    value = expiryDate,
                                    onValueChange = {
                                        if (it.length <= 5) expiryDate = it.formatExpiryDate()
                                    },
                                    label = { Text("Validade") },
                                    placeholder = { Text("MM/AA") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = cvv,
                                    onValueChange = {
                                        if (it.length <= 3) cvv = it
                                    },
                                    label = { Text("CVV") },
                                    placeholder = { Text("123") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // --- SEÇÃO 6: BOTÃO DE CONFIRMAÇÃO ---
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onReservationConfirmed,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = if (selectedCardType.contains("Cartão")) {
                            cardNumber.isNotBlank() && cardHolder.isNotBlank() &&
                                    expiryDate.isNotBlank() && cvv.isNotBlank()
                        } else true
                    ) {
                        Text("Confirmar Reserva - ${currencyFormat.format(finalPrice)}")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    currencyFormat: NumberFormat
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cartItem.menuItem.name, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${currencyFormat.format(cartItem.menuItem.price)} cada",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                "× ${cartItem.quantity}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Text(
                currencyFormat.format(cartItem.totalPrice),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Funções de formatação (mantenha as mesmas)
private fun String.formatCardNumber(): String {
    val cleaned = this.replace(" ", "").take(16)
    return cleaned.chunked(4).joinToString(" ")
}

private fun String.formatExpiryDate(): String {
    val cleaned = this.replace("/", "").take(4)
    return if (cleaned.length > 2) {
        "${cleaned.take(2)}/${cleaned.drop(2)}"
    } else {
        cleaned
    }
}