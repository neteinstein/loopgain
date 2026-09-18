package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.neteinstein.loopgain.data.source.bundledDeck
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.Language

/** The deck and its category names are fully authored in EN/PT/ES/FR — no language falls back to another. */
class LanguageFallbackTest {

    @Test
    fun everyCardHasNonBlankTextInEveryLanguage() {
        bundledDeck.forEach { card ->
            Language.entries.forEach { language ->
                assertTrue(card.text(language).isNotBlank(), "${card.id} is blank for $language")
            }
        }
    }

    @Test
    fun cardTextMatchesItsOwnLanguageField() {
        val card = bundledDeck.first()
        assertEquals(card.en, card.text(Language.EN))
        assertEquals(card.pt, card.text(Language.PT))
        assertEquals(card.es, card.text(Language.ES))
        assertEquals(card.fr, card.text(Language.FR))
    }

    @Test
    fun everyCategoryHasNonBlankDisplayNameInEveryLanguage() {
        CardCategory.entries.forEach { category ->
            Language.entries.forEach { language ->
                assertTrue(category.displayName(language).isNotBlank(), "$category is blank for $language")
            }
        }
    }

    @Test
    fun everyLanguageHasANativeName() {
        Language.entries.forEach { language ->
            assertTrue(language.nativeName.isNotBlank())
        }
    }
}
