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
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.mesapronta.app.R // Assume que R está disponível
import com.mesapronta.app.model.Restaurant
import kotlin.random.Random

// IMPORTAÇÃO CORRETA para as cores de OutlinedTextField (resolve o erro)
import androidx.compose.material3.OutlinedTextFieldDefaults

// --- DADOS DE EXEMPLO (Mantenha ou substitua pelo seu ViewModel) ---
val sampleRestaurants = listOf(
    Restaurant(
        id = 1, name = "Cantina Bella Napoli", description = "Massas artesanais.", type = "Italiana", rating = 4.8f, deliveryTime = "20-30 min", imageUrl = "https://images.unsplash.com/photo-1603079849561-7cf1f68f4b35", address = "São Paulo", menu = listOf("Lasanha"), availableTimes = listOf("19:00", "20:00"), isOpen = true
    ),
    Restaurant(
        id = 2, name = "Sushi House", description = "Peixes frescos e ambiente minimalista.", type = "Japonesa", rating = 4.6f, deliveryTime = "25-35 min", imageUrl = "https://images.unsplash.com/photo-1553621042-f6e147245754", address = "Rio de Janeiro", menu = listOf("Combinado Salmão"), availableTimes = listOf("18:30", "19:30"), isOpen = true
    ),
    Restaurant(
        id = 3, name = "Churrasco do Zé", description = "Cortes premium e buffet completo.", type = "Brasileira", rating = 4.9f, deliveryTime = "30-40 min", imageUrl = "https://images.unsplash.com/photo-1600891964599-f61ba0e24092", address = "São Paulo", menu = listOf("Picanha"), availableTimes = listOf("12:00", "13:00"), isOpen = false
    )
)
val categories = listOf("Italiana", "Japonesa", "Brasileira", "Chinesa", "Mexicana")
// --- FIM DOS DADOS DE EXEMPLO ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onRestaurantSelected: (Restaurant) -> Unit
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
                (selectedLocation.isEmpty() || it.address.equals(selectedLocation, ignoreCase = true))
    }

    // --- DIÁLOGO DE CUPOM ---
    if (showCouponDialog) {
        Dialog(onDismissRequest = { showCouponDialog = false }) {
            Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Gerar Cupom de Desconto", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            FilterChip(
                                selected = category == couponCategorySelected,
                                onClick = { couponCategorySelected = category },
                                label = { Text(category) }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (couponCategorySelected != null) {
                                generatedCoupon = generateSixDigitCoupon()
                            }
                        },
                        enabled = couponCategorySelected != null
                    ) {
                        Text("Gerar Cupom para ${couponCategorySelected ?: "Categoria"}")
                    }

                    generatedCoupon?.let { coupon ->
                        Spacer(Modifier.height(16.dp))
                        Text("Seu Cupom: $coupon")
                        Button(onClick = {
                            clipboardManager.setText(AnnotatedString(coupon))
                            Toast.makeText(ctx, "Cupom copiado!", Toast.LENGTH_SHORT).show()
                            showCouponDialog = false
                        }) {
                            Text("Copiar e Fechar")
                        }
                    }
                }
            }
        }
    }
    // --- FIM DO DIÁLOGO DE CUPOM ---

    LazyColumn(
        modifier = Modifier
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
                    val logoResourceId = R.drawable.img01

                    if (logoResourceId != 0) {
                        Image(
                            painter = painterResource(id = logoResourceId),
                            contentDescription = "Logo Mesa Pronta",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        Text(text = "Mesa Pronta", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("Olá 👋", fontSize = 14.sp)

                        // --- CORREÇÃO DEFINITIVA DO OutlinedTextField ---
                        OutlinedTextField(
                            value = selectedLocation,
                            onValueChange = { selectedLocation = it },
                            placeholder = { Text("Digite sua localização") },
                            singleLine = true,
                            modifier = Modifier
                                .width(220.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(24.dp),
                            // Resolve Unresolved reference 'outlinedTextFieldColors'
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                // Adiciona cores de contêiner transparentes para um visual limpo de Home Screen
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                            )
                        )
                        // ------------------------------------------------
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        couponCategorySelected = null
                        generatedCoupon = null
                        showCouponDialog = true
                    }) {
                        // Ícone corrigido (substitui 'ic_coupon')
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Cupom",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = onLogout) {
                        Text("Sair")
                    }
                }
            }
        }

        // --- 2. CATEGORIAS ---
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = if (selectedCategory == category) null else category },
                        label = { Text(category) }
                    )
                }
            }
        }

        // --- 3. PROMOÇÕES ---
        item {
            Text(
                "Promoções da Semana",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
            PromotionCard(restaurant = restaurants.first())
        }

        // --- 4. LISTA DE RESTAURANTES ---
        item {
            Text(
                "Restaurantes Populares",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        items(filteredRestaurants) { restaurant ->
            RestaurantCard(
                restaurant = restaurant,
                onClick = { onRestaurantSelected(restaurant) }
            )
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

// --- Funções Auxiliares ---

@Composable
fun PromotionCard(restaurant: Restaurant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { /* Ação */ },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = "Promoção em ${restaurant.name}",
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Text(
                "20% OFF em ${restaurant.name}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(8.dp)
            )
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(restaurant.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(restaurant.type, style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(restaurant.rating.toString(), style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                if (restaurant.isOpen) "Aberto" else "Fechado",
                color = if (restaurant.isOpen) Color.Green.copy(alpha = 0.8f) else Color.Red.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }
    }
}