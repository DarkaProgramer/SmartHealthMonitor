package mx.utng.smart_health_monitor.tv.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.smart_health_monitor.data.db.LecturaFC
import mx.utng.smart_health_monitor.tv.TvViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvCatalogScreen(
    onCardClick: (Int) -> Unit,
    viewModel: TvViewModel = viewModel()
) {
    val historial by viewModel.historial.collectAsState()
    val fc by viewModel.fc.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartHealth TV") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Fila 1: Estado actual
            Text(
                text = "Estado actual",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    TvCard(
                        lectura = LecturaFC(
                            id = 0,
                            valorBpm = if (fc == 0) 72 else fc,
                            hora = "Ahora"
                        ),
                        onClick = { onCardClick(0) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Fila 2: Historial
            Text(
                text = "Historial FC",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(historial) { lectura ->
                    TvCard(
                        lectura = lectura,
                        onClick = { onCardClick(lectura.id) }
                    )
                }
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
            containerColor = if (lectura.esNormal)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.error
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