package org.neteinstein.loopgain.ui.screens.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.ui.theme.LocalSessionColors
import org.neteinstein.loopgain.ui.viewmodel.SessionCopy
import org.neteinstein.loopgain.ui.viewmodel.SessionUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SessionReflectScreen(
    state: SessionUiState,
    onReflectionChange: (String) -> Unit,
    onQuickPick: (String) -> Unit,
    onLog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = SessionCopy.stageLabel(SessionStage.REFLECT, state.language), color = LocalSessionColors.current.MutedLabel, fontSize = 10.sp, letterSpacing = 1.8.sp)
            Text(
                text = SessionCopy.howDidThatFeel(state.language),
                color = LocalSessionColors.current.Ink,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
            )
            Text(
                text = SessionCopy.sayItOutLoud(state.language),
                color = LocalSessionColors.current.Accent,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 14.dp),
            )
            BasicTextField(
                value = state.reflection,
                onValueChange = onReflectionChange,
                textStyle = TextStyle(color = LocalSessionColors.current.Ink, fontSize = 15.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LocalSessionColors.current.Accent)
                    .padding(15.dp),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                state.quickReflections.forEach { quick ->
                    Text(
                        text = quick,
                        color = LocalSessionColors.current.Accent,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .border(1.dp, LocalSessionColors.current.Border)
                            .clickable { onQuickPick(quick) }
                            .padding(horizontal = 11.dp, vertical = 9.dp),
                    )
                }
            }
            Text(
                text = SessionCopy.savedWithSession(state.language),
                color = LocalSessionColors.current.MutedLabel,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
        Button(
            onClick = onLog,
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LocalSessionColors.current.Accent, contentColor = LocalSessionColors.current.OnAccent),
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalSessionColors.current.PanelBackground)
                .padding(16.dp),
        ) {
            Text(text = SessionCopy.logSessionButton(state.language), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.4.sp)
        }
    }
}
