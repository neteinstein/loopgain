# LoopGain Application Flow

## User Journey

```
┌──────────────────────────────────────────────────────────────────┐
│                         APP LAUNCH                                │
└──────────────────┬───────────────────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────────────┐
│                     LOADING SCREEN                                │
│  ┌────────────────────────────────────────────────────────┐      │
│  │                                                          │      │
│  │            [Blue Gradient Background]                   │      │
│  │                                                          │      │
│  │                    LoopGain                             │      │
│  │              (Fade In Animation)                        │      │
│  │                                                          │      │
│  │             Value Different Things                      │      │
│  │                                                          │      │
│  └────────────────────────────────────────────────────────┘      │
│                      Duration: 2.5s                               │
└──────────────────┬───────────────────────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────────────┐
│                    CARD DECK SCREEN                               │
│  ┌────────────────────────────────────────────────────────┐      │
│  │                                                          │      │
│  │    ┌──────────────────────────────────┐               │      │
│  │   ┌┼──────────────────────────────────┼┐              │      │
│  │  ┌┼┼──────────────────────────────────┼┼┐             │      │
│  │  │││         MOTTO                     │││ ← Top Card  │      │
│  │  │││                                   │││   (Dark Blue)│      │
│  │  │││  We are all different,            │││             │      │
│  │  │││  we probably value different      │││             │      │
│  │  │││  things and there is nothing      │││             │      │
│  │  │││  wrong about that.                │││             │      │
│  │  │││                                   │││             │      │
│  │  └┼┼──────────────────────────────────┼┼┘             │      │
│  │   └┼──────────────────────────────────┼┘              │      │
│  │    └──────────────────────────────────┘               │      │
│  │                                                          │      │
│  │    Middle Card: "What can we do to admire our          │      │
│  │                  differences?" (Medium Blue)            │      │
│  │                                                          │      │
│  │    Bottom Card: "It all begins with communication"     │      │
│  │                  (Light Blue)                           │      │
│  │                                                          │      │
│  └────────────────────────────────────────────────────────┘      │
│                    [Piled Cards with Rotation]                    │
└──────────────────────────────────────────────────────────────────┘
```

## Technical Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                        Platform Layer                            │
├─────────────────────┬───────────────────────────────────────────┤
│   Android           │   iOS                                      │
│   MainActivity      │   iOSApp.swift                            │
│   LoopGainApp       │   ContentView.swift                       │
└─────────────────────┴───────────────┬───────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Shared Compose UI (commonMain)                │
├─────────────────────────────────────────────────────────────────┤
│  App.kt                                                          │
│    └─> LoopGainTheme                                            │
│          └─> AppNavigation                                      │
│                ├─> LoadingScreen (2.5s)                         │
│                │     └─> Navigate to CardDeck                   │
│                └─> CardDeckScreen                               │
│                      └─> PiledCard (x3)                         │
└─────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Dependency Injection (Koin)                  │
├─────────────────────────────────────────────────────────────────┤
│  - ViewModels (future)                                           │
│  - Repositories (future)                                         │
│  - Use Cases (future)                                            │
│  - Data Sources (Firebase, future)                               │
└─────────────────────────────────────────────────────────────────┘
```

## Navigation Flow

```
     Start
       │
       ▼
┌─────────────┐
│   Loading   │ ──── (Auto navigate after 2.5s) ────┐
│   Screen    │                                      │
└─────────────┘                                      │
                                                     │
                                                     ▼
                                              ┌─────────────┐
                                              │  Card Deck  │
                                              │   Screen    │
                                              └─────────────┘
                                                     │
                                                     │
                                        (Future: Click card)
                                                     │
                                                     ▼
                                              ┌─────────────┐
                                              │Card Details │
                                              │   (Future)  │
                                              └─────────────┘
```

## Data Flow (Future Implementation)

```
┌──────────┐      ┌──────────┐      ┌────────────┐      ┌──────────┐
│   UI     │ ───> │ViewModel │ ───> │  Use Case  │ ───> │Repository│
│(Compose) │      │          │      │            │      │          │
└──────────┘      └──────────┘      └────────────┘      └──────────┘
     ▲                  │                                      │
     │                  │                                      ▼
     │                  │                              ┌──────────────┐
     │                  │                              │  Data Source │
     │                  │                              │  (Firebase)  │
     │                  │                              └──────────────┘
     │                  │                                      │
     │                  ▼                                      │
     │            ┌──────────┐                                │
     └────────────│  State   │ <──────────────────────────────┘
                  │  Flow    │
                  └──────────┘
```

## Build & Deployment Flow

```
┌──────────────┐
│  Developer   │
│  Commits     │
└──────┬───────┘
       │
       ▼
┌──────────────────────────────────────────────────────┐
│               GitHub Actions                          │
├──────────────────────────────────────────────────────┤
│                                                       │
│  Pull Request                                         │
│  ├─> Lint                                             │
│  ├─> Unit Tests                                       │
│  └─> UI Tests                                         │
│                                                       │
│  Merge to Main                                        │
│  ├─> Build Release APK                                │
│  ├─> Create GitHub Release                            │
│  └─> Upload Artifacts                                 │
│                                                       │
│  Manual Trigger                                       │
│  ├─> Build Signed APK/AAB                             │
│  ├─> Deploy to Play Store                             │
│  └─> Deploy to App Store                              │
│                                                       │
└──────────────────────────────────────────────────────┘
```

## Component Hierarchy

```
App
 └─> LoopGainTheme
      └─> AppNavigation
           ├─> LoadingScreen
           │    ├─> Box (Gradient Background)
           │    │    └─> Column
           │    │         ├─> Text "LoopGain" (animated)
           │    │         └─> Text "Value Different Things"
           │    │
           │    └─> LaunchedEffect (auto-navigate)
           │
           └─> CardDeckScreen
                └─> Box (Container)
                     └─> PiledCard (x3)
                          ├─> Card #1 (Dark Blue, -8° rotation)
                          ├─> Card #2 (Med Blue, -4° rotation)
                          └─> Card #3 (Light Blue, 0° rotation)
```

## Color Scheme

```
┌────────────────────────────────────────┐
│  LoopGain Brand Colors                 │
├────────────────────────────────────────┤
│                                         │
│  Primary:   #1E3A5F  ███ Dark Blue     │
│  Secondary: #4A90E2  ███ Medium Blue   │
│  Accent:    #7FB3D5  ███ Light Blue    │
│                                         │
│  Background: #FFFBFE  ░░░ Light        │
│  Surface:    #FFFBFE  ░░░ Light        │
│                                         │
└────────────────────────────────────────┘
```

## State Management Pattern

```
┌─────────────┐
│ User Action │
└──────┬──────┘
       │
       ▼
┌─────────────┐        ┌──────────────┐
│  ViewModel  │───────>│ State Update │
└──────┬──────┘        └──────┬───────┘
       │                      │
       │                      │
       ▼                      ▼
┌─────────────┐        ┌──────────────┐
│  Use Case   │        │   UI Layer   │
│  (Business  │        │ (Recompose)  │
│   Logic)    │        └──────────────┘
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Repository  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Data Source │
│  (Firebase) │
└─────────────┘
```

## Testing Strategy

```
┌─────────────────────────────────────────┐
│           Unit Tests                     │
├─────────────────────────────────────────┤
│  - ViewModels                            │
│  - Use Cases                             │
│  - Repositories                          │
│  - Domain Models                         │
│  - Navigation Logic                      │
└─────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│        Integration Tests                 │
├─────────────────────────────────────────┤
│  - Repository + Data Source              │
│  - ViewModel + Use Case + Repository     │
└─────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│           UI Tests                       │
├─────────────────────────────────────────┤
│  - Screen Rendering                      │
│  - User Interactions                     │
│  - Navigation Flows                      │
│  - State Changes                         │
└─────────────────────────────────────────┘
```

## Deployment Pipeline

```
Local Development
       │
       ▼
Feature Branch
       │
       ▼
Pull Request ──> [CI: Lint + Tests]
       │
       │ (Approved)
       ▼
Merge to Main ──> [CI: Build + Release]
       │
       │
       ├──> GitHub Release (Automatic)
       │         │
       │         └──> APK Artifact
       │
       └──> Manual Store Deploy
                 │
                 ├──> Google Play Store
                 │         │
                 │         └──> Production Track
                 │
                 └──> Apple App Store
                           │
                           └──> TestFlight → Production
```

---

This flow diagram illustrates the complete application structure, user journey, technical architecture, and deployment process for the LoopGain KMP + CMP application.
