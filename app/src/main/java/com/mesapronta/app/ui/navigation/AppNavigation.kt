//package com.mesapronta.app.ui.navigation
//
//import androidx.navigation.NavHostController
//
///**
// * Funções auxiliares para navegação
// */
//
//// Navega para uma tela específica
//fun navigateTo(navController: NavHostController, screen: Screen) {
//    navController.navigate(screen.route)
//}
//
//// Navega para uma tela específica limpando o back stack
//fun navigateToWithClearBackStack(navController: NavHostController, screen: Screen) {
//    navController.navigate(screen.route) {
//        popUpTo(Screen.Home.route) { inclusive = true }
//    }
//}
//
//// Navegação para voltar
//fun navigateBack(navController: NavHostController) {
//    navController.popBackStack()
//}
//
//// Configurações de navegação
//fun setupNavigationGraph(navController: NavHostController) {
//    // Aqui você pode adicionar lógica adicional de configuração de navegação
//    // como interceptadores, deep links, etc.
//}
//
//// Função para navegar para nova reserva com parâmetros (futuramente)
//fun navigateToNovaReserva(navController: NavHostController, mesaId: String? = null) {
//    val route = if (mesaId != null) {
//        "${Screen.NovaReserva.route}/$mesaId"
//    } else {
//        Screen.NovaReserva.route
//    }
//    navController.navigate(route)
//}