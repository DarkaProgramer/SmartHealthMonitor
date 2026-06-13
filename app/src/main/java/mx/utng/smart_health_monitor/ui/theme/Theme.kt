package mx.utng.smart_health_monitor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// ── Esquema Light ─────────────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary            = SHPrimary,
    onPrimary          = SHOnPrimary,
    primaryContainer   = SHPrimaryContainer,
    onPrimaryContainer = SHOnPrimaryContainer,
    secondary          = SHSecondary,
    onSecondary        = SHOnSecondary,
    secondaryContainer = SHSecondaryContainer,
    tertiary           = SHTertiary,
    error              = SHError,
    onError            = SHOnError,
    errorContainer     = SHErrorContainer,
    background         = SHBackground,
    onBackground       = SHOnBackground,
    surface            = SHSurface,
    onSurface          = SHOnSurface,
    onSurfaceVariant   = SHOnSurfaceVariant,
    outlineVariant     = SHOutlineVariant,
)

// ── Esquema Dark ──────────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary            = SHPrimaryDark,
    onPrimary          = SHOnPrimaryDark,
    primaryContainer   = SHPrimaryContainerDark,
    secondary          = SHSecondaryDark,
    onSecondary        = SHOnSecondaryDark,
    tertiary           = SHTertiaryDark,
    error              = SHErrorDark,
    onError            = SHOnErrorDark,
    background         = SHBackgroundDark,
    onBackground       = SHOnBackgroundDark,
    surface            = SHSurfaceDark,
    onSurface          = SHOnSurfaceDark,
    onSurfaceVariant   = SHOnSurfaceVariantDark,
    outlineVariant     = SHOutlineVariantDark,
)

// ── Tema principal de SmartHealth Monitor ─────────────────────────────────────
@Composable
fun Smart_Health_MonitorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}

// Alias para compatibilidad con el código de los ejercicios
val SmartHealthMonitorTheme: @Composable (@Composable () -> Unit) -> Unit
    get() = { content -> Smart_Health_MonitorTheme(content = content) }