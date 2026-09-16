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

Open `iosApp/iosApp.xcodeproj` in Xcode and run, or build headlessly:

```bash
xcodebuild build -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug \
  -sdk iphonesimulator -destination "generic/platform=iOS Simulator" CODE_SIGNING_ALLOWED=NO
```

The project has no CocoaPods dependency - a Run Script build phase invokes
`./gradlew :composeApp:embedAndSignAppleFrameworkForXcode` to build and embed the shared
`ComposeApp.framework`. There is no Apple Distribution signing configured yet, so this builds
and runs on the simulator only; see [CI_CD.md](./CI_CD.md#ios-status-as-of-this-writing) for
what's needed to sign and ship a real device build.

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
   - Runs **in parallel**: Linting, Unit tests, UI tests, iOS simulator build (not yet required)
   - Automatic cancellation of outdated runs on new commits
   - Lint/Unit/UI must pass before merge

2. **Release** (`release.yml`): Runs on push to `master` (the default branch)
   - Re-runs lint + unit tests, then builds a **signed** APK/AAB and publishes a GitHub Release
   - Auto-publishes to the Play Store's internal track if `ANDROID_PUBLISHER_CREDENTIALS` is set
   - Attaches an **unsigned** iOS build (verification only, not installable) to the same release

3. **Deploy to Stores** (`deploy-stores.yml`): Manual trigger
   - Promotes an already-published Play Store release to another track (e.g. production)
   - Builds a signed IPA and uploads it to App Store Connect (needs Apple signing secrets - not yet configured, see [CI_CD.md](./CI_CD.md))

📖 **For detailed CI/CD documentation, see [CI_CD.md](./CI_CD.md)**

### Required Secrets

**Android release** (required): `KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`

**Android release** (optional): `GOOGLE_SERVICES_JSON_BASE64` (real Firebase config), `ANDROID_PUBLISHER_CREDENTIALS` (Play Console service account JSON, enables auto-publish + promotion)

**iOS App Store deployment** (required, not yet configured): `BUILD_CERTIFICATE_BASE64`, `P12_PASSWORD`, `BUILD_PROVISION_PROFILE_BASE64`, `KEYCHAIN_PASSWORD`, `EXPORT_OPTIONS_PLIST`, `APP_STORE_CONNECT_API_KEY_ID`, `APP_STORE_CONNECT_ISSUER_ID`, `APP_STORE_CONNECT_API_KEY`

Full details, including the one-time manual first Play Console upload and Apple signing setup, are in [CI_CD.md](./CI_CD.md).

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

