package org.neteinstein.loopgain.ui.viewmodel

import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.CardLevel
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.model.SessionTurnPhase

/**
 * Bilingual UI copy for the session flow. Kept separate from [SessionUiState] so every string in
 * the flow lives in one place — same rule as the deck content itself: authored once, in both
 * languages, never assembled ad hoc in a screen.
 */
internal object SessionCopy {
    val dash = "—"

    fun about(category: CardCategory, language: Language): String = when (category) {
        CardCategory.MOTTO -> if (language == Language.EN) "read aloud by the facilitator" else "lido em voz alta pelo facilitador"
        CardCategory.PERSONAL_QUESTION -> if (language == Language.EN) "about yourself" else "sobre ti próprio"
        else -> if (language == Language.EN) "about every other person" else "sobre cada um dos outros"
    }

    fun mathLine(config: SessionConfig, people: Int, language: Language): String =
        if (language == Language.EN) {
            "${config.minutesPerPerson} min × $people + ${config.overheadMinutes}"
        } else {
            "${config.minutesPerPerson} min × $people + ${config.overheadMinutes}"
        }

    fun addNames(language: Language): String =
        if (language == Language.EN) "Add at least two names" else "Adiciona pelo menos dois nomes"

    fun startLabel(language: Language): String =
        if (language == Language.EN) "START SESSION" else "INICIAR SESSÃO"

    fun addNamesToStart(language: Language): String =
        if (language == Language.EN) "ADD NAMES TO START" else "ADICIONA NOMES PARA COMEÇAR"

    fun readFoot(index: Int, total: Int, language: Language): String {
        val shown = (index.coerceAtMost(3) + 1)
        val of = total.coerceAtLeast(1)
        return if (language == Language.EN) {
            "Card $shown of $of · phone stays on the table"
        } else {
            "Carta $shown de $of · o telemóvel fica na mesa"
        }
    }

    fun writeArrow(language: Language): String = if (language == Language.EN) "WRITE →" else "ESCREVER →"
    fun nextCard(language: Language): String = if (language == Language.EN) "NEXT CARD" else "CARTA SEGUINTE"
    fun closing(language: Language): String = if (language == Language.EN) "Closing" else "A encerrar"

    fun turnPhaseLabel(phase: SessionTurnPhase, language: Language): String = when (phase) {
        SessionTurnPhase.FEEDBACK ->
            if (language == Language.EN) "EVERYONE ELSE SHARES · CLOCKWISE" else "OS RESTANTES PARTILHAM · SENTIDO DOS PONTEIROS"
        SessionTurnPhase.PERSONAL ->
            if (language == Language.EN) "VOLUNTEER RESPONDS + PERSONAL CARD" else "VOLUNTÁRIO RESPONDE + PERGUNTA PESSOAL"
    }

    fun turnNote(over: Boolean, minutesPerPerson: Int, language: Language): String = when {
        over && language == Language.EN -> "Over the per-person budget — wrap up"
        over -> "Acima do tempo previsto por pessoa — conclui"
        language == Language.EN -> "$minutesPerPerson min for this person"
        else -> "$minutesPerPerson min para esta pessoa"
    }

    fun done(language: Language): String = if (language == Language.EN) "DONE" else "FEITO"
    fun now(language: Language): String = if (language == Language.EN) "NOW" else "AGORA"

    fun orderFoot(turnIndex: Int, totalPeople: Int, language: Language): String {
        val n = (turnIndex + 1).coerceAtMost(totalPeople)
        return if (language == Language.EN) {
            val word = if (totalPeople == 1) "volunteer" else "volunteers"
            "$n of $totalPeople $word · nobody is scored"
        } else {
            val word = if (totalPeople == 1) "voluntário" else "voluntários"
            "$n de $totalPeople $word · ninguém é avaliado"
        }
    }

    fun respondsArrow(name: String, language: Language): String =
        if (language == Language.EN) "${name.uppercase()} RESPONDS →" else "${name.uppercase()} RESPONDE →"

    fun nextArrow(name: String, language: Language): String =
        if (language == Language.EN) "NEXT: ${name.uppercase()}" else "SEGUINTE: ${name.uppercase()}"

    fun toClosingArrow(language: Language): String =
        if (language == Language.EN) "TO CLOSING →" else "PARA O FIM →"

    fun quickReflections(language: Language): List<String> = if (language == Language.EN) {
        listOf("Heavier than usual", "Best one so far", "Ran short on time", "Needed more warm-up")
    } else {
        listOf("Mais pesada que o normal", "A melhor até agora", "Faltou tempo", "Precisava de mais aquecimento")
    }

    fun doneSummary(people: Int, used: String, planned: String, volunteers: Int, language: Language): String =
        if (language == Language.EN) {
            val word = if (people == 1) "person" else "people"
            val volWord = if (volunteers == 1) "volunteer" else "volunteers"
            "$people $word · $used of $planned used · $volunteers $volWord"
        } else {
            val word = if (people == 1) "pessoa" else "pessoas"
            val volWord = if (volunteers == 1) "voluntário" else "voluntários"
            "$people $word · $used de $planned usados · $volunteers $volWord"
        }

    fun heldBackNote(language: Language): String = if (language == Language.EN) {
        "These four join the recent pile. Twelve cards per category means a full year before anything repeats."
    } else {
        "Estas quatro juntam-se ao histórico recente. Doze cartas por categoria significam um ano inteiro sem repetições."
    }

    fun hint(stage: SessionStage, config: SessionConfig, language: Language): String? = when (stage) {
        SessionStage.SETUP -> if (language == Language.EN) {
            "First time? Add everyone at the table. The clock is ${config.minutesPerPerson} minutes per person plus ${config.overheadMinutes}."
        } else {
            "Primeira vez? Junta toda a gente à mesa. O tempo é ${config.minutesPerPerson} minutos por pessoa mais ${config.overheadMinutes}."
        }

        SessionStage.DRAW -> if (language == Language.EN) {
            "One card per category. Cards used in the last twelve sessions are held back automatically."
        } else {
            "Uma carta por categoria. Cartas usadas nas últimas doze sessões ficam reservadas automaticamente."
        }

        SessionStage.READ -> if (language == Language.EN) {
            "Read the card to the room — pass the phone round or hold it up, everyone reads it, not just you."
        } else {
            "Lê a carta em voz alta — passa o telemóvel ou levanta-o, para que todos a leiam, não só tu."
        }

        SessionStage.WRITE -> if (language == Language.EN) {
            "${config.writeMinutes} silent minutes. Everything is written on paper; nothing is typed here."
        } else {
            "${config.writeMinutes} minutos em silêncio. Tudo é escrito em papel; nada é escrito aqui."
        }

        SessionStage.ROUNDS -> if (language == Language.EN) {
            "One volunteer at a time. Everyone else shares, then the volunteer responds."
        } else {
            "Um voluntário de cada vez. Os restantes partilham, depois o voluntário responde."
        }

        SessionStage.REFLECT -> if (language == Language.EN) {
            "One line about the session. No individual answers are ever stored."
        } else {
            "Uma linha sobre a sessão. Nenhuma resposta individual é guardada."
        }

        SessionStage.DONE -> null
    }

    // --- Tablet-only additions below. Same rule: dynamic/interpolated copy lives here in both
    // languages; purely static section labels (e.g. "CLOCKWISE ORDER") stay as literals in the
    // tablet composables, matching the precedent already set by the phone screens above.

    fun levelLabel(level: CardLevel?, language: Language): String {
        if (level == null) return dash
        return if (language == Language.EN) "LEVEL ${level.dots} OF 3" else "NÍVEL ${level.dots} DE 3"
    }

    fun readAloud(language: Language): String =
        if (language == Language.EN) "READ THIS TO THE ROOM" else "LÊ ISTO EM VOZ ALTA"

    fun drawAnother(language: Language): String =
        if (language == Language.EN) "DRAW ANOTHER" else "TIRAR OUTRA"

    fun nextCategoryCard(categoryLabel: String, language: Language): String =
        if (language == Language.EN) {
            "NEXT: ${categoryLabel.uppercase()} CARD →"
        } else {
            "SEGUINTE: CARTA ${categoryLabel.uppercase()} →"
        }

    fun backToBoard(language: Language): String =
        if (language == Language.EN) "BACK TO THE BOARD →" else "VOLTAR AO TABULEIRO →"

    fun historyMeta(participants: Int, minutes: Int, language: Language): String =
        if (language == Language.EN) {
            val word = if (participants == 1) "person" else "people"
            "$participants $word · $minutes min"
        } else {
            val word = if (participants == 1) "pessoa" else "pessoas"
            "$participants $word · $minutes min"
        }

    fun noReflectionNote(language: Language): String =
        if (language == Language.EN) "No note added." else "Sem nota adicionada."

    fun noSessionsYet(language: Language): String =
        if (language == Language.EN) "No sessions logged yet." else "Ainda não há sessões registadas."
}
