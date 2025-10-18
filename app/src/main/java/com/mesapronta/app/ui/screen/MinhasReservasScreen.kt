//package com.mesapronta.app.ui.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.mesapronta.app.data.model.Reserva
//import com.mesapronta.app.data.model.StatusReserva
//import java.time.LocalDate
//import java.time.LocalTime
//import java.time.format.DateTimeFormatter
//
//@Composable
//fun MinhasReservasScreen(navController: NavController) {
//    // Lista de exemplo - substituir por dados reais do ViewModel
//    val reservasExemplo = listOf(
//        Reserva(
//            nomeCliente = "João Silva",
//            dataReserva = LocalDate.now().plusDays(1),
//            horarioReserva = LocalTime.of(19, 30),
//            numeroPessoas = 4,
//            status = StatusReserva.CONFIRMADA
//        ),
//        Reserva(
//            nomeCliente = "Maria Santos",
//            dataReserva = LocalDate.now().plusDays(3),
//            horarioReserva = LocalTime.of(20, 0),
//            numeroPessoas = 2,
//            status = StatusReserva.PENDENTE
//        )
//    )
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Minhas Reservas") },
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
//        ) {
//            if (reservasExemplo.isEmpty()) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    contentAlignment = androidx.compose.ui.Alignment.Center
//                ) {
//                    Text(
//                        text = "Nenhuma reserva encontrada",
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                }
//            } else {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(reservasExemplo) { reserva ->
//                        ReservaCard(reserva = reserva)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ReservaCard(reserva: Reserva) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = reserva.nomeCliente,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//                )
//                Text(
//                    text = reserva.status.name,
//                    color = when (reserva.status) {
//                        StatusReserva.CONFIRMADA -> androidx.compose.ui.graphics.Color.Green
//                        StatusReserva.PENDENTE -> androidx.compose.ui.graphics.Color.Orange
//                        StatusReserva.CANCELADA -> androidx.compose.ui.graphics.Color.Red
//                        StatusReserva.FINALIZADA -> androidx.compose.ui.graphics.Color.Blue
//                    }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text("Data: ${reserva.dataReserva.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
//            Text("Horário: ${reserva.horarioReserva.format(DateTimeFormatter.ofPattern("HH:mm"))}")
//            Text("Pessoas: ${reserva.numeroPessoas}")
//        }
//    }
//}