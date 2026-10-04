package llmapi.jutt.repository

import llmapi.jutt.data.FreeLlmSettings
import llmapi.jutt.data.ModelInfo
import llmapi.jutt.data.ProviderInfo
import llmapi.jutt.network.FreeLlmApiClient
import llmapi.jutt.network.MessageDto
import llmapi.jutt.network.ChatRequest
import llmapi.jutt.network.ChatResponse
import llmapi.jutt.network.ModelListResponse

class FreeLlmRepository {
    suspend fun fetchModels(settings: FreeLlmSettings): List<ModelInfo> {
        if (settings.serverUrl.isBlank() || settings.apiKey.isBlank()) {
            return emptyList()
        }
        val client = FreeLlmApiClient(settings.serverUrl, settings.apiKey)
        val result: ModelListResponse = client.service.getModels()
        return result.data.map { model ->
            ModelInfo(
                id = model.id,
                provider = model.provider ?: model.ownedBy ?: "unknown",
                name = model.id,
                contextWindow = model.contextWindow,
                capabilities = model.capabilities.keys.toList(),
                modality = model.modality ?: "text",
                status = model.status ?: "available",
                endpointType = model.endpointType ?: "OpenAI-compatible",
                isFree = model.free
            )
        }
    }

    suspend fun sendChat(settings: FreeLlmSettings, model: String, prompt: String): ChatResponse {
        val client = FreeLlmApiClient(settings.serverUrl, settings.apiKey)
        val request = ChatRequest(
            model = model,
            messages = listOf(MessageDto("user", prompt)),
            stream = settings.streamingEnabled,
            temperature = 0.7,
            maxTokens = 512
        )
        return client.service.chatCompletions(request)
    }

    fun getDefaultProviders(): List<ProviderInfo> = listOf(
        ProviderInfo("Google", true, "https://generativelanguage.googleapis.com", "sk-*********abcd", "healthy", 1, 12),
        ProviderInfo("Groq", true, "https://api.groq.com/openai/v1", "sk-*********efgh", "healthy", 2, 17),
        ProviderInfo("OpenRouter", true, "https://openrouter.ai/api/v1", "sk-*********ijkl", "healthy", 3, 18),
        ProviderInfo("Custom", false, "", "", "not-configured", 0, 0)
    )
}
