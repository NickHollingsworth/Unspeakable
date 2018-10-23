package unspeakable

import org.junit.Assert.*
import org.junit.Test
import org.slf4j.LoggerFactory
import unspeakable.SymbolType.*

class SymbolTest {

    private val logger = LoggerFactory.getLogger(SymbolTest::class.java)

    @Test
    fun `1) a BAD symbol can exist`() {
        val symbol = Symbol(BAD)
    }

    @Test
    fun `2) a GOOD symbol can exist`() {
        val symbol = Symbol(BAD)
    }

    @Test
    fun `3) we can group symbols in a collection`() {
        val badSymbol = Symbol(BAD)
        val goodSymbolOne = Symbol(GOOD)
        val goodSymbolTwo = Symbol(GOOD)
        val symbols = listOf<Symbol>(badSymbol, goodSymbolOne, goodSymbolTwo)
        assertEquals(3, symbols.size)
    }

    @Test
    fun `4) we can count symbols of a given type`() {
        val symbols = listOf<Symbol>(Symbol(BAD), Symbol(GOOD), Symbol(GOOD))
        val goodSymbols = symbols.filter{ it.type(GOOD)}
        assertEquals(2, goodSymbols.size)
    }

}