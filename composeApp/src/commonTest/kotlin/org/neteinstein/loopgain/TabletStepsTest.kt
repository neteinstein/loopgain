package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.ui.screens.session.tablet.TABLET_STEP_LABELS
import org.neteinstein.loopgain.ui.screens.session.tablet.TABLET_STEP_STAGES
import org.neteinstein.loopgain.ui.screens.session.tablet.tabletStepIndex

class TabletStepsTest {

    @Test
    fun stagesAndLabelsLineUpOneToOne() {
        assertEquals(TABLET_STEP_STAGES.size, TABLET_STEP_LABELS.size)
        assertEquals(6, TABLET_STEP_STAGES.size)
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
