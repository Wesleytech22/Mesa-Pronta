package com.mesapronta.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mesapronta.app.model.ReservationDetails
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    reservation: ReservationDetails,
    onCheckInComplete: (Boolean) -> Unit, // True se for consumo no local, False se for retirada
    onBack: () -> Unit
) {
    var checkInCompleted by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale("pt", "BR"))

    // Converte o horário da reserva para LocalTime para comparação
    val reservationTime = try {
        LocalTime.parse(reservation.reservationTime, formatter)
    } catch (e: Exception) {
        LocalTime.NOON
    }

    // Determina se o check-in está atrasado
    val isDelayed = LocalTime.now().isAfter(reservationTime)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Check-in",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (checkInCompleted) {
                // ... (Tela de confirmação - mantida)
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Check-in confirmado",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(120.dp)
                )

                Text(
                    "Check-in Realizado!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF4CAF50)
                )

                Text(
                    "Seu check-in na mesa ${reservation.tableNumber} foi confirmado com sucesso!\n\nAproveite sua refeição no ${reservation.restaurantName}!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = { onCheckInComplete(!isDelayed) }, // Passa 'isDelayed' para o MainActivity
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        if (isDelayed) "Confirmar Retirada" else "Confirmar Check-in",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {
                // Lógica para atraso
                if (isDelayed) {
                    DelayedCheckInWarning(
                        onConfirmRetirada = {
                            checkInCompleted = true
                        },
                        onPayFee = {
                            // Implementar navegação para pagamento de taxa
                            onCheckInComplete(true) // Simula que a taxa foi paga e permite o check-in
                        }
                    )
                } else {
                    // Tela de check-in normal (código anterior)
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = "Check-in",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(120.dp)
                    )

                    Text(
                        "Check-in na Mesa",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    // ... (Card de informações da reserva)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                reservation.restaurantName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Mesa:"); Text("${reservation.tableNumber}", fontWeight = FontWeight.Bold) }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Horário:"); Text(reservation.reservationTime, fontWeight = FontWeight.Bold) }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Pessoas:"); Text("${reservation.numberOfPeople}", fontWeight = FontWeight.Bold) }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "QR CODE\nMesa ${reservation.tableNumber}\n${reservation.restaurantName}",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Button(
                        onClick = { checkInCompleted = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Confirmar Check-in",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DelayedCheckInWarning(
    onConfirmRetirada: () -> Unit,
    onPayFee: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCC80))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = "Atraso",
                tint = Color(0xFFE65100),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Atenção: Horário Expirado",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "O horário agendado para sua reserva já passou. Você tem as seguintes opções:",
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Opção 1: Retirada (Default)
    Button(
        onClick = onConfirmRetirada,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
    ) {
        Text("Confirmar Retirada (Sem Custo)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Opção 2: Pagar Taxa para Consumo no Local
    Button(
        onClick = onPayFee,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
    ) {
        Text("Pagar Taxa de Atraso (R$ 50,00) e Consumir no Local", textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}