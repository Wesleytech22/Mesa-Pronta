package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.Restaurant

data class MenuItem(
    val name: String,
    val price: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewOrderScreen(
    restaurant: Restaurant,
    selectedItems: List<MenuItem>,
    onBack: () -> Unit,
    onAddMoreMeals: () -> Unit,
    onGoToPaymentOptions: () -> Unit
) {
    var couponCode by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf(0.0) }

    val totalPrice = selectedItems.sumOf { it.price }
    val discountedTotal = (totalPrice - discount).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Revisar Pedido") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Restaurante: ${restaurant.name}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(selectedItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.name)
                        Text("R$ %.2f".format(item.price))
                    }
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de cupom
            OutlinedTextField(
                value = couponCode,
                onValueChange = { couponCode = it },
                label = { Text("Cupom de desconto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    discount = if (couponCode.equals("MESA10", ignoreCase = true)) {
                        totalPrice * 0.10
                    } else 0.0
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Cupom")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Total visualmente destacado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Total a pagar:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "R$ %.2f".format(discountedTotal),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botões principais
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = onAddMoreMeals, modifier = Modifier.weight(1f)) {
                    Text("Incluir mais refeições")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onGoToPaymentOptions, modifier = Modifier.weight(1f)) {
                    Text("Pagar")
                }
            }
        }
    }
}
