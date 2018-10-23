package unspeakable

import org.junit.Assert.*
import org.junit.Test
import org.slf4j.LoggerFactory
import unspeakable.ResultType.*
import unspeakable.SymbolType.*

class DrawTest {

    private val logger = LoggerFactory.getLogger(DrawTest::class.java)

    @Test
    fun `1) the result of an empty set of cards is a COMPLICATION`() {
        val cards = listOf<Card>()
        val result = Draw(cards, 1).result()
        assertEquals(ResultType.COMPLICATION, result)
    }

    @Test
    fun `2) the result of cards with no symbols is a COMPLICATION`() {
        val cards = listOf<Card>(Card(), Card())
        val draw = Draw(cards, 1)
        assertEquals("Check number of cards", 2, draw.cards.size)
        assertEquals(ResultType.COMPLICATION, draw.result())
    }

    @Test
    fun `3) the result of cards with equal GOOD and BAD symbols is a COMPLICATION`() {
        val cards = listOf<Card>(Card(BAD), Card(BAD), Card(), Card(GOOD, GOOD))
        val draw = Draw(cards, 1)
        assertEquals("Check number of cards", 4, draw.cards.size)
        assertEquals(ResultType.COMPLICATION, draw.result())
    }

    @Test
    fun `4) the result of cards with more GOOD than BAD symbols is an ADVANCE`() {
        val cards = listOf<Card>(Card(BAD), Card(GOOD), Card(BAD), Card(), Card(GOOD, GOOD))
        val draw = Draw(cards, 1)
        assertEquals("Check number of cards", 5, draw.cards.size)
        assertEquals(ResultType.ADVANCE, draw.result())
    }

    @Test
    fun `5) the result of cards with more BAD than GOOD symbols is a SETBACK`() {
        val cards = listOf<Card>(Card(BAD, BAD), Card(BAD), Card(), Card(GOOD, GOOD))
        val draw = Draw(cards, 1)
        assertEquals("Check number of cards", 4, draw.cards.size)
        assertEquals(SETBACK, draw.result())
    }

    @Test
    fun `6) we can create a set of draws and get their results`() {
        val emptySet: List<Card> = listOf() // COMPLICATION
        val badSet = listOf(Card(BAD, BAD), Card(GOOD)) // SETBACK
        val goodSet = listOf(Card(BAD, BAD), Card(GOOD), Card(GOOD, GOOD)) //ADVANCE
        val draws: List<Draw> = listOf<Draw>(Draw(emptySet, 1), Draw(badSet, 1), Draw(goodSet, 1))
        val results = draws.map { it.result() }
        assertEquals(listOf(COMPLICATION, SETBACK, ADVANCE), results)
    }

    @Test
    fun `7) we can specify a number of ADVANTAGES, DISADVANTAGES and Stat benefits before resolving a draw`() {
        val cards = listOf<Card>(Card(BAD), Card(), Card(GOOD, GOOD))
        val basicDraw = Draw(cards, 1) // Starts as an ADVANCE
        assertEquals(ADVANCE, basicDraw.result())
        val worseDraw = basicDraw.plus(Card(BAD)) // Turn it into a SETBACK
        assertEquals(COMPLICATION, worseDraw.result())
        val evenWorseDraw = worseDraw.plus(Card(BAD)) // Turn it into a SETBACK
        assertEquals(SETBACK, evenWorseDraw.result())
    }

    @Test
    fun `8) we can say ADVANCE and SETBACK depend on one uncancelled card`() {
        //When...
        val uncancelledCardsNeeded = 1
        val goodGoodDraw = Draw(listOf<Card>(Card(BAD), Card(), Card(GOOD, GOOD, GOOD)), uncancelledCardsNeeded)
        val goodDraw = Draw(listOf<Card>(Card(BAD), Card(), Card(GOOD, GOOD)), uncancelledCardsNeeded)
        val balancedDraw = Draw(listOf<Card>(Card(BAD), Card(), Card(GOOD)), uncancelledCardsNeeded)
        val badDraw = Draw(listOf<Card>(Card(BAD,BAD), Card(), Card(GOOD)), uncancelledCardsNeeded)
        val badBadDraw = Draw(listOf<Card>(Card(BAD, BAD, BAD), Card(), Card(GOOD)),uncancelledCardsNeeded )

        assertEquals("GOOD, GOOD", ADVANCE, goodGoodDraw.result()) // GOOD, GOOD = ADVANCE
        assertEquals("GOOD", ADVANCE, goodDraw.result()) // 1 GOOD = COMPLICATION
        assertEquals("balanced", COMPLICATION, balancedDraw.result()) // nothing = COMPLICATION
        assertEquals("BAD", SETBACK, badDraw.result()) // 1 BAD = COMPLICATION
        assertEquals("BAD, BAD", SETBACK, badBadDraw.result()) // BAD, BAD = SETBACK

    }

    @Test
    fun `8) we can say ADVANCE and SETBACK depend on several uncancelled cards`() {
        //When...
        val uncancelledCardsNeeded = 2
        val goodGoodDraw = Draw(listOf<Card>(Card(BAD), Card(), Card(GOOD, GOOD, GOOD)), uncancelledCardsNeeded)
        val goodDraw = Draw(listOf<Card>(Card(BAD), Card(), Card(GOOD, GOOD)), uncancelledCardsNeeded)
        val balancedDraw = Draw(listOf<Card>(Card(BAD), Card(), Card(GOOD)), uncancelledCardsNeeded)
        val badDraw = Draw(listOf<Card>(Card(BAD,BAD), Card(), Card(GOOD)), uncancelledCardsNeeded)
        val badBadDraw = Draw(listOf<Card>(Card(BAD, BAD, BAD), Card(), Card(GOOD)),uncancelledCardsNeeded )
        //Then...
        assertEquals("GOOD, GOOD", ADVANCE, goodGoodDraw.result()) // GOOD, GOOD = ADVANCE
        assertEquals("GOOD", COMPLICATION, goodDraw.result()) // 1 GOOD = COMPLICATION
        assertEquals("balanced", COMPLICATION, balancedDraw.result()) // nothing = COMPLICATION
        assertEquals("BAD", COMPLICATION, badDraw.result()) // 1 BAD = COMPLICATION
        assertEquals("BAD, BAD", SETBACK, badBadDraw.result()) // BAD, BAD = SETBACK
    }


}