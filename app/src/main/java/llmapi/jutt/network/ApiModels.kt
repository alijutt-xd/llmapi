package llmapi.jutt.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelListResponse(
    val data: List<ModelDto> = emptyList()
)

@Serializable
data class ModelDto(
    val id: String = "",
    val object: String = "model",
    @SerialName("owned_by") val ownedBy: String = "",
    val created: Long = 0L,
    val status: String? = null,
    val capabilities: Map<String, String> = emptyMap(),
    val modality: String? = null,
    @SerialName("context_window") val contextWindow: Int? = null,
    val provider: String? = null,
    val endpointType: String? = null,
    val free: Boolean = false
)

@Serializable
data class ChatRequest(
    val model: String = "auto",
    val messages: List<MessageDto>,
    val stream: Boolean = false,
    val temperature: Double? = null,
    @SerialName("max_tokens") val maxTokens: Int? = null
)

@Serializable
data class MessageDto(
    val role: String,
    val content: String
)

@Serializable
data class ChatResponse(
    val id: String = "",
    val choices: List<ChoiceDto> = emptyList(),
    val usage: UsageDto? = null
)

@Serializable
data class ChoiceDto(
    val message: MessageDto? = null,
    val finishReason: String? = null,
    @SerialName("finish_reason") val finishReasonAlt: String? = null
)

@Serializable
data class UsageDto(
    @SerialName("prompt_tokens") val promptTokens: Int = 0,
    @SerialName("completion_tokens") val completionTokens: Int = 0,
    @SerialName("total_tokens") val totalTokens: Int = 0
)
