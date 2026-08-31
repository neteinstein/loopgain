---
name: verify-build
description: Verify a LoopGain change when Gradle cannot run — the cloud container has no Android SDK and dl.google.com is blocked. Use before claiming a change works, and when a PR's lint, unit-test or UI-test job goes red.
---

# Verifying without a build

**Cloud agent containers cannot build this project.** `dl.google.com` is blocked by the egress
gateway, so AGP, the Compose plugin and the Android SDK never resolve, and no SDK is installed.
`./gradlew` will fail at configuration time; that failure says nothing about your change.
`BUILD_ENVIRONMENT.md` records the same limitation.

So: **GitHub Actions is the build.** Push the branch, open the PR, and drive
`.github/workflows/pr-checks.yml` green. Never describe a change as compiling, passing tests or
"verified" on the strength of a local run that did not happen.

## What you can check offline

Cheap and worth doing before every push:

- **Read the diff adversarially.** Unresolved imports, a `when` that no longer covers every enum
  case, a composable called outside a `@Composable` scope, a nullable `CardLevel` dereferenced.
- **Every reference to a symbol you moved or deleted**: `grep -rn "CardDeckScreen\|CardType" composeApp/src`.
  The tests in `commonTest` reference UI types and are the usual casualty.
- **Version catalog**: new dependencies belong in `gradle/libs.versions.toml`, referenced as
  `libs.*`. A literal coordinate in a build file is a review comment waiting to happen.
- **Source-set placement**: anything in `androidMain` or `iosMain` that could have lived in
  `commonMain` should move; anything in `commonMain` using a platform API will fail on the other
  target.
- **Deck data**: `python3 .claude/skills/deck-content/extract_deck.py` — pure stdlib, runs fine
  offline, and checks the 48-card invariants.
- **Workflow YAML** parses:
  `python3 -c "import yaml;yaml.safe_load(open('.github/workflows/pr-checks.yml'))"`.

## What CI runs

Three parallel jobs on JDK 17, gated by a summary job that fails if any of them does:

| Job | Command |
| --- | --- |
| lint | `./gradlew lint --no-daemon` |
| unit-tests | `./gradlew testDebugUnitTest --no-daemon` (this is what runs `commonTest`) |
| ui-tests | `./gradlew connectedDebugAndroidTest --no-daemon` on an API 29 emulator |

Each job first copies `.github/ci/google-services.json.ci` to `composeApp/google-services.json`.
A failure mentioning a missing `google-services.json` means that step, not your code.

Reports are uploaded as artifacts (`lint-reports`, `unit-test-reports`, `ui-test-reports`) —
fetch them instead of guessing at a failure.

## Reading a red job

- **Configuration or dependency-resolution errors** — usually a `libs.versions.toml` edit, or a
  KMP source-set block where a Gradle accessor doesn't apply (the Firebase BOM needs
  `project.dependencies.platform(libs.firebase.bom.get())`).
- **Unresolved reference in `commonTest`** — a test still points at a type the change removed.
  Rewrite the test; do not delete it to get green, and never skip or quarantine a test.
- **Emulator or runner death before any test body ran** — the one case worth a single re-run.
  Two failures in a row is a real failure, not a flake.
- **Runtime crash on iOS around dependency injection** — Koin is started from
  `LoopGainApplication` on Android only; the iOS entry point never calls `initKoin()`.
- **Lint only, everything else green** — read the uploaded HTML report; lint failures here are
  usually real Android API-level or resource issues.

Fix, push, and let the next run answer. One validated push beats three speculative ones.
