package unspeakable

import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.LoggerFactory
import unspeakable.SymbolType.*

class CardTest {

    private val logger = LoggerFactory.getLogger(CardTest::class.java)

    @Test
    fun `1) a card can be created without a symbol`() {
        val card = Card()
        assertEquals(0, card.symbols().size)
    }

    @Test
    fun `2) a card can have a single symbol on it`() {
        val card = Card(BAD)
        assertEquals(1, card.symbols().size)
    }

    @Test
    fun `3) a card can have a several symbols on it`() {
        val card = Card(BAD, BAD, GOOD)
        assertEquals(3, card.symbols().size)
    }

    @Test
    fun `4) a card can return all the symbols it has of a given type`() {
        val card = Card(BAD, BAD, GOOD, BAD, GOOD, BAD)
        assertEquals(4, card.symbols(BAD).size)
    }

    @Test
    fun `5) we can create a set of cards and examine all the symbols on them`() {
        val cards = listOf(
                Card(BAD, BAD),
                Card(BAD),
                Card(BAD),
                Card(),
                Card(GOOD),
                Card(GOOD, GOOD)
        )
        assertEquals("Check number of cards",6, cards.size)
        val allSymbols = cards.flatMap { it.symbols() }
        assertEquals("Check total number of symbols", 7, allSymbols.size)
        val badSymbols = cards.flatMap { it.symbols(BAD) }
        assertEquals("Check number of BAD symbols", 4, badSymbols.size)
    }

}