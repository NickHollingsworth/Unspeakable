package unspeakable

import org.slf4j.LoggerFactory
import unspeakable.ResultType.*

/**
 * A card can have zero or many symbols. Cards can be grouped in collections like hands and decks.
 * @author nick
 */

/** create value pool with no available values  */
open class Odds {

    constructor()

    constructor(listOfResults: List<ResultType>) {
        listOfResults.forEach { results += it }
    }

    private val logger = LoggerFactory.getLogger(Odds::class.java)


    val results = mutableListOf<ResultType>()

    /** add another result to the set of results counted */
    fun add(result: ResultType) {
        results += result
    }

    /** return the total number of results counted */
    fun size(): Int {
        return results.size
    }

    /** return the total number of results counted that are of the specified type*/
    fun results(resultType: ResultType): Int {
        return results.filter{ it == resultType}.size
    }

    /** return the proportion of results that are of the specified type */
    fun proportion(resultType: ResultType): Double {
        val count = results.filter{ it == resultType}.size
        val total = results.size
        return count/total.toDouble()
    }

    /** return as string with specified decimal places */
    fun percent(resultType: ResultType): Double {
        return (100 * proportion(resultType))
    }

    /** return as string with specified decimal places */
    fun percent(resultTypes: List<ResultType>): Double {
        val totalCount: Double = resultTypes.fold(0.0) {count: Double, resultType -> count + proportion(resultType)}
        return (100 * totalCount)
    }



    override fun toString(): String = "Odds:" + results.toString()
}