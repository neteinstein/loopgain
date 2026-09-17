# Store listing — LoopGain: Teams

Draft store listing copy and image assets for the Google Play Console listing (the layout —
title, short/full description, graphic assets, phone/tablet screenshots — matches Play Console;
the same copy works for the App Store's equivalent fields once iOS ships). Everything here was
produced against the app as it actually runs today: [`AGENTS.md`](../../AGENTS.md) and
[`docs/PLAN-main-screen-session-flow.md`](../PLAN-main-screen-session-flow.md) are the design
authority, and the screenshots in `assets/` are real captures from a debug build running on the
`Pixel_10_Pro_XL` and `Pixel_Tablet` emulators — not mockups.

The app's own display name (`AppInfo.APP_NAME`, shown in Settings → About) is **"LoopGain:
Teams"** — use that exact string as the store title, not the bare wordmark "LoopGain".

## Open items before this can be submitted

These are placeholders on purpose — fill them in rather than inventing values:

- **Support email** — no contact address exists anywhere in this repo. Play Console requires one.
- **Privacy policy URL** — required by Play Console even though the app is fully offline (see
  the wording below already lifted from the app's own copy: *"No answers are stored. Only which
  cards were drawn, and one session-level note."*). A one-page policy stating "no data leaves
  the device, no accounts, no analytics" is likely all that's needed, but it has to be hosted
  somewhere (`loopgain.org` is the obvious home).
- **Google Play service account JSON** (`PLAY_STORE_SERVICE_ACCOUNT_JSON`) and the release
  track — `.github/workflows/deploy-stores.yml` already expects this secret; it's a deployment
  credential, not a listing-content question, so it's out of scope here.
- **Screenshot localization** — Play Console lets each screenshot set vary by listing locale.
  The captures below are all in the app's English UI; if the Portuguese listing should show
  Portuguese screenshots instead, re-run the same walkthrough with Settings → Language set to
  **Português** first.

## Categorization

| Field | Value |
| --- | --- |
| Category | Business (alternative: Productivity) |
| Tags | team feedback, retrospective, facilitator, 1:1s, psychological safety, team building |
| Content rating | Everyone — no user-generated content leaves the device, no ads, no accounts |
| Ads | None |
| In-app purchases | None |

## Google Play — English (en-US)

**Title** (max 30 characters — used 15)

```
LoopGain: Teams
```

**Short description** (max 80 characters — used 73)

```
Structured feedback sessions for teams — timed, kind, no accounts needed.
```

**Full description** (max 4000 characters)

```
LoopGain: Teams runs the physical LoopGain feedback deck as a guided, timed session — the
structure that keeps team feedback honest, kind, and something people actually look forward to.

HOW A SESSION WORKS
Add everyone at the table, then draw one card from each of four categories: a Motto to set the
tone, a Positive Reinforcement question, an Improvements question, and a Personal Question.
Read them aloud, give everyone a few silent minutes to write on paper, then go around the table:
each person receives feedback from everyone else, one at a time — not a conversation, not a
debate. The app keeps the clock, the order, and the questions in view so the facilitator can
focus on the room instead of the stopwatch.

TWO LAYOUTS, ONE FLOW
On a phone, LoopGain walks through the session one screen per step — draw, read aloud, write,
rounds, reflect. On a tablet, the same session runs as a wide facilitator board: every stage,
the clockwise turn order, and a live per-person timer on screen at once, designed to be propped
up in the middle of the table for the whole team to see.

BUILT FOR REAL SESSIONS, NOT JUST A DEMO
• 48 bilingual cards (English + Portuguese) across four categories, matching the printed deck
  card-for-card — Motto cards carry no difficulty level, the other three run 1 to 3.
• Cards drawn in the last twelve sessions are held back automatically, so a team doesn't see the
  same question twice in a row.
• A short session log (which cards came up, one closing note) so a team can look back on past
  sessions — no answers are ever recorded, only which cards were drawn.
• Light, dark, and system theme; English or Portuguese UI, switchable anytime in Settings.
• No accounts, no sign-in, no ads, no data leaving the device.

WHY THE STRUCTURE MATTERS
Unstructured feedback tends to either not happen or turn into a "shit sandwich." LoopGain forces
the useful parts of a good feedback culture — psychological safety, going one person at a time,
answers that aren't just "good job" — into a repeatable 30–90 minute session any team can run,
with or without an outside facilitator.

Taking your team from the comfort zone to the trust zone.
```

## Google Play — Portuguese (pt-PT)

**Título** (máx. 30 caracteres — usados 15)

```
LoopGain: Teams
```

**Descrição breve** (máx. 80 caracteres — usados 74)

```
Sessões de feedback estruturadas para equipas — cronometradas, sem contas.
```

**Descrição completa** (máx. 4000 caracteres)

```
O LoopGain: Teams transforma o baralho físico de feedback LoopGain numa sessão guiada e
cronometrada — a estrutura que mantém o feedback em equipa honesto, gentil e algo que as pessoas
passam a esperar em vez de temer.

COMO FUNCIONA UMA SESSÃO
Adiciona todas as pessoas à mesa e tira uma carta de cada uma das quatro categorias: um Mote para
dar o tom, uma pergunta de Reforço Positivo, uma pergunta de Melhorias e uma Pergunta Pessoal.
Lê as cartas em voz alta, dá alguns minutos de silêncio para escrever em papel e depois percorre
a mesa: cada pessoa recebe feedback de todas as outras, uma de cada vez — não é uma conversa,
não é um debate. A app mantém o relógio, a ordem e as perguntas visíveis para que quem facilita
se concentre na sala, não no cronómetro.

DOIS FORMATOS, UM SÓ FLUXO
No telemóvel, o LoopGain avança um ecrã por etapa — tirar, ler, escrever, rondas, reflexão. No
tablet, a mesma sessão corre como um painel largo do facilitador: todas as etapas, a ordem no
sentido dos ponteiros e um cronómetro por pessoa sempre visíveis, pensado para ficar de pé no
meio da mesa e ser visto por toda a equipa.

FEITO PARA SESSÕES REAIS, NÃO UMA DEMONSTRAÇÃO
• 48 cartas bilingues (inglês + português) em quatro categorias, iguais ao baralho impresso
  carta a carta — as cartas de Mote não têm nível de dificuldade, as outras três vão de 1 a 3.
• As cartas tiradas nas últimas doze sessões ficam reservadas automaticamente, para que uma
  equipa não veja a mesma pergunta duas vezes seguidas.
• Um registo curto de sessões (que cartas saíram, uma nota final) para a equipa rever sessões
  passadas — nunca são guardadas respostas, apenas quais as cartas tiradas.
• Tema claro, escuro ou do sistema; interface em inglês ou português, trocável em qualquer
  momento nas Definições.
• Sem contas, sem login, sem anúncios, sem dados a saírem do dispositivo.

PORQUE A ESTRUTURA IMPORTA
Feedback sem estrutura tende a não acontecer, ou a tornar-se uma "sandwich de feedback". O
LoopGain força as partes úteis de uma boa cultura de feedback — segurança psicológica, uma
pessoa de cada vez, respostas que vão além de "bom trabalho" — numa sessão repetível de 30 a 90
minutos que qualquer equipa pode correr, com ou sem facilitador externo.

Levando a tua equipa da zona de conforto para a zona de confiança.
```

## Release notes (version 1.0)

**English**

```
First release. Run a full LoopGain feedback session on your phone or tablet: setup, draw,
read-aloud, silent writing, feedback rounds, and a closing reflection — bilingual EN/PT, light
and dark themes, no accounts, nothing leaves your device.
```

**Português**

```
Primeira versão. Corre uma sessão completa de feedback LoopGain no telemóvel ou tablet:
configuração, tirar cartas, leitura em voz alta, escrita em silêncio, rondas de feedback e
reflexão final — bilingue EN/PT, temas claro e escuro, sem contas, nada sai do dispositivo.
```

## Graphic assets

All generated straight from the app's own brand assets — the launcher's adaptive-icon vector
(`androidApp/src/main/res/drawable/ic_launcher_foreground.xml` /
`ic_launcher_background.xml`) and the loading-screen gradient/wordmark
(`LoadingScreen.kt`, `LoopGainMark.kt`) — not redrawn by eye. See
`assets/graphics/README.md` for how they were built.

| File | Size | Use |
| --- | --- | --- |
| `assets/graphics/icon_512.png` | 512×512 | Play Console hi-res icon |
| `assets/graphics/feature_graphic_1024x500.png` | 1024×500 | Play Console feature graphic |

## Screenshots

Captured from a debug build (`androidApp-debug.apk`) on `Pixel_10_Pro_XL` (phone) and
`Pixel_Tablet` (tablet) emulators, English UI, one real session in progress (3 participants:
Ana, Bea, Cid) so every screen shows live state rather than an empty form.

### Phone (`assets/phone/`)

| File | Screen |
| --- | --- |
| `01_home.png` | Loading / brand splash |
| `02_setup.png` | Session setup — participants, per-category depth, calculated clock |
| `03_draw.png` | Draw the four — one drawn card per category, real question text |
| `04_read_aloud.png` | Read-aloud — full-screen Motto card |
| `05_settings.png` | Settings — theme, language, card-history reset, about |

### Tablet (`assets/tablet/`)

| File | Screen |
| --- | --- |
| `01_home.png` | Loading / brand splash |
| `02_draw_board.png` | Facilitator board — Draw step, all four piles drawn |
| `03_write_board.png` | Facilitator board — Write step, 2×2 grid + silent-writing countdown |
| `04_rounds_board.png` | Facilitator board — Rounds step, clockwise order + per-person timer |
| `05_settings.png` | Settings (dark theme) |

Play Console limits: 2–8 screenshots per form factor, each side between 320px and 3840px —
every file above is within range (`1344×2992` phone, `2560×1600` tablet).
