package unspeakable

import org.slf4j.LoggerFactory

/**
 * A card can have zero or many symbols. Cards can be grouped in collections like hands and decks.
 * @author nick
 */

/** create value pool with no available values  */
open class Card {

    private var symbols: List<Symbol> = listOf<Symbol>()

    constructor(vararg symbolTypes: SymbolType) {
        for (symbolType in symbolTypes) {
            val symbol = Symbol(symbolType)
            symbols+=symbol
        }
    }
    private val logger = LoggerFactory.getLogger(Card::class.java)

    /** returns all the symbols on the card */
    fun symbols() : List<Symbol> {return symbols}

    /** returns all the symbols of a given type on the card */
    fun symbols(symbolType: SymbolType) : List<Symbol> {return symbols.filter{ it.type(symbolType)}}

    override fun toString(): String = "Card" + symbols.toString()
}