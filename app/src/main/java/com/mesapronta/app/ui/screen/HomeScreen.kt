package com.mesapronta.app.ui.screen

// ... (Imports omitidos por concisão - use os do original)
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mesapronta.app.model.ReadyOrder
import com.mesapronta.app.model.ReservationDetails
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.viewmodel.ReadyOrdersViewModel

val sampleReservations = listOf(
    ReservationDetails("RES001", "Pizzaria do DED", 5, "19:00", 4, 150.0),
    ReservationDetails("RES002", "Japão Express", 12, "21:30", 2, 80.0)
)
val sampleRestaurants = listOf(
    Restaurant(1, "Pizzaria do DED", "Massas artesanais", "Italiana", 4.8f, "20-30 min", "https://i.imgur.com/G5gDk7E.jpeg", "São Paulo", emptyList(), emptyList()),
    Restaurant(2, "Sushi House", "Peixes frescos", "Japonesa", 4.6f, "25-35 min", "https://i.imgur.com/8QG3y4P.jpeg", "Rio de Janeiro", emptyList(), emptyList())
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onRestaurantSelected: (Restaurant) -> Unit,
    onCheckInSelected: (ReservationDetails) -> Unit,
    readyOrdersViewModel: ReadyOrdersViewModel,
    modifier: Modifier = Modifier
) {
    val readyOrders by readyOrdersViewModel.readyOrders.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mesa Pronta", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Sair")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { CurrentReservations(sampleReservations, onCheckInSelected) }
            item { ReadyOrderBanner(readyOrders) }
            item { RestaurantList(sampleRestaurants, onRestaurantSelected) }
            item { Spacer(modifier = Modifier.height(70.dp)) }
        }
    }
}

// Sub-componente 1: Reservas Atuais
@Composable
fun CurrentReservations(
    reservations: List<ReservationDetails>,
    onCheckInSelected: (ReservationDetails) -> Unit
) { /* ... Implementação do card de reserva ... */
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Minhas Reservas Ativas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        reservations.forEach { res ->
            Card(onClick = { onCheckInSelected(res) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(res.restaurantName, fontWeight = FontWeight.Bold)
                        Text("Mesa ${res.tableNumber} - ${res.reservationTime}")
                    }
                    Button(onClick = { onCheckInSelected(res) }) { Text("Check-in") }
                }
            }
        }
    }
}

// Sub-componente 2: Banner de Pedido Pronto
@Composable
fun ReadyOrderBanner(readyOrders: List<ReadyOrder>) { /* ... Implementação do banner ... */
    if (readyOrders.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEB3B))
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Restaurant, contentDescription = "Pronto", tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Seu pedido está pronto!", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Retire em ${readyOrders.first().restaurantName}", color = Color.Black)
                }
            }
        }
    }
}

// Sub-componente 3: Lista de Restaurantes
@Composable
fun RestaurantList(restaurants: List<Restaurant>, onRestaurantSelected: (Restaurant) -> Unit) { /* ... Implementação da lista ... */
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Restaurantes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        LazyRow(contentPadding = PaddingValues(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(restaurants) { restaurant ->
                RestaurantCard(restaurant, onRestaurantSelected)
            }
        }
    }
}

// Sub-componente 4: Card de Restaurante
@Composable
fun RestaurantCard(restaurant: Restaurant, onClick: (Restaurant) -> Unit) { /* ... Implementação do card ... */
    Card(onClick = { onClick(restaurant) }, modifier = Modifier.width(200.dp)) {
        Column {
            AsyncImage(model = restaurant.imageUrl, contentDescription = null, modifier = Modifier.height(100.dp).fillMaxWidth(), contentScale = ContentScale.Crop)
            Column(Modifier.padding(8.dp)) {
                Text(restaurant.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(restaurant.type, style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text(restaurant.rating.toString(), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}