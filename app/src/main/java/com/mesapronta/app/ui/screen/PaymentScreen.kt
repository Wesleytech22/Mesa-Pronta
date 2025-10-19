package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment // MUDEI AQUI
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.Order
import com.mesapronta.app.model.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    order: Order,
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit
) {
    var paymentMethod by remember { mutableStateOf(PaymentMethod.CREDIT_CARD) }
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Pagamento",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Resumo do Pedido
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📦 Resumo do Pedido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Order items
                    order.items.forEach { item ->
                        OrderSummaryRow(
                            "${item.quantity}x ${item.dish.name}",
                            "R$ %.2f".format(item.dish.price * item.quantity)
                        )
                    }

                    if (order.selectedTable != null) {
                        OrderSummaryRow("Mesa ${order.selectedTable}", "R$ 0.00")
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    OrderSummaryRow(
                        "Total",
                        "R$ %.2f".format(order.total),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Seção: Método de Pagamento
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "💳 Método de Pagamento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    PaymentMethod.entries.forEach { method ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = paymentMethod == method,
                                onClick = { paymentMethod = method }
                            )
                            Text(
                                text = when (method) {
                                    PaymentMethod.CREDIT_CARD -> "Cartão de Crédito"
                                    PaymentMethod.DEBIT_CARD -> "Cartão de Débito"
                                    PaymentMethod.MEAL_VOUCHER -> "Vale Refeição"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            // Campos do Cartão (apenas para cartões)
            if (paymentMethod == PaymentMethod.CREDIT_CARD || paymentMethod == PaymentMethod.DEBIT_CARD) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Dados do Cartão",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = {
                                if (it.length <= 19) cardNumber = it
                            },
                            label = { Text("Número do Cartão") },
                            placeholder = { Text("1234 5678 9012 3456") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            visualTransformation = CardNumberFilter // JÁ CORRIGIDO
                        )

                        OutlinedTextField(
                            value = cardHolder,
                            onValueChange = { cardHolder = it },
                            label = { Text("Nome no Cartão") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = expiryDate,
                                onValueChange = {
                                    if (it.length <= 5) expiryDate = it
                                },
                                label = { Text("Validade") },
                                placeholder = { Text("MM/AA") },
                                modifier = Modifier.weight(1f),
                                visualTransformation = CardExpiryFilter // JÁ CORRIGIDO
                            )

                            OutlinedTextField(
                                value = cvv,
                                onValueChange = {
                                    if (it.length <= 3) cvv = it
                                },
                                label = { Text("CVV") },
                                placeholder = { Text("123") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Botão de Finalizar Pagamento
            Button(
                onClick = {
                    isProcessing = true
                    // Simular processamento
                    onPaymentSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = when (paymentMethod) {
                    PaymentMethod.CREDIT_CARD, PaymentMethod.DEBIT_CARD ->
                        cardNumber.isNotBlank() && cardHolder.isNotBlank() &&
                                expiryDate.isNotBlank() && cvv.isNotBlank()
                    PaymentMethod.MEAL_VOUCHER -> true
                }
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        Icons.Default.Payment, // MUDEI AQUI - de CreditCard para Payment
                        contentDescription = "Pagar",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        "Finalizar Pagamento - R$ %.2f".format(order.total),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OrderSummaryRow(
    label: String,
    value: String,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight? = null,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = style, fontWeight = fontWeight, color = color)
        Text(text = value, style = style, fontWeight = fontWeight, color = color)
    }
}

// Filtros para formatação - JÁ ESTÃO CORRETOS
val CardNumberFilter = androidx.compose.ui.text.input.VisualTransformation { text ->
    val trimmed = text.text.take(16)
    val formatted = trimmed.chunked(4).joinToString(" ")
    androidx.compose.ui.text.input.TransformedText(
        androidx.compose.ui.text.AnnotatedString(formatted),
        cardNumberOffsetTranslator
    )
}

val CardExpiryFilter = androidx.compose.ui.text.input.VisualTransformation { text ->
    val trimmed = text.text.take(4)
    val formatted = if (trimmed.length >= 2) {
        "${trimmed.take(2)}/${trimmed.drop(2)}"
    } else {
        trimmed
    }
    androidx.compose.ui.text.input.TransformedText(
        androidx.compose.ui.text.AnnotatedString(formatted),
        cardExpiryOffsetTranslator
    )
}

private val cardNumberOffsetTranslator = object : androidx.compose.ui.text.input.OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return when (offset) {
            in 0..4 -> offset
            in 5..8 -> offset + 1
            in 9..12 -> offset + 2
            in 13..16 -> offset + 3
            else -> offset + 3
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        return when (offset) {
            in 0..4 -> offset
            in 6..9 -> offset - 1
            in 11..14 -> offset - 2
            in 16..19 -> offset - 3
            else -> offset - 3
        }
    }
}

private val cardExpiryOffsetTranslator = object : androidx.compose.ui.text.input.OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return if (offset <= 2) offset else offset + 1
    }

    override fun transformedToOriginal(offset: Int): Int {
        return if (offset <= 2) offset else offset - 1
    }
}