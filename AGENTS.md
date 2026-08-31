# AGENTS.md

Working notes for AI coding agents (Claude Code, Copilot, and friends) on the LoopGain
repository. Human-facing docs live in `README.md`, `ARCHITECTURE.md`, `CONTRIBUTING.md` and
`CI_CD.md` — this file records the things that are easy to get wrong here, and where the
project is heading.

## What LoopGain is

LoopGain is a Kotlin Multiplatform + Compose Multiplatform app (Android + iOS) that brings the
physical **LoopGain feedback deck** into an app. A team runs a timed feedback session: a
facilitator picks one card from each of four categories, the team writes answers in silence,
then each person receives feedback from the others in turn.

The four categories, and the constraint that trips people up:

| Category | PT | Cards | Levels |
| --- | --- | --- | --- |
| Motto | Mote | 12 | **none — Motto cards have no difficulty level** |
| Positive Reinforcement | Reforço Positivo | 12 | 4 each at 1 / 2 / 3 |
| Improvements | Melhorias | 12 | 4 each at 1 / 2 / 3 |
| Personal Question | Pergunta Pessoal | 12 | 4 each at 1 / 2 / 3 |

All content is **bilingual EN + PT**. Every card carries both languages; the app toggles
between them. Never ship a card, guide section or label in one language only.

### The source documents are the authority

`docs/` holds the two files everything is derived from:

- `docs/LoopGain - Questions.xlsx` — sheet **`Teams Cards v2`** (not v1, not "Cards Questions"):
  the 48 cards plus the deck's Intro / Instructions / Back-of-the-Deck copy.
- `docs/Don’t Wait for the CHAOS to think about Feedback - (Feedback Workshop from LoopGain.pdf`
  — the 128-slide workshop talk: the facilitator role, the run-through, tips, and photographs
  of the printed cards.

Do not invent card text, timings or guide copy, and do not "clean up" what these files say.
The loopgain.org and Google Drive links that originally carried this content are **unreachable
from cloud sessions** (the egress gateway rejects them), which is why the files are committed.
Keep them in the repo.

Two skills wrap these documents so you don't have to re-derive them:
`.claude/skills/deck-content/` (the cards) and `.claude/skills/workshop-source/` (the talk).

## Where the code is

```
composeApp/src/
├── commonMain/kotlin/org/neteinstein/loopgain/   # shared — put code here by default
│   ├── App.kt                 # theme + navigation entry point
│   ├── di/AppModule.kt        # Koin module (currently empty)
│   └── ui/{screens,theme,navigation}/
├── androidMain/               # MainActivity, LoopGainApplication, res/, manifest
├── iosMain/                   # MainViewController.kt
└── commonTest/                # kotlin.test, runs on the JVM via testDebugUnitTest
iosApp/                        # Swift wrapper (iOSApp.swift, ContentView.swift)
```

`commonMain` first, always. Reach for `androidMain` / `iosMain` (or `expect`/`actual`) only for
something a platform genuinely cannot share — Firebase and the activity/view-controller hosts
are the current examples.

`ARCHITECTURE.md` documents `domain/`, `data/` and `ui/components/` packages that **do not exist
yet**. Creating them is expected work, not a mistake in the doc.

### Current state, honestly

The app is a two-screen skeleton: `LoadingScreen` waits 2.5 s and navigates to
`CardDeckScreen`, which renders four hard-coded piled cards. There is no deck, no session, no
timer, no facilitator guide, and `AppModule` binds nothing.

### Work in flight

[PR #16](https://github.com/neteinstein/loopgain/pull/16) adds
`docs/PLAN-main-screen-session-flow.md` — the approved, not-yet-implemented plan for the home
screen, the timed session flow and the facilitator guide. **Read that plan before starting any
feature work on screens, the deck or the theme**, and prefer it over the older prose in
`README.md` and `THEME.md` where they disagree. It settles, among other things: bilingual with
an EN/PT toggle, 5 minutes of note-taking, level dots rather than stars, and a domain model
that keeps Compose `Color` out of `domain/`.

That plan calls for deleting `ui/screens/CardDeckScreen.kt` and rewriting `CardDataTest.kt` and
`NavigationTest.kt`. If you are implementing it, that removal is intended; if you are doing
something else, leave them alone.

## Building and verifying

JDK 17, Gradle via the wrapper. `gradle/libs.versions.toml` is the single place versions live —
add dependencies there, never inline in a build file.

```bash
./gradlew lint                      # what CI's lint job runs
./gradlew testDebugUnitTest         # commonTest, on the JVM
./gradlew connectedDebugAndroidTest # instrumented; needs an emulator
./gradlew :composeApp:assembleDebug
```

**Cloud agent containers cannot build this project.** `dl.google.com` is blocked by the egress
gateway, so AGP and the Android SDK never resolve, and no SDK is installed — `BUILD_ENVIRONMENT.md`
records the same limitation. Do not report a change as "builds cleanly" from such a container.
Push the branch, open the PR, and let `.github/workflows/pr-checks.yml` be the build; then drive
it green. `.claude/skills/verify-build/` covers what you *can* check offline and how to read a
failure.

## Conventions

- **Kotlin official style** (`kotlin.code.style=official`), 4-space indent, no wildcard imports.
- **Composables are stateless by default.** A screen composable pulls its state from a
  ViewModel (`koinViewModel()`); the content composable takes state and callbacks as parameters
  so it can be previewed and tested.
- **MVVM with `StateFlow`.** ViewModels expose an immutable state class; UI events go back as
  method calls. No business logic in composables.
- **Koin for DI**, wired in `di/AppModule.kt`. Register new dependencies in that shared module,
  not per platform.
- **The domain layer holds no Compose types.** Card colours belong in `ui/theme`, not on a
  domain enum — today's `CardType(displayName, color)` is the pattern being replaced.
- **Tests in `commonTest`** with `kotlin.test`. New domain and data code arrives with tests;
  that is what CI can actually verify on every PR.
- **Conventional Commits**: `feat(cards): …`, `fix(navigation): …`, `docs(readme): …`.

## Git and PRs

- The default branch is **`master`**. `CONTRIBUTING.md` says to branch from `main`; that is
  stale — branch from `master` and target it.
- Agent branches follow `claude/<short-topic>-<suffix>`; push with `git push -u origin <branch>`.
- `.github/pull_request_template.md` is the PR body: Summary / Changes / Test plan / Risk. Fill
  in the test plan honestly, including what you could not run and why.
- Never commit `composeApp/google-services.json` — it is gitignored. CI copies
  `.github/ci/google-services.json.ci` into place; that placeholder is for validation only.
- Store credentials stay in GitHub secrets (`README.md` lists them). Nothing secret goes in the
  repo, a PR body or a comment.

## Gotchas

- **Koin is only started on Android.** `LoopGainApplication.onCreate()` calls `initKoin()`;
  `MainViewController` does not, so the first `koinViewModel()` or `get()` on iOS will throw.
  Whoever introduces the first injected dependency needs to call `initKoin()` from the iOS entry
  point too.
- **`release.yml` triggers on `main`, but the default branch is `master`** — so the release
  workflow does not currently fire on merge. Worth fixing deliberately; don't be surprised by it.
- **`THEME.md` is partly wrong.** It prescribes white typography on every card. The printed
  deck uses **navy** text on the three light cards (white only on the navy Motto card), and it
  marks difficulty with **dots**, not stars — the spreadsheet's asterisks are just its notation.
  `.claude/skills/card-face/` has the corrected palette and layout.
- **Motto has no levels.** Anything that filters or labels by level must handle a null level,
  and the Motto carousel gets no level filter at all.
- Card text uses `_` as a placeholder for the teammate being discussed ("3 things `_` is very
  good at…"). Render it as an underscored blank, as the printed card does — don't substitute a
  name or strip it.
- `iosX64` was dropped from the targets (removed upstream in Compose Multiplatform 1.11); the
  targets are `iosArm64` and `iosSimulatorArm64`.
- The Firebase BOM is applied with `project.dependencies.platform(libs.firebase.bom.get())` in
  the KMP source-set block — the plain `platform(...)` accessor does not resolve there.
- `settings.gradle.kts` enables `TYPESAFE_PROJECT_ACCESSORS` and includes `:iosApp`, which has
  no build file (it's the Xcode wrapper).

## Skills in this repo

| Skill | Use it when |
| --- | --- |
| `.claude/skills/deck-content/` | Reading, transcribing or validating the 48 cards from the spreadsheet |
| `.claude/skills/workshop-source/` | Pulling facilitator guide content or card artwork out of the workshop PDF |
| `.claude/skills/card-face/` | Rendering a card that has to match the printed deck |
| `.claude/skills/verify-build/` | Verifying a change from a container that cannot run Gradle |
