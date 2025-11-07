package com.mesapronta.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Done
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
    var currentStep by remember { mutableStateOf(0) }
    var estimatedTime by remember { mutableStateOf(25) }

    // Simulação de tempo real
    LaunchedEffect(key1 = currentStep) {
        while (currentStep < 3) {
            delay(30000L) // Atualiza a cada 30 segundos
            currentStep++
            estimatedTime = maxOf(0, estimatedTime - 10)
        }
    }

    val steps = listOf(
        TrackingStep("Confirmado", "Pedido confirmado pelo restaurante", Icons.Default.Done),
        TrackingStep("Preparando", "Seu pedido está sendo preparado", Icons.Default.RestaurantMenu),
        TrackingStep("Pronto", "Pedido pronto para entrega", Icons.Default.Timer),
        TrackingStep("Entregue", "Pedido entregue", Icons.Default.LocalShipping)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acompanhar Pedido") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Informações do pedido atual
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Pizzaria do DED",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pedido: #ORD002")
                    Text("Tempo estimado: $estimatedTime min")
                    Text("Status: ${steps[currentStep].title}")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Timeline do pedido
            Text("Andamento do Pedido", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Column {
                steps.forEachIndexed { index, step ->
                    TrackingStepItem(
                        step = step,
                        isCompleted = index <= currentStep,
                        isCurrent = index == currentStep,
                        isLast = index == steps.size - 1
                    )
                    if (index < steps.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de atualizar
            Button(
                onClick = { /* Atualizar status */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Atualizar Status")
            }
        }
    }
}

data class TrackingStep(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun TrackingStepItem(
    step: TrackingStep,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(verticalAlignment = Alignment.Top) {
        // Ícone e linha de conexão
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

        // Textos
        Column(modifier = Modifier.weight(1f)) {
            Text(
                step.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isCompleted) MaterialTheme.colorScheme.primary else Color.Gray
            )
            Text(
                step.description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isCompleted) MaterialTheme.colorScheme.onSurface else Color.Gray
            )
        }
    }
}