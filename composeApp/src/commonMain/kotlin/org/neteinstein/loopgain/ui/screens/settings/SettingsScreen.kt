package org.neteinstein.loopgain.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import org.neteinstein.loopgain.data.repository.SessionHistoryRepository
import org.neteinstein.loopgain.data.repository.SettingsRepository
import org.neteinstein.loopgain.domain.model.AppInfo
import org.neteinstein.loopgain.domain.model.AppTheme
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.ui.viewmodel.SettingsCopy

/**
 * App-wide preferences, card management and About — structured after the settings screen in the
 * sibling neteinstein/CoupleMoments app (a Scaffold + TopAppBar, section title + rounded Card
 * groups, a segmented-button row for an enum choice, a navigable row opening a dialog for the
 * other). Unlike the session flow screens, this one is theme-aware Material3 — it's the screen
 * that lets you change the theme, so it should visibly reflect the choice itself.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    settingsRepository: SettingsRepository = koinInject(),
    sessionHistoryRepository: SessionHistoryRepository = koinInject(),
) {
    val theme by settingsRepository.theme.collectAsStateWithLifecycle()
    val language by settingsRepository.language.collectAsStateWithLifecycle()
    var heldBackCount by remember { mutableStateOf(sessionHistoryRepository.recentlyUsedCardIds().size) }
    var showLanguagePicker by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = SettingsCopy.title(language), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(text = "←", fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            SettingsSection(title = SettingsCopy.appPreferences(language)) {
                ThemeRow(theme = theme, language = language, onSelect = settingsRepository::setTheme)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                LanguageRow(language = language, onClick = { showLanguagePicker = true })
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = SettingsCopy.cardManagement(language)) {
                CardManagementBody(
                    heldBackCount = heldBackCount,
                    language = language,
                    onReset = { showResetConfirm = true },
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AboutSection(language = language)

            Spacer(modifier = Modifier.height(32.dp))

            FooterLinks(language = language, modifier = Modifier.fillMaxWidth())
        }
    }

    if (showLanguagePicker) {
        LanguagePickerDialog(
            current = language,
            uiLanguage = language,
            onSelect = {
                settingsRepository.setLanguage(it)
                showLanguagePicker = false
            },
            onDismiss = { showLanguagePicker = false },
        )
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text(text = SettingsCopy.resetConfirmTitle(language)) },
            text = { Text(text = SettingsCopy.resetConfirmBody(language)) },
            confirmButton = {
                TextButton(onClick = {
                    sessionHistoryRepository.clearAll()
                    heldBackCount = 0
                    showResetConfirm = false
                }) {
                    Text(text = SettingsCopy.reset(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(text = SettingsCopy.cancel(language))
                }
            },
        )
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 12.dp),
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        content()
    }
}

@Composable
private fun ThemeRow(theme: AppTheme, language: Language, onSelect: (AppTheme) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = SettingsCopy.theme(language),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(12.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            AppTheme.entries.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = option == theme,
                    onClick = { onSelect(option) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = AppTheme.entries.size),
                ) {
                    Text(text = SettingsCopy.themeOption(option, language))
                }
            }
        }
    }
}

@Composable
private fun LanguageRow(language: Language, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableRow(onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = SettingsCopy.language(language),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = language.nativeName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(text = "›", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 20.sp)
    }
}

@Composable
private fun CardManagementBody(heldBackCount: Int, language: Language, onReset: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = SettingsCopy.cardManagementDescription(language),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = SettingsCopy.heldBackStatus(heldBackCount, language),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        androidx.compose.material3.OutlinedButton(
            onClick = onReset,
            enabled = heldBackCount > 0,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = SettingsCopy.resetHeldBackCards(language))
        }
    }
}

@Composable
private fun AboutSection(language: Language) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = SettingsCopy.about(language),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            Text(
                text = AppInfo.APP_NAME,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = SettingsCopy.version(AppInfo.VERSION_NAME, language),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = SettingsCopy.tagline(language),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun FooterLinks(language: Language, modifier: Modifier = Modifier) {
    val text = SettingsCopy.footer(language)
    val linkStyles = TextLinkStyles(
        style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline),
    )
    val annotated = buildAnnotatedString {
        append(text)
        addUrlLink(text, "LoopGain", "https://loopgain.org", linkStyles)
        addUrlLink(text, "Pedro Vicente", "https://www.pedrovicente.pt", linkStyles)
    }
    Text(
        text = annotated,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

private fun AnnotatedString.Builder.addUrlLink(
    text: String,
    label: String,
    url: String,
    styles: TextLinkStyles,
) {
    val start = text.indexOf(label)
    if (start < 0) return
    addLink(LinkAnnotation.Url(url, styles), start, start + label.length)
}

@Composable
private fun LanguagePickerDialog(
    current: Language,
    uiLanguage: Language,
    onSelect: (Language) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = SettingsCopy.chooseLanguage(uiLanguage)) },
        text = {
            Column {
                Language.entries.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableRow { onSelect(option) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = option == current, onClick = { onSelect(option) })
                        Text(text = option.nativeName, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(text = SettingsCopy.cancel(uiLanguage)) }
        },
    )
}

private fun Modifier.clickableRow(onClick: () -> Unit): Modifier =
    this.clickable(onClick = onClick)
