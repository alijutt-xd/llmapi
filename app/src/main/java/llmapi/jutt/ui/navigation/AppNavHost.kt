package llmapi.jutt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import llmapi.jutt.ui.screens.DashboardScreen
import llmapi.jutt.ui.screens.ChatScreen
import llmapi.jutt.ui.screens.ModelsScreen
import llmapi.jutt.ui.screens.ProvidersScreen
import llmapi.jutt.ui.screens.SettingsScreen

object AppDestinations {
    const val DASHBOARD = "dashboard"
    const val CHAT = "chat"
    const val MODELS = "models"
    const val PROVIDERS = "providers"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppDestinations.DASHBOARD) {
        composable(AppDestinations.DASHBOARD) {
            DashboardScreen(navController)
        }
        composable(AppDestinations.CHAT) {
            ChatScreen()
        }
        composable(AppDestinations.MODELS) {
            ModelsScreen()
        }
        composable(AppDestinations.PROVIDERS) {
            ProvidersScreen()
        }
        composable(AppDestinations.SETTINGS) {
            SettingsScreen()
        }
    }
}
