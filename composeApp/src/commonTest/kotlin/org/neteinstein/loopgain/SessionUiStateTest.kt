package org.neteinstein.loopgain

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.neteinstein.loopgain.data.repository.DefaultCardRepository
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.session.SessionEngine
import org.neteinstein.loopgain.domain.session.SessionState
import org.neteinstein.loopgain.ui.viewmodel.toUiState

class SessionUiStateTest {

    private val repository = DefaultCardRepository()

    private fun readyState(engine: SessionEngine): SessionState {
        var s = SessionState()
        s = engine.renamePerson(s, s.people[0].id, "Alice")
        s = engine.renamePerson(s, s.people[1].id, "Bob")
        return s
    }

    @Test
    fun notReadyShowsAddNamesCopy() {
        val config = SessionConfig()
        val ui = SessionState().toUiState(config, Language.EN, repository)
        assertFalse(ui.canStart)
        assertEquals("—", ui.totalLabel)
        assertEquals("ADD NAMES TO START", ui.startLabel)
    }

    @Test
    fun readyStateComputesTheDeckMath() {
        val config = SessionConfig()
        val engine = SessionEngine(repository, config)
        val ui = readyState(engine).toUiState(config, Language.EN, repository)
        assertTrue(ui.canStart)
        assertEquals("30:00", ui.totalLabel) // 10 min x 2 people + 10
    }

    @Test
    fun portugueseTogglesCopyAndCategoryNames() {
        val config = SessionConfig()
        val ui = SessionState().toUiState(config, Language.PT, repository)
        assertEquals("ADICIONA NOMES PARA COMEÇAR", ui.startLabel)
        assertEquals("Mote", ui.depthPickers.first { it.category == CardCategory.MOTTO }.label)
    }

    @Test
    fun spanishAndFrenchFallBackToEnglishContent() {
        val config = SessionConfig()
        val es = SessionState().toUiState(config, Language.ES, repository)
        val fr = SessionState().toUiState(config, Language.FR, repository)
        assertEquals("ADD NAMES TO START", es.startLabel)
        assertEquals("ADD NAMES TO START", fr.startLabel)
    }

    @Test
    fun mottoDepthPickerHasNoLevels() {
        val config = SessionConfig()
        val ui = SessionState().toUiState(config, Language.EN, repository)
        val motto = ui.depthPickers.first { it.category == CardCategory.MOTTO }
        assertFalse(motto.hasLevels)
        assertTrue(motto.levels.isEmpty())
        assertNull(motto.selectedLevel)
    }

    @Test
    fun negativeTurnClockRendersWithAMinusSign() {
        val config = SessionConfig()
        val engine = SessionEngine(repository, config)
        var s = engine.startRounds(readyState(engine)).copy(turnSecondsLeft = -5)
        val ui = s.toUiState(config, Language.EN, repository)
        assertEquals("−0:05", ui.turnClock)
        assertTrue(ui.turnOver)
    }

    @Test
    fun dismissedHintIsHiddenForItsStage() {
        val config = SessionConfig()
        val engine = SessionEngine(repository, config)
        var s = SessionState()
        assertEquals(SessionStage.SETUP, s.stage)
        val before = s.toUiState(config, Language.EN, repository)
        assertTrue(before.hint != null)

        s = engine.dismissHint(s, s.stage)
        val after = s.toUiState(config, Language.EN, repository)
        assertNull(after.hint)
    }

    @Test
    fun cardTextBlanksThePlaceholderUnderscoreForEveryConsumer() {
        // Every screen (read, write/rounds/done previews, the tablet's reveal overlay) renders
        // CardFaceUi.text as-is — the "_" placeholder must already be a visible blank run here,
        // not a bare underscore a screen might forget to transform.
        val config = SessionConfig()
        val positiveCard = repository.byCategory(CardCategory.POSITIVE_REINFORCEMENT).first { it.id == "positive_reinforcement_01" }
        assertTrue(positiveCard.en.contains("_")) // sanity check on the fixture itself

        val s = SessionState(drawn = mapOf(CardCategory.POSITIVE_REINFORCEMENT to positiveCard))
        val card = s.toUiState(config, Language.EN, repository).drawnCards.single()
        assertTrue(card.text.contains("_____"), "expected a blanked run in: ${card.text}")
        assertFalse(Regex("(?<!_)_(?!_)").containsMatchIn(card.text), "found a bare underscore in: ${card.text}")
    }

    @Test
    fun drawScreenReflectsWhetherAllFourAreDrawn() {
        val config = SessionConfig()
        val engine = SessionEngine(repository, config)
        var s = engine.startSession(readyState(engine), heldBackIds = emptySet(), random = Random(7))
        val ui = s.toUiState(config, Language.EN, repository)
        assertEquals(4, ui.drawnCount)
        assertTrue(ui.canProceedFromDraw)
        assertEquals(4, ui.piles.size)
        assertTrue(ui.piles.all { it.isDrawn })
    }
}
