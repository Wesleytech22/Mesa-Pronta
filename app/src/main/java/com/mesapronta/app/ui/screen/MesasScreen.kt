//package com.mesapronta.app.ui.screen
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.Card
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.mesapronta.app.data.model.Mesa
//import com.mesapronta.app.ui.viewmodel.MesaViewModel
//
//@Composable
//fun MesasScreen(
//    viewModel: MesaViewModel = viewModel(),
//    navController: NavHostController
//) {
//    val mesas by viewModel.mesasState.collectAsState()
//    val loading by viewModel.loadingState.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(
//            text = "Mesas Disponíveis",
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        if (loading) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else {
//            LazyColumn {
//                items(mesas) { mesa ->
//                    MesaItem(mesa = mesa)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MesaItem(mesa: Mesa) {
//    Card(
//        modifier = Modifier
//            .padding(vertical = 4.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = "Mesa ${mesa.numero}",
//                style = MaterialTheme.typography.titleMedium
//            )
//            Text(
//                text = "Capacidade: ${mesa.capacidade} pessoas",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            Text(
//                text = "Local: ${mesa.localizacao.name}",
//                style = MaterialTheme.typography.bodyMedium
//            )
//            Text(
//                text = if (mesa.disponivel) "✅ Disponível" else "❌ Indisponível",
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//}