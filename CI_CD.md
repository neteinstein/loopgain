# CI/CD Pipeline Documentation

This document describes the Continuous Integration and Continuous Deployment (CI/CD) pipelines configured for the LoopGain project.

## Overview

The LoopGain project uses GitHub Actions for automated testing, validation, and deployment. The CI/CD pipeline ensures code quality through automated checks and streamlines the release process.

## Workflows

### 1. PR Checks (`pr-checks.yml`)

**Trigger:**
- `pull_request` - Runs when pull requests are opened, synchronized (new commits pushed), or reopened

**Purpose:** Validates code changes through automated testing and linting.

#### Jobs

Runs in **parallel** for faster feedback:

1. **Lint** - `./gradlew lint --no-daemon`, uploads lint reports as artifacts
2. **Unit Tests** - `./gradlew testDebugUnitTest --no-daemon` (commonTest, on the JVM), uploads test reports
3. **UI Tests** - instrumented Android tests via `reactivecircus/android-emulator-runner` (API 29), `./gradlew connectedDebugAndroidTest --no-daemon`
4. **iOS Build** *(not yet a required check - see below)* - builds `iosApp/iosApp.xcodeproj` for the simulator SDK (`-sdk iphonesimulator`, no signing needed). Runs with `continue-on-error: true` because the Xcode project is a new, hand-authored scaffold with no track record in CI. Once it has run clean for a while, add `ios-build` to `pr-validation-summary`'s `needs` to make it required.
5. **PR Validation Summary** - depends on `[lint, unit-tests, ui-tests]` and fails the check if any of those failed. New commits cancel in-progress runs for the same PR (`concurrency`).

### 2. Release (`release.yml`)

**Trigger:**
- Push to `master` (the default branch)
- Manually via `workflow_dispatch`

**Purpose:** Builds a signed Android release, publishes a GitHub Release, optionally publishes to the Play Store, and builds an unsigned iOS artifact for verification.

#### `release-android` job

1. **Validate required secrets** - fails fast if `KEYSTORE_BASE64` / `KEYSTORE_PASSWORD` / `KEY_ALIAS` / `KEY_PASSWORD` are missing, before spending CI time on anything else.
2. **Set up `google-services.json`** - decodes the `GOOGLE_SERVICES_JSON_BASE64` secret if set; otherwise falls back to the CI placeholder (`.github/ci/google-services.json.ci`) with a warning. A release built from the placeholder ships with non-functional Firebase (Crashlytics/Analytics/Firestore) - add the secret before relying on those features in a real release.
3. **Lint + unit tests** - re-run here (not just relying on `pr-checks.yml`) because a squash- or rebase-merge can land a commit on `master` that was never itself built or tested. UI tests are *not* re-run - they're slow, and `pr-checks.yml` already covers them.
4. **Build signed APK + AAB** - `./gradlew assembleRelease bundleRelease`, signed with the decoded keystore. `versionCode`/`versionName` come from `APP_VERSION_CODE` (the GitHub Actions run number) / `APP_VERSION_NAME` (`1.0.<run number>`) env vars, read in `androidApp/build.gradle.kts`; local/PR builds fall back to static defaults and debug signing.
5. **Create GitHub Release** - tagged `v1.0.<run number>`, with the APK and AAB attached plus their SHA-1 hashes in the release notes.
6. **Publish to Play Store** *(optional)* - only runs if `ANDROID_PUBLISHER_CREDENTIALS` is set, via the Gradle Play Publisher plugin's `publishReleaseBundle` task (`androidApp/build.gradle.kts`'s `play { }` block). Publishes to the `internal` track by default, overridable with the `PLAY_TRACK` repo variable. If unset, the step is skipped with a notice rather than failing - the workflow still produces a GitHub Release without Play Console access configured.

   **Note:** the Play Developer API can only publish *updates* to an app that already has at least one release uploaded manually through the Play Console. Download the `.aab` asset from the GitHub Release and upload it by hand under Play Console → your app → Production/Testing → Create release for that one-time first upload; every release after that is handled automatically.

   **Troubleshooting a `403 PERMISSION_DENIED`:** the failing call is always the first one Gradle Play Publisher makes (`POST .../applications/<applicationId>/edits`), so the cause is the service account's standing in the Play Console, not this repo's Gradle config. Check, in order: (1) the service account (the `client_email` inside the `ANDROID_PUBLISHER_CREDENTIALS` JSON) is invited as a user under Play Console → Users and permissions, with access to *this specific app* and release permission for at least the target track; (2) a first release has been uploaded manually (see above); (3) the Google Play Android Developer API is enabled on the service account's Google Cloud project; (4) a newly-granted permission can take a few hours to propagate - a re-run after a short wait can resolve it with no config change.

#### `release-ios` job

Runs after `release-android` (`needs: release-android`), on `macos-latest`, with `continue-on-error: true` so it never blocks the Android release while the Xcode project is unproven.

Builds `iosApp/iosApp.xcodeproj` **unsigned** for a real device architecture (`-sdk iphoneos`, `CODE_SIGNING_ALLOWED=NO`), packages the resulting `.app` into a zip named like an `.ipa` (`Payload/iosApp.app` inside a `.ipa`-suffixed zip), and attaches it to the same GitHub Release. This is **not installable as-is** - there is no provisioning profile or code signature - it exists purely to catch iOS build breakage (the Compose Multiplatform framework failing to embed, an iOS-only compile error) on every release and to give someone a starting point to re-sign locally. It is not uploaded to App Store Connect; that requires real Apple signing secrets (see `deploy-stores.yml` below), which this repo does not have configured yet.

### 3. Deploy to Stores (`deploy-stores.yml`)

**Trigger:** Manual only (`workflow_dispatch`), with a `target` input: `playstore-promote`, `appstore`, or `both`.

#### `promote-playstore` job

`release.yml` already auto-publishes every release to the Play Console's `internal` track. This job does not rebuild or re-upload anything - it promotes the artifact already sitting on `play_from_track` (input, default `internal`) to `play_to_track` (input, default `production`) via Gradle Play Publisher's `promoteReleaseArtifact` task. This is how a release actually reaches production. Requires `ANDROID_PUBLISHER_CREDENTIALS`.

#### `deploy-ios` job

Builds a **signed** archive, exports a real `.ipa`, and uploads it to App Store Connect via `altool`. This requires Apple Distribution signing, which is not set up in this repo yet - the job validates all required secrets up front and fails with a clear message (pointing back here) if any are missing, rather than failing deep in an `xcodebuild` invocation.

**Apple signing setup (not done yet):** to make this job work you need, as repo secrets:

- `BUILD_CERTIFICATE_BASE64` - a base64-encoded Apple Distribution `.p12` certificate (export from Keychain Access, or generate via a Certificate Signing Request in your Apple Developer account)
- `P12_PASSWORD` - the password the `.p12` was exported with
- `BUILD_PROVISION_PROFILE_BASE64` - a base64-encoded App Store distribution provisioning profile for `org.neteinstein.loopgain`, downloaded from the Apple Developer portal
- `KEYCHAIN_PASSWORD` - any password; the workflow creates a throwaway keychain for the run and uses this to unlock it
- `EXPORT_OPTIONS_PLIST` - base64-encoded `ExportOptions.plist` (method `app-store`, matching team ID)
- `APP_STORE_CONNECT_API_KEY_ID`, `APP_STORE_CONNECT_ISSUER_ID`, `APP_STORE_CONNECT_API_KEY` (base64-encoded `.p8` key) - from App Store Connect → Users and Access → Keys

Until these exist, use `release.yml`'s unsigned iOS artifact (see above) for build verification only.

## Requirements

### Secrets

**Android release (`release.yml`, `release-android`) - required:**
- `KEYSTORE_BASE64` - base64-encoded Android keystore (`.jks`/`.keystore`)
- `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD` - signing config credentials

**Android release - optional:**
- `GOOGLE_SERVICES_JSON_BASE64` - base64-encoded real `androidApp/google-services.json`. Without it, release builds use the CI placeholder and ship with non-functional Firebase.
- `ANDROID_PUBLISHER_CREDENTIALS` - raw contents of a Play Console service account JSON key. Enables automatic publish to the `internal` track from `release.yml`, and is required by `deploy-stores.yml`'s `promote-playstore` job.
- `PLAY_TRACK` (repo **variable**, not secret) - overrides the track `release.yml` auto-publishes to (default `internal`)

**iOS App Store deployment (`deploy-stores.yml`, `deploy-ios`) - required, not yet configured:**
- `BUILD_CERTIFICATE_BASE64`, `P12_PASSWORD`, `BUILD_PROVISION_PROFILE_BASE64`, `KEYCHAIN_PASSWORD`
- `EXPORT_OPTIONS_PLIST`
- `APP_STORE_CONNECT_API_KEY_ID`, `APP_STORE_CONNECT_ISSUER_ID`, `APP_STORE_CONNECT_API_KEY`

### Software Requirements
- JDK 17
- Gradle (via wrapper)
- Android SDK (automatically installed by GitHub Actions' Android runners)
- Xcode (automatically available on GitHub Actions' `macos-latest` runners; pinned to `latest-stable` via `maxim-lobanov/setup-xcode`)

## Running Locally

```bash
# Linting
./gradlew lint --no-daemon

# Unit tests
./gradlew testDebugUnitTest --no-daemon

# Instrumented tests (requires Android emulator or device)
./gradlew connectedDebugAndroidTest --no-daemon

# Signed release build (requires KEYSTORE_FILE/KEYSTORE_PASSWORD/KEY_ALIAS/KEY_PASSWORD env vars;
# falls back to debug signing without them)
./gradlew assembleRelease bundleRelease --no-daemon

# iOS build (macOS only, requires full Xcode - not just Command Line Tools)
xcodebuild build -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug \
  -sdk iphonesimulator -destination "generic/platform=iOS Simulator" CODE_SIGNING_ALLOWED=NO
```

See `.claude/skills/verify-build/` for what can be checked without a full Gradle build (e.g. from a cloud agent container, where `dl.google.com` is blocked and no Android SDK is installed).

## Viewing Results

### In GitHub
1. Go to the **Actions** tab in the repository
2. Select the workflow run
3. Click on individual jobs to view logs
4. Download artifacts to view detailed reports

### Locally
- `androidApp/build/reports/lint-results*.html` - Lint reports
- `composeApp/build/reports/tests/testDebugUnitTest/` - Unit test reports (commonTest runs on the JVM)
- `androidApp/build/reports/androidTests/connected/` - UI test reports

## Troubleshooting

### A job failed, how do I debug?
1. Check the job logs in the GitHub Actions UI
2. Download the uploaded artifacts for detailed reports
3. Run the same Gradle/xcodebuild command locally to reproduce

### PR is blocked, but I want to merge anyway?
This is intentional - `lint`, `unit-tests` and `ui-tests` must all pass. `ios-build` does not currently block merges (see above).

### `release-ios` failed
It's `continue-on-error: true` and does not block the Android release or the GitHub Release creation. Check the job logs - most failures will be either the Kotlin/Native framework failing to embed (`Compile Kotlin Framework` build phase) or an iOS-only Swift compile error.

### Play Store publish / promote failed with a 403
See the troubleshooting note under `release-android`'s "Publish to Play Store" step above.

## iOS status (as of this writing)

`iosApp/iosApp.xcodeproj` was hand-authored (no CocoaPods, no `xcodegen` - just a single app target linking the `ComposeApp.framework` produced by `:composeApp:embedAndSignAppleFrameworkForXcode`) because this repo previously committed only the Swift source files and gitignored the actual Xcode project. It has not yet been built by a real Xcode install as part of authoring it - `pr-checks.yml`'s `ios-build` job and `release.yml`'s `release-ios` job are its first real verification, on GitHub's `macos-latest` runners. If either fails, that is expected until proven otherwise; fix forward rather than assuming the failure means something else is broken.

There is no Apple Distribution signing configured, so nothing here can install on a real device or reach App Store Connect yet - see "Apple signing setup" above for what's needed.

## Future Improvements

- Configure branch protection rules requiring status checks
- Promote `ios-build` to a required PR check once proven stable
- Add automated dependency updates (Dependabot)
- Implement automatic changelog generation
- Set up Apple Distribution signing (see "Apple signing setup" above) to enable real IPA builds and App Store Connect / TestFlight uploads

---

Last Updated: 2026-09-16
