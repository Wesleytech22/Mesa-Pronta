package com.mesapronta.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val orders = listOf(
        "Pizzaria do DED — Unidade Central",
        "Hamburgueria Black House Burgers Premium Delivery",
        "Sushi Top Express Fast Fresh Delivery"
    )

    var currentOrderIndex by rememberSaveable { mutableStateOf(0) }
    var orderStatus by rememberSaveable { mutableStateOf(OrderStatus.CONFIRMED) }
    var timeElapsed by rememberSaveable { mutableStateOf(0) }
    var estimatedTime by rememberSaveable { mutableStateOf(5) }

    val totalTime = 15 // menor para teste

    // TIMER
    LaunchedEffect(currentOrderIndex) {
        timeElapsed = 0
        orderStatus = OrderStatus.CONFIRMED

        while (timeElapsed < totalTime) {
            delay(1000L)
            timeElapsed++

            val progress = timeElapsed.toFloat() / totalTime.toFloat()

            orderStatus = when {
                progress < 0.25f -> OrderStatus.CONFIRMED
                progress < 0.50f -> OrderStatus.PREPARING
                progress < 0.75f -> OrderStatus.READY
                else -> OrderStatus.DELIVERED
            }

            estimatedTime = (totalTime - timeElapsed) / 3 + 1
        }

        delay(500)

        if (currentOrderIndex < orders.lastIndex) {
            currentOrderIndex++
        } else {
            Toast.makeText(
                context,
                "Pedidos finalizados, prontos para o consumo!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val currentOrder = CurrentOrder(
        id = "ORD-${System.currentTimeMillis().toString().takeLast(6)}",
        restaurantName = orders[currentOrderIndex],
        status = orderStatus,
        timeElapsed = timeElapsed,
        estimatedTime = estimatedTime,
        items = listOf("Pizza Margherita", "Refrigerante", "Brownie"),
        total = 89.90
    )

    // Criar lista de pedidos para o LazyRow
    val allOrders = remember {
        orders.mapIndexed { index, restaurantName ->
            CurrentOrder(
                id = "ORD-${1000 + index}",
                restaurantName = restaurantName,
                status = if (index == currentOrderIndex) orderStatus else
                    if (index < currentOrderIndex) OrderStatus.DELIVERED else OrderStatus.CONFIRMED,
                timeElapsed = if (index == currentOrderIndex) timeElapsed else 0,
                estimatedTime = if (index == currentOrderIndex) estimatedTime else 5,
                items = listOf("Item ${index + 1}A", "Item ${index + 1}B", "Item ${index + 1}C"),
                total = 89.90 + (index * 10)
            )
        }
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
            // LazyRow para mostrar múltiplos pedidos
            OrdersCarousel(
                orders = allOrders,
                currentOrderIndex = currentOrderIndex,
                onOrderSelected = { index ->
                    coroutineScope.launch {
                        currentOrderIndex = index
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OrderHeader(currentOrder)
            OrderTimeline(currentOrder.status, currentOrder.timeElapsed)
            OrderDetails(currentOrder)
            OrderControls(
                currentStatus = currentOrder.status,
                onContactRestaurant = {},
                onCancelOrder = {}
            )
        }
    }
}

@Composable
fun OrdersCarousel(
    orders: List<CurrentOrder>,
    currentOrderIndex: Int,
    onOrderSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = currentOrderIndex
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Seus Pedidos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(orders) { order ->
                OrderCardItem(
                    order = order,
                    isSelected = orders.indexOf(order) == currentOrderIndex,
                    onClick = { onOrderSelected(orders.indexOf(order)) }
                )
            }
        }
    }
}

@Composable
fun OrderCardItem(
    order: CurrentOrder,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(280.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder()
        } else {
            null
        },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    order.restaurantName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Pedido #${order.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    getStatusText(order.status),
                    style = MaterialTheme.typography.labelSmall,
                    color = when (order.status) {
                        OrderStatus.DELIVERED -> MaterialTheme.colorScheme.primary
                        OrderStatus.READY -> MaterialTheme.colorScheme.secondary
                        OrderStatus.PREPARING -> MaterialTheme.colorScheme.tertiary
                        OrderStatus.CONFIRMED -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

                Text(
                    "R$ ${"%.2f".format(order.total)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// O resto do código permanece igual (enum, data class, e outras composables)...

enum class OrderStatus {
    CONFIRMED, PREPARING, READY, DELIVERED
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
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(Modifier.weight(1f)) {

                    Text(
                        order.restaurantName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        "Pedido #${order.id}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                RealTimeTimer(order.timeElapsed)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = calculateProgress(order.status),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Status: ${getStatusText(order.status)}")
                Text("⏱ ${order.estimatedTime} min")
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

data class TimelineStep(
    val status: OrderStatus,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

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
                TimelineStep(OrderStatus.DELIVERED, "Entregue", Icons.Default.DoneAll)
            )

            Column {
                steps.forEachIndexed { index, step ->

                    val isCompleted = steps.indexOfFirst { it.status == step.status } <=
                            steps.indexOfFirst { it.status == currentStatus }

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
                        .background(
                            if (isCompleted) MaterialTheme.colorScheme.primary else Color.Gray
                        )
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

private fun calculateProgress(status: OrderStatus): Float {
    return when (status) {
        OrderStatus.CONFIRMED -> 0.2f
        OrderStatus.PREPARING -> 0.4f
        OrderStatus.READY -> 0.6f
        OrderStatus.DELIVERED -> 1.0f
    }
}

private fun getStatusText(status: OrderStatus): String {
    return when (status) {
        OrderStatus.CONFIRMED -> "Confirmado"
        OrderStatus.PREPARING -> "Preparando"
        OrderStatus.READY -> "Pronto"
        OrderStatus.DELIVERED -> "Entregue"
    }
}