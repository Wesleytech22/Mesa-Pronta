package com.mesapronta.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mesapronta.app.model.MenuItem
import com.mesapronta.app.model.ReadyOrder
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.viewmodel.ReadyOrdersViewModel
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

// NOVO: Mapa de categorias com seus respectivos ícones
val categoriesWithIcons = mapOf(
    "Italiana" to Icons.Default.Restaurant,
    "Japonesa" to Icons.Default.SetMeal,
    "Brasileira" to Icons.Default.OutdoorGrill,
    "Mexicana" to Icons.Default.LocalFireDepartment,
    "Chinesa" to Icons.Default.RamenDining,
    "Árabe" to Icons.Default.KebabDining
)

val categories = categoriesWithIcons.keys.toList()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onRestaurantSelected: (Restaurant) -> Unit,
    onNavigateToOrderTracking: () -> Unit,
    readyOrdersViewModel: ReadyOrdersViewModel,
    currentUserName: String? = null,
    autoShowReadyOrders: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf("São Paulo") }
    var showCouponDialog by remember { mutableStateOf(false) }
    var couponCategorySelected by remember { mutableStateOf<String?>(null) }
    var generatedCoupon by remember { mutableStateOf<String?>(null) }
    var showReadyOrdersDialog by remember { mutableStateOf(false) }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // Coletar os pedidos prontos do ViewModel
    val readyOrders by readyOrdersViewModel.readyOrders.collectAsStateWithLifecycle()
    val hasNewReadyOrders by readyOrdersViewModel.hasNewReadyOrders.collectAsStateWithLifecycle()

    val uncollectedOrders = readyOrders.filter { !it.isCollected }

    // MODIFICAÇÃO: Abrir automaticamente o diálogo se solicitado
    LaunchedEffect(autoShowReadyOrders) {
        if (autoShowReadyOrders && uncollectedOrders.isNotEmpty()) {
            showReadyOrdersDialog = true
        }
    }

    fun generateSixDigitCoupon(): String {
        return String.format("%06d", Random.nextInt(0, 1_000_000))
    }

    val restaurants = sampleRestaurants
    val filteredRestaurants = restaurants.filter {
        (selectedCategory == null || it.type == selectedCategory) &&
                (selectedLocation.isEmpty() || it.address.contains(selectedLocation, ignoreCase = true))
    }

    // DIÁLOGO DE PEDIDOS PRONTOS
    if (showReadyOrdersDialog) {
        ReadyOrdersDialog(
            readyOrders = uncollectedOrders,
            onMarkAsCollected = { orderId ->
                readyOrdersViewModel.markAsCollected(orderId)
            },
            onDismiss = { showReadyOrdersDialog = false }
        )
    }

    // DIÁLOGO DE CUPOM
    if (showCouponDialog) {
        CouponDialog(
            couponCategorySelected = couponCategorySelected,
            generatedCoupon = generatedCoupon,
            onCategorySelected = { couponCategorySelected = it },
            onGenerateCoupon = { generatedCoupon = generateSixDigitCoupon() },
            onCopyAndClose = { coupon ->
                clipboardManager.setText(AnnotatedString(coupon))
                Toast.makeText(context, "Cupom copiado!", Toast.LENGTH_SHORT).show()
                showCouponDialog = false
            },
            onDismiss = { showCouponDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // BANNER DE PEDIDOS PRONTOS (aparece apenas se houver pedidos)
        if (uncollectedOrders.isNotEmpty()) {
            item {
                ReadyOrdersBanner(
                    orderCount = uncollectedOrders.size,
                    onClick = { showReadyOrdersDialog = true },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // CABEÇALHO
        item {
            HomeHeader(
                selectedLocation = selectedLocation,
                onLocationChange = { selectedLocation = it },
                onCouponClick = {
                    couponCategorySelected = null
                    generatedCoupon = null
                    showCouponDialog = true
                },
                onLogout = onLogout,
                hasNotifications = hasNewReadyOrders,
                onNavigateToOrderTracking = onNavigateToOrderTracking,
                currentUserName = currentUserName
            )
        }

        // CATEGORIAS
        item {
            CategoriesSection(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
        }

        // PROMOÇÕES
        item {
            Text(
                "🔥 Promoções da Semana",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
        item {
            PromotionCard(
                restaurant = restaurants.random(),
                onClick = onRestaurantSelected
            )
        }

        // RESTAURANTES
        item {
            Text(
                "🍽️ Restaurantes Populares",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        if (filteredRestaurants.isEmpty()) {
            item {
                EmptyRestaurantsState()
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

// COMPONENTE: Banner de Pedidos Prontos
@Composable
fun ReadyOrdersBanner(
    orderCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error
                        ) {
                            Text(
                                orderCount.toString(),
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.RestaurantMenu,
                        contentDescription = "Pedidos Prontos",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        "Seu pedido está pronto!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "$orderCount pedido(s) aguardando retirada",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Ver pedidos",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// COMPONENTE: Diálogo de Pedidos Prontos
@Composable
fun ReadyOrdersDialog(
    readyOrders: List<ReadyOrder>,
    onMarkAsCollected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🎉 Pedidos Prontos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (readyOrders.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Todos coletados",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Todos os pedidos foram coletados!",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.heightIn(max = 400.dp)
                    ) {
                        items(readyOrders) { order ->
                            ReadyOrderItem(
                                order = order,
                                onMarkAsCollected = onMarkAsCollected
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fechar")
                }
            }
        }
    }
}

// COMPONENTE: Item de Pedido Pronto
@Composable
fun ReadyOrderItem(
    order: ReadyOrder,
    onMarkAsCollected: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        order.restaurantName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Pedido #${order.id}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Informações de horário
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Horário",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Reserva: ${order.reservationTime} | Pronto: ${order.readyTime}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Mesa
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.TableRestaurant,
                            contentDescription = "Mesa",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Mesa ${order.tableNumber}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Itens do pedido
                    Column {
                        order.items.forEach { item ->
                            Text(
                                "• $item",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Botão de confirmar retirada
                if (!order.isCollected) {
                    Button(
                        onClick = { onMarkAsCollected(order.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Retirei")
                    }
                } else {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Coletado",
                        tint = Color.Green,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

// COMPONENTE: Cabeçalho da Home (modificado para mostrar nome do usuário)
@Composable
fun HomeHeader(
    selectedLocation: String,
    onLocationChange: (String) -> Unit,
    onCouponClick: () -> Unit,
    onLogout: () -> Unit,
    hasNotifications: Boolean,
    onNavigateToOrderTracking: () -> Unit,
    currentUserName: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Logo com badge de notificação
            BadgedBox(
                badge = {
                    if (hasNotifications) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    }
                }
            ) {
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
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                // NOVO: Mostra o nome do usuário ou mensagem genérica
                Text(
                    if (currentUserName != null) "Olá, $currentUserName! 👋" else "Seja Bem-vindo! 👋",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (currentUserName != null) FontWeight.Bold else FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = selectedLocation,
                    onValueChange = onLocationChange,
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
            // Botão para acompanhar pedidos
            IconButton(onClick = onNavigateToOrderTracking) {
                Icon(
                    imageVector = Icons.Default.DeliveryDining,
                    contentDescription = "Acompanhar Pedidos",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onCouponClick) {
                Icon(
                    imageVector = Icons.Default.Sell,
                    contentDescription = "Cupons de desconto",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(onClick = onLogout) {
                Text("Sair", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// COMPONENTE: Seção de Categorias
@Composable
fun CategoriesSection(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    Column {
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
                        onCategorySelected(
                            if (selectedCategory == category) "" else category
                        )
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // NOVO: Ícone da categoria
                            categoriesWithIcons[category]?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(category)
                        }
                    }
                )
            }
        }
    }
}

// COMPONENTE: Card de Promoção
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
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

// COMPONENTE: Card de Restaurante
@Composable
fun RestaurantCard(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
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

// COMPONENTE: Estado vazio
@Composable
fun EmptyRestaurantsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.SearchOff,
            contentDescription = "Nenhum restaurante",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Nenhum restaurante encontrado",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            "Tente alterar os filtros de busca",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

// COMPONENTE: Diálogo de Cupom (ATUALIZADO COM ÍCONES)
@Composable
fun CouponDialog(
    couponCategorySelected: String?,
    generatedCoupon: String?,
    onCategorySelected: (String) -> Unit,
    onGenerateCoupon: () -> Unit,
    onCopyAndClose: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "🎫 Gerar Cupom de Desconto",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Selecione uma categoria:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(12.dp))

                // NOVO: LazyRow com ícones para as categorias
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val icon = categoriesWithIcons[category]
                        CategoryChip(
                            category = category,
                            icon = icon,
                            isSelected = category == couponCategorySelected,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onGenerateCoupon,
                    enabled = couponCategorySelected != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    // NOVO: Mostra o ícone da categoria selecionada no botão
                    if (couponCategorySelected != null) {
                        val selectedIcon = categoriesWithIcons[couponCategorySelected]
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            selectedIcon?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text("Gerar Cupom para $couponCategorySelected")
                        }
                    } else {
                        Text("Selecione uma categoria")
                    }
                }

                generatedCoupon?.let { coupon ->
                    Spacer(Modifier.height(20.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "🎉 Seu Cupom Gerado!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                coupon,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 4.sp
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "Use este código na finalização do pedido",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(16.dp))

                            Button(
                                onClick = { onCopyAndClose(coupon) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copiar",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Copiar e Fechar")
                            }
                        }
                    }
                }
            }
        }
    }
}

// NOVO: Componente personalizado para chips de categoria com ícones
@Composable
fun CategoryChip(
    category: String,
    icon: ImageVector?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = category,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}