package org.neteinstein.loopgain.ui.viewmodel

import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.model.SessionTurnPhase

/**
 * UI copy for the session flow, authored in English and Portuguese. Kept separate from
 * [SessionUiState] so every string in the flow lives in one place — same rule as the deck content
 * itself: authored once, in both languages, never assembled ad hoc in a screen.
 *
 * Every function below checks for [Language.PT] specifically and falls back to English for
 * anything else — that covers [Language.EN] correctly and, deliberately, [Language.ES] /
 * [Language.FR] too: those are UI-selectable (see the Settings screen) but this copy and the deck
 * itself have no Spanish or French translation yet, so they read in English rather than silently
 * showing Portuguese.
 */
internal object SessionCopy {
    val dash = "—"

    fun about(category: CardCategory, language: Language): String = when (category) {
        CardCategory.MOTTO -> if (language == Language.PT) "lido em voz alta pelo facilitador" else "read aloud by the facilitator"
        CardCategory.PERSONAL_QUESTION -> if (language == Language.PT) "sobre ti próprio" else "about yourself"
        else -> if (language == Language.PT) "sobre cada um dos outros" else "about every other person"
    }

    fun mathLine(config: SessionConfig, people: Int, language: Language): String =
        "${config.minutesPerPerson} min × $people + ${config.overheadMinutes}"

    fun addNames(language: Language): String =
        if (language == Language.PT) "Adiciona pelo menos dois nomes" else "Add at least two names"

    fun startLabel(language: Language): String =
        if (language == Language.PT) "INICIAR SESSÃO" else "START SESSION"

    fun addNamesToStart(language: Language): String =
        if (language == Language.PT) "ADICIONA NOMES PARA COMEÇAR" else "ADD NAMES TO START"

    fun readFoot(index: Int, total: Int, language: Language): String {
        val shown = (index.coerceAtMost(3) + 1)
        val of = total.coerceAtLeast(1)
        return if (language == Language.PT) {
            "Carta $shown de $of · o telemóvel fica na mesa"
        } else {
            "Card $shown of $of · phone stays on the table"
        }
    }

    fun writeArrow(language: Language): String = if (language == Language.PT) "ESCREVER →" else "WRITE →"
    fun nextCard(language: Language): String = if (language == Language.PT) "CARTA SEGUINTE" else "NEXT CARD"
    fun closing(language: Language): String = if (language == Language.PT) "A encerrar" else "Closing"

    fun turnPhaseLabel(phase: SessionTurnPhase, language: Language): String = when (phase) {
        SessionTurnPhase.FEEDBACK ->
            if (language == Language.PT) "OS RESTANTES PARTILHAM · SENTIDO DOS PONTEIROS" else "EVERYONE ELSE SHARES · CLOCKWISE"
        SessionTurnPhase.PERSONAL ->
            if (language == Language.PT) "VOLUNTÁRIO RESPONDE + PERGUNTA PESSOAL" else "VOLUNTEER RESPONDS + PERSONAL CARD"
    }

    fun turnNote(over: Boolean, minutesPerPerson: Int, language: Language): String = when {
        over && language == Language.PT -> "Acima do tempo previsto por pessoa — conclui"
        over -> "Over the per-person budget — wrap up"
        language == Language.PT -> "$minutesPerPerson min para esta pessoa"
        else -> "$minutesPerPerson min for this person"
    }

    fun done(language: Language): String = if (language == Language.PT) "FEITO" else "DONE"
    fun now(language: Language): String = if (language == Language.PT) "AGORA" else "NOW"

    fun orderFoot(turnIndex: Int, totalPeople: Int, language: Language): String {
        val n = (turnIndex + 1).coerceAtMost(totalPeople)
        return if (language == Language.PT) {
            val word = if (totalPeople == 1) "voluntário" else "voluntários"
            "$n de $totalPeople $word · ninguém é avaliado"
        } else {
            val word = if (totalPeople == 1) "volunteer" else "volunteers"
            "$n of $totalPeople $word · nobody is scored"
        }
    }

    fun respondsArrow(name: String, language: Language): String =
        if (language == Language.PT) "${name.uppercase()} RESPONDE →" else "${name.uppercase()} RESPONDS →"

    fun nextArrow(name: String, language: Language): String =
        if (language == Language.PT) "SEGUINTE: ${name.uppercase()}" else "NEXT: ${name.uppercase()}"

    fun toClosingArrow(language: Language): String =
        if (language == Language.PT) "PARA O FIM →" else "TO CLOSING →"

    fun quickReflections(language: Language): List<String> = if (language == Language.PT) {
        listOf("Mais pesada que o normal", "A melhor até agora", "Faltou tempo", "Precisava de mais aquecimento")
    } else {
        listOf("Heavier than usual", "Best one so far", "Ran short on time", "Needed more warm-up")
    }

    fun doneSummary(people: Int, used: String, planned: String, volunteers: Int, language: Language): String =
        if (language == Language.PT) {
            val word = if (people == 1) "pessoa" else "pessoas"
            val volWord = if (volunteers == 1) "voluntário" else "voluntários"
            "$people $word · $used de $planned usados · $volunteers $volWord"
        } else {
            val word = if (people == 1) "person" else "people"
            val volWord = if (volunteers == 1) "volunteer" else "volunteers"
            "$people $word · $used of $planned used · $volunteers $volWord"
        }

    fun heldBackNote(language: Language): String = if (language == Language.PT) {
        "Estas quatro juntam-se ao histórico recente. Doze cartas por categoria significam um ano inteiro sem repetições."
    } else {
        "These four join the recent pile. Twelve cards per category means a full year before anything repeats."
    }

    fun hint(stage: SessionStage, config: SessionConfig, language: Language): String? = when (stage) {
        SessionStage.SETUP -> if (language == Language.PT) {
            "Primeira vez? Junta toda a gente à mesa. O tempo é ${config.minutesPerPerson} minutos por pessoa mais ${config.overheadMinutes}."
        } else {
            "First time? Add everyone at the table. The clock is ${config.minutesPerPerson} minutes per person plus ${config.overheadMinutes}."
        }

        SessionStage.DRAW -> if (language == Language.PT) {
            "Uma carta por categoria. Cartas usadas nas últimas doze sessões ficam reservadas automaticamente."
        } else {
            "One card per category. Cards used in the last twelve sessions are held back automatically."
        }

        SessionStage.READ -> if (language == Language.PT) {
            "Lê a carta em voz alta — passa o telemóvel ou levanta-o, para que todos a leiam, não só tu."
        } else {
            "Read the card to the room — pass the phone round or hold it up, everyone reads it, not just you."
        }

        SessionStage.WRITE -> if (language == Language.PT) {
            "${config.writeMinutes} minutos em silêncio. Tudo é escrito em papel; nada é escrito aqui."
        } else {
            "${config.writeMinutes} silent minutes. Everything is written on paper; nothing is typed here."
        }

        SessionStage.ROUNDS -> if (language == Language.PT) {
            "Um voluntário de cada vez. Os restantes partilham, depois o voluntário responde."
        } else {
            "One volunteer at a time. Everyone else shares, then the volunteer responds."
        }

        SessionStage.REFLECT -> if (language == Language.PT) {
            "Uma linha sobre a sessão. Nenhuma resposta individual é guardada."
        } else {
            "One line about the session. No individual answers are ever stored."
        }

        SessionStage.DONE -> null
    }

    // --- Tablet-only additions below. Same rule: dynamic/interpolated copy lives here in both
    // languages; purely static section labels (e.g. "CLOCKWISE ORDER") stay as literals in the
    // tablet composables, matching the precedent already set by the phone screens above.

    fun levelLabel(level: CardLevel?, language: Language): String {
        if (level == null) return dash
        return if (language == Language.PT) "NÍVEL ${level.dots} DE 3" else "LEVEL ${level.dots} OF 3"
    }

    fun readAloud(language: Language): String =
        if (language == Language.PT) "LÊ ISTO EM VOZ ALTA" else "READ THIS TO THE ROOM"

    fun drawAnother(language: Language): String =
        if (language == Language.PT) "TIRAR OUTRA" else "DRAW ANOTHER"

    fun nextCategoryCard(categoryLabel: String, language: Language): String =
        if (language == Language.PT) {
            "SEGUINTE: CARTA ${categoryLabel.uppercase()} →"
        } else {
            "NEXT: ${categoryLabel.uppercase()} CARD →"
        }

    fun backToBoard(language: Language): String =
        if (language == Language.PT) "VOLTAR AO TABULEIRO →" else "BACK TO THE BOARD →"

    fun historyMeta(participants: Int, minutes: Int, language: Language): String {
        val word = if (language == Language.PT) {
            if (participants == 1) "pessoa" else "pessoas"
        } else {
            if (participants == 1) "person" else "people"
        }
        return "$participants $word · $minutes min"
    }

    fun noReflectionNote(language: Language): String =
        if (language == Language.PT) "Sem nota adicionada." else "No note added."

    fun noSessionsYet(language: Language): String =
        if (language == Language.PT) "Ainda não há sessões registadas." else "No sessions logged yet."

    // --- Screen titles/labels that were static English literals in the Compose screens until
    // Settings made the language toggle a real, user-facing choice rather than an internal-only
    // default. Same PT-special/English-fallback rule as everything above.

    fun stageLabel(stage: SessionStage, language: Language): String = when (stage) {
        SessionStage.SETUP -> if (language == Language.PT) "CONFIGURAÇÃO" else "SETUP"
        SessionStage.DRAW -> if (language == Language.PT) "TIRAR" else "DRAW"
        SessionStage.READ -> if (language == Language.PT) "LER EM VOZ ALTA" else "READ ALOUD"
        SessionStage.WRITE -> if (language == Language.PT) "ESCREVER" else "WRITE"
        SessionStage.ROUNDS -> if (language == Language.PT) "RONDAS" else "ROUNDS"
        SessionStage.REFLECT -> if (language == Language.PT) "FECHAR" else "CLOSING"
        SessionStage.DONE -> if (language == Language.PT) "REGISTADA" else "LOGGED"
    }

    fun setupTitle(language: Language): String =
        if (language == Language.PT) "Quem está à mesa?" else "Who is at the table?"

    fun setupSubtitle(language: Language): String = if (language == Language.PT) {
        "Escreve cada nome. A lista é a ordem em que o feedback é dado."
    } else {
        "Type each name. The list is the order feedback goes round."
    }

    fun addPerson(language: Language): String =
        if (language == Language.PT) "+ ADICIONAR PESSOA" else "+ ADD PERSON"

    fun depthPerCategory(language: Language): String =
        if (language == Language.PT) "PROFUNDIDADE, POR CATEGORIA" else "DEPTH, PER CATEGORY"

    fun drawTitle(language: Language): String =
        if (language == Language.PT) "Tira as quatro" else "Draw the four"

    fun drawnCountLabel(count: Int, language: Language): String = if (language == Language.PT) {
        "$count de 4 tiradas · toca numa pilha para tirar ou voltar a tirar"
    } else {
        "$count of 4 drawn · tap a pile to draw or redraw it"
    }

    fun redraw(language: Language): String = if (language == Language.PT) "VOLTAR A TIRAR" else "REDRAW"

    fun redrawAll(language: Language): String =
        if (language == Language.PT) "VOLTAR A TIRAR TODAS" else "REDRAW ALL"

    fun pileAvailabilityLabel(isDrawn: Boolean, available: Int, heldBack: Int, language: Language): String = when {
        isDrawn && language == Language.PT -> "tirada"
        isDrawn -> "drawn"
        language == Language.PT && heldBack > 0 -> "$available disponíveis · $heldBack reservadas"
        language == Language.PT -> "$available disponíveis"
        heldBack > 0 -> "$available ready · $heldBack held back"
        else -> "$available ready"
    }

    fun stepTwoDraw(language: Language): String =
        if (language == Language.PT) "PASSO 2 — TIRAR" else "STEP 2 — DRAW"

    fun drawnOfFourLabel(count: Int, language: Language): String =
        if (language == Language.PT) "$count de 4 tiradas" else "$count of 4 drawn"

    fun readOrDrawAllLabel(allDrawn: Boolean, language: Language): String = when {
        allDrawn && language == Language.PT -> "LÊ-AS EM VOZ ALTA →"
        allDrawn -> "READ THEM OUT →"
        language == Language.PT -> "TIRA AS QUATRO"
        else -> "DRAW ALL FOUR"
    }

    fun receivingFeedback(language: Language): String =
        if (language == Language.PT) "A RECEBER FEEDBACK" else "RECEIVING FEEDBACK"

    fun orderLabel(language: Language): String = if (language == Language.PT) "ORDEM" else "ORDER"

    fun fiveMinutesLeft(language: Language): String =
        if (language == Language.PT) "FALTAM CINCO MINUTOS" else "FIVE MINUTES LEFT"

    fun finishTurnNote(language: Language): String = if (language == Language.PT) {
        "Termina esta vez e decidam juntos: fechar, ou passar o resto para a próxima sessão."
    } else {
        "Finish this turn, then decide together: close, or carry the rest to next session."
    }

    fun closeLabel(language: Language): String = if (language == Language.PT) "FECHAR" else "CLOSE"

    fun howDidThatFeel(language: Language): String =
        if (language == Language.PT) "Como é que correu?" else "How did that feel?"

    fun sayItOutLoud(language: Language): String = if (language == Language.PT) {
        "Diz em voz alta primeiro. Depois uma linha para o registo — sobre a sessão, não sobre ninguém."
    } else {
        "Say it out loud first. Then one line for the record — about the session, not about anyone in it."
    }

    fun savedWithSession(language: Language): String = if (language == Language.PT) {
        "Guardado com a sessão: quatro códigos de cartas, a data, a duração, esta linha."
    } else {
        "Saved with the session: four card codes, the date, the length, this line."
    }

    fun logSessionButton(language: Language): String =
        if (language == Language.PT) "REGISTAR SESSÃO" else "LOG SESSION"

    fun everyoneWritesHeader(language: Language): String =
        if (language == Language.PT) "TODOS ESCREVEM — SÓ EM PAPEL" else "EVERYONE WRITES — PAPER ONLY"

    fun oneAnswerPerCard(language: Language): String = if (language == Language.PT) {
        "Uma resposta por carta sobre cada uma das outras pessoas. A carta pessoal é sobre ti."
    } else {
        "One answer per card about every other person. The personal card is about you."
    }

    fun everyoneIsDone(language: Language): String =
        if (language == Language.PT) "TODOS TERMINARAM →" else "EVERYONE IS DONE →"

    fun sessionLoggedTitle(language: Language): String =
        if (language == Language.PT) "Sessão registada" else "Session logged"

    fun heldBackNextTime(language: Language): String =
        if (language == Language.PT) "RESERVADAS PARA A PRÓXIMA" else "HELD BACK NEXT TIME"

    fun finishButton(language: Language): String = if (language == Language.PT) "TERMINAR" else "FINISH"

    // --- Tablet-only static copy.

    fun tapEachPileReadToRoom(language: Language): String =
        if (language == Language.PT) "Toca em cada pilha. Lê-a para a sala." else "Tap each pile. Read it to the room."

    fun readAloudThenWriteLabel(allDrawn: Boolean, language: Language): String = when {
        allDrawn && language == Language.PT -> "LÊ EM VOZ ALTA E DEPOIS ESCREVE →"
        allDrawn -> "READ ALOUD, THEN WRITE →"
        language == Language.PT -> "TIRA AS QUATRO PARA CONTINUAR"
        else -> "DRAW ALL FOUR TO CONTINUE"
    }

    fun drawnTapToReadAgain(language: Language): String =
        if (language == Language.PT) "tirada — toca para reler" else "drawn — tap to read again"

    fun tapToDraw(language: Language): String = if (language == Language.PT) "TOCA PARA TIRAR" else "TAP TO DRAW"

    fun sessionLog(language: Language): String = if (language == Language.PT) "REGISTO DE SESSÕES" else "SESSION LOG"

    fun cardDrawsByCategory(language: Language): String =
        if (language == Language.PT) "CARTAS TIRADAS POR CATEGORIA" else "CARD DRAWS BY CATEGORY"

    fun everySessionLogged(language: Language): String =
        if (language == Language.PT) "Todas as sessões registadas até agora." else "Every session logged so far."

    fun categoryFrequencyDescription(language: Language): String = if (language == Language.PT) {
        "Quantas cartas de cada categoria foram tiradas em todas as sessões registadas."
    } else {
        "How many of each category have been drawn across every logged session."
    }

    fun teamLevelOnlyNote(language: Language): String = if (language == Language.PT) {
        "Apenas ao nível da equipa — nunca mostra quem disse o quê."
    } else {
        "Team level only — this never shows who said what."
    }

    fun historyToggleLabel(showHistory: Boolean, language: Language): String = when {
        showHistory && language == Language.PT -> "FECHAR"
        showHistory -> "CLOSE"
        language == Language.PT -> "HISTÓRICO"
        else -> "HISTORY"
    }

    fun sessionNumberHeaderMeta(number: Int, peopleCount: Int, language: Language): String = if (language == Language.PT) {
        val word = if (peopleCount == 1) "PESSOA" else "PESSOAS"
        "SESSÃO $number · $peopleCount $word"
    } else {
        val word = if (peopleCount == 1) "PERSON" else "PEOPLE"
        "SESSION $number · $peopleCount $word"
    }

    fun sessionLogHeaderMeta(language: Language): String =
        if (language == Language.PT) "REGISTO DE SESSÕES · APENAS AO NÍVEL DA EQUIPA" else "SESSION LOG · TEAM LEVEL ONLY"

    fun clockwiseOrder(language: Language): String =
        if (language == Language.PT) "ORDEM (SENTIDO DOS PONTEIROS)" else "CLOCKWISE ORDER"

    fun thisTurn(language: Language): String = if (language == Language.PT) "ESTA VEZ" else "THIS TURN"

    fun nextUp(language: Language): String = if (language == Language.PT) "A SEGUIR" else "NEXT UP"

    fun skipToClosing(language: Language): String =
        if (language == Language.PT) "SALTAR PARA O FECHO" else "SKIP TO CLOSING"

    fun clockwiseOrderNote(language: Language): String = if (language == Language.PT) {
        "Todos partilham sobre o/a voluntário/a, um de cada vez — não é uma conversa. O/a voluntário/a responde no fim."
    } else {
        "Everyone shares about the volunteer, one at a time — not a conversation. The volunteer answers last."
    }

    fun stayVisibleCue(language: Language): String = if (language == Language.PT) {
        "Fica visível para toda a sala. Um aviso soa quando faltam cinco minutos."
    } else {
        "Stays visible to the whole room. A cue fires at five minutes left."
    }

    fun sessionClockCalculated(language: Language): String =
        if (language == Language.PT) "RELÓGIO DA SESSÃO — CALCULADO" else "SESSION CLOCK — CALCULATED"

    fun noAnswersStored(language: Language): String = if (language == Language.PT) {
        "Nenhuma resposta é guardada. Só que cartas foram tiradas, e uma nota da sessão."
    } else {
        "No answers are stored. Only which cards were drawn, and one session-level note."
    }

    fun stepOneSetup(language: Language): String =
        if (language == Language.PT) "PASSO 1 — CONFIGURAÇÃO" else "STEP 1 — SETUP"

    fun stepThreeEveryoneWrites(language: Language): String =
        if (language == Language.PT) "PASSO 3 — TODOS ESCREVEM" else "STEP 3 — EVERYONE WRITES"

    fun silentWriting(language: Language): String =
        if (language == Language.PT) "ESCRITA EM SILÊNCIO" else "SILENT WRITING"

    fun paperOnlyNote(language: Language): String = if (language == Language.PT) {
        "Só papel. Nada é escrito neste dispositivo, agora ou depois."
    } else {
        "Paper only. Nothing is typed into this device, now or later."
    }

    fun leftLabel(language: Language): String = if (language == Language.PT) "RESTANTES" else "LEFT"

    fun sessionSummary(language: Language): String =
        if (language == Language.PT) "RESUMO DA SESSÃO" else "SESSION SUMMARY"

    fun reflectionLabel(language: Language): String =
        if (language == Language.PT) "REFLEXÃO" else "REFLECTION"

    fun nextSessionHeldBackNote(language: Language): String = if (language == Language.PT) {
        "Na próxima sessão estes quatro códigos juntam-se à reserva. Nada do que foi dito na sala foi registado."
    } else {
        "Next session these four codes join the held-back pile. Nothing anyone said in the room was recorded."
    }
}
