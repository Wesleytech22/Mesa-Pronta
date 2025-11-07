package com.mesapronta.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintsScreen(modifier: Modifier = Modifier) {
    var complaintText by remember { mutableStateOf(TextFieldValue()) }
    var selectedOrder by remember { mutableStateOf("") }
    var complaintType by remember { mutableStateOf("") }
    val context = LocalContext.current

    val orders = listOf("ORD001", "ORD002", "ORD003", "ORD004")
    val complaintTypes = listOf(
        "Problema com o pedido",
        "Atendimento",
        "Entrega",
        "Qualidade da comida",
        "Outro"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reclamações") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // Ícone de suporte
            Icon(
                imageVector = Icons.Default.SupportAgent,
                contentDescription = "Suporte",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Central de Ajuda",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Seleção do pedido
            Text("Número do Pedido (opcional)", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            orders.forEach { order ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedOrder == order,
                        onClick = { selectedOrder = order }
                    )
                    Text(
                        text = order,
                        modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tipo de reclamação
            Text("Tipo de Reclamação", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            complaintTypes.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = complaintType == type,
                        onClick = { complaintType = type }
                    )
                    Text(
                        text = type,
                        modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Descrição da reclamação
            Text("Descreva sua reclamação", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = complaintText,
                onValueChange = { complaintText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = { Text("Descreva detalhadamente o problema encontrado...") },
                singleLine = false
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botão de enviar
            Button(
                onClick = {
                    if (complaintText.text.isNotBlank() && complaintType.isNotBlank()) {
                        Toast.makeText(context, "Reclamação enviada com sucesso!", Toast.LENGTH_SHORT).show()
                        complaintText = TextFieldValue()
                        selectedOrder = ""
                        complaintType = ""
                    } else {
                        Toast.makeText(context, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = complaintText.text.isNotBlank() && complaintType.isNotBlank()
            ) {
                Text("Enviar Reclamação")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Informações de contato
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Outras formas de contato:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📞 Telefone: (11) 9999-9999")
                    Text("📧 Email: suporte@mesapronta.com")
                    Text("🕒 Horário: 8h às 22h")
                }
            }
        }
    }
}