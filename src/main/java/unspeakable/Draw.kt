package unspeakable

import org.slf4j.LoggerFactory
import unspeakable.ResultType.*
import unspeakable.SymbolType.*

/**
 * A draw is a set of cards and gives a result based on the total symbols on its cards.
 * @author nick
 */
class Draw (val cards: List<Card>, val cardsNeededForResult: Int){

    private val logger = LoggerFactory.getLogger(Draw::class.java)


    /** returns all the symbols of all the cards that make up this collection */
    fun symbols() = cards.flatMap { it.symbols() }

    /** returns all the symbols of a given type on all the cards that make up this collection */
    fun symbols(symbolType: SymbolType) = cards.flatMap { it.symbols(symbolType) }

    fun minus(card: Card): Draw {
        val reducedCards = cards - card
        return Draw(reducedCards, cardsNeededForResult)
    }

    fun plus(card: Card): Draw {
        val reducedCards = cards + card
        return Draw(reducedCards, cardsNeededForResult)
    }

    fun result(): ResultType {
        val good = symbols(GOOD).size
        val bad = symbols(BAD).size
        return when {
            good >= bad + cardsNeededForResult-> ADVANCE
            bad >= good + cardsNeededForResult -> SETBACK
            else -> COMPLICATION
        }
    }

    override fun toString(): String = "Draw" + cards.toString()
}