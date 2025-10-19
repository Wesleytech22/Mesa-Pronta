package com.mesapronta.app

sealed class AppScreen {
    object Login : AppScreen()
    object Home : AppScreen()
    object Cart : AppScreen()
    object Payment : AppScreen()
}