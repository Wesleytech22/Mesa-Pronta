package com.mesapronta.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)

class AuthViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError.asStateFlow()

    // Lista de usuários cadastrados
    private val registeredUsers = mutableListOf<User>()

    init {
        // Usuário de exemplo para teste
        registeredUsers.add(
            User(
                id = "1",
                fullName = "Wesley Rodrigues",
                username = "wesley.dias",
                email = "wesley@email.com",
                password = "1234"
            )
        )
    }

    fun login(username: String, password: String): Boolean {
        val user = registeredUsers.find {
            it.username == username && it.password == password
        }

        return if (user != null) {
            viewModelScope.launch {
                _isLoading.value = true
                delay(1000)
                _currentUser.value = user
                _isLoggedIn.value = true
                _isLoading.value = false
            }
            true
        } else {
            false
        }
    }

    fun register(fullName: String, username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _registerError.value = null

            // Simula chamada de API
            delay(1500)

            // Verifica se o usuário já existe
            val userExists = registeredUsers.any { it.username == username }

            if (userExists) {
                _registerError.value = "Nome de usuário já existe"
                _isLoading.value = false
            } else {
                val newUser = User(
                    id = System.currentTimeMillis().toString(),
                    fullName = fullName,
                    username = username,
                    email = email,
                    password = password
                )

                registeredUsers.add(newUser)
                _currentUser.value = newUser
                _isLoggedIn.value = true
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            _isLoggedIn.value = false
            _registerError.value = null
        }
    }

    fun clearError() {
        _registerError.value = null
    }
}