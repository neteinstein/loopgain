package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.ui.screens.session.tablet.TABLET_STEP_STAGES
import org.neteinstein.loopgain.ui.screens.session.tablet.tabletStepIndex
import org.neteinstein.loopgain.ui.screens.session.tablet.tabletStepLabels

class TabletStepsTest {

    @Test
    fun stagesAndLabelsLineUpOneToOne() {
        assertEquals(TABLET_STEP_STAGES.size, tabletStepLabels(Language.EN).size)
        assertEquals(6, TABLET_STEP_STAGES.size)
    }

    @Test
    fun labelsAreTranslatedPerLanguage() {
        assertEquals(listOf("SETUP", "DRAW", "WRITE", "ROUNDS", "CLOSING", "LOGGED"), tabletStepLabels(Language.EN))
        assertEquals(
            listOf("CONFIGURAÇÃO", "TIRAR", "ESCREVER", "RONDAS", "FECHAR", "REGISTADA"),
            tabletStepLabels(Language.PT),
        )
    }

    @Test
    fun indexMatchesEachStagesPositionInTheProgressBar() {
        assertEquals(0, tabletStepIndex(SessionStage.SETUP))
        assertEquals(1, tabletStepIndex(SessionStage.DRAW))
        assertEquals(2, tabletStepIndex(SessionStage.WRITE))
        assertEquals(3, tabletStepIndex(SessionStage.ROUNDS))
        assertEquals(4, tabletStepIndex(SessionStage.REFLECT))
        assertEquals(5, tabletStepIndex(SessionStage.DONE))
    }

    @Test
    fun readStageFallsBackToZeroSinceTheTabletNeverEntersIt() {
        assertEquals(0, tabletStepIndex(SessionStage.READ))
    }
}
