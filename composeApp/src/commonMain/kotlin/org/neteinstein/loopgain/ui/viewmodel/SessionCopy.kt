package org.neteinstein.loopgain.ui.viewmodel

import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.Language
import org.neteinstein.loopgain.domain.model.SessionConfig
import org.neteinstein.loopgain.domain.model.SessionStage
import org.neteinstein.loopgain.domain.model.SessionTurnPhase

/**
 * UI copy for the session flow, authored in English, Portuguese, Spanish and French. Kept
 * separate from [SessionUiState] so every string in the flow lives in one place — same rule as
 * the deck content itself: authored once, in every language, never assembled ad hoc in a screen.
 */
internal object SessionCopy {
    val dash = "—"

    fun about(category: CardCategory, language: Language): String = when (category) {
        CardCategory.MOTTO -> when (language) {
            Language.EN -> "read aloud by the facilitator"
            Language.PT -> "lido em voz alta pelo facilitador"
            Language.ES -> "leído en voz alta por el facilitador"
            Language.FR -> "lu à voix haute par le facilitateur"
        }
        CardCategory.PERSONAL_QUESTION -> when (language) {
            Language.EN -> "about yourself"
            Language.PT -> "sobre ti próprio"
            Language.ES -> "sobre ti mismo"
            Language.FR -> "sur toi-même"
        }
        else -> when (language) {
            Language.EN -> "about every other person"
            Language.PT -> "sobre cada um dos outros"
            Language.ES -> "sobre cada una de las otras personas"
            Language.FR -> "sur chacune des autres personnes"
        }
    }

    fun mathLine(config: SessionConfig, people: Int, language: Language): String =
        "${config.minutesPerPerson} min × $people + ${config.overheadMinutes}"

    fun addNames(language: Language): String = when (language) {
        Language.EN -> "Add at least two names"
        Language.PT -> "Adiciona pelo menos dois nomes"
        Language.ES -> "Añade al menos dos nombres"
        Language.FR -> "Ajoute au moins deux noms"
    }

    fun startLabel(language: Language): String = when (language) {
        Language.EN -> "START SESSION"
        Language.PT -> "INICIAR SESSÃO"
        Language.ES -> "INICIAR SESIÓN"
        Language.FR -> "DÉMARRER LA SESSION"
    }

    fun addNamesToStart(language: Language): String = when (language) {
        Language.EN -> "ADD NAMES TO START"
        Language.PT -> "ADICIONA NOMES PARA COMEÇAR"
        Language.ES -> "AÑADE NOMBRES PARA EMPEZAR"
        Language.FR -> "AJOUTE DES NOMS POUR COMMENCER"
    }

    fun readFoot(index: Int, total: Int, language: Language): String {
        val shown = (index.coerceAtMost(3) + 1)
        val of = total.coerceAtLeast(1)
        return when (language) {
            Language.EN -> "Card $shown of $of · phone stays on the table"
            Language.PT -> "Carta $shown de $of · o telemóvel fica na mesa"
            Language.ES -> "Carta $shown de $of · el móvil se queda en la mesa"
            Language.FR -> "Carte $shown sur $of · le téléphone reste sur la table"
        }
    }

    fun writeArrow(language: Language): String = when (language) {
        Language.EN -> "WRITE →"
        Language.PT -> "ESCREVER →"
        Language.ES -> "ESCRIBIR →"
        Language.FR -> "ÉCRIRE →"
    }

    fun nextCard(language: Language): String = when (language) {
        Language.EN -> "NEXT CARD"
        Language.PT -> "CARTA SEGUINTE"
        Language.ES -> "SIGUIENTE CARTA"
        Language.FR -> "CARTE SUIVANTE"
    }

    fun closing(language: Language): String = when (language) {
        Language.EN -> "Closing"
        Language.PT -> "A encerrar"
        Language.ES -> "Cerrando"
        Language.FR -> "Clôture"
    }

    fun turnPhaseLabel(phase: SessionTurnPhase, language: Language): String = when (phase) {
        SessionTurnPhase.FEEDBACK -> when (language) {
            Language.EN -> "EVERYONE ELSE SHARES · CLOCKWISE"
            Language.PT -> "OS RESTANTES PARTILHAM · SENTIDO DOS PONTEIROS"
            Language.ES -> "LOS DEMÁS COMPARTEN · EN SENTIDO HORARIO"
            Language.FR -> "LES AUTRES PARTAGENT · DANS LE SENS HORAIRE"
        }
        SessionTurnPhase.PERSONAL -> when (language) {
            Language.EN -> "VOLUNTEER RESPONDS + PERSONAL CARD"
            Language.PT -> "VOLUNTÁRIO RESPONDE + PERGUNTA PESSOAL"
            Language.ES -> "EL VOLUNTARIO RESPONDE + CARTA PERSONAL"
            Language.FR -> "LE VOLONTAIRE RÉPOND + CARTE PERSONNELLE"
        }
    }

    fun turnNote(over: Boolean, minutesPerPerson: Int, language: Language): String = when {
        over -> when (language) {
            Language.EN -> "Over the per-person budget — wrap up"
            Language.PT -> "Acima do tempo previsto por pessoa — conclui"
            Language.ES -> "Fuera del tiempo previsto por persona — termina"
            Language.FR -> "Dépassement du temps prévu par personne — termine"
        }
        else -> when (language) {
            Language.EN -> "$minutesPerPerson min for this person"
            Language.PT -> "$minutesPerPerson min para esta pessoa"
            Language.ES -> "$minutesPerPerson min para esta persona"
            Language.FR -> "$minutesPerPerson min pour cette personne"
        }
    }

    fun done(language: Language): String = when (language) {
        Language.EN -> "DONE"
        Language.PT -> "FEITO"
        Language.ES -> "HECHO"
        Language.FR -> "TERMINÉ"
    }

    fun now(language: Language): String = when (language) {
        Language.EN -> "NOW"
        Language.PT -> "AGORA"
        Language.ES -> "AHORA"
        Language.FR -> "MAINTENANT"
    }

    fun orderFoot(turnIndex: Int, totalPeople: Int, language: Language): String {
        val n = (turnIndex + 1).coerceAtMost(totalPeople)
        return when (language) {
            Language.EN -> {
                val word = if (totalPeople == 1) "volunteer" else "volunteers"
                "$n of $totalPeople $word · nobody is scored"
            }
            Language.PT -> {
                val word = if (totalPeople == 1) "voluntário" else "voluntários"
                "$n de $totalPeople $word · ninguém é avaliado"
            }
            Language.ES -> {
                val word = if (totalPeople == 1) "voluntario" else "voluntarios"
                "$n de $totalPeople $word · nadie es evaluado"
            }
            Language.FR -> {
                val word = if (totalPeople == 1) "volontaire" else "volontaires"
                "$n sur $totalPeople $word · personne n'est noté"
            }
        }
    }

    fun respondsArrow(name: String, language: Language): String = when (language) {
        Language.EN -> "${name.uppercase()} RESPONDS →"
        Language.PT -> "${name.uppercase()} RESPONDE →"
        Language.ES -> "${name.uppercase()} RESPONDE →"
        Language.FR -> "${name.uppercase()} RÉPOND →"
    }

    fun nextArrow(name: String, language: Language): String = when (language) {
        Language.EN -> "NEXT: ${name.uppercase()}"
        Language.PT -> "SEGUINTE: ${name.uppercase()}"
        Language.ES -> "SIGUIENTE: ${name.uppercase()}"
        Language.FR -> "SUIVANT : ${name.uppercase()}"
    }

    fun toClosingArrow(language: Language): String = when (language) {
        Language.EN -> "TO CLOSING →"
        Language.PT -> "PARA O FIM →"
        Language.ES -> "AL CIERRE →"
        Language.FR -> "VERS LA CLÔTURE →"
    }

    fun quickReflections(language: Language): List<String> = when (language) {
        Language.EN -> listOf("Heavier than usual", "Best one so far", "Ran short on time", "Needed more warm-up")
        Language.PT -> listOf("Mais pesada que o normal", "A melhor até agora", "Faltou tempo", "Precisava de mais aquecimento")
        Language.ES -> listOf("Más intensa de lo normal", "La mejor hasta ahora", "Faltó tiempo", "Necesitaba más calentamiento")
        Language.FR -> listOf("Plus lourde que d'habitude", "La meilleure jusqu'à présent", "Il a manqué de temps", "Avait besoin de plus d'échauffement")
    }

    fun doneSummary(people: Int, used: String, planned: String, volunteers: Int, language: Language): String =
        when (language) {
            Language.EN -> {
                val word = if (people == 1) "person" else "people"
                val volWord = if (volunteers == 1) "volunteer" else "volunteers"
                "$people $word · $used of $planned used · $volunteers $volWord"
            }
            Language.PT -> {
                val word = if (people == 1) "pessoa" else "pessoas"
                val volWord = if (volunteers == 1) "voluntário" else "voluntários"
                "$people $word · $used de $planned usados · $volunteers $volWord"
            }
            Language.ES -> {
                val word = if (people == 1) "persona" else "personas"
                val volWord = if (volunteers == 1) "voluntario" else "voluntarios"
                "$people $word · $used de $planned utilizados · $volunteers $volWord"
            }
            Language.FR -> {
                val word = if (people == 1) "personne" else "personnes"
                val volWord = if (volunteers == 1) "volontaire" else "volontaires"
                "$people $word · $used sur $planned utilisées · $volunteers $volWord"
            }
        }

    fun heldBackNote(language: Language): String = when (language) {
        Language.EN -> "These four join the recent pile. Twelve cards per category means a full year before anything repeats."
        Language.PT -> "Estas quatro juntam-se ao histórico recente. Doze cartas por categoria significam um ano inteiro sem repetições."
        Language.ES -> "Estas cuatro se unen al historial reciente. Doce cartas por categoría significan un año entero sin repeticiones."
        Language.FR -> "Ces quatre rejoignent l'historique récent. Douze cartes par catégorie signifient une année entière sans répétition."
    }

    fun hint(stage: SessionStage, config: SessionConfig, language: Language): String? = when (stage) {
        SessionStage.SETUP -> when (language) {
            Language.EN -> "First time? Add everyone at the table. The clock is ${config.minutesPerPerson} minutes per person plus ${config.overheadMinutes}."
            Language.PT -> "Primeira vez? Junta toda a gente à mesa. O tempo é ${config.minutesPerPerson} minutos por pessoa mais ${config.overheadMinutes}."
            Language.ES -> "¿Primera vez? Añade a todos los que están en la mesa. El tiempo es de ${config.minutesPerPerson} minutos por persona más ${config.overheadMinutes}."
            Language.FR -> "Première fois ? Ajoute tout le monde à la table. Le temps est de ${config.minutesPerPerson} minutes par personne plus ${config.overheadMinutes}."
        }

        SessionStage.DRAW -> when (language) {
            Language.EN -> "One card per category. Cards used in the last twelve sessions are held back automatically."
            Language.PT -> "Uma carta por categoria. Cartas usadas nas últimas doze sessões ficam reservadas automaticamente."
            Language.ES -> "Una carta por categoría. Las cartas usadas en las últimas doce sesiones se reservan automáticamente."
            Language.FR -> "Une carte par catégorie. Les cartes utilisées lors des douze dernières sessions sont automatiquement mises de côté."
        }

        SessionStage.READ -> when (language) {
            Language.EN -> "Read the card to the room — pass the phone round or hold it up, everyone reads it, not just you."
            Language.PT -> "Lê a carta em voz alta — passa o telemóvel ou levanta-o, para que todos a leiam, não só tu."
            Language.ES -> "Lee la carta en voz alta para la sala — pasa el móvil o levántalo, para que todos la lean, no solo tú."
            Language.FR -> "Lis la carte à voix haute pour la salle — fais circuler le téléphone ou lève-le, pour que tout le monde la lise, pas seulement toi."
        }

        SessionStage.WRITE -> when (language) {
            Language.EN -> "${config.writeMinutes} silent minutes. Everything is written on paper; nothing is typed here."
            Language.PT -> "${config.writeMinutes} minutos em silêncio. Tudo é escrito em papel; nada é escrito aqui."
            Language.ES -> "${config.writeMinutes} minutos en silencio. Todo se escribe en papel; aquí no se escribe nada."
            Language.FR -> "${config.writeMinutes} minutes de silence. Tout est écrit sur papier ; rien n'est écrit ici."
        }

        SessionStage.ROUNDS -> when (language) {
            Language.EN -> "One volunteer at a time. Everyone else shares, then the volunteer responds."
            Language.PT -> "Um voluntário de cada vez. Os restantes partilham, depois o voluntário responde."
            Language.ES -> "Un voluntario a la vez. Los demás comparten, después el voluntario responde."
            Language.FR -> "Un volontaire à la fois. Les autres partagent, puis le volontaire répond."
        }

        SessionStage.REFLECT -> when (language) {
            Language.EN -> "One line about the session. No individual answers are ever stored."
            Language.PT -> "Uma linha sobre a sessão. Nenhuma resposta individual é guardada."
            Language.ES -> "Una línea sobre la sesión. Nunca se guarda ninguna respuesta individual."
            Language.FR -> "Une ligne sur la session. Aucune réponse individuelle n'est jamais enregistrée."
        }

        SessionStage.DONE -> null
    }

    // --- Tablet-only additions below. Same rule: dynamic/interpolated copy lives here in every
    // language; purely static section labels (e.g. "CLOCKWISE ORDER") stay as literals in the
    // tablet composables, matching the precedent already set by the phone screens above.

    fun historyMeta(participants: Int, minutes: Int, language: Language): String {
        val word = when (language) {
            Language.EN -> if (participants == 1) "person" else "people"
            Language.PT -> if (participants == 1) "pessoa" else "pessoas"
            Language.ES -> if (participants == 1) "persona" else "personas"
            Language.FR -> if (participants == 1) "personne" else "personnes"
        }
        return "$participants $word · $minutes min"
    }

    fun noReflectionNote(language: Language): String = when (language) {
        Language.EN -> "No note added."
        Language.PT -> "Sem nota adicionada."
        Language.ES -> "No se añadió ninguna nota."
        Language.FR -> "Aucune note ajoutée."
    }

    fun noSessionsYet(language: Language): String = when (language) {
        Language.EN -> "No sessions logged yet."
        Language.PT -> "Ainda não há sessões registadas."
        Language.ES -> "Todavía no hay sesiones registradas."
        Language.FR -> "Aucune session enregistrée pour l'instant."
    }

    // --- Screen titles/labels that were static English literals in the Compose screens until
    // Settings made the language toggle a real, user-facing choice rather than an internal-only
    // default.

    fun stageLabel(stage: SessionStage, language: Language): String = when (stage) {
        SessionStage.SETUP -> when (language) {
            Language.EN -> "SETUP"
            Language.PT -> "CONFIGURAÇÃO"
            Language.ES -> "CONFIGURACIÓN"
            Language.FR -> "CONFIGURATION"
        }
        SessionStage.DRAW -> when (language) {
            Language.EN -> "DRAW"
            Language.PT -> "TIRAR"
            Language.ES -> "SACAR"
            Language.FR -> "TIRER"
        }
        SessionStage.READ -> when (language) {
            Language.EN -> "READ ALOUD"
            Language.PT -> "LER EM VOZ ALTA"
            Language.ES -> "LEER EN VOZ ALTA"
            Language.FR -> "LIRE À VOIX HAUTE"
        }
        SessionStage.WRITE -> when (language) {
            Language.EN -> "WRITE"
            Language.PT -> "ESCREVER"
            Language.ES -> "ESCRIBIR"
            Language.FR -> "ÉCRIRE"
        }
        SessionStage.ROUNDS -> when (language) {
            Language.EN -> "ROUNDS"
            Language.PT -> "RONDAS"
            Language.ES -> "RONDAS"
            Language.FR -> "TOURS"
        }
        SessionStage.REFLECT -> when (language) {
            Language.EN -> "CLOSING"
            Language.PT -> "FECHAR"
            Language.ES -> "CIERRE"
            Language.FR -> "CLÔTURE"
        }
        SessionStage.DONE -> when (language) {
            Language.EN -> "LOGGED"
            Language.PT -> "REGISTADA"
            Language.ES -> "REGISTRADA"
            Language.FR -> "ENREGISTRÉE"
        }
    }

    fun setupTitle(language: Language): String = when (language) {
        Language.EN -> "Who is at the table?"
        Language.PT -> "Quem está à mesa?"
        Language.ES -> "¿Quién está en la mesa?"
        Language.FR -> "Qui est à la table ?"
    }

    fun setupSubtitle(language: Language): String = when (language) {
        Language.EN -> "Type each name. The list is the order feedback goes round."
        Language.PT -> "Escreve cada nome. A lista é a ordem em que o feedback é dado."
        Language.ES -> "Escribe cada nombre. La lista es el orden en que se da el feedback."
        Language.FR -> "Saisis chaque nom. La liste est l'ordre dans lequel le feedback est donné."
    }

    fun addPerson(language: Language): String = when (language) {
        Language.EN -> "+ ADD PERSON"
        Language.PT -> "+ ADICIONAR PESSOA"
        Language.ES -> "+ AÑADIR PERSONA"
        Language.FR -> "+ AJOUTER UNE PERSONNE"
    }

    fun depthPerCategory(language: Language): String = when (language) {
        Language.EN -> "DEPTH, PER CATEGORY"
        Language.PT -> "PROFUNDIDADE, POR CATEGORIA"
        Language.ES -> "PROFUNDIDAD, POR CATEGORÍA"
        Language.FR -> "PROFONDEUR, PAR CATÉGORIE"
    }

    fun drawTitle(language: Language): String = when (language) {
        Language.EN -> "Draw the four"
        Language.PT -> "Tira as quatro"
        Language.ES -> "Saca las cuatro"
        Language.FR -> "Tire les quatre"
    }

    fun drawnCountLabel(count: Int, language: Language): String = when (language) {
        Language.EN -> "$count of 4 drawn · tap a pile to draw or redraw it"
        Language.PT -> "$count de 4 tiradas · toca numa pilha para tirar ou voltar a tirar"
        Language.ES -> "$count de 4 sacadas · toca un mazo para sacar o volver a sacar"
        Language.FR -> "$count sur 4 tirées · touche une pile pour tirer ou retirer"
    }

    fun redraw(language: Language): String = when (language) {
        Language.EN -> "REDRAW"
        Language.PT -> "VOLTAR A TIRAR"
        Language.ES -> "VOLVER A SACAR"
        Language.FR -> "RETIRER"
    }

    fun redrawAll(language: Language): String = when (language) {
        Language.EN -> "REDRAW ALL"
        Language.PT -> "VOLTAR A TIRAR TODAS"
        Language.ES -> "VOLVER A SACAR TODAS"
        Language.FR -> "TOUT RETIRER"
    }

    fun pileAvailabilityLabel(isDrawn: Boolean, available: Int, heldBack: Int, language: Language): String = when {
        isDrawn -> when (language) {
            Language.EN -> "drawn"
            Language.PT -> "tirada"
            Language.ES -> "sacada"
            Language.FR -> "tirée"
        }
        heldBack > 0 -> when (language) {
            Language.EN -> "$available ready · $heldBack held back"
            Language.PT -> "$available disponíveis · $heldBack reservadas"
            Language.ES -> "$available disponibles · $heldBack reservadas"
            Language.FR -> "$available prêtes · $heldBack mises de côté"
        }
        else -> when (language) {
            Language.EN -> "$available ready"
            Language.PT -> "$available disponíveis"
            Language.ES -> "$available disponibles"
            Language.FR -> "$available prêtes"
        }
    }

    fun stepTwoDraw(language: Language): String = when (language) {
        Language.EN -> "STEP 2 — DRAW"
        Language.PT -> "PASSO 2 — TIRAR"
        Language.ES -> "PASO 2 — SACAR"
        Language.FR -> "ÉTAPE 2 — TIRER"
    }

    fun drawnOfFourLabel(count: Int, language: Language): String = when (language) {
        Language.EN -> "$count of 4 drawn"
        Language.PT -> "$count de 4 tiradas"
        Language.ES -> "$count de 4 sacadas"
        Language.FR -> "$count sur 4 tirées"
    }

    fun readOrDrawAllLabel(allDrawn: Boolean, language: Language): String = when {
        allDrawn -> when (language) {
            Language.EN -> "READ THEM OUT →"
            Language.PT -> "LÊ-AS EM VOZ ALTA →"
            Language.ES -> "LÉELAS EN VOZ ALTA →"
            Language.FR -> "LIS-LES À VOIX HAUTE →"
        }
        else -> when (language) {
            Language.EN -> "DRAW ALL FOUR"
            Language.PT -> "TIRA AS QUATRO"
            Language.ES -> "SACA LAS CUATRO"
            Language.FR -> "TIRE LES QUATRE"
        }
    }

    fun receivingFeedback(language: Language): String = when (language) {
        Language.EN -> "RECEIVING FEEDBACK"
        Language.PT -> "A RECEBER FEEDBACK"
        Language.ES -> "RECIBIENDO FEEDBACK"
        Language.FR -> "RÉCEPTION DU FEEDBACK"
    }

    fun orderLabel(language: Language): String = when (language) {
        Language.EN -> "ORDER"
        Language.PT -> "ORDEM"
        Language.ES -> "ORDEN"
        Language.FR -> "ORDRE"
    }

    fun fiveMinutesLeft(language: Language): String = when (language) {
        Language.EN -> "FIVE MINUTES LEFT"
        Language.PT -> "FALTAM CINCO MINUTOS"
        Language.ES -> "QUEDAN CINCO MINUTOS"
        Language.FR -> "IL RESTE CINQ MINUTES"
    }

    fun finishTurnNote(language: Language): String = when (language) {
        Language.EN -> "Finish this turn, then decide together: close, or carry the rest to next session."
        Language.PT -> "Termina esta vez e decidam juntos: fechar, ou passar o resto para a próxima sessão."
        Language.ES -> "Termina este turno y decidid juntos: cerrar, o llevar el resto a la próxima sesión."
        Language.FR -> "Termine ce tour, puis décidez ensemble : clôturer, ou reporter le reste à la prochaine session."
    }

    fun closeLabel(language: Language): String = when (language) {
        Language.EN -> "CLOSE"
        Language.PT -> "FECHAR"
        Language.ES -> "CERRAR"
        Language.FR -> "FERMER"
    }

    fun howDidThatFeel(language: Language): String = when (language) {
        Language.EN -> "How did that feel?"
        Language.PT -> "Como é que correu?"
        Language.ES -> "¿Cómo se sintió?"
        Language.FR -> "Comment ça s'est passé ?"
    }

    fun sayItOutLoud(language: Language): String = when (language) {
        Language.EN -> "Say it out loud first. Then one line for the record — about the session, not about anyone in it."
        Language.PT -> "Diz em voz alta primeiro. Depois uma linha para o registo — sobre a sessão, não sobre ninguém."
        Language.ES -> "Dilo en voz alta primero. Después una línea para el registro — sobre la sesión, no sobre nadie en ella."
        Language.FR -> "Dis-le d'abord à voix haute. Puis une ligne pour le compte-rendu — sur la session, pas sur qui que ce soit."
    }

    fun savedWithSession(language: Language): String = when (language) {
        Language.EN -> "Saved with the session: four card codes, the date, the length, this line."
        Language.PT -> "Guardado com a sessão: quatro códigos de cartas, a data, a duração, esta linha."
        Language.ES -> "Guardado con la sesión: cuatro códigos de cartas, la fecha, la duración, esta línea."
        Language.FR -> "Enregistré avec la session : quatre codes de cartes, la date, la durée, cette ligne."
    }

    fun logSessionButton(language: Language): String = when (language) {
        Language.EN -> "LOG SESSION"
        Language.PT -> "REGISTAR SESSÃO"
        Language.ES -> "REGISTRAR SESIÓN"
        Language.FR -> "ENREGISTRER LA SESSION"
    }

    fun everyoneWritesHeader(language: Language): String = when (language) {
        Language.EN -> "EVERYONE WRITES — PAPER ONLY"
        Language.PT -> "TODOS ESCREVEM — SÓ EM PAPEL"
        Language.ES -> "TODOS ESCRIBEN — SOLO EN PAPEL"
        Language.FR -> "TOUT LE MONDE ÉCRIT — PAPIER UNIQUEMENT"
    }

    fun oneAnswerPerCard(language: Language): String = when (language) {
        Language.EN -> "One answer per card about every other person. The personal card is about you."
        Language.PT -> "Uma resposta por carta sobre cada uma das outras pessoas. A carta pessoal é sobre ti."
        Language.ES -> "Una respuesta por carta sobre cada una de las otras personas. La carta personal es sobre ti."
        Language.FR -> "Une réponse par carte sur chacune des autres personnes. La carte personnelle porte sur toi."
    }

    fun everyoneIsDone(language: Language): String = when (language) {
        Language.EN -> "EVERYONE IS DONE →"
        Language.PT -> "TODOS TERMINARAM →"
        Language.ES -> "TODOS HAN TERMINADO →"
        Language.FR -> "TOUT LE MONDE A TERMINÉ →"
    }

    fun sessionLoggedTitle(language: Language): String = when (language) {
        Language.EN -> "Session logged"
        Language.PT -> "Sessão registada"
        Language.ES -> "Sesión registrada"
        Language.FR -> "Session enregistrée"
    }

    fun heldBackNextTime(language: Language): String = when (language) {
        Language.EN -> "HELD BACK NEXT TIME"
        Language.PT -> "RESERVADAS PARA A PRÓXIMA"
        Language.ES -> "RESERVADAS PARA LA PRÓXIMA"
        Language.FR -> "MISES DE CÔTÉ POUR LA PROCHAINE FOIS"
    }

    fun finishButton(language: Language): String = when (language) {
        Language.EN -> "FINISH"
        Language.PT -> "TERMINAR"
        Language.ES -> "FINALIZAR"
        Language.FR -> "TERMINER"
    }

    // --- Tablet-only static copy.

    fun tapEachPileReadToRoom(language: Language): String = when (language) {
        Language.EN -> "Tap each pile. Read it to the room."
        Language.PT -> "Toca em cada pilha. Lê-a para a sala."
        Language.ES -> "Toca cada mazo. Léelo a la sala."
        Language.FR -> "Touche chaque pile. Lis-la à la salle."
    }

    fun readAloudThenWriteLabel(allDrawn: Boolean, language: Language): String = when {
        allDrawn -> when (language) {
            Language.EN -> "READ ALOUD, THEN WRITE →"
            Language.PT -> "LÊ EM VOZ ALTA E DEPOIS ESCREVE →"
            Language.ES -> "LEE EN VOZ ALTA Y LUEGO ESCRIBE →"
            Language.FR -> "LIS À VOIX HAUTE, PUIS ÉCRIS →"
        }
        else -> when (language) {
            Language.EN -> "DRAW ALL FOUR TO CONTINUE"
            Language.PT -> "TIRA AS QUATRO PARA CONTINUAR"
            Language.ES -> "SACA LAS CUATRO PARA CONTINUAR"
            Language.FR -> "TIRE LES QUATRE POUR CONTINUER"
        }
    }

    fun tapToDraw(language: Language): String = when (language) {
        Language.EN -> "TAP TO DRAW"
        Language.PT -> "TOCA PARA TIRAR"
        Language.ES -> "TOCA PARA SACAR"
        Language.FR -> "TOUCHE POUR TIRER"
    }

    fun sessionLog(language: Language): String = when (language) {
        Language.EN -> "SESSION LOG"
        Language.PT -> "REGISTO DE SESSÕES"
        Language.ES -> "REGISTRO DE SESIONES"
        Language.FR -> "JOURNAL DES SESSIONS"
    }

    fun cardDrawsByCategory(language: Language): String = when (language) {
        Language.EN -> "CARD DRAWS BY CATEGORY"
        Language.PT -> "CARTAS TIRADAS POR CATEGORIA"
        Language.ES -> "CARTAS SACADAS POR CATEGORÍA"
        Language.FR -> "CARTES TIRÉES PAR CATÉGORIE"
    }

    fun everySessionLogged(language: Language): String = when (language) {
        Language.EN -> "Every session logged so far."
        Language.PT -> "Todas as sessões registadas até agora."
        Language.ES -> "Todas las sesiones registradas hasta ahora."
        Language.FR -> "Toutes les sessions enregistrées jusqu'à présent."
    }

    fun categoryFrequencyDescription(language: Language): String = when (language) {
        Language.EN -> "How many of each category have been drawn across every logged session."
        Language.PT -> "Quantas cartas de cada categoria foram tiradas em todas as sessões registadas."
        Language.ES -> "Cuántas cartas de cada categoría se han sacado en todas las sesiones registradas."
        Language.FR -> "Combien de cartes de chaque catégorie ont été tirées sur l'ensemble des sessions enregistrées."
    }

    fun teamLevelOnlyNote(language: Language): String = when (language) {
        Language.EN -> "Team level only — this never shows who said what."
        Language.PT -> "Apenas ao nível da equipa — nunca mostra quem disse o quê."
        Language.ES -> "Solo a nivel de equipo — esto nunca muestra quién dijo qué."
        Language.FR -> "Uniquement au niveau de l'équipe — cela ne montre jamais qui a dit quoi."
    }

    fun historyToggleLabel(showHistory: Boolean, language: Language): String = when {
        showHistory -> when (language) {
            Language.EN -> "CLOSE"
            Language.PT -> "FECHAR"
            Language.ES -> "CERRAR"
            Language.FR -> "FERMER"
        }
        else -> when (language) {
            Language.EN -> "HISTORY"
            Language.PT -> "HISTÓRICO"
            Language.ES -> "HISTORIAL"
            Language.FR -> "HISTORIQUE"
        }
    }

    fun sessionNumberHeaderMeta(number: Int, peopleCount: Int, language: Language): String = when (language) {
        Language.EN -> {
            val word = if (peopleCount == 1) "PERSON" else "PEOPLE"
            "SESSION $number · $peopleCount $word"
        }
        Language.PT -> {
            val word = if (peopleCount == 1) "PESSOA" else "PESSOAS"
            "SESSÃO $number · $peopleCount $word"
        }
        Language.ES -> {
            val word = if (peopleCount == 1) "PERSONA" else "PERSONAS"
            "SESIÓN $number · $peopleCount $word"
        }
        Language.FR -> {
            val word = if (peopleCount == 1) "PERSONNE" else "PERSONNES"
            "SESSION $number · $peopleCount $word"
        }
    }

    fun sessionLogHeaderMeta(language: Language): String = when (language) {
        Language.EN -> "SESSION LOG · TEAM LEVEL ONLY"
        Language.PT -> "REGISTO DE SESSÕES · APENAS AO NÍVEL DA EQUIPA"
        Language.ES -> "REGISTRO DE SESIONES · SOLO A NIVEL DE EQUIPO"
        Language.FR -> "JOURNAL DES SESSIONS · NIVEAU ÉQUIPE UNIQUEMENT"
    }

    fun clockwiseOrder(language: Language): String = when (language) {
        Language.EN -> "CLOCKWISE ORDER"
        Language.PT -> "ORDEM (SENTIDO DOS PONTEIROS)"
        Language.ES -> "ORDEN (SENTIDO HORARIO)"
        Language.FR -> "ORDRE (SENS HORAIRE)"
    }

    fun thisTurn(language: Language): String = when (language) {
        Language.EN -> "THIS TURN"
        Language.PT -> "ESTA VEZ"
        Language.ES -> "ESTE TURNO"
        Language.FR -> "CE TOUR"
    }

    fun nextUp(language: Language): String = when (language) {
        Language.EN -> "NEXT UP"
        Language.PT -> "A SEGUIR"
        Language.ES -> "A CONTINUACIÓN"
        Language.FR -> "SUIVANT"
    }

    fun skipToClosing(language: Language): String = when (language) {
        Language.EN -> "SKIP TO CLOSING"
        Language.PT -> "SALTAR PARA O FECHO"
        Language.ES -> "SALTAR AL CIERRE"
        Language.FR -> "PASSER À LA CLÔTURE"
    }

    fun clockwiseOrderNote(language: Language): String = when (language) {
        Language.EN -> "Everyone shares about the volunteer, one at a time — not a conversation. The volunteer answers last."
        Language.PT -> "Todos partilham sobre o/a voluntário/a, um de cada vez — não é uma conversa. O/a voluntário/a responde no fim."
        Language.ES -> "Todos comparten sobre el voluntario, uno a la vez — no es una conversación. El voluntario responde al final."
        Language.FR -> "Chacun partage à propos du volontaire, un à la fois — ce n'est pas une conversation. Le volontaire répond en dernier."
    }

    fun stayVisibleCue(language: Language): String = when (language) {
        Language.EN -> "Stays visible to the whole room. A cue fires at five minutes left."
        Language.PT -> "Fica visível para toda a sala. Um aviso soa quando faltam cinco minutos."
        Language.ES -> "Permanece visible para toda la sala. Un aviso suena cuando quedan cinco minutos."
        Language.FR -> "Reste visible pour toute la salle. Une alerte se déclenche quand il reste cinq minutes."
    }

    fun sessionClockCalculated(language: Language): String = when (language) {
        Language.EN -> "SESSION CLOCK — CALCULATED"
        Language.PT -> "RELÓGIO DA SESSÃO — CALCULADO"
        Language.ES -> "RELOJ DE LA SESIÓN — CALCULADO"
        Language.FR -> "MINUTEUR DE SESSION — CALCULÉ"
    }

    fun noAnswersStored(language: Language): String = when (language) {
        Language.EN -> "No answers are stored. Only which cards were drawn, and one session-level note."
        Language.PT -> "Nenhuma resposta é guardada. Só que cartas foram tiradas, e uma nota da sessão."
        Language.ES -> "No se guarda ninguna respuesta. Solo qué cartas se sacaron, y una nota de la sesión."
        Language.FR -> "Aucune réponse n'est enregistrée. Seulement quelles cartes ont été tirées, et une note de session."
    }

    fun stepOneSetup(language: Language): String = when (language) {
        Language.EN -> "STEP 1 — SETUP"
        Language.PT -> "PASSO 1 — CONFIGURAÇÃO"
        Language.ES -> "PASO 1 — CONFIGURACIÓN"
        Language.FR -> "ÉTAPE 1 — CONFIGURATION"
    }

    fun stepThreeEveryoneWrites(language: Language): String = when (language) {
        Language.EN -> "STEP 3 — EVERYONE WRITES"
        Language.PT -> "PASSO 3 — TODOS ESCREVEM"
        Language.ES -> "PASO 3 — TODOS ESCRIBEN"
        Language.FR -> "ÉTAPE 3 — TOUT LE MONDE ÉCRIT"
    }

    fun silentWriting(language: Language): String = when (language) {
        Language.EN -> "SILENT WRITING"
        Language.PT -> "ESCRITA EM SILÊNCIO"
        Language.ES -> "ESCRITURA EN SILENCIO"
        Language.FR -> "ÉCRITURE EN SILENCE"
    }

    fun paperOnlyNote(language: Language): String = when (language) {
        Language.EN -> "Paper only. Nothing is typed into this device, now or later."
        Language.PT -> "Só papel. Nada é escrito neste dispositivo, agora ou depois."
        Language.ES -> "Solo papel. Nada se escribe en este dispositivo, ni ahora ni después."
        Language.FR -> "Papier uniquement. Rien n'est saisi sur cet appareil, ni maintenant ni plus tard."
    }

    fun leftLabel(language: Language): String = when (language) {
        Language.EN -> "LEFT"
        Language.PT -> "RESTANTES"
        Language.ES -> "RESTANTE"
        Language.FR -> "RESTANT"
    }

    fun sessionSummary(language: Language): String = when (language) {
        Language.EN -> "SESSION SUMMARY"
        Language.PT -> "RESUMO DA SESSÃO"
        Language.ES -> "RESUMEN DE LA SESIÓN"
        Language.FR -> "RÉSUMÉ DE LA SESSION"
    }

    fun reflectionLabel(language: Language): String = when (language) {
        Language.EN -> "REFLECTION"
        Language.PT -> "REFLEXÃO"
        Language.ES -> "REFLEXIÓN"
        Language.FR -> "RÉFLEXION"
    }

    fun nextSessionHeldBackNote(language: Language): String = when (language) {
        Language.EN -> "Next session these four codes join the held-back pile. Nothing anyone said in the room was recorded."
        Language.PT -> "Na próxima sessão estes quatro códigos juntam-se à reserva. Nada do que foi dito na sala foi registado."
        Language.ES -> "En la próxima sesión estos cuatro códigos se unirán a la reserva. Nada de lo que se dijo en la sala fue registrado."
        Language.FR -> "À la prochaine session, ces quatre codes rejoindront la réserve. Rien de ce qui a été dit dans la salle n'a été enregistré."
    }
}
