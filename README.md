# WebView Browser

A simple Android browser application built with Kotlin and Jetpack components. The app features a WebView for displaying web content, a search field with a button, and supports opening links from external apps via intent.

## Features

- **WebView Integration**: Browse websites within the app
- **Search Field**: Enter URLs or search queries directly
- **Smart URL Handling**: 
  - Automatically adds `https://` to domains
  - Converts search queries to Google search
- **External Link Support**: Open links from other apps via Android intents
- **Navigation Controls**: Back button support to navigate browser history
- **JavaScript Support**: Full JavaScript and DOM storage enabled

## Requirements

- Android SDK 24 (Android 7.0) or higher
- Android Studio Arctic Fox or later
- Gradle 8.2.0

## Building the App

1. Clone the repository:
```bash
git clone https://github.com/BrokenEngineerDev/web-view-test.git
cd web-view-test
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build the APK:
```bash
./gradlew assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

## Installing the App

### Using Android Studio
1. Connect your Android device or start an emulator
2. Click "Run" in Android Studio

### Using ADB
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## ADB Commands to Open Links in the App

### Open a specific URL in the app:
```bash
adb shell am start -a android.intent.action.VIEW -d "https://www.example.com" com.example.webviewbrowser/.MainActivity
```

### Open Google in the app:
```bash
adb shell am start -a android.intent.action.VIEW -d "https://www.google.com" com.example.webviewbrowser/.MainActivity
```

### Open GitHub in the app:
```bash
adb shell am start -a android.intent.action.VIEW -d "https://github.com" com.example.webviewbrowser/.MainActivity
```

### Launch the app without a specific URL:
```bash
adb shell am start -n com.example.webviewbrowser/.MainActivity
```

### Launch the app and open a URL with http:
```bash
adb shell am start -a android.intent.action.VIEW -d "http://example.com" com.example.webviewbrowser/.MainActivity
```

## Usage

1. **Enter a URL**: Type a website address (e.g., `google.com`, `https://github.com`) in the search field
2. **Search**: Type any search query, and it will automatically search on Google
3. **Press GO**: Click the GO button or press Enter on the keyboard to navigate
4. **Back Navigation**: Use the device back button to navigate through browser history

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/webviewbrowser/
│   │   └── MainActivity.kt          # Main activity with WebView logic
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml    # UI layout
│   │   └── values/
│   │       ├── strings.xml          # String resources
│   │       ├── colors.xml           # Color definitions
│   │       └── themes.xml           # App theme
│   └── AndroidManifest.xml          # App manifest with permissions and intent filters
└── build.gradle.kts                 # App-level build configuration
```

## Permissions

The app requires the following permissions:
- `INTERNET`: To access web content
- `ACCESS_NETWORK_STATE`: To check network connectivity

## Technical Details

- **Language**: Kotlin
- **UI Framework**: Android Views with ViewBinding
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: Single Activity with WebView

## Intent Filters

The app is registered to handle:
- `http://` schemes
- `https://` schemes
- `VIEW` actions from other apps

This allows external apps to open links in this browser.

## License

MIT License