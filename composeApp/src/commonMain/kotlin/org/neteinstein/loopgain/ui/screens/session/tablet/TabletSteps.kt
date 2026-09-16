package org.neteinstein.loopgain.ui.screens.session.tablet

import org.neteinstein.loopgain.domain.model.SessionStage

/**
 * The six steps shown in the tablet's progress bar. Unlike the phone, the tablet has no dedicated
 * [SessionStage.READ] screen — [org.neteinstein.loopgain.ui.viewmodel.SessionViewModel.startWriteDirectly]
 * skips straight from drawing to writing — so that stage is never current here.
 */
val TABLET_STEP_STAGES: List<SessionStage> = listOf(
    SessionStage.SETUP,
    SessionStage.DRAW,
    SessionStage.WRITE,
    SessionStage.ROUNDS,
    SessionStage.REFLECT,
    SessionStage.DONE,
)

val TABLET_STEP_LABELS: List<String> = listOf("SETUP", "DRAW", "WRITE", "ROUNDS", "CLOSE", "LOGGED")

/**
 * Index of [stage] within [TABLET_STEP_STAGES], clamped to a safe default of 0 for a stage the
 * tablet never actually reaches (only [SessionStage.READ] today).
 */
fun tabletStepIndex(stage: SessionStage): Int {
    val index = TABLET_STEP_STAGES.indexOf(stage)
    return if (index < 0) 0 else index
}
