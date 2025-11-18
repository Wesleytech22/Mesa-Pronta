package com.mesapronta.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // Lista de usuários cadastrados (em uma app real, isso viria de um banco de dados)
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
                _currentUser.value = user
                _isLoggedIn.value = true
                _isLoading.value = false
            }
            true
        } else {
            false
        }
    }

    fun register(fullName: String, username: String, email: String, password: String): Boolean {
        // Verifica se o username já existe
        val userExists = registeredUsers.any { it.username == username }

        return if (!userExists) {
            val newUser = User(
                id = System.currentTimeMillis().toString(),
                fullName = fullName,
                username = username,
                email = email,
                password = password
            )

            viewModelScope.launch {
                _isLoading.value = true
                registeredUsers.add(newUser)
                _currentUser.value = newUser
                _isLoggedIn.value = true
                _isLoading.value = false
            }
            true
        } else {
            false
        }
    }

    fun logout() {
        viewModelScope.launch {
            _currentUser.value = null
            _isLoggedIn.value = false
        }
    }

    fun getCurrentUser(): User? {
        return _currentUser.value
    }
}