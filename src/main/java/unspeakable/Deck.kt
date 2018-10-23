package unspeakable

import org.slf4j.LoggerFactory

/**
 * A deck is a set of cards from which one or more draws can be dealt. Each draw is dealSize number of cards.
 * @author nick
 */
class Deck (val cards : List<Card>, val fixedModifiers: List<Card> = listOf<Card>(), val cardsNeededForResult: Int = 1) {

    private val logger = LoggerFactory.getLogger(Deck::class.java)

    fun minus(card: Card): Deck {
        val reducedCards = cards - card
        return Deck(reducedCards, fixedModifiers, cardsNeededForResult)
    }

    fun plus(card: Card): Deck {
        val reducedCards = cards + card
        return Deck(reducedCards, fixedModifiers, cardsNeededForResult)
    }

    /** returns all possible draws of the specified dealSize */
    fun allPossibleDraws(pick: Int): List<Draw> {

        // get all the permutations of the right number of cards
        val dealtDraws = perm(pick, this)

        //add the fixed cards to every draw
        val allDraws = dealtDraws.map { Draw(it.cards + fixedModifiers, cardsNeededForResult) }

        //DEBUG INFO!
        //println("\nDeal size should be " + pick)
        //if (dealtDraws.size > 0) {
        //    println("The " + dealtDraws.size + " deals look like this: " + dealtDraws.map { Draw(it.cards) })
        //    println("Before adding fixedModifier cards first draw num cards is " + dealtDraws[0].cards.size)
        //    println("Num fixedModifier cards " + fixedModifiers.size)
        //    println("After adding fixedModifier cards first draw num cards is " + allDraws[0].cards.size)
        //} else {
        //    println("No suitably sized draws were possible")
        //}

        return allDraws
    }

    private fun perm(pick: Int, fromDeck: Deck): List<Draw> {

        // get all the possible combinations of cards that could be dealt
        val dealtDraws = when {

        // If asked to pick zero (or less) cards
        // return an empty list to show there are no permutations
            pick < 1 -> listOf<Draw>()

        // If asked to pick from an empty deck
        // return an empty list to show there are no permutations
            fromDeck.cards.isEmpty() -> listOf<Draw>()

        // If asked to pick more cards than there are in the deck
        // return an empty list to show there are no permutations
            pick > fromDeck.cards.size -> listOf<Draw>()

        // If asked to pick exactly the number of cards there are in the deck
        // return the a draw with all the cards from this deck to show there is only 1 permutation
            pick == fromDeck.cards.size -> listOf(Draw(fromDeck.cards, cardsNeededForResult))

        // If asked to pick 1 card from a deck with cards
        // return the a set of draws, each with 1 of the cards in, to show there are as many permutations as cards
            pick == 1 -> {
                fromDeck.cards.mapIndexed { index, card -> Draw(listOf(card), cardsNeededForResult) }
            }

        // If asked to pick several cards from a deck with cards
        // for each card in the deck get all the permutations for the cards that have not been considered yet but add the card back in
        // then make all of these one big list and return it
            else -> {
                val allPossibleDraws = mutableListOf<Draw>()
                val remainingCardsToConsider = fromDeck.cards.toMutableList()
                for (card in fromDeck.cards) {
                    remainingCardsToConsider -= card
                    //val otherCards = fromDeck - cardsAlreadyConsidered
                    val permsOfOtherCards = perm(pick - 1, Deck(remainingCardsToConsider))
                    val permsForSelectedCard = permsOfOtherCards.mapIndexed { index, draw -> draw.plus(card) }
                    allPossibleDraws += permsForSelectedCard
                }
                allPossibleDraws
            }
        }

        return dealtDraws
    }

    /** add extra cards to the deal that are face up on the table and hence who's value is known */
    fun fixedModifiers(fixedCards: List<Card>): Deck {
        return Deck(cards, fixedCards, cardsNeededForResult)
    }

    /** returns the set of all results for all draws that can be made from this deck */
    fun results(pick: Int): List<ResultType>{
        return allPossibleDraws(pick).map { it.result() }
    }


    /** returns the odds of the various possible results for this deck */
    fun odds(pick: Int): Odds {
        return Odds(results(pick))
    }

    /** returns the number of draws that have the specified result */
    fun dealsThatAre(resultType: ResultType) : Int {
        return 0
    }

    override fun toString(): String = "Deck:" + cards.toString() + " cardsNeededForResult=${cardsNeededForResult}"

}

