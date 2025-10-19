package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.Dish
import com.mesapronta.app.model.Promotion
import com.mesapronta.app.model.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onDishSelect: (Dish, Restaurant) -> Unit,
    onLogout: () -> Unit,
    onCartClick: () -> Unit,
    onPromotionsClick: () -> Unit,
    cartItemCount: Int
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }

    val categories = listOf("Todos", "Italiano", "Japonês", "Brasileiro", "Fast Food", "Churrascaria", "Vegetariano", "Frutos do Mar")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🍽️ Mesa Pronta",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Cart Icon with badge
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge {
                                    Text(cartItemCount.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrinho")
                        }
                    }

                    // Promotions Icon
                    IconButton(onClick = onPromotionsClick) {
                        Icon(Icons.Default.LocalOffer, contentDescription = "Promoções")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                shape = MaterialTheme.shapes.medium
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
                        onClick = {
                            selectedCategory = category
                            selectedRestaurant = null
                        },
                        label = { Text(category) }
                    )
                }
            }

            // SEÇÃO: RESTAURANTES
            Text(
                text = if (selectedRestaurant != null) "Restaurante Selecionado" else "Restaurantes - $selectedCategory",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 12.dp)
            )

            if (selectedRestaurant != null) {
                // MOSTRA PRATOS DO RESTAURANTE SELECIONADO
                SelectedRestaurantSection(
                    restaurant = selectedRestaurant!!,
                    onBackClick = { selectedRestaurant = null },
                    onDishSelect = onDishSelect
                )
            } else {
                // MOSTRA LISTA DE RESTAURANTES
                RestaurantListSection(
                    category = selectedCategory,
                    onRestaurantClick = { restaurant -> selectedRestaurant = restaurant }
                )
            }

            // SEÇÃO: PROMOÇÕES DO DIA
            Text(
                text = "🎉 Promoções do Dia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 12.dp)
            )

            PromotionsSection()

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
}

// SEÇÃO: RESTAURANTE SELECIONADO (PRATOS)
@Composable
fun SelectedRestaurantSection(
    restaurant: Restaurant,
    onBackClick: () -> Unit,
    onDishSelect: (Dish, Restaurant) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Header do restaurante
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                            text = restaurant.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = restaurant.type,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(onClick = onBackClick) {
                        Text("Voltar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Rating with stars
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Rating",
                                modifier = Modifier.size(16.dp),
                                tint = if (index < restaurant.rating.toInt())
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outline
                            )
                        }
                        Text(
                            text = " %.1f".format(restaurant.rating),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Text(
                        text = "${restaurant.availableTables} mesas disponíveis",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PRATOS DO RESTAURANTE
        Text(
            text = "🍽️ Cardápio",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            restaurant.dishes.forEach { dish ->
                DishItem(
                    dish = dish,
                    restaurant = restaurant,
                    onDishClick = onDishSelect
                )
            }
        }
    }
}

// SEÇÃO: LISTA DE RESTAURANTES
@Composable
fun RestaurantListSection(
    category: String,
    onRestaurantClick: (Restaurant) -> Unit
) {
    val restaurants = getRestaurantsByCategory(category)

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        restaurants.forEach { restaurant ->
            RestaurantItem(
                restaurant = restaurant,
                onClick = { onRestaurantClick(restaurant) }
            )
        }
    }
}

// SEÇÃO: PROMOÇÕES
@Composable
fun PromotionsSection() {
    val promotions = getDailyPromotions()

    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(promotions) { promotion ->
            PromotionCard(promotion)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
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
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = restaurant.type,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${restaurant.availableTables} mesas",
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
                // Rating with stars
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            modifier = Modifier.size(16.dp),
                            tint = if (index < restaurant.rating.toInt())
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                    }
                    Text(
                        text = " %.1f".format(restaurant.rating),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = restaurant.deliveryTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishItem(
    dish: Dish,
    restaurant: Restaurant,
    onDishClick: (Dish, Restaurant) -> Unit
) {
    Card(
        onClick = { onDishClick(dish, restaurant) },
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
                    text = dish.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dish.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "R$ %.2f".format(dish.price),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Icon(
                Icons.Default.AddShoppingCart,
                contentDescription = "Adicionar ao carrinho",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PromotionCard(promotion: Promotion) {
    Card(
        modifier = Modifier.width(280.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "🔥 ${promotion.title}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = promotion.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "De R$ %.2f".format(promotion.originalPrice),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Por R$ %.2f".format(promotion.promoPrice),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⏰ ${promotion.validHours}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "📍 ${promotion.restaurant}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// DADOS DE EXEMPLO
fun getRestaurantsByCategory(category: String): List<Restaurant> {
    return when (category) {
        "Todos" -> listOf(jack33Restaurant, pizzariaBellaRestaurant, sushiMasterRestaurant)
        "Italiano" -> listOf(pizzariaBellaRestaurant)
        "Japonês" -> listOf(sushiMasterRestaurant)
        "Brasileiro" -> listOf(jack33Restaurant)
        else -> emptyList()
    }
}

// RESTAURANTES DE EXEMPLO
val jack33Restaurant = Restaurant(
    id = "1",
    name = "Jack33",
    description = "Comida brasileira com tempero especial",
    type = "Brasileiro",
    rating = 4.6f,
    deliveryTime = "35-45 min",
    availableTables = 8,
    dishes = listOf(
        Dish("1", "Feijoada Completa", "Feijão preto com carne de porco, acompanha arroz, couve e farofa", 35.00),
        Dish("2", "Picanha na Chapa", "Picanha grelhada com alho, acompanha arroz, feijão e farofa", 89.90),
        Dish("3", "Moqueca de Peixe", "Peixe cozido com leite de coco e azeite de dendê", 55.00),
        Dish("4", "Frango à Passarinho", "Frango frito crocante com alho", 42.00)
    )
)

val pizzariaBellaRestaurant = Restaurant(
    id = "2",
    name = "Pizzaria Bella",
    description = "Pizzas artesanais no forno a lenha",
    type = "Italiano",
    rating = 4.5f,
    deliveryTime = "30-40 min",
    availableTables = 5,
    dishes = listOf(
        Dish("5", "Pizza Margherita", "Molho de tomate, mussarela e manjericão", 45.90),
        Dish("6", "Pizza Calabresa", "Calabresa, cebola e azeitonas", 48.90),
        Dish("7", "Lasanha Bolonhesa", "Carne moída com molho branco", 38.50)
    )
)

val sushiMasterRestaurant = Restaurant(
    id = "3",
    name = "Sushi Master",
    description = "Sushi tradicional e contemporâneo",
    type = "Japonês",
    rating = 4.8f,
    deliveryTime = "25-35 min",
    availableTables = 3,
    dishes = listOf(
        Dish("8", "Sushi Salmão", "Salmão fresco com arroz", 32.50),
        Dish("9", "Temaki Atum", "Cone de alga com atum", 28.90),
        Dish("10", "Combo 40 Peças", "Variedade de sushis e sashimis", 79.90)
    )
)

fun getDailyPromotions(): List<Promotion> = listOf(
    Promotion(
        id = "1",
        title = "Almoço Executivo",
        description = "Prato feito + sobremesa + refrigerante",
        originalPrice = 45.90,
        promoPrice = 29.90,
        restaurant = "Jack33",
        validHours = "11:30 às 14:00"
    ),
    Promotion(
        id = "2",
        title = "Happy Hour Pizza",
        description = "Pizza média + 2 refrigerantes",
        originalPrice = 65.90,
        promoPrice = 39.90,
        restaurant = "Pizzaria Bella",
        validHours = "18:00 às 20:00"
    ),
    Promotion(
        id = "3",
        title = "Combo Sushi Família",
        description = "60 peças + 4 temakis",
        originalPrice = 129.90,
        promoPrice = 89.90,
        restaurant = "Sushi Master",
        validHours = "Todo o dia"
    )
)