package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.OrderStatus

data class SimpleOrder(val id: String, val restaurantName: String, val date: String, val status: OrderStatus, val total: Double)

val sampleOrders = listOf(
    SimpleOrder("ORD001", "Pizzaria do DED", "01/11/2025", OrderStatus.DELIVERED, 55.90),
    SimpleOrder("ORD002", "Sushi House", "25/10/2025", OrderStatus.COLLECTED, 120.00),
    SimpleOrder("ORD003", "Churrascaria Gaúcha", "10/10/2025", OrderStatus.DELIVERED, 89.50)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Meus Pedidos", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (sampleOrders.isEmpty()) {
                item {
                    EmptyOrderHistory()
                }
            } else {
                item {
                    Text(
                        "Histórico",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(sampleOrders) { order ->
                    OrderHistoryCard(order)
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(order: SimpleOrder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = { /* Navegar para detalhes do pedido */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(order.restaurantName, fontWeight = FontWeight.Bold)
                Text("R$ ${"%.2f".format(order.total).replace('.', ',')}", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pedido #${order.id}", style = MaterialTheme.typography.bodySmall)
                Text(order.date, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Status: ${order.status.name.lowercase().replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun EmptyOrderHistory() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.History,
            contentDescription = "Sem pedidos",
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Você ainda não fez nenhum pedido.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}