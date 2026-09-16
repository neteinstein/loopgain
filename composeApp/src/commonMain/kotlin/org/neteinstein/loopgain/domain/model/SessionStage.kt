package org.neteinstein.loopgain.domain.model

/**
 * The phases a live session moves through. Shared by every layout — a layout may present [READ]
 * as its own screen (phone) or fold it into an overlay on top of [DRAW] (tablet); either way the
 * underlying stage transitions here are the same.
 */
enum class SessionStage {
    SETUP,
    DRAW,
    READ,
    WRITE,
    ROUNDS,
    REFLECT,
    DONE,
}

/** Within [SessionStage.ROUNDS], whose cards are being read for the current volunteer. */
enum class SessionTurnPhase {
    /** Everyone else shares Positive Reinforcement + Improvements about the volunteer. */
    FEEDBACK,

    /** The volunteer responds, then answers their Personal Question (with Motto as backdrop). */
    PERSONAL,
}
