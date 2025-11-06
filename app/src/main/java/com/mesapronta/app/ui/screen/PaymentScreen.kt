package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.ReservationDetails // Importa o modelo de dados

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    reservation: ReservationDetails,
    onReservationConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmação de Reserva") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Voltar") }
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
            Text("Detalhes da Reserva", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // --- Resumo da Reserva ---
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Restaurante:", fontWeight = FontWeight.Bold)
                    Text(reservation.restaurant.name)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Horário:", fontWeight = FontWeight.Bold)
                    Text(reservation.time)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Pessoas / Mesa:", fontWeight = FontWeight.Bold)
                    Text("${reservation.people} pessoas na Mesa ${reservation.tableNumber}")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Opções de Pagamento (Simulação) ---
            Text("Pagamento", style = MaterialTheme.typography.titleLarge)
            Text("Simulação: A reserva será confirmada ao clicar em 'Confirmar'.")

            Spacer(modifier = Modifier.weight(1f))

            // --- Botão de Confirmação Final ---
            Button(
                onClick = onReservationConfirmed,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("CONFIRMAR RESERVA")
            }
        }
    }
}