package mx.utng.smart_health_monitor.tv.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import mx.utng.smart_health_monitor.data.db.LecturaFC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: TvViewModel = viewModel(
        factory = TvViewModelFactory(context)
    )
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartHealth TV") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = if (state.isLoading) {
                                androidx.compose.material.icons.Icons.Default.Refresh
                            } else {
                                androidx.compose.material.icons.Icons.Default.Refresh
                            },
                            contentDescription = "Actualizar",
                            tint = MaterialTheme.colorScheme.onPrimary
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
                .padding(16.dp)
        ) {
            // ── Fila 1: Estado actual ──────────────────────────────
            Text(
                text = "📊 Estado actual",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Card con la FC actual
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.fcActual in 60..100) {
                        MaterialTheme.colorScheme.primary
                    } else if (state.fcActual > 0) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (state.fcActual > 0) {
                            "${state.fcActual} bpm"
                        } else {
                            "⏳ Esperando datos..."
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (state.fcActual > 0) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Text(
                        text = if (state.fcActual > 0) {
                            "${state.fcEstado} • ${state.ultimaHora}"
                        } else {
                            "Conéctate al broker MQTT"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (state.fcActual > 0) {
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Fila 2: Historial ──────────────────────────────────
            Text(
                text = "📋 Historial",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mostrar mensaje si no hay datos
            if (state.fcActual == 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "No hay datos disponibles. Esperando mensajes MQTT...",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                // Datos simulados para mostrar cards
                val historialDatos = listOf(
                    LecturaFC(id = 1, valorBpm = state.fcActual, hora = state.ultimaHora),
                    LecturaFC(id = 2, valorBpm = state.fcActual - 5, hora = "hace 5 min"),
                    LecturaFC(id = 3, valorBpm = state.fcActual + 3, hora = "hace 10 min"),
                    LecturaFC(id = 4, valorBpm = state.fcActual - 8, hora = "hace 15 min"),
                    LecturaFC(id = 5, valorBpm = state.fcActual + 2, hora = "hace 20 min")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(historialDatos) { lectura ->
                        TvCard(
                            lectura = lectura,
                            onClick = { onCardClick(lectura.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Estado de conexión MQTT ────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.fcActual > 0) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Text(
                    text = if (state.fcActual > 0) {
                        "🟢 Conectado a HiveMQ • Último dato: ${state.ultimaHora}"
                    } else {
                        "🟠 Esperando conexión MQTT..."
                    },
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun TvCard(
    lectura: LecturaFC,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(160.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (lectura.esNormal) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${lectura.valorBpm} bpm",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = lectura.hora,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }
    }
}

/**
 * Factory para TvViewModel
 */
class TvViewModelFactory(private val context: Context) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TvViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}