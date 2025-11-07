package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.mesapronta.app.ui.screens.HomeScreen
import com.mesapronta.app.ui.screens.OrderTrackingScreen

@Composable
fun MainScreenWithBottomNav(
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    onLogout: () -> Unit,
    onRestaurantSelected: (com.mesapronta.app.model.Restaurant) -> Unit
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
                        icon = { Icon(item.icon, contentDescription = item.title) },
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
                modifier = androidx.compose.ui.Modifier.padding(paddingValues)
            )
            "orders" -> OrdersScreen(modifier = androidx.compose.ui.Modifier.padding(paddingValues))
            "tracking" -> OrderTrackingScreen(modifier = androidx.compose.ui.Modifier.padding(paddingValues))
            "complaints" -> ComplaintsScreen(modifier = androidx.compose.ui.Modifier.padding(paddingValues))
        }
    }
}

data class NavigationItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)