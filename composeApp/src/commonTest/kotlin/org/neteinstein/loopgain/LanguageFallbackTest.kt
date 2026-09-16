package org.neteinstein.loopgain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.neteinstein.loopgain.data.source.bundledDeck
import org.neteinstein.loopgain.domain.model.CardCategory
import org.neteinstein.loopgain.domain.model.Language

/**
 * The deck and its category names are only authored in EN/PT. ES and FR are UI-selectable (see
 * Settings) but must read in English, not silently fall through to Portuguese.
 */
class LanguageFallbackTest {

    @Test
    fun spanishAndFrenchCardTextFallsBackToEnglish() {
        val card = bundledDeck.first()
        assertEquals(card.en, card.text(Language.ES))
        assertEquals(card.en, card.text(Language.FR))
        assertEquals(card.en, card.text(Language.EN))
    }

    @Test
    fun portugueseCardTextIsUnaffected() {
        val card = bundledDeck.first()
        assertEquals(card.pt, card.text(Language.PT))
    }

    @Test
    fun spanishAndFrenchCategoryNamesFallBackToEnglish() {
        CardCategory.entries.forEach { category ->
            assertEquals(category.displayNameEn, category.displayName(Language.ES))
            assertEquals(category.displayNameEn, category.displayName(Language.FR))
        }
    }

    @Test
    fun everyLanguageHasANativeName() {
        Language.entries.forEach { language ->
            assertTrue(language.nativeName.isNotBlank())
        }
    }
}
