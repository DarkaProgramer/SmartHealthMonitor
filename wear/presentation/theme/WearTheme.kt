package mx.utng.smart_health_monitor.wear.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun SmartHealthWearTheme(
    content: @Composable () -> Unit
) {
    // Wear Material Theme --- versión circular de MD3
    MaterialTheme(
        content = content
    )
}