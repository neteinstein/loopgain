# LoopGain Architecture

## Overview

LoopGain is built using **Kotlin Multiplatform (KMP)** with **Compose Multiplatform (CMP)** to share code between Android and iOS platforms. This document describes the architectural decisions and patterns used throughout the project.

## Architecture Principles

1. **Code Sharing**: Maximize code reuse between platforms
2. **Clean Architecture**: Separation of concerns with clear layer boundaries
3. **Unidirectional Data Flow**: Predictable state management
4. **Dependency Injection**: Loose coupling and testability
5. **Platform Parity**: Consistent user experience across platforms

## Layer Architecture

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (Compose UI, ViewModels, Navigation)   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│            Domain Layer                 │
│      (Use Cases, Business Logic)        │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│             Data Layer                  │
│  (Repositories, Data Sources, Models)   │
└─────────────────────────────────────────┘
```

## Module Structure

### composeApp (Multiplatform Module)

The main module containing all shared code:

#### commonMain
Shared code that runs on both Android and iOS:

```
org.neteinstein.loopgain/
├── ui/                     # Presentation Layer
│   ├── screens/            # Screen composables
│   │   ├── LoadingScreen.kt
│   │   └── CardDeckScreen.kt
│   ├── components/         # Reusable UI components
│   ├── theme/              # Material3 theming
│   │   └── Theme.kt
│   └── navigation/         # Navigation logic
│       └── AppNavigation.kt
├── domain/                 # Domain Layer
│   ├── usecase/            # Business logic use cases
│   └── model/              # Domain models
├── data/                   # Data Layer
│   ├── repository/         # Repository implementations
│   ├── source/             # Data sources (local, remote)
│   └── model/              # Data models/DTOs
└── di/                     # Dependency Injection
    └── AppModule.kt
```

#### androidMain
Android-specific implementations:
- `MainActivity.kt`: Android activity entry point
- `LoopGainApplication.kt`: Android application class
- Platform-specific implementations (if any)

#### iosMain
iOS-specific implementations:
- `MainViewController.kt`: iOS view controller factory
- Platform-specific implementations (if any)

### iosApp
iOS application wrapper written in Swift:
- `iOSApp.swift`: iOS app entry point
- `ContentView.swift`: SwiftUI wrapper for Compose UI

## Technology Stack

### Core
- **Kotlin**: 2.4.10
- **Compose Multiplatform**: 1.11.1
- **Android Gradle Plugin**: 8.13.2
- **Kotlin Coroutines**: 1.11.0 (Asynchronous programming)

### UI
- **Material3**: Design system
- **Compose Navigation**: Screen navigation
- **Coil**: Image loading

### Networking
- **Ktor Client**: HTTP client
- **kotlinx.serialization**: JSON serialization

### Backend
- **Firebase Firestore**: NoSQL database
- **Firebase Analytics**: User analytics

### Dependency Injection
- **Koin**: Lightweight DI framework

### Testing
- **kotlin.test**: Multiplatform testing
- **Compose UI Testing**: UI tests

## Design Patterns

### MVVM (Model-View-ViewModel)
- **Model**: Data classes representing business entities
- **View**: Composable functions (UI)
- **ViewModel**: State management and business logic coordination

### Repository Pattern
- Abstracts data sources
- Single source of truth
- Centralizes data access logic

```kotlin
interface CardRepository {
    suspend fun getCards(): Result<List<Card>>
    suspend fun updateCard(card: Card): Result<Unit>
}

class CardRepositoryImpl(
    private val remoteDataSource: CardRemoteDataSource,
    private val localDataSource: CardLocalDataSource
) : CardRepository {
    // Implementation
}
```

### Dependency Injection
Using Koin for DI:

```kotlin
val appModule = module {
    // ViewModels
    viewModel { CardDeckViewModel(get()) }
    
    // Use Cases
    factory { GetCardsUseCase(get()) }
    
    // Repositories
    single<CardRepository> { CardRepositoryImpl(get(), get()) }
    
    // Data Sources
    single { FirebaseCardDataSource() }
}
```

## State Management

### Unidirectional Data Flow
```
┌──────────┐
│   User   │
│  Action  │
└────┬─────┘
     │
     ▼
┌──────────┐     ┌─────────┐
│ ViewModel│────▶│  State  │
└──────────┘     └────┬────┘
     ▲                │
     │                │
┌────┴─────┐         │
│ Use Case │         │
└──────────┘         │
                     ▼
                ┌─────────┐
                │   UI    │
                │ (Compose)│
                └─────────┘
```

### State Holders
```kotlin
data class CardDeckState(
    val cards: List<CardData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CardDeckViewModel(
    private val getCardsUseCase: GetCardsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CardDeckState())
    val state: StateFlow<CardDeckState> = _state.asStateFlow()
    
    init {
        loadCards()
    }
    
    private fun loadCards() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getCardsUseCase()
                .onSuccess { cards ->
                    _state.update { 
                        it.copy(cards = cards, isLoading = false) 
                    }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(error = error.message, isLoading = false) 
                    }
                }
        }
    }
}
```

## Navigation

### Navigation Graph
```kotlin
sealed class Screen(val route: String) {
    data object Loading : Screen("loading")
    data object CardDeck : Screen("card_deck")
    data object CardDetail : Screen("card_detail/{cardId}") {
        fun createRoute(cardId: String) = "card_detail/$cardId"
    }
}
```

### Navigation Implementation
Using Compose Navigation for a single navigation graph shared across platforms.

## Firebase Integration

### Structure
```
Firebase Project: LoopGain
├── Android App: org.neteinstein.loopgain
├── iOS App: org.neteinstein.loopgain
└── Firestore Database
    └── cards (collection)
        ├── [cardId] (document)
        │   ├── title: string
        │   ├── content: string
        │   ├── backgroundColor: string
        │   ├── rotation: number
        │   └── position: object
        └── ...
```

### Data Access
```kotlin
// Expect/Actual pattern for platform-specific Firebase
expect class FirebaseService {
    suspend fun getCards(): List<CardData>
    suspend fun addCard(card: CardData): String
    suspend fun updateCard(cardId: String, card: CardData)
}

// Android implementation (androidMain)
actual class FirebaseService {
    private val db = Firebase.firestore
    
    actual suspend fun getCards(): List<CardData> {
        return db.collection("cards")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject<CardData>() }
    }
}
```

## Testing Strategy

### Unit Tests (commonTest)
- Domain logic testing
- ViewModel testing
- Repository testing
- Use case testing

```kotlin
class CardDeckViewModelTest {
    @Test
    fun `when loadCards succeeds, state contains cards`() = runTest {
        // Given
        val cards = listOf(/* test cards */)
        val useCase = FakeGetCardsUseCase(Result.success(cards))
        val viewModel = CardDeckViewModel(useCase)
        
        // When
        advanceUntilIdle()
        
        // Then
        assertEquals(cards, viewModel.state.value.cards)
        assertEquals(false, viewModel.state.value.isLoading)
    }
}
```

### UI Tests
- Screen rendering
- User interactions
- Navigation flows

```kotlin
@Test
fun cardDeckScreen_displaysCards() {
    composeTestRule.setContent {
        CardDeckScreen(/* ... */)
    }
    
    composeTestRule
        .onNodeWithText("MOTTO")
        .assertExists()
}
```

## Performance Considerations

### Image Loading
- Use Coil for async image loading
- Cache images appropriately
- Use placeholder and error images

### State Updates
- Use `StateFlow` for reactive state
- Avoid unnecessary recompositions
- Use `remember` and `derivedStateOf` wisely

### Network Calls
- Use coroutines for async operations
- Implement proper error handling
- Add retry logic for transient failures

## Security

### API Keys
- Store Firebase config securely
- Don't commit `google-services.json` to git
- Use environment variables for sensitive data

### Data Validation
- Validate all user inputs
- Sanitize data before storage
- Use Firebase Security Rules

## Scalability

### Modularization
Future modules could include:
- `feature-cards`: Card management feature
- `feature-profile`: User profile feature
- `core-network`: Network utilities
- `core-database`: Local database

### Caching Strategy
- Cache frequently accessed data locally
- Implement offline-first approach
- Sync data when online

## Accessibility

- Provide content descriptions for images
- Support dynamic text sizing
- Ensure sufficient color contrast
- Test with screen readers

## Internationalization

- Use string resources
- Support RTL layouts
- Format dates/numbers per locale

```kotlin
// Use Compose Multiplatform Resources
@Composable
fun GreetingText() {
    Text(stringResource(Res.strings.greeting))
}
```

## Monitoring and Analytics

### Firebase Analytics
- Track screen views
- Monitor user flows
- Track custom events

### Crash Reporting
- Firebase Crashlytics integration
- Detailed crash logs
- User context in reports

## Future Enhancements

1. **Offline Support**: Local database with sync
2. **User Authentication**: Firebase Auth integration
3. **Push Notifications**: FCM integration
4. **Social Features**: Share cards, collaborate
5. **Gamification**: Points, achievements
6. **Desktop Support**: Extend to Desktop platforms
7. **Web Support**: Compose for Web

## References

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- [Firebase for KMP](https://firebase.google.com/docs)
- [Koin Documentation](https://insert-koin.io/)
- [Ktor Client](https://ktor.io/docs/client.html)
