package unspeakable

import org.slf4j.LoggerFactory

/**
 * A symbol can be exactly one of the types defined by SymbolType.
 * @author nick
 */

/** create value pool with no available values  */
open class Symbol(val symbolType: SymbolType) {

    private val logger = LoggerFactory.getLogger(Symbol::class.java)

    fun type (desiredSymbolType: SymbolType) : Boolean {
        return symbolType == desiredSymbolType
    }

    override fun toString(): String = symbolType.toString()
}
