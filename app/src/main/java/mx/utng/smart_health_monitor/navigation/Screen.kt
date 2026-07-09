package mx.utng.smart_health_monitor.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Historial : Screen("historial")
    object Alerta : Screen("alerta")
}