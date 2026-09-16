package org.neteinstein.loopgain.domain.model

/**
 * Static app metadata for the About section. `versionName` mirrors `androidApp/build.gradle.kts`
 * — there's no KMP-visible `BuildConfig` to read it from automatically (composeApp is a library
 * module, not an application), so keep the two in sync by hand when the version changes.
 */
object AppInfo {
    const val APP_NAME = "LoopGain: Teams"
    const val VERSION_NAME = "1.0"
}
