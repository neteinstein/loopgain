# LoopGain - Setup and Build Guide

## Prerequisites

### Required Software
- **JDK 17 or later** - [Download](https://adoptium.net/)
- **Android Studio Hedgehog (2023.1.1) or newer** - [Download](https://developer.android.com/studio)
- **Xcode 15 or later** (macOS only, for iOS development) - [Download from Mac App Store](https://apps.apple.com/app/xcode/id497799835)

### Recommended IDE Plugins
- Kotlin Multiplatform Mobile (KMM) plugin
- Compose Multiplatform IDE support

## Initial Setup

### 1. Clone the Repository
```bash
git clone https://github.com/neteinstein/loopgain.git
cd loopgain
```

### 2. Open in Android Studio
1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the `loopgain` directory
4. Click "OK"
5. Wait for Gradle sync to complete (first sync may take 5-10 minutes)

## Building the Project

### Android

#### From Android Studio
1. Select "composeApp" from the run configuration dropdown
2. Select an emulator or connected device
3. Click the Run button (green triangle)

#### From Command Line
```bash
# Debug build
./gradlew :composeApp:assembleDebug

# Release build
./gradlew :composeApp:assembleRelease

# Install on connected device
./gradlew :composeApp:installDebug
```

### iOS

#### From Xcode
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a simulator or connected device
3. Click Run (⌘R)

#### From Command Line
```bash
# Build iOS framework
./gradlew linkDebugFrameworkIosArm64

# Run from iosApp directory
cd iosApp
xcodebuild -scheme iosApp -configuration Debug
```

## Running Tests

### Unit Tests
```bash
# All tests
./gradlew test

# Android tests only
./gradlew testDebugUnitTest

# Common (shared) tests
./gradlew :composeApp:commonTest
```

### UI Tests (Android)
```bash
# Start emulator first, then:
./gradlew connectedDebugAndroidTest
```

### iOS Tests
```bash
cd iosApp
xcodebuild test -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 15'
```

## Code Quality

### Linting
```bash
./gradlew lint
```

### Code Formatting
The project follows Kotlin coding conventions. Format code in Android Studio with:
- **macOS**: `⌥⌘L`
- **Windows/Linux**: `Ctrl+Alt+L`

## Firebase Setup

### Android
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing
3. Add an Android app with package name: `org.neteinstein.loopgain`
4. Download `google-services.json`
5. Place it in `composeApp/` directory

### iOS
1. In the same Firebase project, add an iOS app
2. Use bundle identifier: `org.neteinstein.loopgain`
3. Download `GoogleService-Info.plist`
4. Place it in `iosApp/iosApp/` directory
5. Add to Xcode project (right-click iosApp folder → Add Files)

## Troubleshooting

### Gradle Sync Failed
```bash
# Clean build
./gradlew clean

# Refresh dependencies
./gradlew --refresh-dependencies
```

### iOS Build Fails
```bash
# Clean iOS build
cd iosApp
xcodebuild clean

# Reinstall pods (if using CocoaPods)
pod install
```

### "Could not find AGP" Error
Ensure you have internet access and Google Maven repository is accessible.
Check `settings.gradle.kts` includes:
```kotlin
repositories {
    google()
    mavenCentral()
}
```

## Project Structure

```
loopgain/
├── composeApp/                 # Main multiplatform module
│   ├── src/
│   │   ├── commonMain/         # Shared code (Android + iOS)
│   │   │   └── kotlin/org/neteinstein/loopgain/
│   │   │       ├── ui/         # Compose UI screens
│   │   │       ├── data/       # Data layer
│   │   │       ├── domain/     # Business logic
│   │   │       └── di/         # Dependency injection
│   │   ├── androidMain/        # Android-specific code
│   │   ├── iosMain/            # iOS-specific code
│   │   └── commonTest/         # Shared tests
│   └── build.gradle.kts        # Module build configuration
├── iosApp/                     # iOS application wrapper
│   └── iosApp/
│       ├── iOSApp.swift        # iOS app entry point
│       └── ContentView.swift   # iOS content view
├── gradle/                     # Gradle configuration
│   ├── libs.versions.toml      # Dependency versions catalog
│   └── wrapper/                # Gradle wrapper files
├── .github/workflows/          # CI/CD pipelines
├── build.gradle.kts            # Root build configuration
├── settings.gradle.kts         # Project settings
└── README.md                   # Project documentation
```

## Development Workflow

### Adding Dependencies
1. Open `gradle/libs.versions.toml`
2. Add version in `[versions]` section
3. Add library in `[libraries]` section
4. Reference in module's `build.gradle.kts`:
   ```kotlin
   commonMain.dependencies {
       implementation(libs.your.library)
   }
   ```

### Creating New Screens
1. Create composable in `composeApp/src/commonMain/kotlin/org/neteinstein/loopgain/ui/screens/`
2. Add route in `ui/navigation/AppNavigation.kt`
3. Add navigation in NavHost

### Writing Tests
- Unit tests go in `commonTest/` for shared logic
- Android UI tests go in `androidInstrumentedTest/`
- iOS tests go in Xcode test targets

## Continuous Integration

The project includes GitHub Actions workflows:

### PR Checks (`.github/workflows/pr-checks.yml`)
- Runs on every pull request
- Executes lint, unit tests, and UI tests
- Must pass before merging

### Release (`.github/workflows/release.yml`)
- Runs on merge to `main`
- Builds release artifacts
- Creates GitHub release
- Uploads APK

### Store Deployment (`.github/workflows/deploy-stores.yml`)
- Manual trigger
- Deploys to Play Store and/or App Store
- Requires secrets configuration

## Deployment

### Google Play Store

#### First-time Setup
1. Create signing keystore:
   ```bash
   keytool -genkey -v -keystore loopgain.keystore -alias loopgain -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Configure GitHub Secrets:
   - `KEYSTORE_BASE64`: Base64 of keystore file
   - `KEYSTORE_PASSWORD`: Keystore password
   - `KEY_ALIAS`: Key alias
   - `KEY_PASSWORD`: Key password
   - `PLAY_STORE_SERVICE_ACCOUNT_JSON`: Service account JSON

3. Trigger deployment workflow from GitHub Actions

### Apple App Store

#### Requirements
- Apple Developer Account ($99/year)
- App Store Connect app created
- Certificates and provisioning profiles

#### Deployment
Configure GitHub Secrets:
- `EXPORT_OPTIONS_PLIST`: Export options (Base64)
- `APP_STORE_CONNECT_API_KEY_ID`: API Key ID
- `APP_STORE_CONNECT_ISSUER_ID`: Issuer ID

## Additional Resources

- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Android Developer Guides](https://developer.android.com/guide)
- [iOS Developer Documentation](https://developer.apple.com/documentation/)

## Support

- Open an issue on GitHub for bugs or feature requests
- Check existing documentation and issues first
- Provide logs and steps to reproduce for bugs

## License

Copyright © 2024 LoopGain
