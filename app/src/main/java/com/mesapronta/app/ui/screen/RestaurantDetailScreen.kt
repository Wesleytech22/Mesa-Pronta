package com.mesapronta.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    onBack: () -> Unit,
    // Assinatura correta que aceita Restaurant e o horário (String)
    onReserveClicked: (Restaurant, String) -> Unit
) {
    var selectedTime by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurant.name) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Voltar") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(restaurant.description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(24.dp))

            Text("Cardápio", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(restaurant.menu) { item ->
                        Text("🍽 $item", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Horários Disponíveis", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(restaurant.availableTimes) { time ->
                        FilterChip(
                            selected = time == selectedTime,
                            onClick = { selectedTime = time },
                            label = { Text(time) }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    selectedTime?.let {
                        if (restaurant.isOpen) {
                            onReserveClicked(restaurant, it)
                        }
                    }
                },
                enabled = restaurant.isOpen && selectedTime != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (restaurant.isOpen) "Reservar Mesa" else "Restaurante Fechado")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

