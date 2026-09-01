---
name: workshop-source
description: Get facilitator-guide content, session timings and card artwork out of the LoopGain workshop PDF in docs/. Use when writing or checking the facilitator guide, session rules, timers, tips, or anything that cites the workshop talk.
---

# Reading the workshop PDF

`docs/Don’t Wait for the CHAOS to think about Feedback - (Feedback Workshop from LoopGain.pdf`
is Pedro Vicente's 128-slide workshop talk. It is the authority for how a session is run and for
what the facilitator guide says.

## How to read it

Use the **Read tool with a `pages` range** (`pages: "97-110"`, max 20 pages per request). The
slides are image-heavy and their text is set in subset fonts with custom encodings, so:

- there is no `pypdf`, `pdftotext` or `pdfimages` in the container, and
- naive stdlib extraction (inflating content streams and reading `Tj` operators) returns
  unmapped glyph ids, not text. Don't spend time on it.

Read the pages you need and quote them. Cite slide numbers in code comments and docs so the
next session can check the source.

## Slide index

| What | Slides |
| --- | --- |
| Feedback vs evaluation | 4–5 |
| "No feedback" as entropy; teams dissolve into chaos | 8–10 |
| Social cohesion beats the "SuperChicken model" (Margaret Heffernan) | 27–29 |
| Google re:work on psychological safety | 39 |
| Facilitator tips | 57, 58, 59, 63, 71, 103, 104, 108 |
| Typical misconceptions | 63, 65, 75, 76, 103, 105 |
| Question-building rules (positive / improvements / personal) | 81–83 |
| Printed card photographs | 54, 95 |
| Facilitator role | 97 |
| Preparation checklist | 98–100 |
| Session run-through | 101–110 |
| Why "3 things" | 113–114 |
| Resources and further reading | 123–125 |
| Closing tagline — "from the comfort zone to the trust zone" | end |

## Rules the guide must get right

- The **facilitator** may come from inside or outside the team, chooses the cards, sets the
  timings and keeps the session moving; facilitators from within the team get better results.
- The session **starts with the Motto**, read aloud, followed by 30+ seconds of silence.
- Feedback is **not a conversation**. Going clockwise from the person on the receiver's right,
  each member shares their answers; the receiver comments briefly, then answers the Personal
  Question.
- **The goal is improving people, not solving issues** — solved issues are a byproduct. Answers
  need not be work-related.
- Duration rule of thumb: **10 minutes per person + 10 minutes** for start and close. Stop with
  about 5 minutes left and ask how the session felt.
- Remote or mixed sessions are fine: define a round order (alphabetical if fully remote) and
  keep a visible timer.

## The timing conflict, already resolved

The printed Instructions card gives **5 minutes** to note answers; the slides say 10 (slide 59)
and 15 (slide 107). The app defaults to **5 minutes**, and surfaces "some facilitators give
10–15" as a facilitator tip. Don't re-open this; if you need to change it, say so explicitly in
the PR.

The 10-min-per-person + 10 rule is consistent across both sources — use it as the suggested
duration.

## Deck copy lives elsewhere

The deck's own Intro / Instructions / Back-of-the-Deck text comes from the spreadsheet, not the
PDF — see `.claude/skills/deck-content/` (`extract_deck.py --guide`).
