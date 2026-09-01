---
name: deck-content
description: Read, transcribe or validate LoopGain's 48 question cards from docs/LoopGain - Questions.xlsx. Use whenever card text, categories, difficulty levels, the bundled deck, or the deck's Intro/Instructions copy are involved — including writing or checking data/source/BundledDeck.kt and its tests.
---

# LoopGain deck content

`docs/LoopGain - Questions.xlsx`, sheet **`Teams Cards v2`**, is the authority for every card in
the app. Nothing about the deck should be typed from memory, from `README.md`, or from a plan
document — extract it from the spreadsheet each time.

## Extract it

The workbook is a zip of XML and the container has no `openpyxl`, so use the bundled stdlib
script rather than reaching for a dependency:

```bash
python3 .claude/skills/deck-content/extract_deck.py            # summary + invariant check
python3 .claude/skills/deck-content/extract_deck.py --tsv      # id / category / level / EN / PT
python3 .claude/skills/deck-content/extract_deck.py --kotlin   # QuestionCard(...) entries
python3 .claude/skills/deck-content/extract_deck.py --guide    # Intro, Instructions, Back of the Deck
```

Generate `BundledDeck.kt` with `--kotlin` and paste the result; do not retype 48 bilingual cards
by hand. The script exits non-zero if any invariant below fails, so it works as a check too —
run it after regenerating.

## The shape of the deck

48 cards, four categories of 12, every card bilingual (EN + PT):

| Category | Enum | PT name | Levels |
| --- | --- | --- | --- |
| Motto | `MOTTO` | Mote | **none** |
| Positive Reinforcement | `POSITIVE_REINFORCEMENT` | Reforço Positivo | 4 × L1, 4 × L2, 4 × L3 |
| Improvements | `IMPROVEMENTS` | Melhorias | 4 × L1, 4 × L2, 4 × L3 |
| Personal Question | `PERSONAL_QUESTION` | Pergunta Pessoal | 4 × L1, 4 × L2, 4 × L3 |

Invariants worth asserting in `commonTest` (the script checks the same ones):

- 48 cards total, 12 per category, ids unique.
- Every Motto card has `level == null`; the other three categories have exactly 4 per level.
- Every card has non-blank EN **and** PT text.

**Motto having no level is a product constraint, not missing data.** `QuestionCard.level` is
nullable because of it, and the Motto section of the picker gets no level filter.

## Reading the sheet by hand

Columns: `A` index, `B` category header, `C` English, `D` Portuguese, `E` PT category name.

- Column `B` carries a value only on the **first row of each block** (`Positive Reinforcement *`,
  `Positive Reinforcement **`, …). Forward-fill it: the category and level apply until the next
  `B` value.
- The trailing star count in `B` is the level. Stars are the spreadsheet's notation only — the
  printed deck and the app show **dots**.
- Rows 51–53 are not cards: `Intro`, `Instructions` and `Back of the Deck`. They are guide copy
  (`--guide` prints them) and must never end up in the deck list.
- Sheet `Teams Cards v1` is an older revision. Ignore it.

## Text rules

- `_` in a question is a placeholder for the teammate being discussed — "3 things `_` is very
  good at…". Render it as an underscored blank the way the printed card does; never substitute a
  name, and never strip it.
- Keep Portuguese accents and the `o/a` gender forms exactly as written. If PT text comes back
  mangled, the extraction is wrong — the file is fine.
- Escape `"` and `$` when emitting Kotlin string literals (the script does this).
- The Intro copy says the deck has "50 playing cards" while the sheet holds 48 question rows;
  the printed deck counts its own intro/instructions cards. Reproduce the copy as written rather
  than reconciling the two numbers.
