// MainScreenWithBottomNav.kt - ATUALIZADO
package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.ui.screens.*
import com.mesapronta.app.viewmodel.ReadyOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomNav(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    onLogout: () -> Unit,
    onRestaurantSelected: (Restaurant) -> Unit,
    onNavigateToOrderTracking: () -> Unit,
    readyOrdersViewModel: ReadyOrdersViewModel,
    currentUserName: String? = null,
    currentUserEmail: String? = null, // NOVO: Adicionar email do usuário
    modifier: Modifier = Modifier
) {
    var showSettingsScreen by remember { mutableStateOf(false) }

    val navigationItems = listOf(
        NavigationItem("home", "Início", Icons.Default.Home),
        NavigationItem("orders", "Pedidos", Icons.Default.ListAlt),
        NavigationItem("tracking", "Acompanhar", Icons.Default.TrackChanges),
        NavigationItem("complaints", "Reclamações", Icons.Default.Support)
    )

    // Coletar estado dos pedidos prontos
    val hasNewReadyOrders by readyOrdersViewModel.hasNewReadyOrders.collectAsStateWithLifecycle()

    // Se mostrar tela de configurações
    if (showSettingsScreen) {
        SettingsScreen(
            currentUserName = currentUserName,
            currentUserEmail = currentUserEmail,
            onLogout = onLogout,
            onBack = { showSettingsScreen = false },
            modifier = modifier
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    navigationItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                // Adicionar badge apenas na aba "Início" quando houver pedidos prontos
                                if (item.id == "home" && hasNewReadyOrders) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    ) {
                                        Icon(
                                            item.icon,
                                            contentDescription = item.title
                                        )
                                    }
                                } else {
                                    Icon(item.icon, contentDescription = item.title)
                                }
                            },
                            label = { Text(item.title) },
                            selected = selectedScreen == item.id,
                            onClick = { onScreenSelected(item.id) }
                        )
                    }
                }
            },
            floatingActionButton = {
                // NOVO: Botão flutuante para configurações
                ExtendedFloatingActionButton(
                    onClick = { showSettingsScreen = true },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Configurações") },
                    text = { Text("Configurações") },
                    modifier = Modifier.padding(16.dp)
                )
            }
        ) { paddingValues ->
            when (selectedScreen) {
                "home" -> HomeScreen(
                    onLogout = onLogout,
                    onRestaurantSelected = onRestaurantSelected,
                    onNavigateToOrderTracking = onNavigateToOrderTracking,
                    readyOrdersViewModel = readyOrdersViewModel,
                    currentUserName = currentUserName,
                    modifier = Modifier.padding(paddingValues)
                )
                "orders" -> OrdersScreen(
                    modifier = Modifier.padding(paddingValues)
                )
                "tracking" -> OrderTrackingScreen(
                    onNavigateToReadyOrders = {
                        // Quando finaliza o pedido, volta para home
                        onScreenSelected("home")
                    },
                    modifier = Modifier.padding(paddingValues)
                )
                "complaints" -> ComplaintsScreen(
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

data class NavigationItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)