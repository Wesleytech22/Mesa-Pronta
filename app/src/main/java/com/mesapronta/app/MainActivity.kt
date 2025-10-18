package com.mesapronta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mesapronta.app.ui.screen.HomeScreen
import com.mesapronta.app.ui.screen.LoginScreen
import com.mesapronta.app.ui.theme.MesaProntaAppTheme
import com.mesapronta.app.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MesaProntaAppTheme {
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                if (isLoggedIn) {
                    HomeScreen(
                        onLogout = { authViewModel.logout() },
                        onOrdersClick = {
                            // TODO: Navegar para tela de pedidos realizados
                            println("Navegar para Pedidos Realizados")
                        },
                        onTrackOrdersClick = {
                            // TODO: Navegar para tela de acompanhar pedidos
                            println("Navegar para Acompanhar Pedidos")
                        }
                    )
                } else {
                    LoginScreen(
                        onLoginSuccess = { authViewModel.login("admin", "1234") }
                    )
                }
            }
        }
    }
}