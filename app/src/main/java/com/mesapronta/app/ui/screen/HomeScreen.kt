package com.mesapronta.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.mesapronta.app.R
import com.mesapronta.app.model.MenuItem
import com.mesapronta.app.model.Restaurant
import kotlin.random.Random

// --- DADOS DE EXEMPLO COMPLETOS ---
val sampleRestaurants = listOf(
    Restaurant(
        id = 1,
        name = "Pizzaria do DED",
        description = "Massas artesanais e pizzas deliciosas.",
        type = "Italiana",
        rating = 4.8f,
        deliveryTime = "20-30 min",
        imageUrl = "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=500",
        address = "São Paulo",
        menu = listOf(
            MenuItem(1, "Pizza Margherita", "Molho de tomate, mussarela e manjericão", 45.90, "Pizzas"),
            MenuItem(2, "Lasanha Bolonhesa", "Camadas de massa com carne moída e queijo", 38.50, "Massas"),
            MenuItem(3, "Tiramisu", "Sobremesa italiana clássica", 18.90, "Sobremesas")
        ),
        availableTimes = listOf("19:00", "20:00", "21:00"),
        isOpen = true
    ),
    Restaurant(
        id = 2,
        name = "Sushi House",
        description = "Peixes frescos e ambiente minimalista.",
        type = "Japonesa",
        rating = 4.6f,
        deliveryTime = "25-35 min",
        imageUrl = "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500",
        address = "Rio de Janeiro",
        menu = listOf(
            MenuItem(4, "Combinado Salmão", "20 peças variadas de salmão", 89.90, "Sushis"),
            MenuItem(5, "Temaki", "Cone de alga nori recheado", 24.50, "Temakis"),
            MenuItem(6, "Missoshiru", "Sopa tradicional japonesa", 12.90, "Sopas")
        ),
        availableTimes = listOf("18:30", "19:30", "20:30"),
        isOpen = true
    ),
    Restaurant(
        id = 3,
        name = "Churrasco do Zé",
        description = "Cortes premium e buffet completo.",
        type = "Brasileira",
        rating = 4.9f,
        deliveryTime = "30-40 min",
        imageUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=500",
        address = "São Paulo",
        menu = listOf(
            MenuItem(7, "Picanha", "Boi nobre grelhado na churrasqueira", 79.90, "Carnes"),
            MenuItem(8, "Costela", "Costela bovina assada lentamente", 65.50, "Carnes"),
            MenuItem(9, "Farofa", "Farofa caseira com bacon", 8.90, "Acompanhamentos")
        ),
        availableTimes = listOf("12:00", "13:00", "14:00"),
        isOpen = false
    ),
    Restaurant(
        id = 4,
        name = "La Casa de México",
        description = "Autêntica comida mexicana com muito sabor e tradição.",
        type = "Mexicana",
        rating = 4.0f,
        deliveryTime = "20-30 min",
        imageUrl = "https://images.unsplash.com/photo-1565299507177-b0ac66763828?w=500",
        address = "Consolação",
        menu = listOf(
            MenuItem(10, "Tacos", "3 unidades com carne moída e queijo", 32.90, "Pratos Principais"),
            MenuItem(11, "Burrito", "Tortilla recheada com feijão e carne", 28.50, "Pratos Principais"),
            MenuItem(12, "Guacamole", "Pasta de abacate temperada", 15.90, "Acompanhamentos")
        ),
        availableTimes = listOf("18:30", "19:30", "20:30"),
        isOpen = true
    )
)

val categories = listOf("Italiana", "Japonesa", "Brasileira", "Mexicana", "Chinesa", "Árabe")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onRestaurantSelected: (Restaurant) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf("São Paulo") }

    var showCouponDialog by remember { mutableStateOf(false) }
    var couponCategorySelected by remember { mutableStateOf<String?>(null) }
    var generatedCoupon by remember { mutableStateOf<String?>(null) }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val ctx = LocalContext.current

    fun generateSixDigitCoupon(): String {
        return String.format("%06d", Random.nextInt(0, 1_000_000))
    }

    val restaurants = sampleRestaurants

    val filteredRestaurants = restaurants.filter {
        (selectedCategory == null || it.type == selectedCategory) &&
                (selectedLocation.isEmpty() || it.address.contains(selectedLocation, ignoreCase = true))
    }

    // --- DIÁLOGO DE CUPOM ---
    if (showCouponDialog) {
        Dialog(onDismissRequest = { showCouponDialog = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Gerar Cupom de Desconto",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(16.dp))

                    Text("Selecione uma categoria:", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                selected = category == couponCategorySelected,
                                onClick = { couponCategorySelected = category },
                                label = { Text(category) }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (couponCategorySelected != null) {
                                generatedCoupon = generateSixDigitCoupon()
                            }
                        },
                        enabled = couponCategorySelected != null,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Gerar Cupom para ${couponCategorySelected ?: "Categoria"}")
                    }

                    generatedCoupon?.let { coupon ->
                        Spacer(Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Seu Cupom Gerado:", style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    coupon,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(coupon))
                                        Toast.makeText(ctx, "Cupom copiado!", Toast.LENGTH_SHORT).show()
                                        showCouponDialog = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Copiar e Fechar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    // --- FIM DO DIÁLOGO DE CUPOM ---

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- 1. CABEÇALHO (Logo, Localização, Botões) ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Logo do app
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "MP",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("Seja Bem-vindo! 👋", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        Spacer(modifier = Modifier.height(4.dp))

                        // Campo de localização
                        OutlinedTextField(
                            value = selectedLocation,
                            onValueChange = { selectedLocation = it },
                            placeholder = { Text("Digite sua localização") },
                            singleLine = true,
                            modifier = Modifier
                                .width(220.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Botão de cupom
                    IconButton(
                        onClick = {
                            couponCategorySelected = null
                            generatedCoupon = null
                            showCouponDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sell,
                            contentDescription = "Cupons de desconto",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botão de logout
                    TextButton(onClick = onLogout) {
                        Text("Sair", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // --- 2. CATEGORIAS ---
        item {
            Text(
                "Categorias",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        label = { Text(category) }
                    )
                }
            }
        }

        // --- 3. PROMOÇÕES ---
        item {
            Text(
                "🔥 Promoções da Semana",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        item {
            PromotionCard(restaurant = restaurants.random()) { restaurant ->
                onRestaurantSelected(restaurant)
            }
        }

        // --- 4. LISTA DE RESTAURANTES ---
        item {
            Text(
                "🍽️ Restaurantes Populares",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        if (filteredRestaurants.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Nenhum restaurante encontrado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Tente alterar os filtros",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            items(filteredRestaurants) { restaurant ->
                RestaurantCard(
                    restaurant = restaurant,
                    onClick = { onRestaurantSelected(restaurant) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- Funções Auxiliares ---

@Composable
fun PromotionCard(
    restaurant: Restaurant,
    onClick: (Restaurant) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(restaurant) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = "Promoção em ${restaurant.name}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Overlay gradiente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 300f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    "🎉 20% OFF",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "em ${restaurant.name}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    restaurant.description,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem do restaurante
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Informações do restaurante
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    restaurant.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )

                Text(
                    restaurant.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Avaliação",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${restaurant.rating} • ${restaurant.deliveryTime}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Status do restaurante
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    if (restaurant.isOpen) "🟢 Aberto" else "🔴 Fechado",
                    color = if (restaurant.isOpen) Color(0xFF00C853) else Color(0xFFD50000),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    restaurant.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}