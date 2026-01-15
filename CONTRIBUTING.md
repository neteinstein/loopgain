# Contributing to LoopGain

Thank you for your interest in contributing to LoopGain! This document provides guidelines and instructions for contributing.

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Focus on what is best for the community
- Show empathy towards other community members

## How to Contribute

### Reporting Bugs

1. Check if the bug has already been reported in [Issues](https://github.com/neteinstein/loopgain/issues)
2. If not, create a new issue with:
   - Clear, descriptive title
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots if applicable
   - Environment details (OS, device, versions)

### Suggesting Features

1. Check if the feature has been suggested
2. Create an issue describing:
   - The problem you're trying to solve
   - Your proposed solution
   - Alternative solutions considered
   - Additional context

### Pull Requests

1. Fork the repository
2. Create a branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. Make your changes following our coding standards

4. Write or update tests

5. Run tests and ensure they pass:
   ```bash
   ./gradlew test
   ```

6. Commit with clear messages:
   ```bash
   git commit -m "feat: Add card swipe animation"
   ```

7. Push to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

8. Create a Pull Request

## Coding Standards

### Kotlin Style

Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
class CardViewModel(
    private val repository: CardRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CardState())
    val state: StateFlow<CardState> = _state.asStateFlow()
}

// Use meaningful names
fun loadCards() { }  // Good
fun lc() { }         // Bad
```

### Compose

Follow Compose best practices:

```kotlin
// Stateless composables
@Composable
fun CardItem(
    card: CardData,
    onClick: () -> Unit
) {
    // UI implementation
}

// Stateful composables
@Composable
fun CardScreen() {
    val viewModel: CardViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    
    CardScreenContent(
        state = state,
        onAction = viewModel::handleAction
    )
}
```

### Naming Conventions

- **Classes**: PascalCase (`CardViewModel`)
- **Functions**: camelCase (`loadCards`)
- **Constants**: UPPER_SNAKE_CASE (`MAX_CARDS`)
- **Resources**: lowercase_with_underscores (`ic_card_icon`)

### File Structure

```
kotlin/org/neteinstein/loopgain/
├── ui/
│   ├── screens/
│   │   └── CardScreen.kt       # Screen composable
│   ├── components/
│   │   └── CardItem.kt         # Reusable component
│   └── theme/
│       └── Theme.kt
├── domain/
│   ├── model/
│   │   └── Card.kt             # Domain model
│   └── usecase/
│       └── GetCardsUseCase.kt  # Use case
└── data/
    ├── repository/
    │   └── CardRepository.kt   # Repository interface
    └── source/
        └── CardDataSource.kt   # Data source
```

## Testing Guidelines

### Unit Tests

Write tests for:
- ViewModels
- Use cases
- Repositories
- Utility functions

```kotlin
class CardViewModelTest {
    @Test
    fun `loadCards success updates state`() = runTest {
        // Given
        val cards = listOf(testCard1, testCard2)
        val useCase = FakeGetCardsUseCase(Result.success(cards))
        val viewModel = CardViewModel(useCase)
        
        // When
        viewModel.loadCards()
        advanceUntilIdle()
        
        // Then
        assertEquals(cards, viewModel.state.value.cards)
        assertFalse(viewModel.state.value.isLoading)
    }
}
```

### UI Tests

```kotlin
@Test
fun cardScreen_displayCards() {
    composeTestRule.setContent {
        CardScreen()
    }
    
    composeTestRule
        .onNodeWithText("Test Card")
        .assertExists()
        .performClick()
}
```

## Commit Message Format

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

### Types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

### Examples
```
feat(cards): Add swipe gesture to cards
fix(navigation): Resolve back navigation crash
docs(readme): Update setup instructions
test(cards): Add tests for card sorting
```

## Pull Request Process

### Before Submitting

- [ ] Code follows style guidelines
- [ ] Self-reviewed the code
- [ ] Commented complex logic
- [ ] Updated documentation
- [ ] Added/updated tests
- [ ] All tests pass locally
- [ ] No lint warnings

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
Describe how you tested your changes

## Screenshots (if applicable)
Add screenshots for UI changes

## Checklist
- [ ] Tests pass
- [ ] Documentation updated
- [ ] No lint warnings
```

## Development Workflow

1. **Create branch** from `main`
2. **Make changes** with commits
3. **Write tests**
4. **Run locally**
   ```bash
   ./gradlew test
   ./gradlew lint
   ```
5. **Push** to your fork
6. **Create PR** with description
7. **Address review comments**
8. **Squash commits** if requested
9. **Merge** when approved

## Code Review Guidelines

### For Authors
- Respond to all comments
- Make requested changes
- Mark conversations as resolved
- Keep PRs focused and small

### For Reviewers
- Be respectful and constructive
- Explain reasoning for suggestions
- Approve when satisfied
- Test the changes if possible

## Architecture Guidelines

### MVVM Pattern

```kotlin
// ViewModel
class CardViewModel : ViewModel() {
    private val _state = MutableStateFlow(CardState())
    val state: StateFlow<CardState> = _state.asStateFlow()
    
    fun loadCards() {
        viewModelScope.launch {
            // Business logic
        }
    }
}

// Screen
@Composable
fun CardScreen(viewModel: CardViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    // UI
}
```

### Dependency Injection

Use Koin:

```kotlin
val featureModule = module {
    viewModel { CardViewModel(get()) }
    factory { GetCardsUseCase(get()) }
    single<CardRepository> { CardRepositoryImpl(get()) }
}
```

### Error Handling

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

// Usage
when (val result = repository.getCards()) {
    is Result.Success -> updateState(result.data)
    is Result.Error -> showError(result.exception)
}
```

## Documentation

### Code Comments

```kotlin
/**
 * Loads cards from the repository and updates the UI state.
 * 
 * @throws NetworkException if the network request fails
 */
fun loadCards() {
    // Implementation
}
```

### README Updates

Update README.md when:
- Adding new features
- Changing setup steps
- Adding dependencies
- Modifying architecture

## Resources

- [Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html)
- [Compose Guidelines](https://developer.android.com/jetpack/compose/mental-model)
- [KMP Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Testing Guide](https://developer.android.com/training/testing)

## Questions?

- Check existing documentation
- Search closed issues
- Ask in discussions
- Open a new issue

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.

Thank you for contributing to LoopGain! 🙏
