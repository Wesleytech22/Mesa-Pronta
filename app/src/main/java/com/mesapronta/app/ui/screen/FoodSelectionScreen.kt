package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesapronta.app.model.CartItem
import com.mesapronta.app.model.MenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSelectionScreen(
    menuItems: List<MenuItem>,
    mealCount: Int,
    onBack: () -> Unit,
    onContinueToPayment: (List<CartItem>, Double) -> Unit
) {
    var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }
    var includeCouvert by remember { mutableStateOf(false) }
    val couvertPrice = 15.00

    val totalPrice = remember(cartItems, includeCouvert, mealCount) {
        val itemsTotal = cartItems.sumOf { it.menuItem.price * it.quantity }
        val couvertTotal = if (includeCouvert) couvertPrice * mealCount else 0.0
        itemsTotal + couvertTotal
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Cardápio - Selecione os Pratos",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Total: R$ ${"%.2f".format(totalPrice).replace('.', ',')}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${cartItems.sumOf { it.quantity }} itens",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = {
                            onContinueToPayment(cartItems, if (includeCouvert) couvertPrice * mealCount else 0.0)
                        },
                        enabled = cartItems.isNotEmpty(),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Continuar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Continuar")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Opção de Couvert
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "Couvert Artístico",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "R$ ${"%.2f".format(couvertPrice).replace('.', ',')} por pessoa",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Total: R$ ${"%.2f".format(couvertPrice * mealCount).replace('.', ',')}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Switch(
                            checked = includeCouvert,
                            onCheckedChange = { includeCouvert = it }
                        )
                    }
                }
            }

            // Cardápio
            item {
                Text(
                    "Cardápio",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (menuItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Cardápio em desenvolvimento",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(menuItems) { menuItem ->
                    MenuItemCard(
                        menuItem = menuItem,
                        quantity = cartItems.find { it.menuItem.id == menuItem.id }?.quantity ?: 0,
                        onQuantityChange = { newQuantity ->
                            cartItems = if (newQuantity == 0) {
                                cartItems.filter { it.menuItem.id != menuItem.id }
                            } else {
                                val existingItem = cartItems.find { it.menuItem.id == menuItem.id }
                                if (existingItem != null) {
                                    cartItems.map {
                                        if (it.menuItem.id == menuItem.id) it.copy(quantity = newQuantity)
                                        else it
                                    }
                                } else {
                                    cartItems + CartItem(menuItem, newQuantity)
                                }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        menuItem.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        menuItem.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "R$ ${"%.2f".format(menuItem.price).replace('.', ',')}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (quantity > 0) onQuantityChange(quantity - 1) },
                        enabled = quantity > 0
                    ) {
                        Text("−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        "$quantity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = { onQuantityChange(quantity + 1) }
                    ) {
                        Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}