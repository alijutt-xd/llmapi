package llmapi.jutt.repository

import llmapi.jutt.data.FreeLlmSettings
import llmapi.jutt.data.ModelInfo
import llmapi.jutt.network.ChatRequest
import llmapi.jutt.network.ChatResponse
import llmapi.jutt.network.FreeLlmApiService
import llmapi.jutt.network.MessageDto
import llmapi.jutt.network.ModelListResponse
import llmapi.jutt.network.ModelDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

class FreeLlmRepository {
    private val json = Json { ignoreUnknownKeys = true }

    fun createService(settings: FreeLlmSettings): FreeLlmApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(settings.timeoutSeconds.toLong(), TimeUnit.SECONDS)
            .readTimeout(settings.timeoutSeconds.toLong(), TimeUnit.SECONDS)
            .writeTimeout(settings.timeoutSeconds.toLong(), TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(settings.serverUrl.trimEnd('/') + "/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(FreeLlmApiService::class.java)
    }

    suspend fun fetchModels(service: FreeLlmApiService): List<ModelInfo> {
        val result: ModelListResponse = service.getModels()
        return result.data.map { dto ->
            ModelInfo(
                id = dto.id,
                provider = dto.provider ?: dto.ownedBy ?: "unknown",
                name = dto.id,
                contextWindow = dto.contextWindow,
                capabilities = dto.capabilities.keys.toList(),
                modality = dto.modality ?: "text",
                status = dto.status ?: "available",
                endpointType = dto.endpointType ?: "OpenAI-compatible"
            )
        }
    }

    suspend fun sendChat(
        service: FreeLlmApiService,
        model: String,
        prompt: String,
        stream: Boolean,
    ): ChatResponse {
        val request = ChatRequest(
            model = model,
            messages = listOf(MessageDto("user", prompt)),
            stream = stream,
            temperature = 0.7,
            maxTokens = 512
        )
        return service.chatCompletions(request)
    }
}
