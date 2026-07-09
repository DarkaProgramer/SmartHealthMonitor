package mx.utng.smart_health_monitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.utng.smart_health_monitor.tv.presentation.TvCatalogScreen
import mx.utng.smart_health_monitor.tv.presentation.TvDetailScreen
import mx.utng.smart_health_monitor.tv.presentation.TvPlaybackScreen
import mx.utng.smart_health_monitor.tv.theme.SmartHealthTvTheme

class TVActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthTvTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TVNavHost()
                }
            }
        }
    }
}

@Composable
fun TVNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "catalog"
    ) {
        composable("catalog") {
            TvCatalogScreen(
                onCardClick = { lecturaId ->
                    navController.navigate("detail/$lecturaId")
                }
            )
        }

        composable(
            route = "detail/{lecturaId}",
            arguments = listOf(
                navArgument("lecturaId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("lecturaId") ?: return@composable
            TvDetailScreen(
                lecturaId = id,
                navController = navController
            )
        }

        composable("playback") {
            TvPlaybackScreen(
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TVNavHostPreview() {
    SmartHealthTvTheme {
        TVNavHost()
    }
}