package unspeakable

import org.junit.Assert.*
import org.junit.Test
import org.slf4j.LoggerFactory
import unspeakable.ResultType.*

class OddsTest {

    private val logger = LoggerFactory.getLogger(OddsTest::class.java)

    @Test
    fun `1) an Odds set can be empty`() {
        val odds = Odds()
        assertEquals(0, odds.size())
    }

    @Test
    fun `2) an Odds set can have results added`() {
        val odds = Odds()
        odds.add(ADVANCE)
        assertEquals(1, odds.size())
    }

    @Test
    fun `3) an Odds set can count many results and say how many its counted`() {
        val odds = Odds()
        odds.add(ADVANCE)
        odds.add(ADVANCE)
        odds.add(SETBACK)
        odds.add(ADVANCE)
        odds.add(COMPLICATION)
        assertEquals(5, odds.size())
    }

    @Test
    fun `4) an Odds set can say how many of a given result there have been`() {
        val odds = Odds()
        odds.add(ADVANCE)
        odds.add(ADVANCE)
        odds.add(SETBACK)
        odds.add(ADVANCE)
        odds.add(COMPLICATION)
        assertEquals(5, odds.size())
        assertEquals(3, odds.results(ADVANCE))
    }

    @Test
    fun `5) an Odds set can say what proportion of results a given result has been`() {
        val odds = Odds()
        odds.add(ADVANCE)
        odds.add(ADVANCE)
        odds.add(SETBACK)
        odds.add(ADVANCE)
        odds.add(COMPLICATION)
        assertEquals(5, odds.size())
        assertEquals(0.60, odds.proportion(ADVANCE), 0.001)
    }
}