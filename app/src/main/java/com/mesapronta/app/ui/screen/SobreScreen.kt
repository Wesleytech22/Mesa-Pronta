//package com.mesapronta.app.ui.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Phone
//import androidx.compose.material.icons.filled.Email
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//
//@Composable
//fun SobreScreen(navController: NavController) {
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Sobre o Restaurante") },
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
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//        ) {
//            // Informações do Restaurante
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "Mesa Pronta Restaurante",
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Endereço
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.LocationOn,
//                            contentDescription = "Endereço",
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "Rua Principal, 123 - Centro\nSão Paulo, SP",
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    // Telefone
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.Phone,
//                            contentDescription = "Telefone",
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "(11) 9999-9999",
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    // Email
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.Email,
//                            contentDescription = "Email",
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(
//                            text = "contato@mesapronta.com",
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Horário de Funcionamento
//                    Text(
//                        text = "Horário de Funcionamento:",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//                    )
//                    Text("Segunda a Sexta: 11h às 23h")
//                    Text("Sábados e Domingos: 12h às 00h")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Sobre o App
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "Sobre o App",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "O Mesa Pronta é um aplicativo que facilita a reserva de mesas em nosso restaurante. " +
//                                "Com ele, você pode escolher a data, horário e número de pessoas de forma rápida e prática.",
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//            }
//        }
//    }
//}