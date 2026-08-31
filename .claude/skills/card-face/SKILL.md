---
name: card-face
description: Draw a LoopGain card that matches the printed deck — palette, category label, difficulty dots, the navy card back and its wave motif. Use when building or changing any card composable, the piled deck, the card carousels, the session grid, or ui/theme colours. Overrides THEME.md, which is out of date.
---

# Rendering a LoopGain card face

The app's cards have to read as the printed deck. The reference is the photographs on **pages
54 and 95** of `docs/Don’t Wait for the CHAOS to think about Feedback - (Feedback Workshop from
LoopGain.pdf`; open them with the Read tool (`pages: "54"`) before doing detailed visual work.

## Two corrections to `THEME.md`

`THEME.md` predates the printed-deck reference and is wrong on both counts. Fix the code to
match the deck, and update `THEME.md` when you touch it:

1. **Only the Motto card uses white text.** The three light cards (Positive Reinforcement,
   Improvements, Personal Question) print **navy** text. White on Sky Blue is unreadable and
   fails contrast.
2. **Difficulty is dots, not stars.** `•` / `••` / `•••`. The asterisks in the spreadsheet are
   its own notation.

## Layout

Landscape, rounded corners, roughly 1.6:1, identical across categories:

- **Category name top-left** — very small caps, wide letter spacing. White on the navy Motto
  card; tone-on-tone (a darker shade of the card's own colour) on the light cards.
- **Level dots top-right** — one to three. Motto cards have no level, so they get **no dots**;
  the component must handle a null level rather than defaulting to one dot.
- **Question text** large, bold, left-aligned, vertically centred, with size-adaptive
  typography so a long question still fits.
- `_` in the question renders as an **underscored blank**, not a name and not a bare underscore
  glyph.
- **Category / divider cards**: navy ground, red `LoopGain` wordmark top-left, category name
  bottom-left in white with red level dots beneath, and the white **pill-dash waveform** on the
  right — rows of rounded horizontal dashes of varying length, like a loop signal.

Draw the back and the waveform with `Canvas` (capsules of varying length and alpha). No bitmap
assets are needed, and none should be added for this.

## Palette

Sampled from the photographs inside the PDF, so treat the blues and the red as "match the
printed deck" targets rather than exact brand values — if a brand asset ever turns up, it wins.

| Role | Value | Note |
| --- | --- | --- |
| Navy — Motto ground, chrome, text on light cards | `#0D2254` | matches the existing `CardColors.Motto`; keep it |
| Positive Reinforcement | `≈ #5BB8E8` | brighter/cyan-er than today's `#4A90E2` |
| Improvements | `≈ #A9D0E8` | close to today's Steel `#86B3D1`, a touch lighter |
| Personal Question | `≈ #C8DFEF` | close to today's Sky `#B9D9EB` |
| Red accent — wordmark, dots on divider cards | `≈ #E63946` | **not in the theme today; add it** |

## Where it goes

Colour lives in the **UI layer only**. `ui/theme/` owns the palette and a category →
`containerColor` / `contentColor` / `labelColor` mapping; the domain model carries the category
and level and no Compose types. The current `CardType(displayName, color)` enum in
`ui/screens/CardDeckScreen.kt` is the coupling being removed — don't extend it.

One card composable serves the piled deck, the selection carousels and the session grid. If you
find yourself writing a second card renderer, parameterise the first instead.
