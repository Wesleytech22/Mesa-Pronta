//package com.mesapronta.app.ui.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.mesapronta.app.model.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun OrderScreen(
//    dish: Dish,
//    restaurant: RestaurantData,
//    onBackClick: () -> Unit,
//    onNewOrderClick: () -> Unit,
//    onPaymentClick: (Order) -> Unit
//) {
//    var quantity by remember { mutableStateOf(1) }
//    var consumptionType by remember { mutableStateOf(ConsumptionType.DINE_IN) }
//    var selectedTable by remember { mutableStateOf<Int?>(1) }
//    var deliveryAddress by remember { mutableStateOf("") }
//
//    // Cálculo de valores
//    val dishPrice = dish.price
//    val subtotal = dishPrice * quantity
//    val additionalFee = calculateAdditionalFee(consumptionType, restaurant.availableTables)
//    val total = subtotal + additionalFee
//
//    val order = Order(
//        id = 1,
//        dish = dish,
//        quantity = quantity,
//        consumptionType = consumptionType,
//        selectedTable = selectedTable,
//        deliveryAddress = deliveryAddress,
//        subtotal = subtotal,
//        additionalFee = additionalFee,
//        total = total
//    )
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        "Fazer Pedido",
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState())
//        ) {
//            // Informações do Prato
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = dish.name,
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        text = dish.description,
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        modifier = Modifier.padding(top = 4.dp)
//                    )
//                    Text(
//                        text = "R$ %.2f".format(dishPrice),
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//
//            // Seção: Quantidade
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "Quantidade",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(bottom = 12.dp)
//                    )
//
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Button(
//                            onClick = { if (quantity > 1) quantity-- },
//                            modifier = Modifier.size(48.dp)
//                        ) {
//                            Text("-")
//                        }
//
//                        Text(
//                            text = quantity.toString(),
//                            style = MaterialTheme.typography.headlineMedium,
//                            fontWeight = FontWeight.Bold
//                        )
//
//                        Button(
//                            onClick = { quantity++ },
//                            modifier = Modifier.size(48.dp)
//                        ) {
//                            Text("+")
//                        }
//                    }
//                }
//            }
//
//            // Seção: Tipo de Consumo
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "Como você vai consumir?",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(bottom = 12.dp)
//                    )
//
//                    ConsumptionType.entries.forEach { type ->
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp)
//                        ) {
//                            RadioButton(
//                                selected = consumptionType == type,
//                                onClick = {
//                                    consumptionType = type
//                                    // Reset valores quando mudar o tipo
//                                    if (type != ConsumptionType.DINE_IN) selectedTable = null
//                                    if (type != ConsumptionType.DELIVERY) deliveryAddress = ""
//                                }
//                            )
//                            Text(
//                                text = when (type) {
//                                    ConsumptionType.DINE_IN -> "🍽️ Reservar Mesa no Restaurante"
//                                    ConsumptionType.TAKEAWAY -> "🥡 Retirar no Balcão"
//                                    ConsumptionType.DELIVERY -> "🏍️ Entrega via Motoboy"
//                                },
//                                style = MaterialTheme.typography.bodyMedium,
//                                modifier = Modifier.padding(start = 8.dp)
//                            )
//                        }
//                    }
//
//                    // Campo para selecionar mesa (apenas para DINE_IN)
//                    if (consumptionType == ConsumptionType.DINE_IN) {
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            text = "Selecione a Mesa:",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Medium
//                        )
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            modifier = Modifier.padding(top = 8.dp)
//                        ) {
//                            (1..restaurant.availableTables).forEach { tableNumber ->
//                                FilterChip(
//                                    selected = selectedTable == tableNumber,
//                                    onClick = { selectedTable = tableNumber },
//                                    label = { Text("Mesa $tableNumber") }
//                                )
//                            }
//                        }
//                    }
//
//                    // Campo para endereço (apenas para DELIVERY)
//                    if (consumptionType == ConsumptionType.DELIVERY) {
//                        Spacer(modifier = Modifier.height(16.dp))
//                        OutlinedTextField(
//                            value = deliveryAddress,
//                            onValueChange = { deliveryAddress = it },
//                            label = { Text("Endereço de entrega") },
//                            modifier = Modifier.fillMaxWidth(),
//                            placeholder = { Text("Digite seu endereço completo") }
//                        )
//                    }
//                }
//            }
//
//            // Seção: Resumo do Pedido
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer
//                )
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "💰 Resumo do Pedido",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(bottom = 12.dp)
//                    )
//
//                    OrderSummaryRow("Subtotal (${quantity}x)", "R$ %.2f".format(subtotal))
//                    OrderSummaryRow(
//                        "Taxa adicional",
//                        "R$ %.2f".format(additionalFee),
//                        when (consumptionType) {
//                            ConsumptionType.DINE_IN -> "Reserva de mesa"
//                            ConsumptionType.TAKEAWAY -> "Sem taxa"
//                            ConsumptionType.DELIVERY -> "Entrega via motoboy"
//                        }
//                    )
//
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//                    OrderSummaryRow(
//                        "Total",
//                        "R$ %.2f".format(total),
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//
//                    // Informação sobre taxa adicional
//                    if (additionalFee > 0) {
//                        Text(
//                            text = when (consumptionType) {
//                                ConsumptionType.DINE_IN -> "💡 Taxa reembolsável se chegar no horário"
//                                ConsumptionType.DELIVERY -> "🚚 Taxa de entrega incluída"
//                                else -> ""
//                            },
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
//                    }
//                }
//            }
//
//            // Botões de Ação
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Button(
//                    onClick = onNewOrderClick,
//                    modifier = Modifier.weight(1f),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                ) {
//                    Text("Novo Pedido")
//                }
//
//                Button(
//                    onClick = { onPaymentClick(order) },
//                    modifier = Modifier.weight(1f),
//                    enabled = when (consumptionType) {
//                        ConsumptionType.DINE_IN -> selectedTable != null
//                        ConsumptionType.DELIVERY -> deliveryAddress.isNotBlank()
//                        else -> true
//                    }
//                ) {
//                    Text("Pagamento")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//
//@Composable
//fun OrderSummaryRow(
//    label: String,
//    value: String,
//    description: String = "",
//    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
//    fontWeight: FontWeight = FontWeight.Normal,
//    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//    ) {
//        Column {
//            Text(
//                text = label,
//                style = style,
//                fontWeight = fontWeight,
//                color = color
//            )
//            if (description.isNotEmpty()) {
//                Text(
//                    text = description,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//        Text(
//            text = value,
//            style = style,
//            fontWeight = fontWeight,
//            color = color
//        )
//    }
//}
//
//// Função para calcular taxa adicional
//fun calculateAdditionalFee(consumptionType: ConsumptionType, availableTables: Int): Double {
//    return when (consumptionType) {
//        ConsumptionType.DINE_IN -> 5.00 // Taxa de reserva (reembolsável)
//        ConsumptionType.DELIVERY -> 8.00 // Taxa de entrega
//        ConsumptionType.TAKEAWAY -> 0.00 // Sem taxa
//    }
//}