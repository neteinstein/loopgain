package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.ui.components.DepthPickerTile
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.PersonRowUi
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

@Composable
fun SessionSetupScreen(
    state: SessionUiState,
    onAddPerson: () -> Unit,
    onRemovePerson: (Int) -> Unit,
    onRenamePerson: (Int, String) -> Unit,
    onPickLevel: (org.neteinstein.loopgain.domain.model.CardCategory, CardLevel) -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(
                text = SessionCopy.setupTitle(state.language),
                color = LocalSessionColors.current.Ink,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = SessionCopy.setupSubtitle(state.language),
                color = LocalSessionColors.current.MutedLabel,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 14.dp),
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                state.people.forEach { person ->
                    PersonRow(
                        person = person,
                        onNameChange = { onRenamePerson(person.id, it) },
                        onRemove = { onRemovePerson(person.id) },
                    )
                }
            }

            Text(
                text = SessionCopy.addPerson(state.language),
                color = LocalSessionColors.current.Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .border(1.dp, LocalSessionColors.current.Accent, RoundedCornerShape(4.dp))
                    .clickable(onClick = onAddPerson)
                    .padding(vertical = 13.dp),
            )

            Text(
                text = SessionCopy.depthPerCategory(state.language),
                color = LocalSessionColors.current.MutedLabel,
                fontSize = 10.sp,
                letterSpacing = 1.6.sp,
                modifier = Modifier.padding(top = 20.dp, bottom = 9.dp),
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.height(180.dp),
            ) {
                items(state.depthPickers.filter { it.hasLevels }) { depth ->
                    DepthPickerTile(depth = depth, onPick = { onPickLevel(depth.category, it) })
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalSessionColors.current.PanelBackground)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(text = state.mathLine, color = LocalSessionColors.current.MutedLabel, fontSize = 12.sp)
                Text(text = state.totalLabel, color = LocalSessionColors.current.Ink, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            ) {
                Text(text = state.startLabel, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
            }
        }
    }
}

@Composable
private fun PersonRow(person: PersonRowUi, onNameChange: (String) -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            modifier = Modifier
                .width(30.dp)
                .padding(vertical = 13.dp),
        )
        BasicTextField(
            value = person.name,
            onValueChange = onNameChange,
            textStyle = androidx.compose.ui.text.TextStyle(color = LocalSessionColors.current.Ink, fontSize = 15.sp),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 13.dp),
        )
        Text(
            text = "×",
            color = LocalSessionColors.current.MutedSecondary,
            fontSize = 17.sp,
            modifier = Modifier
                .clickable(onClick = onRemove)
                .padding(6.dp),
        )
    }
}
