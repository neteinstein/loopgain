# LoopGain

A Kotlin Multiplatform (KMP) + Compose Multiplatform (CMP) mobile application for Android and iOS.

## 🎯 Project Overview

LoopGain is built following modern Kotlin Multiplatform architecture, enabling code sharing across Android and iOS platforms while maintaining native performance and user experience.

### Features

- ✅ **Timed Feedback Session**: Setup → Draw → Read → Write → Rounds → Reflect → Done, driven by a
  shared session engine with a live countdown
- ✅ **Adaptive Layout**: a phone "faithful deck" flow (one stage per screen) and a wide tablet
  "facilitator board" (step tabs, reveal overlay, session history panel) sharing one view model
- ✅ **48-Card Bundled Deck**: Motto, Positive Reinforcement, Improvements and Personal Question
  cards, drawn with level filtering and "held back" history so recent cards don't repeat
- ✅ **Bilingual**: English/Portuguese content and UI copy, with Spanish/French selectable in
  Settings (falls back to English until PT-equivalent content exists)
- ✅ **Settings**: theme switching (Light/Dark/System), language picker, and card-history reset
- ✅ **Modern Architecture**: Koin-driven DI, an MVVM session view model over `StateFlow`, and a
  Compose-free domain layer
- ✅ **CI/CD Pipeline**: Automated testing, linting, and deployment workflows

## 🏗️ Project Structure

```
loopgain/
├── composeApp/          # Shared KMP library module
│   ├── commonMain/      # Shared business logic and UI
│   │   ├── domain/      # model/ + session/ — pure Kotlin, no Compose types
│   │   ├── data/        # repositories, local key-value store, bundled deck
│   │   └── ui/          # screens/, components/, navigation/, theme/, viewmodel/
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

The app follows the physical LoopGain feedback deck's printed look: navy text on the three light
category cards, white text on the navy Motto card, and difficulty marked with dots (not stars).
Theme (Light/Dark/System) is switchable from Settings.

### Screens

1. **Loading Screen**: animated splash screen with the LoopGain mark and wordmark
2. **Session**: the timed feedback session — Setup, Draw, Read, Write, Rounds, Reflect, Done —
   rendered as a one-stage-per-screen phone flow or, above ~840dp width, a single-screen tablet
   facilitator board with step tabs and a session history panel
3. **Settings**: theme, language, and card-history management

> **Status**: there is no Home screen yet (the app goes straight from Loading into a session)
> and no in-app Facilitator Guide. See [`docs/PLAN-main-screen-session-flow.md`](docs/PLAN-main-screen-session-flow.md)
> for what's planned next, and [`AGENTS.md`](AGENTS.md) for contributor-facing notes.

## 🔧 Development

### Package Structure

```
org.neteinstein.loopgain/
├── domain/
│   ├── model/       # QuestionCard, CardCategory, CardLevel, SessionConfig, SessionStage, ...
│   └── session/     # SessionEngine (pure state transitions) + SessionState
├── data/
│   ├── repository/  # CardRepository, SettingsRepository, SessionHistoryRepository
│   ├── source/      # BundledDeck — the 48 cards
│   └── local/       # KeyValueStore (expect/actual persistence)
├── di/              # Koin module
└── ui/
    ├── theme/       # App theming and colors
    ├── screens/     # Screen composables (loading, session/, session/tablet/, settings/)
    ├── components/  # Reusable UI components (card face, level dots, pile tile, ...)
    ├── viewmodel/   # SessionViewModel + UI state/copy
    └── navigation/  # Navigation logic
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

Tests live in `commonTest` and run on the JVM via `./gradlew testDebugUnitTest` — this is what CI
checks on every PR. Coverage includes the bundled deck, the session engine and its config math,
session/settings repositories, language fallback, and navigation routes.

## 📄 License

Copyright © 2024 LoopGain

## 🤝 Contributing

Contributions are welcome! Please follow the existing code style and include tests for new features.

## 📞 Support

For issues and questions, please open a GitHub issue.

