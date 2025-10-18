//package com.mesapronta.app.ui.navigation
//
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.compose.runtime.Composable
//import com.mesapronta.app.ui.screen.HomeScreen
//import com.mesapronta.app.ui.screen.MesasScreen
//import com.mesapronta.app.ui.screen.MinhasReservasScreen
//import com.mesapronta.app.ui.screen.NovaReservaScreen
//import com.mesapronta.app.ui.screen.SobreScreen
//
//@Composable
//fun AppNavHost(
//    navController: NavHostController
//) {
//    NavHost(
//        navController = navController,
//        startDestination = Screen.Home.route
//    ) {
//        composable(Screen.Home.route) {
//            HomeScreen(navController = navController)
//        }
//        composable(Screen.NovaReserva.route) {
//            NovaReservaScreen(navController = navController)
//        }
//        composable(Screen.MinhasReservas.route) {
//            MinhasReservasScreen(navController = navController)
//        }
//        composable(Screen.Sobre.route) {
//            SobreScreen(navController = navController)
//        }
//        composable(Screen.Mesas.route) {
//            MesasScreen(navController = navController)
//        }
//    }
//}