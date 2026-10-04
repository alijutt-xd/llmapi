# FreeLLMAPI Android Client

This Android app is a real client for a FreeLLMAPI-compatible backend.

## What it does

- Connects to a FreeLLMAPI server
- Stores secure settings using Android Keystore-backed encryption
- Shows dashboard, chat, models, providers, and settings screens
- Uses Material 3 UI and modern Android Compose navigation
- Fetches model data from `/v1/models`
- Sends chat requests to `/v1/chat/completions`

## Architecture

Android UI
→ ViewModel
→ Repository
→ Retrofit/OkHttp API layer
→ FreeLLMAPI server

## Build

1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the app on emulator or device.

## Configuration

Set the backend URL and API key in Settings. Example:
- `http://localhost:3001`
- `http://192.168.1.10:3001`
- `https://your-server.example.com`

## Important limitation

This app is a client for a real FreeLLMAPI backend. It does not emulate provider execution or fake LLM responses locally.
