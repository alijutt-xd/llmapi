package llmapi.jutt.data

import kotlinx.serialization.Serializable

@Serializable
data class FreeLlmSettings(
    val serverUrl: String = "http://localhost:3001",
    val apiKey: String = "",
    val streamingEnabled: Boolean = true,
    val timeoutSeconds: Int = 30,
    val darkMode: Boolean = true,
    val useRemoteServer: Boolean = true,
    val defaultModel: String = "auto",
    val saveHistory: Boolean = true,
    val saveRequestHistory: Boolean = true
)

@Serializable
data class ChatMessage(
    val id: String,
    val role: String,
    val content: String,
    val timestamp: Long,
    val provider: String? = null,
    val model: String? = null
)

@Serializable
data class Conversation(
    val id: String,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val model: String = "auto",
    val provider: String = "remote"
)

@Serializable
data class ModelInfo(
    val id: String,
    val provider: String,
    val name: String,
    val contextWindow: Int? = null,
    val capabilities: List<String> = emptyList(),
    val modality: String = "text",
    val status: String = "available",
    val endpointType: String = "OpenAI-compatible",
    val isFree: Boolean = false
)

@Serializable
data class ProviderInfo(
    val name: String,
    val enabled: Boolean = true,
    val baseUrl: String = "",
    val apiKeyMask: String = "",
    val status: String = "unknown",
    val priority: Int = 0,
    val modelCount: Int = 0
)

@Serializable
data class RequestHistoryItem(
    val id: String,
    val provider: String,
    val model: String,
    val timestamp: Long,
    val success: Boolean,
    val latencyMs: Long,
    val tokenUsage: Int = 0,
    val fallbackStatus: String = "none",
    val error: String? = null,
    val requestType: String = "chat"
)
