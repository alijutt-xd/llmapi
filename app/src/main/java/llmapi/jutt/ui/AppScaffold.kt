package llmapi.jutt.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import llmapi.jutt.ui.navigation.AppDestinations
import llmapi.jutt.ui.screens.DashboardScreen
import llmapi.jutt.ui.screens.ChatScreen
import llmapi.jutt.ui.screens.ModelsScreen
import llmapi.jutt.ui.screens.ProvidersScreen
import llmapi.jutt.ui.screens.SettingsScreen

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppScaffold(navController: NavController) {
    val items = listOf(
        NavItem(AppDestinations.DASHBOARD, "Home", Icons.Default.Home),
        NavItem(AppDestinations.CHAT, "Chat", Icons.Default.Chat),
        NavItem(AppDestinations.MODELS, "Models", Icons.Default.Star),
        NavItem(AppDestinations.PROVIDERS, "Providers", Icons.Default.Cloud),
        NavItem(AppDestinations.SETTINGS, "Settings", Icons.Default.Settings)
    )

    val current = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = current == item.route,
                        onClick = { navController.navigate(item.route) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (current ?: AppDestinations.DASHBOARD) {
            AppDestinations.DASHBOARD -> DashboardScreen(navController)
            AppDestinations.CHAT -> ChatScreen()
            AppDestinations.MODELS -> ModelsScreen()
            AppDestinations.PROVIDERS -> ProvidersScreen()
            AppDestinations.SETTINGS -> SettingsScreen()
            else -> DashboardScreen(navController)
        }
    }
}
