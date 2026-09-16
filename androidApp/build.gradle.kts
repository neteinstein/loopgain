plugins {
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.playPublisher)
}

android {
    namespace = "org.neteinstein.loopgain"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.neteinstein.loopgain"
        minSdk = 24
        targetSdk = 36
        // Overridable by .github/workflows/release.yml (APP_VERSION_CODE/APP_VERSION_NAME env
        // vars, derived from the GitHub Actions run number) so a release build gets a unique,
        // monotonically increasing versionCode without editing this file on every release. Local
        // and PR builds fall back to these defaults.
        versionCode = System.getenv("APP_VERSION_CODE")?.toIntOrNull() ?: 1
        versionName = System.getenv("APP_VERSION_NAME") ?: "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // Populated by .github/workflows/release.yml from Action secrets (KEYSTORE_BASE64 decoded to
    // a file + KEYSTORE_PASSWORD/KEY_ALIAS/KEY_PASSWORD) so the release workflow can produce a
    // signed build. Left unset for local/PR builds - see the release buildType below for the
    // debug-signing fallback.
    signingConfigs {
        create("release") {
            val keystoreFile = System.getenv("KEYSTORE_FILE")
            if (keystoreFile != null) {
                storeFile = file(keystoreFile)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            // Sign with the real release key when CI provides one; otherwise fall back to debug
            // signing so `./gradlew assembleRelease` still works on a developer machine without
            // the signing secrets configured.
            signingConfig =
                if (System.getenv("KEYSTORE_FILE") != null) {
                    signingConfigs.getByName("release")
                } else {
                    signingConfigs.getByName("debug")
                }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Uploads the signed release App Bundle to the Play Console via the Google Play Developer API
// (invoked as `publishReleaseBundle` by .github/workflows/release.yml). Authenticates via the
// ANDROID_PUBLISHER_CREDENTIALS env var (Gradle Play Publisher's default lookup - the raw
// contents of a Play Console service account JSON key), left unset for local builds where no
// publish task is ever invoked. Publishes to the "internal" track unless PLAY_TRACK overrides it,
// so a release never reaches production without an explicit promotion in the Play Console. See
// CI_CD.md for the one-time manual first upload the Play Developer API requires before this can
// publish, and the secret's required Play Console permissions.
play {
    enabled.set(System.getenv("ANDROID_PUBLISHER_CREDENTIALS") != null)
    track.set(System.getenv("PLAY_TRACK")?.takeIf { it.isNotBlank() } ?: "internal")
    defaultToAppBundles.set(true)
}

dependencies {
    implementation(projects.composeApp)
    implementation(compose.preview)
    implementation(libs.androidx.activity.compose)
    implementation(project.dependencies.platform(libs.firebase.bom.get()))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    debugImplementation(compose.uiTooling)
}
