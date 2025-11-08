package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mesapronta.app.model.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    onBack: () -> Unit,
    onReserveClicked: (Restaurant, String, Int) -> Unit
) {
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var peopleCount by remember { mutableStateOf(4) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurant.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            BottomBarDetail(
                restaurant = restaurant,
                selectedTime = selectedTime,
                peopleCount = peopleCount,
                onPeopleCountChange = { peopleCount = it },
                onReserveClicked = {
                    if (selectedTime != null) {
                        onReserveClicked(restaurant, selectedTime!!, peopleCount)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                AsyncImage(
                    model = restaurant.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            item { RestaurantInfo(restaurant) }
            item { TimeSlotSelection(restaurant.availableTimes, selectedTime) { selectedTime = it } }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

// Sub-componentes omitidos para brevidade (RestaurantInfo, TimeSlotSelection, BottomBarDetail)
@Composable
fun RestaurantInfo(restaurant: Restaurant) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(restaurant.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(restaurant.description, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun TimeSlotSelection(availableTimes: List<String>, selectedTime: String?, onTimeSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Horários Disponíveis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        // Implementação básica com LazyRow
    }
}

@Composable
fun BottomBarDetail(restaurant: Restaurant, selectedTime: String?, peopleCount: Int, onPeopleCountChange: (Int) -> Unit, onReserveClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Seletor de Pessoas e botão de Reservar
            Button(onClick = onReserveClicked, enabled = selectedTime != null, modifier = Modifier.fillMaxWidth()) {
                Text("Reservar")
            }
        }
    }
}