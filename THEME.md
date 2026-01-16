# LoopGain Theme Documentation

## Overview

This document describes the LoopGain app theme implementation, including the color palette and card type system.

## Color Palette

The LoopGain theme uses a cohesive blue color palette as specified:

### Primary Colors

| Color Name | Hex Code | Usage |
|------------|----------|-------|
| Navy Blue | `#0D2254` | Primary color - headers, primary buttons, heavy text |
| Cerulean | `#4A90E2` | Secondary accent color |
| Steel Blue | `#86B3D1` | Secondary color - accents, differentiating categories |
| Sky Blue | `#B9D9EB` | Background color - soft backgrounds, cards |
| Pure White | `#FFFFFF` | Typography and card edges |

## Card Types

The app defines four distinct card types, each with its own color:

### 1. MOTTO Card
- **Color**: Deep Navy (`#0D2254`)
- **Purpose**: Display core mottos and foundational values
- **Typography**: White text for maximum contrast

### 2. PERSONAL QUESTION Card
- **Color**: Sky Blue (`#B9D9EB`)
- **Purpose**: Present personal reflection questions
- **Typography**: White text on light blue background

### 3. IMPROVEMENTS Card
- **Color**: Steel Blue (`#86B3D1`)
- **Purpose**: Highlight areas for improvement
- **Typography**: White text on mid-tone blue

### 4. POSITIVE REINFORCEMENT Card
- **Color**: Cerulean (`#4A90E2`)
- **Purpose**: Provide positive affirmations and encouragement
- **Typography**: White text on vibrant blue

## Implementation

### Theme Structure

The theme is implemented in `Theme.kt` with:
- `CardColors` object containing all card-specific colors
- Material3 color schemes for both light and dark modes
- Type-safe `CardType` enum in `CardDeckScreen.kt`

### Usage Example

```kotlin
// Creating a card with a specific type
CardData(
    type = CardType.MOTTO,
    content = "Your motto text here",
    rotation = -8f,
    offsetX = -20f,
    offsetY = 30f
)
```

### CardType Enum

```kotlin
enum class CardType(val displayName: String, val color: Color) {
    MOTTO("MOTTO", CardColors.Motto),
    PERSONAL_QUESTION("PERSONAL QUESTION", CardColors.PersonalQuestion),
    IMPROVEMENTS("IMPROVEMENTS", CardColors.Improvements),
    POSITIVE_REINFORCEMENT("POSITIVE REINFORCEMENT", CardColors.PositiveReinforcement)
}
```

## Material3 Theme Integration

The theme integrates with Material3's color system:

### Light Mode
- **Primary**: Navy Blue - for main actions and emphasis
- **Secondary**: Cerulean - for secondary actions
- **Tertiary**: Steel Blue - for tertiary elements
- **Background**: Sky Blue - for screen backgrounds
- **Surface**: White - for elevated surfaces

### Dark Mode
- **Primary**: Sky Blue - adjusted for dark backgrounds
- **Secondary**: Cerulean - maintains vibrant accent
- **Tertiary**: Navy Blue - provides depth
- **Background/Surface**: Standard dark theme colors

## Design Principles

1. **Consistency**: All cards use white typography for consistent readability
2. **Hierarchy**: Color intensity indicates different card types and importance
3. **Accessibility**: High contrast ratios ensure text readability
4. **Brand Identity**: Blue palette maintains LoopGain brand recognition

## Future Enhancements

Potential improvements to consider:
- Additional card types if needed
- Animation colors for transitions
- Error and success state colors
- Extended color variations for different contexts
