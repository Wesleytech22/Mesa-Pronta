package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.mesapronta.app.model.ReservationDetails
import com.mesapronta.app.model.Restaurant
import com.mesapronta.app.ui.screens.OrderTrackingScreen
import com.mesapronta.app.viewmodel.ReadyOrdersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomNav(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    onLogout: () -> Unit,
    onRestaurantSelected: (Restaurant) -> Unit,
    onCheckInSelected: (ReservationDetails) -> Unit,
    readyOrdersViewModel: ReadyOrdersViewModel
) {
    val navigationItems = listOf(
        NavigationItem("home", "Início", Icons.Default.Home),
        NavigationItem("orders", "Pedidos", Icons.Default.ListAlt),
        NavigationItem("tracking", "Acompanhar", Icons.Default.TrackChanges),
        NavigationItem("complaints", "Reclamações", Icons.Default.Support)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(item.icon, contentDescription = item.title)
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
                onCheckInSelected = onCheckInSelected,
                readyOrdersViewModel = readyOrdersViewModel,
                modifier = Modifier.padding(paddingValues)
            )
            "orders" -> OrdersScreen(
                modifier = Modifier.padding(paddingValues)
            )
            "tracking" -> OrderTrackingScreen(
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