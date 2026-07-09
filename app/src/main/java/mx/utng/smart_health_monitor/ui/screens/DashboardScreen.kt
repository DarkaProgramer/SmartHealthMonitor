package mx.utng.smart_health_monitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import kotlinx.coroutines.launch
import mx.utng.smart_health_monitor.data.SmartHealthRepository
import mx.utng.smart_health_monitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val historial by viewModel.historial.collectAsState()

    // ── Estado del diálogo y Snackbar ──────────────────────────────
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ── Diálogo condicional ────────────────────────────────────────
    if (mostrarAlerta) {
        AlertaScreen(
            fc = fc,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = {
                mostrarAlerta = false
                scope.launch {
                    snackbarHost.showSnackbar(
                        message = "✅ Alerta enviada a tus contactos de emergencia",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartHealth Monitor") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // ✅ CastButton: AndroidView que envuelve MediaRouteButton (S13)
                    AndroidView(
                        factory = { context ->
                            MediaRouteButton(context).apply {
                                CastButtonFactory.setUpMediaRouteButton(context, this)
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHost) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarAlerta = true },
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Enviar alerta de emergencia"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Tarjeta de FC ──────────────────────────────────────
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Frecuencia Cardíaca",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$fc BPM",
                            style = MaterialTheme.typography.headlineLarge,
                            color = if (fc in 60..100)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // ── Tarjeta de Pasos ────────────────────────────────────
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pasos Diarios",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$pasos pasos",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // ── Historial ───────────────────────────────────────────
            item {
                Text(
                    text = "Historial Reciente",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(historial) { lectura ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (lectura.esNormal)
                            MaterialTheme.colorScheme.surfaceVariant
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Lectura: ${lectura.valorBpm} BPM",
                            color = if (lectura.esNormal)
                                MaterialTheme.colorScheme.onSurface
                            else
                                MaterialTheme.colorScheme.error
                        )
                        Text(lectura.hora)
                    }
                }
            }

            // ── Botón de simulación (DEBUG) ────────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val fcSimulado = (60..110).random()
                        val pasosSimulados = (3000..8000).random()
                        scope.launch {
                            SmartHealthRepository.actualizarFC(fcSimulado)
                        }
                        SmartHealthRepository.actualizarPasos(pasosSimulados)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Simular dato del wearable (DEBUG)")
                }
            }

            // ── Botón Ver Historial ────────────────────────────────
            item {
                Button(
                    onClick = onHistorialClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver historial completo")
                }
            }
        }
    }
}