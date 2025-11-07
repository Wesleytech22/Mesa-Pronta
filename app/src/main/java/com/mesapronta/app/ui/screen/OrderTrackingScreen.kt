package com.mesapronta.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(modifier: Modifier = Modifier) {
    var orderStatus by remember { mutableStateOf(OrderStatus.CONFIRMED) }
    var timeElapsed by remember { mutableStateOf(0) }
    var estimatedTime by remember { mutableStateOf(25) }

    // Timer em tempo real
    LaunchedEffect(key1 = orderStatus) {
        while (timeElapsed < 3600 && orderStatus != OrderStatus.DELIVERED) {
            delay(1000L)
            timeElapsed++

            when (timeElapsed) {
                in 0..300 -> estimatedTime = 25
                in 301..600 -> estimatedTime = 20
                in 601..900 -> estimatedTime = 15
                in 901..1200 -> estimatedTime = 10
                in 1201..1500 -> estimatedTime = 5
                else -> estimatedTime = 0
            }

            when (timeElapsed) {
                120 -> orderStatus = OrderStatus.PREPARING
                480 -> orderStatus = OrderStatus.READY
                900 -> orderStatus = OrderStatus.ON_THE_WAY
                1200 -> orderStatus = OrderStatus.DELIVERED
            }
        }
    }

    val currentOrder = remember(orderStatus, timeElapsed) {
        CurrentOrder(
            id = "ORD-${System.currentTimeMillis().toString().takeLast(6)}",
            restaurantName = "Pizzaria do DED",
            status = orderStatus,
            timeElapsed = timeElapsed,
            estimatedTime = estimatedTime,
            items = listOf("Pizza Margherita", "Coca-Cola 2L", "Brownie"),
            total = 89.90
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acompanhar Pedido") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            OrderHeader(currentOrder)
            OrderTimeline(currentStatus = orderStatus, timeElapsed = timeElapsed)
            OrderDetails(currentOrder)
            OrderControls(
                currentStatus = orderStatus,
                onContactRestaurant = { /* Abrir chat */ },
                onCancelOrder = { /* Cancelar pedido */ }
            )
        }
    }
}

enum class OrderStatus {
    CONFIRMED, PREPARING, READY, ON_THE_WAY, DELIVERED
}

data class CurrentOrder(
    val id: String,
    val restaurantName: String,
    val status: OrderStatus,
    val timeElapsed: Int,
    val estimatedTime: Int,
    val items: List<String>,
    val total: Double
)

@Composable
fun OrderHeader(order: CurrentOrder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        order.restaurantName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Pedido #${order.id}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RealTimeTimer(timeElapsed = order.timeElapsed)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = calculateProgress(order.status),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Status: ${getStatusText(order.status)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    if (order.estimatedTime > 0) "⏱ ${order.estimatedTime} min" else "🕐 Chegando...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RealTimeTimer(timeElapsed: Int) {
    val minutes = timeElapsed / 60
    val seconds = timeElapsed % 60

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Schedule,
                contentDescription = "Tempo decorrido",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OrderTimeline(currentStatus: OrderStatus, timeElapsed: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Andamento do Pedido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val steps = listOf(
                TimelineStep(OrderStatus.CONFIRMED, "Pedido Confirmado", Icons.Default.CheckCircle),
                TimelineStep(OrderStatus.PREPARING, "Preparando", Icons.Default.RestaurantMenu),
                TimelineStep(OrderStatus.READY, "Pronto", Icons.Default.Verified),
                TimelineStep(OrderStatus.ON_THE_WAY, "A Caminho", Icons.Default.DirectionsWalk),
                TimelineStep(OrderStatus.DELIVERED, "Entregue", Icons.Default.DoneAll)
            )

            Column {
                for ((index, step) in steps.withIndex()) {
                    val isCompleted = steps.indexOf<Any>(step.status) <= steps.indexOf<Any>(currentStatus)
                    val isCurrent = step.status == currentStatus

                    TimelineStepItem(
                        step = step,
                        isCompleted = isCompleted,
                        isCurrent = isCurrent,
                        isLast = index == steps.size - 1
                    )
                    if (index < steps.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

data class TimelineStep(
    val status: OrderStatus,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun TimelineStepItem(
    step: TimelineStep,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = step.icon,
                contentDescription = step.title,
                tint = if (isCompleted) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            if (!isLast) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(if (isCompleted) MaterialTheme.colorScheme.primary else Color.Gray)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                step.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isCompleted) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}

@Composable
fun OrderDetails(order: CurrentOrder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Detalhes do Pedido",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            order.items.forEach { item ->
                Text("• $item", modifier = Modifier.padding(vertical = 2.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Total: R$ ${"%.2f".format(order.total)}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OrderControls(
    currentStatus: OrderStatus,
    onContactRestaurant: () -> Unit,
    onCancelOrder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onContactRestaurant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Chat, contentDescription = "Chat")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Falar com o Restaurante")
        }

        if (currentStatus == OrderStatus.CONFIRMED || currentStatus == OrderStatus.PREPARING) {
            OutlinedButton(
                onClick = onCancelOrder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar Pedido")
            }
        }
    }
}

// Funções auxiliares
private fun calculateProgress(status: OrderStatus): Float {
    return when (status) {
        OrderStatus.CONFIRMED -> 0.2f
        OrderStatus.PREPARING -> 0.4f
        OrderStatus.READY -> 0.6f
        OrderStatus.ON_THE_WAY -> 0.8f
        OrderStatus.DELIVERED -> 1.0f
    }
}

private fun getStatusText(status: OrderStatus): String {
    return when (status) {
        OrderStatus.CONFIRMED -> "Confirmado"
        OrderStatus.PREPARING -> "Preparando"
        OrderStatus.READY -> "Pronto"
        OrderStatus.ON_THE_WAY -> "A Caminho"
        OrderStatus.DELIVERED -> "Entregue"
    }
}