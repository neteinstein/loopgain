# LoopGain — Main screen, session flow, facilitator guide

> **Status: approved, not yet implemented.** This is an implementation plan intended to be
> picked up by a separate session. All content decisions below are settled — the source
> documents in `docs/` are the authority, and this file records what was extracted from them.

## Context

The app is currently a two-screen skeleton: `LoadingScreen` (2.5s splash) navigates to `CardDeckScreen`, which renders four hard-coded piled cards and nothing else. There is no session flow, no question deck, no timer, and no facilitator content. `domain/`, `data/` and `ui/components/` are documented in `ARCHITECTURE.md` but don't exist.

The goal is the real product loop of the physical LoopGain deck. From the home screen a team either **starts a session** (set duration → pick one card per category → run a timed 2×2 board) or opens the **facilitator guide**. Card faces must match the printed deck.

Both source documents are now in the repo on `master` and are the authority for all content:

- `docs/LoopGain - Questions.xlsx` — sheet **`Teams Cards v2`** (note: the sheet is named "Teams Cards v2", not "Cards Questions v2")
- `docs/Don't Wait for the CHAOS to think about Feedback - (Feedback Workshop from LoopGain.pdf` — 128-slide workshop talk by Pedro Vicente

**This plan is written to be executed by a session that has not read those files.** Everything extracted from them is reproduced below, but re-extract from the originals when transcribing all 48 cards — do not retype from this document.

Decisions already made with the user: **bilingual EN + PT with a toggle**; **5 minutes** note-taking (the printed deck's number, with the workshop's longer range surfaced as a facilitator tip); facilitator guide is **practical + a short "why"**, not the full talk.

## Flow

```
Loading → Home ──"Start Session"──→ Setup (participants → duration) → Pick Cards (4 carousels) → Session board (2×2 + countdown)
              └─"Become a Facilitator"──→ Facilitator Guide
```

## What the source documents actually contain

### Deck structure (`Teams Cards v2`)

48 cards, **bilingual**. Columns: `A` index, `B` category header, `C` EN text, `D` PT text, `E` PT category name.

| Category | PT name | Cards | Levels |
| --- | --- | --- | --- |
| Motto | Mote | 12 | **none** — mottos have no level |
| Positive Reinforcement | Reforço Positivo | 12 | 4 × L1, 4 × L2, 4 × L3 |
| Improvements | Melhorias | 12 | 4 × L1, 4 × L2, 4 × L3 |
| Personal Question | Pergunta Pessoal | 12 | 4 × L1, 4 × L2, 4 × L3 |

**That Motto has no levels is a real constraint on the UI** — the Motto carousel gets no level filter.

Column `B` only carries a value on the **first row of each block** (`Positive Reinforcement *`, `Positive Reinforcement **`, …); the level applies to every row until the next `B` value. The trailing star count in `B` is the level. Rows 51–53 are not question cards: `Intro`, `Instructions`, and `Back of the Deck` — use them as guide copy, not deck entries.

Question text uses `_` as a placeholder for the teammate being discussed ("3 things `_` is very good at…"). Render it as an underscored blank, as the printed card does.

Extraction recipe (no openpyxl in a clean container; stdlib works):

```python
import zipfile, xml.etree.ElementTree as ET
z = zipfile.ZipFile('docs/LoopGain - Questions.xlsx')
N = '{http://schemas.openxmlformats.org/spreadsheetml/2006/main}'
ss = [''.join(t.text or '' for t in si.iter(N+'t'))
      for si in ET.fromstring(z.read('xl/sharedStrings.xml'))]
root = ET.fromstring(z.read('xl/worksheets/sheet2.xml'))   # sheet2 = Teams Cards v2
```
Cells with `t="s"` index into `ss`.

### Card graphics (photographed on PDF pages 54 and 95)

Landscape, rounded corners, roughly 1.6:1. Layout is identical across categories:

- Category name **top-left**, very small caps, wide letter-spacing — white on the navy Motto card, tone-on-tone (a shade of the card's own colour) on the light cards.
- Level **dots top-right**: `•` / `••` / `•••`. **The printed deck uses dots, not stars** — use dots in the app; the spreadsheet's asterisks are only its notation.
- Question text large, bold, left-aligned, vertically centred.
- Motto cards: navy ground, **white** text. All three light cards: **navy** text. `THEME.md`'s "all cards use white typography" is wrong and is fixed by this change.
- Category/divider cards: navy ground, red `LoopGain` wordmark top-left, category name bottom-left in white with red level dots beneath, and the signature **white "pill-dash waveform" motif** on the right — rows of rounded horizontal dashes of varying length, like a loop signal.

Palette sampled from the photographs (photo white-balance is warm, so these are approximations of the print colours):

| Role | Sampled | Use |
| --- | --- | --- |
| Navy — Motto, chrome, text on light cards | `#0D2254` | matches the existing `CardColors.Motto`, keep it |
| Positive Reinforcement | `≈ #5BB8E8` | brighter/cyan-er than the theme's current `#4A90E2` |
| Improvements | `≈ #A9D0E8` | close to the theme's Steel `#86B3D1`, a touch lighter |
| Personal Question | `≈ #C8DFEF` | close to the theme's Sky `#B9D9EB` |
| Red accent (wordmark, dots on category cards) | `≈ #E63946` | **not in the theme today** — add it |

Sampling was from JPEGs inside the PDF, so treat the three blues and the red as "match the printed deck" targets rather than exact brand values; if a brand asset turns up later, it wins.

### Facilitator content (PDF)

Everything the guide needs, with slide numbers for the implementer:

- **Why (short)** — feedback vs evaluation (4–5); "no feedback" as entropy, teams dissolve into chaos (8–10); social cohesion beats the "SuperChicken model" of star employees, per Margaret Heffernan (27–29); Google re:work on psychological safety (39).
- **Facilitator role** (97) — can be from inside or outside the team; chooses the cards, sets the timings, pushes the session forward; may participate if from the team; **facilitators from within the team get better results**.
- **Prep** (98–100) — pick 4 cards, one per category (or write new ones); paper and pen per person; define duration and bring a **visual timer**; gather the team with no laptops or phones; works best under 10 people; remote or mixed is fine — define a round order (alphabetical if fully remote).
- **Run-through** (101–110, and the deck's own `Instructions` row) — start with the **Motto**, read aloud, then **30+ seconds of silence** to set the tone; reveal the two "about others" questions (Positive Reinforcement, Improvements); reveal the Personal Question; give silent writing time; pick someone to receive feedback first; going clockwise from the person on their right, each member shares their Positive Reinforcement and Improvements answers about that person, **not as a conversation**; the receiver briefly comments, then answers the Personal Question; move to the next person; stop with ~5 min left and ask how the session felt.
- **Facilitator tips** (57, 58, 59, 63, 71, 103, 104, 108) — clockwise, alphabetical if remote; keep a visible timer (`timer.pizza` when remote); it is not a conversation; **10 minutes per person + 10 for start and close**; answers need not be work-related; write the questions up (whiteboard, or chat when remote); silence feels awkward but produces better feedback.
- **Typical misconceptions** (63, 65, 75, 76, 103, 105) — that you should reply to feedback; that being honest means being rude; that the goal is solving issues (**the goal is improving people; solved issues are a byproduct**); that answers must be work-related; that it's a "shit sandwich".
- **Question-building rules** (81–83) — Positive: appraise what we value as good ("name a good thing about John" → "name 3 things"). Improvements: focus on actions, not issues or the person ("what does ___ do wrong?" → "what can I help ___ improve? Why?"). Personal: avoid teamwork, aim at the unknown ("what do you like to do at work?" → "what is your biggest hobby?").
- **Why "3 things"** (113–114) — people can't produce three at first, which makes them think harder and trains their eye between sessions.
- **Resources** (123–125) — Margaret Heffernan, "Forget the pecking order at work"; Freakonomics ep. 451, "Can I Ask You a Ridiculously Personal Question?"; Google re:work, "Trust in teams".

**Timing conflict, already resolved:** the printed Instructions card says 5 minutes to note answers; the workshop says 10 (slide 59) and 15 (slide 107). Use **5 minutes** as the app's default and mention "some facilitators give 10–15" as a tip. The 10-min-per-person + 10 duration rule is consistent across both sources — use it.

## Domain and data layer (new)

`domain/model/`
- `CardCategory.kt` — enum `MOTTO`, `POSITIVE_REINFORCEMENT`, `IMPROVEMENTS`, `PERSONAL_QUESTION`, each with EN and PT display names. **Colour-free** — colours live in the UI layer (today's `CardType` embeds a Compose `Color`; that coupling goes away).
- `CardLevel.kt` — enum `ONE`, `TWO`, `THREE` with a `dots` count. `QuestionCard.level` is **nullable**, because Motto cards have none.
- `QuestionCard.kt` — `@Serializable data class QuestionCard(id, category, level: CardLevel?, en: String, pt: String)` with `fun text(lang: Language)`.
- `Language.kt` — `EN`, `PT`.
- `SessionConfig.kt` — `participants`, `durationMinutes`, `language`; `fun suggestedMinutes(participants: Int) = participants * 10 + 10`; clamps participants to 2–15 (the deck's stated range) and duration to 5–180.
- `SessionSelection.kt` — `Map<CardCategory, QuestionCard>`, `isComplete` when all four are filled.

`data/source/BundledDeck.kt` — all 48 cards as a `val bundledDeck: List<QuestionCard>`, transcribed from `Teams Cards v2` with both languages. Plain Kotlin rather than a JSON asset: compile-checked, no Compose-resources loading risk on iOS. Generate it with a script rather than by hand, and keep the PT text's accents intact.

`data/repository/CardRepository.kt` — `byCategory(c)`, `byCategoryAndLevel(c, level?)`, `random(c, level?)`, `levelsFor(c)` (empty for Motto). Registered in the currently-empty `di/AppModule.kt` alongside the ViewModel.

`data/content/FacilitatorGuide.kt` — `GuideSection(title, body, bullets)` list built from the PDF content above, in both languages, plus the deck's own `Intro` / `Instructions` / `Back of the Deck` copy from rows 51–53 of the sheet.

## UI

`ui/theme/` — extend `Theme.kt`'s `CardColors` with the sampled palette above, add the red accent, and add `CardStyles.kt` mapping category → `containerColor` / `contentColor` / `labelColor`. Light cards get navy text.

`ui/components/` (new — one card renderer used by the pile, the carousels and the grid)
- `LoopGainCard.kt` — the printed face: rounded rect, category label top-left in small letter-spaced caps, **level dots top-right**, bold question centred, size-adaptive typography. `_` in the text renders as an underscored blank.
- `CardBack.kt` and `WaveMotif.kt` — the navy back and the pill-dash waveform, drawn with `Canvas` (rows of rounded capsules of varying length and alpha). No bitmap assets needed.
- `PiledDeck.kt` — the stacked-deck visual, generalised from today's `PiledCard`.

`ui/screens/`
- `HomeScreen.kt` — piled deck centred with a gentle idle animation, wordmark and tagline ("Taking your team from the comfort zone to the trust zone", from the PDF's closing slide), then **Start Session** (filled navy) and **Become a Facilitator** (outlined) beneath the pile.
- `SessionSetupScreen.kt` — participants stepper (2–15) driving the suggested duration, shown as its arithmetic ("10 min × 5 people + 10 = 60 min"); duration stays editable; EN/PT toggle.
- `CardSelectionScreen.kt` — a scrolling column of four sections in deck order (Motto, Positive Reinforcement, Improvements, Personal Question). Each: category header, a `HorizontalPager` carousel of `LoopGainCard`s, a shuffle button, and a pick toggle. Level filter chips (`Any / • / •• / •••`) on the three levelled categories; **the Motto section has no chips**. Bottom bar shows `n/4` and enables **Start** only when complete.
- `SessionScreen.kt` — top bar with `mm:ss` countdown, thin progress bar, pause/resume; 2×2 `LazyVerticalGrid` of the four picked cards, annotated the way the workshop slides do (Positive Reinforcement and Improvements = "About others", Personal Question = "About me"); tap to open a card full-screen; `End session` confirms before popping Home; at zero the bar switches to "Time's up" and the grid dims rather than force-closing.
- `FacilitatorGuideScreen.kt` — scrollable sections from `FacilitatorGuide.kt`, with tips and misconceptions visually distinct from the run-through steps.
- `CardDeckScreen.kt` — **deleted**; superseded by the domain model and `ui/components/`.

`ui/viewmodel/SessionViewModel.kt` — holds `SessionConfig`, the selection, and timer state. Countdown runs in `viewModelScope` against a `TimeSource.Monotonic` deadline (`delay(250)` tick) so it can't drift, with `pause()` / `resume()` / `reset()`. Koin `viewModelOf`, obtained with `koinViewModel()` scoped to a **nested nav graph** so setup → selection → board share one instance and it clears on exit.

`ui/navigation/AppNavigation.kt` — routes `loading`, `home`, `facilitator_guide`, and a nested `session_graph` of `session_setup`, `session_cards`, `session_board`. Loading pops to `home`.

## Tests (`composeApp/src/commonTest`)

Both existing tests reference code this change removes, so they are rewritten, not extended:

- `CardDataTest.kt` → `DeckTest.kt` — 48 cards total; 12 per category; Motto cards all have `level == null`; the other three have exactly 4 per level; ids unique; every card has non-blank EN **and** PT text.
- `NavigationTest.kt` — the new route set and its uniqueness.
- `SessionConfigTest.kt` — `suggestedMinutes` (5 → 60) and clamping.
- `SessionSelectionTest.kt` — `isComplete` only with all four; re-picking a category replaces rather than grows.
- `TimerFormatTest.kt` — `mm:ss` at zero and past an hour.

## Docs

Update `README.md` (screens, and where card content lives), `THEME.md` (the corrected palette, the red accent, dots-not-stars, and the light-card text-contrast fix), and `FLOW_DIAGRAM.md` (the new navigation flow).

## Verification

This container **cannot build the project**: `dl.google.com` is blocked by the egress gateway, so AGP and the Android SDK can't resolve, and no SDK is installed — `BUILD_ENVIRONMENT.md` records the same limitation. So:

1. Push the branch and open the PR; `.github/workflows/pr-checks.yml` runs lint, unit tests and UI tests on GitHub runners. Drive that CI green.
2. Reproducible locally: `./gradlew :composeApp:assembleDebug`, `./gradlew testDebugUnitTest`, `./gradlew lint`.
3. Manual pass: splash → home shows the pile and both buttons → Start Session → 5 participants suggests 60 min → the three levelled carousels filter by dots and the Motto carousel has no filter → Start → 2×2 grid counting down, pause/resume works, "Time's up" at zero → back → the facilitator guide renders → the EN/PT toggle switches card and guide text.
