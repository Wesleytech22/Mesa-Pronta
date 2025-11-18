package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.ui.screen.ComplaintsScreen
import com.mesapronta.app.ui.screen.OrdersScreen
import com.mesapronta.app.ui.screens.HomeScreen
import com.mesapronta.app.ui.screens.OrderTrackingScreen
import com.mesapronta.app.viewmodel.ReadyOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomNav(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    onLogout: () -> Unit,
    onRestaurantSelected: (Restaurant) -> Unit,
    onNavigateToOrderTracking: () -> Unit, // NOVO: Parâmetro adicionado
    readyOrdersViewModel: ReadyOrdersViewModel,
    currentUserName: String? = null, // NOVO: Parâmetro para nome do usuário
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem("home", "Início", Icons.Default.Home),
        NavigationItem("orders", "Pedidos", Icons.Default.ListAlt),
        NavigationItem("tracking", "Acompanhar", Icons.Default.TrackChanges),
        NavigationItem("complaints", "Reclamações", Icons.Default.Support)
    )

    // Coletar estado dos pedidos prontos
    val hasNewReadyOrders by readyOrdersViewModel.hasNewReadyOrders.collectAsStateWithLifecycle()

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
        }
    ) { paddingValues ->
        when (selectedScreen) {
            "home" -> HomeScreen(
                onLogout = onLogout,
                onRestaurantSelected = onRestaurantSelected,
                onNavigateToOrderTracking = onNavigateToOrderTracking, // NOVO: Passa o callback
                readyOrdersViewModel = readyOrdersViewModel,
                currentUserName = currentUserName, // NOVO: Passa o nome do usuário
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

data class NavigationItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)