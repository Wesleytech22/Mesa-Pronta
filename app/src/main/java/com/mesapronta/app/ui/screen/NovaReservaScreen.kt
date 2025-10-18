//package com.mesapronta.app.ui.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.CalendarToday
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Phone
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import java.time.LocalDate
//import java.time.LocalTime
//import java.time.format.DateTimeFormatter
//
//@Composable
//fun NovaReservaScreen(
//    navController: NavController
//) {
//    var nome by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var telefone by remember { mutableStateOf("") }
//    var numeroPessoas by remember { mutableStateOf(1) }
//    var data by remember { mutableStateOf(LocalDate.now()) }
//    var horario by remember { mutableStateOf(LocalTime.of(19, 0)) }
//    var observacoes by remember { mutableStateOf("") }
//
//    val scrollState = rememberScrollState()
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Nova Reserva") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(scrollState)
//                .padding(16.dp)
//        ) {
//            // Formulário de Reserva
//            OutlinedTextField(
//                value = nome,
//                onValueChange = { nome = it },
//                label = { Text("Nome Completo") },
//                modifier = Modifier.fillMaxWidth(),
//                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("E-mail") },
//                modifier = Modifier.fillMaxWidth(),
//                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Email
//                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = telefone,
//                onValueChange = { telefone = it },
//                label = { Text("Telefone") },
//                modifier = Modifier.fillMaxWidth(),
//                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
//                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Phone
//                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("Número de Pessoas: $numeroPessoas")
//                Row {
//                    IconButton(
//                        onClick = { if (numeroPessoas > 1) numeroPessoas-- }
//                    ) {
//                        Text("-", style = MaterialTheme.typography.titleLarge)
//                    }
//                    IconButton(
//                        onClick = { if (numeroPessoas < 20) numeroPessoas++ }
//                    ) {
//                        Text("+", style = MaterialTheme.typography.titleLarge)
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Data e Hora
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("Data: ${data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
//                Button(
//                    onClick = { /* Abrir date picker */ }
//                ) {
//                    Icon(Icons.Default.CalendarToday, contentDescription = null)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Escolher Data")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text("Horário: ${horario.format(DateTimeFormatter.ofPattern("HH:mm"))}")
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = observacoes,
//                onValueChange = { observacoes = it },
//                label = { Text("Observações (opcional)") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp),
//                singleLine = false
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Button(
//                onClick = {
//                    // Criar reserva
//                    navController.popBackStack()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text("Confirmar Reserva", style = MaterialTheme.typography.titleMedium)
//            }
//        }
//    }
//}