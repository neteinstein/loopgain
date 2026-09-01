# LoopGain

A Kotlin Multiplatform (KMP) + Compose Multiplatform (CMP) mobile application for Android and iOS.

## 🎯 Project Overview

LoopGain is built following modern Kotlin Multiplatform architecture, enabling code sharing across Android and iOS platforms while maintaining native performance and user experience.

### Features

- ✅ **Shared UI**: Built with Compose Multiplatform for consistent UI across platforms
- ✅ **Modern Architecture**: Clean architecture with dependency injection via Koin
- ✅ **Firebase Integration**: Ready for Firebase Firestore database integration
- ✅ **Brand Identity**: Custom LoopGain branding with themed loading screen
- ✅ **Card Deck UI**: Interactive card deck interface with piled card design
- ✅ **CI/CD Pipeline**: Automated testing, linting, and deployment workflows

## 🏗️ Project Structure

```
loopgain/
├── composeApp/          # Shared KMP library module
│   ├── commonMain/      # Shared business logic and UI
│   ├── androidMain/     # Android-specific shared code
│   └── iosMain/         # iOS-specific code
├── androidApp/          # Android app entry point (manifest, MainActivity, Firebase)
├── iosApp/              # iOS app wrapper
├── .github/workflows/   # CI/CD pipelines
└── gradle/              # Gradle configuration
```

## 🚀 Getting Started

### Prerequisites

- **JDK 17** or higher
- **Android Studio** (latest stable, for Android development)
- **Xcode 15** or newer (for iOS development, macOS only)
- **Gradle 9.7.1** (included via wrapper)

### Building the Project

#### Android

```bash
./gradlew :androidApp:assembleDebug
```

#### iOS

```bash
cd iosApp
xcodebuild -workspace iosApp.xcworkspace -scheme iosApp -configuration Debug
```

### Running Tests

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Run Android instrumented tests
./gradlew connectedDebugAndroidTest
```

## 📦 Tech Stack

- **Kotlin Multiplatform**: 2.4.10
- **Compose Multiplatform**: 1.11.1
- **Android Gradle Plugin**: 9.3.0
- **Ktor**: 3.5.2 (Networking)
- **Koin**: 4.2.2 (Dependency Injection)
- **Coil**: 3.5.0 (Image Loading)
- **Firebase BOM**: 34.16.0 (Backend services)
- **kotlinx.serialization**: 1.11.0 (JSON handling)
- **kotlinx.coroutines**: 1.11.0
- **Compose Navigation**: 2.9.2

## 🎨 Design

The app follows the LoopGain brand identity from [LoopGain.org](https://loopgain.org):

- **Primary Color**: Dark Blue (#1E3A5F)
- **Secondary Color**: Medium Blue (#4A90E2)
- **Accent Color**: Light Blue (#7FB3D5)
- **Typography**: Bold headers with generous letter spacing

### Screens

1. **Loading Screen**: Animated splash screen with LoopGain branding
2. **Card Deck**: Main screen featuring piled cards with motivational content

## 🔧 Development

### Package Structure

```
org.neteinstein.loopgain/
├── ui/
│   ├── theme/       # App theming and colors
│   ├── screens/     # Screen composables
│   ├── components/  # Reusable UI components
│   └── navigation/  # Navigation logic
├── data/            # Data layer (repositories, data sources)
├── domain/          # Business logic (use cases, models)
└── di/              # Dependency injection modules
```

### Adding Dependencies

Edit `gradle/libs.versions.toml` to add new dependencies, then sync the project.

## 🚀 CI/CD

The project uses GitHub Actions for automated testing, validation, and deployment with parallel job execution and comprehensive test coverage.

### Workflows

1. **PR Checks** (`pr-checks.yml`): Runs on pull requests
   - Runs **in parallel**: Linting, Unit tests, UI tests
   - Automatic cancellation of outdated runs on new commits
   - All checks must pass before merge

2. **Release** (`release.yml`): Runs on merge to main
   - Runs **in parallel**: Linting, Unit tests, UI tests, Snapshot tests
   - Builds release APK/AAB only after all validations pass
   - Creates GitHub release with artifacts

3. **Deploy to Stores** (`deploy-stores.yml`): Manual trigger
   - Deploys to Google Play Store
   - Deploys to Apple App Store

📖 **For detailed CI/CD documentation, see [CI_CD.md](./CI_CD.md)**

### Required Secrets

For store deployment, configure these GitHub secrets:

- `KEYSTORE_BASE64`: Base64-encoded Android keystore
- `KEYSTORE_PASSWORD`: Keystore password
- `KEY_ALIAS`: Key alias
- `KEY_PASSWORD`: Key password
- `PLAY_STORE_SERVICE_ACCOUNT_JSON`: Google Play service account
- `EXPORT_OPTIONS_PLIST`: iOS export options
- `APP_STORE_CONNECT_API_KEY_ID`: App Store Connect API key ID
- `APP_STORE_CONNECT_ISSUER_ID`: App Store Connect issuer ID

## 📝 Firebase Setup

To enable Firebase features:

1. Add `google-services.json` to `androidApp/` (Android)
2. Add `GoogleService-Info.plist` to `iosApp/` (iOS)
3. Initialize Firebase in the Application class (already configured)

## 🧪 Testing

The project includes:

- **Unit Tests**: Common business logic tests
- **UI Tests**: Compose UI testing for screens
- **Integration Tests**: End-to-end feature testing

## 📄 License

Copyright © 2024 LoopGain

## 🤝 Contributing

Contributions are welcome! Please follow the existing code style and include tests for new features.

## 📞 Support

For issues and questions, please open a GitHub issue.

