package mx.utng.smart_health_monitor.wear.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.*
import mx.utng.smart_health_monitor.wear.presentation.components.WearFCCard
import mx.utng.smart_health_monitor.wear.presentation.theme.SmartHealthWearTheme

@Composable
fun WearDashboardScreen(
    viewModel: WearViewModel = WearViewModel(androidx.compose.ui.platform.LocalContext.current)
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Item 1: Card de FC
            item {
                WearFCCard(
                    fc = state.fcActual,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Item 2: Estado de conexión
            item {
                Chip(
                    label = {
                        Text(
                            text = if (state.fcActual > 0) {
                                "🟢 FC: ${state.fcActual} bpm"
                            } else {
                                "⏳ Esperando datos..."
                            }
                        )
                    },
                    onClick = { /* No action */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Item 3: Chip de Alerta
            item {
                Chip(
                    label = { Text("⚠️ Alerta") },
                    onClick = { /* Navegar a Alerta */ },
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Item 4: Hora de última actualización
            item {
                Text(
                    text = "Última actualización: ${state.ultimaActualizacion}",
                    style = MaterialTheme.typography.caption2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WearDashboardScreenPreview() {
    SmartHealthWearTheme {
        WearDashboardScreen()
    }
}