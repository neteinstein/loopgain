package org.neteinstein.loopgain.ui.screens.session.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.ui.components.asBlankedQuestion
import org.neteinstein.loopgain.ui.theme.CardStyles
import org.neteinstein.loopgain.ui.theme.SessionPalette
import org.neteinstein.loopgain.ui.viewmodel.CategoryFrequencyUi
import org.neteinstein.loopgain.ui.viewmodel.HistoryEntryUi
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
import org.neteinstein.loopgain.ui.viewmodel.SessionHistoryUiState

/**
 * Tablet-only session log, toggled from the header (not part of [org.neteinstein.loopgain.domain.model.SessionStage]).
 * The right-hand panel reports a real per-category draw count instead of the original design's
 * fabricated theme labels ("Ownership & handover" and the like) — see
 * [org.neteinstein.loopgain.ui.viewmodel.toHistoryUiState] for why.
 */
@Composable
fun TabletHistoryScreen(historyState: SessionHistoryUiState, language: Language, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(horizontal = 30.dp, vertical = 26.dp)) {
            Text(text = "SESSION LOG", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
            Text(
                text = "Every session logged so far.",
                color = SessionPalette.Ink,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
            )
            if (historyState.entries.isEmpty()) {
                Text(text = SessionCopy.noSessionsYet(language), color = SessionPalette.MutedLabel, fontSize = 13.sp)
            } else {
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    historyState.entries.forEach { entry -> TabletHistoryEntryRow(entry) }
                }
            }
        }
        Column(
            modifier = Modifier
                .width(340.dp)
                .fillMaxHeight()
                .background(SessionPalette.PanelBackground)
                .padding(26.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column {
                Text(text = "CARD DRAWS BY CATEGORY", color = SessionPalette.MutedLabel, fontSize = 10.sp, letterSpacing = 1.8.sp)
                Text(
                    text = "How many of each category have been drawn across every logged session.",
                    color = SessionPalette.MutedLabel,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            historyState.categoryFrequency.forEach { freq -> TabletFrequencyRow(freq) }
            Text(
                text = "Team level only — this never shows who said what.",
                color = SessionPalette.Accent,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                modifier = Modifier.border(1.dp, SessionPalette.Border).padding(14.dp),
            )
        }
    }
}

@Composable
private fun TabletHistoryEntryRow(entry: HistoryEntryUi, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().border(1.dp, SessionPalette.Border).padding(16.dp)) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(text = "#${entry.sessionNumber}", color = SessionPalette.Accent, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = entry.meta, color = SessionPalette.MutedLabel, fontSize = 12.sp)
        }
        Row(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            entry.cards.forEach { card ->
                val swatch = CardStyles.forCategory(card.category).containerColor
                Text(
                    text = card.code,
                    color = swatch,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.border(1.dp, SessionPalette.Border).padding(horizontal = 9.dp, vertical = 7.dp),
                )
            }
        }
        Text(text = entry.reflectionNote, color = SessionPalette.Ink, fontSize = 13.sp)
        if (entry.cards.isNotEmpty()) {
            Text(
                text = entry.cards.first().text.asBlankedQuestion(),
                color = SessionPalette.MutedLabel,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
private fun TabletFrequencyRow(freq: CategoryFrequencyUi, modifier: Modifier = Modifier) {
    val swatch = CardStyles.forCategory(freq.category).containerColor
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = freq.label, color = SessionPalette.Ink, fontSize = 14.sp)
            Text(text = "${freq.count}×", color = SessionPalette.MutedLabel, fontSize = 11.sp)
        }
        Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(freq.maxCount) { i ->
                Box(modifier = Modifier.size(14.dp).background(if (i < freq.count) swatch else SessionPalette.Disabled))
            }
        }
    }
}
