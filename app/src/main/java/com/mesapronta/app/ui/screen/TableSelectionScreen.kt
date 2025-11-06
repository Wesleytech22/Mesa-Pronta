package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // CORREÇÃO 1: Importa o ícone Add
import androidx.compose.material.icons.filled.Remove // CORREÇÃO 2: Importa o ícone Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.model.ReservationDetails

// Definição de Mesa (Simulação)
data class Table(val number: Int, val capacity: Int)

val availableTables = listOf(
    Table(1, 2), Table(2, 4), Table(3, 2),
    Table(4, 6), Table(5, 4), Table(6, 8)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableSelectionScreen(
    restaurant: Restaurant,
    selectedTime: String,
    onBack: () -> Unit,
    onContinueToPayment: (ReservationDetails) -> Unit
) {
    var peopleCount by remember { mutableStateOf(2) }
    var selectedTable by remember { mutableStateOf<Table?>(null) }

    val filteredTables = remember(peopleCount) {
        availableTables.filter { it.capacity >= peopleCount }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mesa: ${restaurant.name}") },
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
            Text("Horário: $selectedTime", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // --- Seleção de Pessoas (Onde os ícones são usados) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pessoas:", style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (peopleCount > 1) peopleCount-- }) {
                        Icon(Icons.Default.Remove, contentDescription = "Menos") // Agora resolvido
                    }
                    Text(peopleCount.toString(), modifier = Modifier.padding(horizontal = 16.dp))
                    IconButton(onClick = { peopleCount++ }) {
                        Icon(Icons.Default.Add, contentDescription = "Mais") // Agora resolvido
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Seleção de Mesas Disponíveis ---
            Text("Mesas disponíveis (Capacidade Mínima: $peopleCount)", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                filteredTables.forEach { table ->
                    FilterChip(
                        selected = selectedTable == table,
                        onClick = { selectedTable = table },
                        label = { Text("Mesa ${table.number} (${table.capacity}p)") }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    selectedTable?.let { table ->
                        val reservation = ReservationDetails(
                            restaurant = restaurant,
                            time = selectedTime,
                            people = peopleCount,
                            tableNumber = table.number
                        )
                        onContinueToPayment(reservation)
                    }
                },
                enabled = selectedTable != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar para o Pagamento")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}