package mx.utng.smart_health_monitor.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import mx.utng.smart_health_monitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartHealthWearTheme {
                WearApp()
            }
        }
    }
}

@Composable
fun WearApp() {
    val context = LocalContext.current
    val viewModel = WearViewModel(context)
    WearDashboardScreen(viewModel = viewModel)
}

@Preview(showBackground = true)
@Composable
fun WearAppPreview() {
    SmartHealthWearTheme {
        WearApp()
    }
}