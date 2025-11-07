package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mesapronta.app.model.MenuItem
import com.mesapronta.app.model.CartItem
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSelectionScreen(
    menuItems: List<MenuItem>,
    onBack: () -> Unit,
    onContinueToPayment: (List<CartItem>) -> Unit
) {
    var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }

    val totalPrice = remember(cartItems) {
        cartItems.sumOf { it.totalPrice }
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Comidas") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Voltar") }
                },
                actions = {
                    // Mostrar total do carrinho no topo
                    if (cartItems.isNotEmpty()) {
                        Text(
                            text = currencyFormat.format(totalPrice),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total: ${currencyFormat.format(totalPrice)}")
                            Text("${cartItems.sumOf { it.quantity }} itens",
                                style = MaterialTheme.typography.bodySmall)
                        }
                        Button(
                            onClick = { onContinueToPayment(cartItems) }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Pagamento")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ir para Pagamento")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(menuItems) { menuItem ->
                MenuItemCard(
                    menuItem = menuItem,
                    quantity = cartItems.find { it.menuItem.id == menuItem.id }?.quantity ?: 0,
                    onQuantityChange = { newQuantity ->
                        cartItems = if (newQuantity > 0) {
                            val existingItem = cartItems.find { it.menuItem.id == menuItem.id }
                            if (existingItem != null) {
                                cartItems.map {
                                    if (it.menuItem.id == menuItem.id) it.copy(quantity = newQuantity)
                                    else it
                                }
                            } else {
                                cartItems + CartItem(menuItem, newQuantity)
                            }
                        } else {
                            cartItems.filter { it.menuItem.id != menuItem.id }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(menuItem.name, style = MaterialTheme.typography.titleMedium)
                Text(menuItem.description, style = MaterialTheme.typography.bodyMedium)
                Text(
                    currencyFormat.format(menuItem.price),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Controles de quantidade
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onQuantityChange(quantity - 1) },
                    enabled = quantity > 0
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Remover")
                }

                Text(
                    text = if (quantity > 0) quantity.toString() else "Add",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = { onQuantityChange(quantity + 1) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar")
                }
            }
        }
    }
}