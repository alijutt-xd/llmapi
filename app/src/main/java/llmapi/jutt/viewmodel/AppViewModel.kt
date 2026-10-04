package llmapi.jutt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import llmapi.jutt.data.FreeLlmSettings
import llmapi.jutt.data.ModelInfo
import llmapi.jutt.repository.FreeLlmRepository
import llmapi.jutt.security.SecureSettingsStore

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FreeLlmRepository()
    private val secureSettings = SecureSettingsStore(application)

    private val _settings = MutableStateFlow(FreeLlmSettings())
    val settings: StateFlow<FreeLlmSettings> = _settings.asStateFlow()

    private val _models = MutableStateFlow<List<ModelInfo>>(emptyList())
    val models: StateFlow<List<ModelInfo>> = _models.asStateFlow()

    init {
        viewModelScope.launch {
            _settings.value = secureSettings.readSettings()
            refreshModels()
        }
    }

    fun updateSettings(newSettings: FreeLlmSettings) {
        viewModelScope.launch {
            secureSettings.saveSettings(newSettings)
            _settings.value = newSettings
        }
    }

    fun refreshModels() {
        viewModelScope.launch {
            val current = _settings.value
            if (current.serverUrl.isNotBlank() && current.apiKey.isNotBlank()) {
                runCatching {
                    repository.fetchModels(current)
                }.onSuccess { _models.value = it }
            }
        }
    }
}
