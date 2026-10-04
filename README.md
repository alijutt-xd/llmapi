# FreeLLMAPI Android Client

This Android app is a real client for a FreeLLMAPI-compatible backend.

## Architecture

Android UI
→ ViewModel
→ Repository
→ Retrofit/OkHttp API layer
→ FreeLLMAPI server

The app is designed to work with a self-hosted FreeLLMAPI server such as:

- `http://localhost:3001`
- `http://192.168.1.10:3001`
- `https://your-server.example.com`

## What is implemented

- Material 3 UI and navigation
- Dashboard, chat, models, providers, and settings screens
- API connection settings
- encrypted local storage for server URL and API key
- model catalog fetch from `/v1/models`
- chat completion requests to `/v1/chat/completions`
- streaming toggle support
- provider and settings screens for remote configuration

## What is intentionally server-side

The actual FreeLLMAPI server remains the source of truth for:

- provider routing and failover
- the live model catalog
- provider key management
- rate limiting and cooldown handling
- OpenAI-compatible and Anthropic/Gemini/Ollama adapter logic
- CLI, desktop, and Docker features

## Build

1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the app on emulator or device.

## Configuration

In Settings, set:
- Server URL
- API key
- Streaming enabled
- default model

## Security

API keys are stored using Android Keystore backed encryption via `EncryptedSharedPreferences`.

## Important note

This app connects to a real FreeLLMAPI server. It does not emulate the server or fake LLM responses locally.
