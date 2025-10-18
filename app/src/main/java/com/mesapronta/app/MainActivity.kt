package com.mesapronta.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.mesapronta.app.ui.screen.HomeScreen
import com.mesapronta.app.ui.screen.LoginScreen
import com.mesapronta.app.ui.theme.MesaProntaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MesaProntaAppTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                if (isLoggedIn) {
                    HomeScreen(
                        onLogout = {
                            println("🔙 FAZENDO LOGOUT")
                            isLoggedIn = false
                        },
                        onOrdersClick = {
                            println("📦 PEDIDOS REALIZADOS")
                        },
                        onTrackOrdersClick = {
                            println("🚚 ACOMPANHAR PEDIDOS")
                        }
                    )
                } else {
                    LoginScreen(
                        onLoginSuccess = {
                            println("🎯 LOGIN BEM SUCEDIDO")
                            isLoggedIn = true
                        }
                    )
                }
            }
        }
    }
}