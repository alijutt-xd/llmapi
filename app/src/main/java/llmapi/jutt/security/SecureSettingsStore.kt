package llmapi.jutt.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import llmapi.jutt.data.FreeLlmSettings

class SecureSettingsStore(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        "free_llm_api_secure_prefs",
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun readSettings(): FreeLlmSettings = withContext(Dispatchers.IO) {
        FreeLlmSettings(
            serverUrl = prefs.getString("server_url", "http://localhost:3001") ?: "http://localhost:3001",
            apiKey = prefs.getString("api_key", "") ?: "",
            streamingEnabled = prefs.getBoolean("streaming_enabled", true),
            timeoutSeconds = prefs.getInt("timeout_seconds", 30),
            darkMode = prefs.getBoolean("dark_mode", true),
            useRemoteServer = prefs.getBoolean("use_remote_server", true),
            defaultModel = prefs.getString("default_model", "auto") ?: "auto",
            saveHistory = prefs.getBoolean("save_history", true),
            saveRequestHistory = prefs.getBoolean("save_request_history", true)
        )
    }

    suspend fun saveSettings(settings: FreeLlmSettings) = withContext(Dispatchers.IO) {
        prefs.edit().apply {
            putString("server_url", settings.serverUrl)
            putString("api_key", settings.apiKey)
            putBoolean("streaming_enabled", settings.streamingEnabled)
            putInt("timeout_seconds", settings.timeoutSeconds)
            putBoolean("dark_mode", settings.darkMode)
            putBoolean("use_remote_server", settings.useRemoteServer)
            putString("default_model", settings.defaultModel)
            putBoolean("save_history", settings.saveHistory)
            putBoolean("save_request_history", settings.saveRequestHistory)
        }.apply()
    }
}
