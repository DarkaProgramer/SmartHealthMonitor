package mx.utng.smart_health_monitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.utng.smart_health_monitor.tv.TvViewModel

@Composable
fun TvDetailScreen(
    lecturaId: Int,
    navController: NavController,
    viewModel: TvViewModel = viewModel()
) {
    val historial by viewModel.historial.collectAsState()
    val lectura = historial.find { it.id == lecturaId }

    if (lectura == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Lectura no encontrada")
        }
        return
    }

    val firstBtnFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        firstBtnFocus.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B4A))
            .padding(64.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        // Panel izquierdo - ícono + datos
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color(0xFF1565C0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("❤", fontSize = 80.sp)
            }

            Text(
                text = "${lectura.valorBpm} bpm",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Estado: ${if (lectura.esNormal) "Normal" else "Alerta"}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )

            Text(
                text = "Hora: ${lectura.hora}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        // Panel derecho - botones de acción
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Botón Reproducir
            Surface(
                onClick = { navController.navigate("playback") },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp)
                    .focusRequester(firstBtnFocus),
                colors = SurfaceDefaults.colors(
                    containerColor = Color(0xFF1B5E20),
                    focusedContainerColor = Color(0xFF76FF03)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "▶ Reproducir",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Botón Volver
            Surface(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                colors = SurfaceDefaults.colors(
                    containerColor = Color(0xFF37474F),
                    focusedContainerColor = Color(0xFF90A4AE)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "← Volver",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}