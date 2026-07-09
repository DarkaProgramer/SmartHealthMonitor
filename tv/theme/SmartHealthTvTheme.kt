package mx.utng.smart_health_monitor.tv.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1B4F8A),
    secondary = androidx.compose.ui.graphics.Color(0xFFD4860A),
    background = androidx.compose.ui.graphics.Color(0xFF0D1117),
    surface = androidx.compose.ui.graphics.Color(0xFF1C1C1E),
    error = androidx.compose.ui.graphics.Color(0xFFB3261E)
)

@Composable
fun SmartHealthTvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}