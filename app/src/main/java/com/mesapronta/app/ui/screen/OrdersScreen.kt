package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(modifier: Modifier = Modifier) {
    val orders = listOf(
        Order(
            id = "ORD001",
            restaurantName = "Pizzaria do DED",
            date = "15 Nov 2024",
            status = "Entregue",
            total = 89.90,
            items = listOf("Pizza Margherita", "Coca-Cola 2L")
        ),
        Order(
            id = "ORD002",
            restaurantName = "Sushi House",
            date = "14 Nov 2024",
            status = "Preparando",
            total = 156.50,
            items = listOf("Combinado Salmão", "Temaki")
        ),
        Order(
            id = "ORD003",
            restaurantName = "Churrasco do Zé",
            date = "13 Nov 2024",
            status = "Confirmado",
            total = 230.00,
            items = listOf("Picanha", "Arroz", "Farofa")
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pedidos") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(orders) { order ->
                OrderCard(order = order)
            }
        }
    }
}

data class Order(
    val id: String,
    val restaurantName: String,
    val date: String,
    val status: String,
    val total: Double,
    val items: List<String>
)

@Composable
fun OrderCard(order: Order) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    order.restaurantName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    order.status,
                    color = when (order.status) {
                        "Entregue" -> Color.Green
                        "Preparando" -> Color(0xFFFFA500)
                        "Confirmado" -> Color.Blue
                        else -> Color.Gray
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Pedido: ${order.id}", style = MaterialTheme.typography.bodySmall)
            Text("Data: ${order.date}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                order.items.forEach { item ->
                    Text("• $item", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Total: ${currencyFormat.format(order.total)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}