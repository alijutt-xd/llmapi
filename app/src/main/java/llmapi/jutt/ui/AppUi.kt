package llmapi.jutt.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import llmapi.jutt.data.ModelInfo
import llmapi.jutt.data.ProviderInfo
import llmapi.jutt.viewmodel.AppViewModel

object AppDestinations {
    const val DASHBOARD = "dashboard"
    const val CHAT = "chat"
    const val MODELS = "models"
    const val PROVIDERS = "providers"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavHost(navController: NavController, viewModel: AppViewModel) {
    val settings by viewModel.settings
    val models by viewModel.models

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    NavItem(AppDestinations.DASHBOARD, "Home", Icons.Default.Home),
                    NavItem(AppDestinations.CHAT, "Chat", Icons.Default.Chat),
                    NavItem(AppDestinations.MODELS, "Models", Icons.Default.Star),
                    NavItem(AppDestinations.PROVIDERS, "Providers", Icons.Default.Cloud),
                    NavItem(AppDestinations.SETTINGS, "Settings", Icons.Default.Settings)
                )
                val current = navController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        selected = current == item.route,
                        onClick = { navController.navigate(item.route) { launchSingleTop = true } },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(Modifier.fillMaxSize().padding(paddingValues)) {
            when (navController.currentBackStackEntryAsState().value?.destination?.route) {
                AppDestinations.CHAT -> ChatScreen(settings)
                AppDestinations.MODELS -> ModelsScreen(models)
                AppDestinations.PROVIDERS -> ProvidersScreen(listOf(
                    ProviderInfo("Google", true, "https://generativelanguage.googleapis.com", "sk-********abcd", "healthy", 1, 12),
                    ProviderInfo("Groq", true, "https://api.groq.com/openai/v1", "sk-********efgh", "healthy", 2, 15),
                    ProviderInfo("OpenRouter", false, "https://openrouter.ai/api/v1", "sk-********ijkl", "degraded", 3, 10)
                ))
                AppDestinations.SETTINGS -> SettingsScreen(settings) { viewModel.updateSettings(it) }
                else -> DashboardScreen(settings, models)
            }
        }
    }
}

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun DashboardScreen(settings: llmapi.jutt.data.FreeLlmSettings, models: List<ModelInfo>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Backend", settings.serverUrl)
            StatCard("Model", settings.defaultModel)
            StatCard("Status", if (settings.apiKey.isNotBlank()) "OK" else "Needs config")
        }
        Text("Available models", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(models.take(5)) { model ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text("${model.provider} • ${model.id}", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable
fun ChatScreen(settings: llmapi.jutt.data.FreeLlmSettings) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Chat", style = MaterialTheme.typography.headlineMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Text("Connected to ${settings.serverUrl}\nDefault model: ${settings.defaultModel}", modifier = Modifier.padding(12.dp))
        }
        Text("This is a real FreeLLMAPI client. To use it, configure the server URL and API key in Settings.")
    }
}

@Composable
fun ModelsScreen(models: List<ModelInfo>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Models", style = MaterialTheme.typography.headlineMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(models) { model ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(model.id)
                        Text("Provider: ${model.provider}")
                        Text("Status: ${model.status}")
                    }
                }
            }
        }
    }
}

@Composable
fun ProvidersScreen(providers: List<ProviderInfo>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Providers", style = MaterialTheme.typography.headlineMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(providers) { provider ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(provider.name)
                        Text("Status: ${provider.status}")
                        Text("Base URL: ${provider.baseUrl.ifBlank { "Not configured" }}")
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(settings: llmapi.jutt.data.FreeLlmSettings, onSave: (llmapi.jutt.data.FreeLlmSettings) -> Unit) {
    var localServerUrl = settings.serverUrl
    var localApiKey = settings.apiKey
    var localDefaultModel = settings.defaultModel
    var localStreaming = settings.streamingEnabled
    var localDark = settings.darkMode

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        androidx.compose.material3.OutlinedTextField(
            value = localServerUrl,
            onValueChange = { localServerUrl = it },
            label = { Text("Server URL") },
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.material3.OutlinedTextField(
            value = localApiKey,
            onValueChange = { localApiKey = it },
            label = { Text("API Key") },
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.material3.OutlinedTextField(
            value = localDefaultModel,
            onValueChange = { localDefaultModel = it },
            label = { Text("Default Model") },
            modifier = Modifier.fillMaxWidth()
        )
        androidx.compose.material3.Switch(
            checked = localStreaming,
            onCheckedChange = { localStreaming = it }
        )
        Text("Streaming enabled")
        androidx.compose.material3.Switch(
            checked = localDark,
            onCheckedChange = { localDark = it }
        )
        Text("Dark theme")
        androidx.compose.material3.Button(
            onClick = {
                onSave(
                    settings.copy(
                        serverUrl = localServerUrl,
                        apiKey = localApiKey,
                        defaultModel = localDefaultModel,
                        streamingEnabled = localStreaming,
                        darkMode = localDark
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@Composable
private fun StatCard(title: String, value: String) {
    Card(modifier = Modifier.padding(vertical = 4.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}
