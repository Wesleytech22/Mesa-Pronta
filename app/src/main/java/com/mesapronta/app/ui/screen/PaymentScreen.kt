package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.CartItem
import com.mesapronta.app.model.ReservationDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    reservation: ReservationDetails,
    cartItems: List<CartItem>,
    couvertAmount: Double,
    onReservationConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    val totalItems = cartItems.sumOf { it.totalPrice }
    val totalAmount = totalItems + couvertAmount

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagamento da Reserva") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Voltar") } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Detalhes da Reserva
            Text("Detalhes da Reserva", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    // ... (Detalhes da reserva e do pedido)
                    Text("Restaurante: ${reservation.restaurantName}")
                    Text("Horário: ${reservation.reservationTime}")
                    Text("Pessoas: ${reservation.numberOfPeople}")
                }
            }
            Spacer(Modifier.height(16.dp))

            // Resumo de Valores
            Text("Resumo do Pedido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    // ... (Valores)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Itens:")
                        Text("R$ ${"%.2f".format(totalItems)}")
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Couvert Artístico:")
                        Text("R$ ${"%.2f".format(couvertAmount)}")
                    }
                    Divider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total a Pagar:", fontWeight = FontWeight.Bold)
                        Text("R$ ${"%.2f".format(totalAmount)}", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.weight(1f))

            // Botão de Pagamento
            Button(
                onClick = onReservationConfirmed,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = totalAmount > 0
            ) {
                Text("Confirmar Pagamento e Reserva")
            }
        }
    }
}