package mx.utng.smart_health_monitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import mx.utng.smart_health_monitor.navigation.NavGraph
import mx.utng.smart_health_monitor.ui.theme.Smart_Health_MonitorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Smart_Health_MonitorTheme {
                NavGraph()  // ← Usa NavGraph, no DashboardScreen
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    Smart_Health_MonitorTheme {
        NavGraph()
    }
}