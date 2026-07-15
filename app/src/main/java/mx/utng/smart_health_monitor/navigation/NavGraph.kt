package mx.utng.smart_health_monitor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.utng.smart_health_monitor.ui.screens.DashboardScreen
import mx.utng.smart_health_monitor.ui.screens.HistorialScreen
import mx.utng.smart_health_monitor.ui.screens.LoginScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // ── Login ──────────────────────────────────────
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ── Dashboard ──────────────────────────────────
        composable("dashboard") {
            DashboardScreen(
                onHistorialClick = {
                    navController.navigate("historial")
                },
                onAlertClick = {
                    // TODO: Navegar a AlertaScreen
                }
            )
        }

        // ── Historial ──────────────────────────────────
        composable("historial") {
            HistorialScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}