package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onOrdersClick: () -> Unit,
    onTrackOrdersClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }

    val categories = listOf("Todos", "Italiano", "Japonês", "Brasileiro", "Fast Food", "Churrascaria", "Vegetariano", "Frutos do Mar")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Barra de Pesquisa
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Pesquisar")
            },
            placeholder = { Text("Pesquisar restaurantes...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp)
        )

        // Seção: Categorias
        Text(
            text = "Categorias",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 12.dp)
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }

        // Seção: Restaurantes da Categoria Selecionada
        Text(
            text = "Restaurantes - $selectedCategory",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 12.dp)
        )

        // Restaurantes de exemplo
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedCategory) {
                "Todos" -> {
                    RestaurantItem("Pizzaria Bella", "Italiano", 4.5f, 5, "30-40 min")
                    RestaurantItem("Sushi Master", "Japonês", 4.8f, 3, "25-35 min")
                    RestaurantItem("Sabores do Brasil", "Brasileiro", 4.3f, 8, "35-45 min")
                    RestaurantItem("Burger House", "Fast Food", 4.2f, 6, "20-30 min")
                }
                "Italiano" -> {
                    RestaurantItem("Pizzaria Bella", "Italiano", 4.5f, 5, "30-40 min")
                    RestaurantItem("Trattoria Roma", "Italiano", 4.4f, 4, "25-35 min")
                }
                "Japonês" -> {
                    RestaurantItem("Sushi Master", "Japonês", 4.8f, 3, "25-35 min")
                    RestaurantItem("Tokyo Grill", "Japonês", 4.3f, 5, "30-40 min")
                }
                "Brasileiro" -> {
                    RestaurantItem("Sabores do Brasil", "Brasileiro", 4.3f, 8, "35-45 min")
                }
                "Fast Food" -> {
                    RestaurantItem("Burger House", "Fast Food", 4.2f, 6, "20-30 min")
                }
                "Churrascaria" -> {
                    RestaurantItem("Churrascaria Gaúcha", "Churrascaria", 4.7f, 4, "40-50 min")
                }
                "Vegetariano" -> {
                    RestaurantItem("Verde Vida", "Vegetariano", 4.4f, 7, "25-35 min")
                }
                "Frutos do Mar" -> {
                    RestaurantItem("Mar & Cia", "Frutos do Mar", 4.6f, 2, "30-40 min")
                }
            }
        }

        // Seção: Pratos da Categoria Selecionada
        Text(
            text = "Pratos - $selectedCategory",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 12.dp)
        )

        // Pratos de exemplo
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (selectedCategory) {
                "Todos" -> {
                    DishItem("Pizza Margherita", "Molho de tomate, mussarela e manjericão", 45.90)
                    DishItem("Sushi Salmão", "Salmão fresco com arroz", 32.50)
                    DishItem("Feijoada", "Feijão preto com carne de porco", 35.00)
                    DishItem("Hambúrguer Artesanal", "Carne 180g com queijo cheddar", 24.90)
                }
                "Italiano" -> {
                    DishItem("Pizza Margherita", "Molho de tomate, mussarela e manjericão", 45.90)
                    DishItem("Lasanha Bolonhesa", "Carne moída com molho branco", 38.50)
                    DishItem("Risotto de Cogumelos", "Arroz arbóreo com cogumelos", 42.00)
                }
                "Japonês" -> {
                    DishItem("Sushi Salmão", "Salmão fresco com arroz", 32.50)
                    DishItem("Temaki Atum", "Cone de alga com atum", 28.90)
                    DishItem("Yakissoba", "Macarrão com legumes e carne", 36.00)
                }
                "Brasileiro" -> {
                    DishItem("Feijoada", "Feijão preto com carne de porco", 35.00)
                    DishItem("Picanha", "Corte nobre da carne bovina", 89.90)
                    DishItem("Moqueca de Peixe", "Peixe cozido com leite de coco", 55.00)
                }
                "Fast Food" -> {
                    DishItem("Hambúrguer Artesanal", "Carne 180g com queijo cheddar", 24.90)
                    DishItem("Batata Frita", "Porção grande com ketchup", 12.00)
                }
                "Churrascaria" -> {
                    DishItem("Picanha na Chapa", "Acompanha farofa e vinagrete", 79.90)
                    DishItem("Costela Bovino", "Costela assada lentamente", 65.00)
                }
                "Vegetariano" -> {
                    DishItem("Salada Caesar", "Alface, croutons e molho caesar", 22.90)
                    DishItem("Quibe de Abóbora", "Quibe assado com abóbora", 18.50)
                }
                "Frutos do Mar" -> {
                    DishItem("Camarão alho e óleo", "Camarão temperado com alho", 48.90)
                    DishItem("Polenta com lula", "Lula frita com polenta cremosa", 42.00)
                }
            }
        }

        // Seção: Botões de Ação
        Text(
            text = "Meus Pedidos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onOrdersClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Pedidos Realizados")
            }

            Button(
                onClick = onTrackOrdersClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Acompanhar Pedidos")
            }
        }

        // Botão Sair
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Sair")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RestaurantItem(name: String, type: String, rating: Float, tables: Int, deliveryTime: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = type,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$tables mesas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "%.1f".format(rating),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = deliveryTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DishItem(name: String, description: String, price: Double) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = "R$ %.2f".format(price),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}