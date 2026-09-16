package org.neteinstein.loopgain.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neteinstein.loopgain.data.repository.CardRepository
import org.neteinstein.loopgain.data.repository.SessionHistoryRepository
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionHistoryEntry
import org.neteinstein.loopgain.domain.session.SessionEngine
import org.neteinstein.loopgain.domain.session.SessionState

/**
 * Shared session view model: one instance of this drives the phone's screen-per-stage flow and
 * the tablet's facilitator board equally. It owns [SessionEngine] (the pure state machine),
 * a one-second timer, and exposes a formatted [SessionUiState] so screens do no logic of their
 * own beyond layout.
 */
class SessionViewModel(
    private val cardRepository: CardRepository,
    private val sessionHistoryRepository: SessionHistoryRepository,
    private val config: SessionConfig = SessionConfig(),
) : ViewModel() {

    private val engine = SessionEngine(cardRepository, config)

    private val state = MutableStateFlow(SessionState())

    val uiState: StateFlow<SessionUiState> = state
        .map { it.toUiState(config, cardRepository) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, state.value.toUiState(config, cardRepository))

    init {
        viewModelScope.launch {
            while (true) {
                delay(1_000)
                state.update { engine.tick(it, deltaSeconds = 1) }
            }
        }
    }

    fun addPerson() = state.update { engine.addPerson(it) }
    fun removePerson(id: Int) = state.update { engine.removePerson(it, id) }
    fun renamePerson(id: Int, name: String) = state.update { engine.renamePerson(it, id, name) }
    fun setLevel(category: CardCategory, level: CardLevel) = state.update { engine.setLevel(it, category, level) }

    fun startSession() = state.update {
        engine.startSession(it, heldBackIds = sessionHistoryRepository.recentlyUsedCardIds())
    }

    fun drawOne(category: CardCategory) = state.update { engine.drawOne(it, category) }
    fun redrawAll() = state.update { engine.drawAll(it) }

    fun enterRead() = state.update { engine.enterRead(it) }
    fun previousReadCard() = state.update { engine.previousReadCard(it) }
    fun advanceRead() = state.update { engine.advanceRead(it) }

    /** Tablet-style shortcut: skip the dedicated read stage and go straight to writing. */
    fun startWriteDirectly() = state.update { engine.startWrite(it) }

    fun startRounds() = state.update { engine.startRounds(it) }
    fun advanceRounds() = state.update { engine.advanceRounds(it) }
    fun skipToReflect() = state.update { engine.skipToReflect(it) }

    fun setReflection(text: String) = state.update { engine.setReflection(it, text) }

    fun logSession() {
        val current = state.value
        val total = config.suggestedMinutes(current.namedPeople.size.coerceAtLeast(1)) * 60
        sessionHistoryRepository.record(
            SessionHistoryEntry(
                sessionNumber = sessionHistoryRepository.nextSessionNumber(),
                loggedAtEpochMillis = 0L,
                participantCount = current.namedPeople.size,
                plannedSeconds = total,
                usedSeconds = total - current.sessionSecondsLeft,
                drawnCardIds = current.drawnInOrder.map { it.id },
                reflectionNote = current.reflection,
            ),
        )
        state.update { engine.logSession(it) }
    }

    fun resetSession() = state.update { engine.reset(it) }

    fun dismissHint() = state.update { engine.dismissHint(it, it.stage) }
}
