# Quick Start Guide

Get LoopGain up and running in 5 minutes!

## Prerequisites Check

Before you start, make sure you have:
- [ ] JDK 17 or later installed
- [ ] Android Studio (latest version)
- [ ] Xcode 15+ (macOS only, for iOS)
- [ ] Internet connection

## 1. Clone the Project

```bash
git clone https://github.com/neteinstein/loopgain.git
cd loopgain
```

## 2. Open in Android Studio

1. Launch Android Studio
2. Click "Open"
3. Select the `loopgain` folder
4. Wait for Gradle sync (5-10 minutes first time)

## 3. Run on Android

### Using Emulator
1. Create an Android Virtual Device (AVD):
   - Tools → Device Manager → Create Device
   - Choose Pixel 5 or similar
   - Select API 36 (Android 16)
   - Finish

2. Select "composeApp" configuration
3. Select your emulator
4. Click Run ▶️

### Using Physical Device
1. Enable Developer Options on your phone
2. Enable USB Debugging
3. Connect via USB
4. Select your device
5. Click Run ▶️

## 4. Run on iOS (macOS only)

1. Open Terminal in the `iosApp` directory:
   ```bash
   cd iosApp
   ```

2. Open Xcode project:
   ```bash
   open iosApp.xcodeproj
   ```

3. Select a simulator (iPhone 15)

4. Click Run ▶️

## 5. What You'll See

### Loading Screen (2.5 seconds)
- Blue gradient background
- "LoopGain" text with fade animation
- "Value Different Things" subtitle

### Card Deck Screen
- Three piled cards with rotation
- Different shades of blue
- Motivational content about valuing differences

## Common Issues & Quick Fixes

### "Gradle sync failed"
```bash
./gradlew clean
./gradlew --refresh-dependencies
```

### "Android SDK not found"
1. File → Settings → Appearance & Behavior → System Settings → Android SDK
2. Click "Edit" next to Android SDK Location
3. Follow wizard to install SDK

### "Cannot resolve plugin"
Check your internet connection and try again. The first build downloads ~500MB of dependencies.

### iOS build fails
```bash
cd iosApp
xcodebuild clean
```

## Next Steps

### Add Firebase (Optional)
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a project named "LoopGain"
3. Add Android app:
   - Package: `org.neteinstein.loopgain`
   - Download `google-services.json`
   - Place in `composeApp/` directory
4. Add iOS app:
   - Bundle ID: `org.neteinstein.loopgain`
   - Download `GoogleService-Info.plist`
   - Add to `iosApp/iosApp/` in Xcode

### Explore the Code
- **Screens**: `composeApp/src/commonMain/kotlin/org/neteinstein/loopgain/ui/screens/`
- **Theme**: `composeApp/src/commonMain/kotlin/org/neteinstein/loopgain/ui/theme/`
- **Navigation**: `composeApp/src/commonMain/kotlin/org/neteinstein/loopgain/ui/navigation/`

### Make Your First Change
1. Open `LoadingScreen.kt`
2. Change the text from "LoopGain" to "Welcome"
3. Hot reload or restart the app
4. See your changes!

### Run Tests
```bash
./gradlew test
```

## Development Tips

### Fast Iteration
- Use hot reload when available
- Keep the app running while editing
- Use previews in Android Studio

### Debugging
- Add breakpoints in Kotlin code
- Use Android Studio debugger
- Check Logcat for Android logs

### Code Style
- Use Ctrl+Alt+L (Win/Linux) or Cmd+Opt+L (Mac) to format
- Follow Kotlin conventions
- The project already has the right style

## Getting Help

1. Check `SETUP.md` for detailed instructions
2. Check `ARCHITECTURE.md` for technical details
3. Open an issue on GitHub
4. Check the logs for error messages

## Useful Commands

```bash
# Build everything
./gradlew build

# Run unit tests
./gradlew test

# Clean build
./gradlew clean

# List all tasks
./gradlew tasks

# Build Android debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

## Success Checklist

- [ ] Project opens in Android Studio without errors
- [ ] Gradle sync completes successfully
- [ ] App runs on Android emulator/device
- [ ] App runs on iOS simulator (if on macOS)
- [ ] Loading screen appears with LoopGain branding
- [ ] Card deck screen shows three piled cards
- [ ] Tests pass with `./gradlew test`

## You're All Set! 🎉

The app is running and you're ready to start development. Check out:
- `README.md` - Project overview
- `SETUP.md` - Detailed setup guide
- `ARCHITECTURE.md` - Technical architecture
- `DELIVERY_SUMMARY.md` - What's been implemented

Happy coding! 🚀
