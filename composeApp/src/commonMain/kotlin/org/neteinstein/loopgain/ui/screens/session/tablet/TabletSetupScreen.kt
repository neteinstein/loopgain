package org.neteinstein.loopgain.ui.screens.session.tablet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.ui.components.DepthPickerTile
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.PersonRowUi
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

/**
 * Step 1 of the tablet board: roster on the left, the calculated session clock on the right.
 * Mirrors [org.neteinstein.loopgain.ui.screens.session.SessionSetupScreen] but as a two-column
 * layout instead of a scroll-then-footer stack, matching the design's `1.15fr .85fr` split.
 */
@Composable
fun TabletSetupScreen(
    state: SessionUiState,
    onAddPerson: () -> Unit,
    onRemovePerson: (Int) -> Unit,
    onRenamePerson: (Int, String) -> Unit,
    onPickLevel: (CardCategory, CardLevel) -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1.15f)
                .fillMaxHeight()
                .border(width = 1.dp, color = LocalSessionColors.current.Border)
                .padding(30.dp),
        ) {
            Text(text = SessionCopy.stepOneSetup(state.language), color = LocalSessionColors.current.MutedLabel, fontSize = 10.sp, letterSpacing = 2.sp)
            Text(
                text = SessionCopy.setupTitle(state.language),
                color = LocalSessionColors.current.Ink,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
            )
            Text(
                text = SessionCopy.setupSubtitle(state.language),
                color = LocalSessionColors.current.MutedLabel,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 18.dp),
            )

            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                state.people.chunked(2).forEach { pair ->
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        pair.forEach { person ->
                            TabletPersonRow(
                                person = person,
                                onNameChange = { onRenamePerson(person.id, it) },
                                onRemove = { onRemovePerson(person.id) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (pair.size == 1) {
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                Text(
                    text = SessionCopy.addPerson(state.language),
                    color = LocalSessionColors.current.Accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LocalSessionColors.current.Accent, RoundedCornerShape(4.dp))
                        .clickable(onClick = onAddPerson)
                        .padding(vertical = 15.dp),
                )

                Text(
                    text = SessionCopy.depthPerCategory(state.language),
                    color = LocalSessionColors.current.MutedLabel,
                    fontSize = 10.sp,
                    letterSpacing = 1.8.sp,
                    modifier = Modifier.padding(top = 26.dp, bottom = 11.dp),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    state.depthPickers.forEach { depth ->
                        DepthPickerTile(
                            depth = depth,
                            onPick = { onPickLevel(depth.category, it) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(0.85f)
                .fillMaxHeight()
                .background(LocalSessionColors.current.PanelBackground)
                .padding(30.dp),
        ) {
            Text(
                text = SessionCopy.sessionClockCalculated(state.language),
                color = LocalSessionColors.current.MutedLabel,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 18.dp),
            )
            Text(text = state.mathLine, color = LocalSessionColors.current.Accent, fontSize = 14.sp)
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
                    .height(1.dp)
                    .background(LocalSessionColors.current.Border),
            )
            Text(
                text = state.totalLabel,
                color = LocalSessionColors.current.Ink,
                fontSize = 88.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = SessionCopy.stayVisibleCue(state.language),
                color = LocalSessionColors.current.MutedLabel,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
            )

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
                Text(
                    text = state.heldBackNote,
                    color = LocalSessionColors.current.Accent,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.fillMaxWidth().border(1.dp, LocalSessionColors.current.Border).padding(14.dp),
                )
                Text(
                    text = SessionCopy.noAnswersStored(state.language),
                    color = LocalSessionColors.current.Accent,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 9.dp)
                        .border(1.dp, LocalSessionColors.current.Border)
                        .padding(14.dp),
                )
                Button(
                    onClick = onStart,
                    enabled = state.canStart,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalSessionColors.current.Accent,
                        contentColor = LocalSessionColors.current.OnAccent,
                        disabledContainerColor = LocalSessionColors.current.Disabled,
                        disabledContentColor = LocalSessionColors.current.DisabledText,
                    ),
                    modifier = Modifier.fillMaxWidth().padding(top = 9.dp),
                ) {
                    Text(text = state.startLabel, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.6.sp)
                }
            }
        }
    }
}

@Composable
private fun TabletPersonRow(
    person: PersonRowUi,
    onNameChange: (String) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .border(1.dp, LocalSessionColors.current.Border)
            .background(LocalSessionColors.current.Background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${person.index}",
            color = LocalSessionColors.current.MutedLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(34.dp).padding(vertical = 15.dp),
        )
        BasicTextField(
            value = person.name,
            onValueChange = onNameChange,
            textStyle = TextStyle(color = LocalSessionColors.current.Ink, fontSize = 16.sp),
            modifier = Modifier.weight(1f).padding(vertical = 15.dp),
        )
        Text(
            text = "×",
            color = LocalSessionColors.current.MutedSecondary,
            fontSize = 18.sp,
            modifier = Modifier.clickable(onClick = onRemove).padding(7.dp),
        )
    }
}
