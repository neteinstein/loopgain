# CI/CD Pipeline Documentation

This document describes the Continuous Integration and Continuous Deployment (CI/CD) pipelines configured for the LoopGain project.

## Overview

The LoopGain project uses GitHub Actions for automated testing, validation, and deployment. The CI/CD pipeline ensures code quality through automated checks and streamlines the release process.

## Workflows

### 1. PR Checks (`pr-checks.yml`)

**Trigger:** 
- `pull_request` - Runs when pull requests are opened, synchronized (new commits pushed), or reopened

**Purpose:** Validates code changes through automated testing and linting.

**How it works:** The workflow automatically runs when a PR is created or updated, providing immediate feedback on code quality without duplicate runs.

#### Jobs

The workflow runs three validation jobs **in parallel** for faster feedback:

1. **Lint Job**
   - Runs Android/Kotlin linting
   - Command: `./gradlew lint --no-daemon`
   - Uploads lint reports as artifacts

2. **Unit Tests Job**
   - Runs unit tests for the debug build
   - Command: `./gradlew testDebugUnitTest --no-daemon`
   - Uploads test reports and results as artifacts

3. **UI Tests Job**
   - Runs instrumented Android tests on an emulator
   - Uses Android Emulator Runner (API level 29)
   - Command: `./gradlew connectedDebugAndroidTest --no-daemon`
   - Uploads test reports as artifacts

4. **PR Validation Summary Job**
   - Runs after all other jobs complete
   - Checks results of all validation jobs
   - **Fails the PR check if any job fails**
   - Provides a single status check for the PR

#### Failure Handling

- **Concurrency Control:** If new commits are pushed to the PR, in-progress workflow runs are automatically cancelled to save resources
- **Job Dependencies:** The summary job depends on all validation jobs via `needs: [lint, unit-tests, ui-tests]`
- **Fail Fast:** If any validation job fails, the summary job will fail, blocking the PR from being merged
- **No `continue-on-error`:** All jobs must pass for the workflow to succeed (unlike the previous implementation)

### 2. Release Workflow (`release.yml`)

**Trigger:** 
- Automatically when code is merged/pushed to `main` branch
- Manually via `workflow_dispatch`

**Purpose:** Runs comprehensive validations including snapshot tests, then builds and releases the application.

#### Jobs

The workflow runs four validation jobs **in parallel**:

1. **Lint Job**
   - Same as PR checks
   - Must pass before release

2. **Unit Tests Job**
   - Same as PR checks
   - Must pass before release

3. **UI Tests Job**
   - Same as PR checks
   - Must pass before release

4. **Snapshot Tests Job** ⭐ *New*
   - Runs visual regression/snapshot tests
   - Command: `./gradlew verifyPaparazziDebug --no-daemon`
   - Currently configured with `continue-on-error: true` as snapshot tests may not be fully configured yet
   - Uploads snapshot test reports and failure images

5. **Build and Release Job**
   - **Only runs after all validation jobs pass** (`needs: [lint, unit-tests, ui-tests, snapshot-tests]`)
   - Builds the release APK
   - Extracts version information from `build.gradle.kts`
   - Creates a GitHub release with version tag
   - Uploads the APK as a release asset

#### Workflow Flow

```
┌─────────┐  ┌──────────────┐  ┌──────────┐  ┌────────────────┐
│  Lint   │  │  Unit Tests  │  │ UI Tests │  │ Snapshot Tests │
└────┬────┘  └──────┬───────┘  └────┬─────┘  └───────┬────────┘
     │              │               │                 │
     └──────────────┴───────────────┴─────────────────┘
                           │
                    ┌──────▼──────┐
                    │   Build &   │
                    │   Release   │
                    └─────────────┘
```

### 3. Deploy to Stores (`deploy-stores.yml`)

**Trigger:** Manual trigger only (`workflow_dispatch`)

**Purpose:** Deploys the application to Google Play Store and/or Apple App Store.

This workflow is unchanged and remains available for production deployments.

## Key Features

### ✅ Parallel Execution
- All validation jobs run simultaneously, reducing total CI time
- Jobs are independent and don't wait for each other

### ✅ Automatic Cancellation
- PR workflow uses concurrency groups to cancel outdated runs when new commits are pushed
- Saves CI minutes and provides faster feedback

### ✅ Comprehensive Testing
- **PR Stage:** Linting, unit tests, and UI tests
- **Main Branch:** All PR checks + snapshot tests
- Each test type uploads detailed reports as artifacts

### ✅ Fail-Safe Mechanisms
- Summary job ensures all checks must pass
- Build and release only happen after successful validations
- Clear visibility into which jobs failed

### ✅ Artifact Uploads
All test results and reports are uploaded as artifacts for debugging:
- Lint reports (HTML and XML)
- Unit test reports and results
- UI test reports
- Snapshot test reports and failure images

## Requirements

### Secrets (for Deploy to Stores)
The following GitHub secrets must be configured for store deployments:

- `KEYSTORE_BASE64` - Base64-encoded Android keystore
- `KEYSTORE_PASSWORD` - Keystore password
- `KEY_ALIAS` - Key alias
- `KEY_PASSWORD` - Key password
- `PLAY_STORE_SERVICE_ACCOUNT_JSON` - Google Play service account JSON
- `EXPORT_OPTIONS_PLIST` - iOS export options plist
- `APP_STORE_CONNECT_API_KEY_ID` - App Store Connect API key ID
- `APP_STORE_CONNECT_ISSUER_ID` - App Store Connect issuer ID

### Software Requirements
- JDK 17
- Gradle (via wrapper)
- Android SDK (automatically installed by GitHub Actions)

## Running Locally

To run the same checks locally before pushing:

```bash
# Linting
./gradlew lint --no-daemon

# Unit tests
./gradlew testDebugUnitTest --no-daemon

# Instrumented tests (requires Android emulator or device)
./gradlew connectedDebugAndroidTest --no-daemon

# Snapshot tests (if configured)
./gradlew verifyPaparazziDebug --no-daemon
```

## Viewing Results

### In GitHub
1. Go to the **Actions** tab in the repository
2. Select the workflow run
3. Click on individual jobs to view logs
4. Download artifacts to view detailed reports

### Locally
After running tests locally, reports are available in:
- `composeApp/build/reports/lint-results*.html` - Lint reports
- `composeApp/build/reports/tests/testDebugUnitTest/` - Unit test reports
- `composeApp/build/reports/androidTests/connected/` - UI test reports
- `composeApp/build/reports/paparazzi/` - Snapshot test reports

## Troubleshooting

### A job failed, how do I debug?
1. Check the job logs in the GitHub Actions UI
2. Download the uploaded artifacts for detailed reports
3. Run the same Gradle command locally to reproduce

### PR is blocked, but I want to merge anyway?
- This is intentional! All checks must pass to maintain code quality
- Fix the failing tests/linting issues
- If a check is incorrectly failing, investigate and fix the test

### Snapshot tests are failing on main branch
- The snapshot test job uses `continue-on-error: true` currently
- This allows releases to proceed even if snapshot tests fail
- Review the snapshot test reports to see what changed
- Update snapshots if the changes are intentional

## Best Practices

1. **Before creating a PR:** Run linting and tests locally
2. **During PR review:** Check the uploaded test reports for detailed results
3. **After merge:** Monitor the release workflow to ensure deployment succeeds
4. **For releases:** Use the manual deploy-stores workflow only for production-ready versions

## Future Improvements

Potential enhancements to consider:

- Add code coverage reporting and enforcement
- Implement automatic snapshot baseline updates
- Add performance benchmarking tests
- Configure branch protection rules requiring status checks
- Add automated dependency updates (Dependabot)
- Implement automatic changelog generation
- Add iOS-specific testing workflows

## Support

For questions or issues with the CI/CD pipeline:
1. Check the workflow logs in GitHub Actions
2. Review this documentation
3. Create an issue in the repository

---

Last Updated: 2026-01-15
