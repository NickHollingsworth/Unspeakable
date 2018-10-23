package unspeakable

import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.LoggerFactory
import unspeakable.ResultType.*
import unspeakable.SymbolType.*

class _Simulation {

    private val logger = LoggerFactory.getLogger(_Simulation::class.java)

    private val unspeakableCardGroup = listOf<Card>(
        Card(GOOD, GOOD),
        Card(GOOD),
        Card(),
        Card(BAD), Card(BAD), Card(BAD), Card(BAD),
        Card(BAD, BAD)
    )
    private val unspeakableCardPack = unspeakableCardGroup + unspeakableCardGroup + unspeakableCardGroup + unspeakableCardGroup

    private val balancedCardGroup = listOf<Card>(
            Card(GOOD, GOOD),
            Card(GOOD), Card(GOOD),
            Card(), Card(),
            Card(BAD), Card(BAD),
            Card(BAD, BAD)
    )
    private val balancedCardPack = balancedCardGroup + balancedCardGroup + balancedCardGroup + balancedCardGroup

    private val experimentalCardGroup = listOf<Card>(
            Card(GOOD, GOOD),
            Card(GOOD), Card(GOOD), Card(GOOD),
            Card(), Card(),
            Card(BAD), Card(BAD), Card(BAD),
            Card(BAD, BAD)
    )
    private val experimentalCardPack = experimentalCardGroup + experimentalCardGroup + experimentalCardGroup + experimentalCardGroup


    private fun pad(width: Int, str: Any): String {
        return "%${width}d".format(str)
    }
    private fun places(places: Int, double: Double): String {
        return "%.${places}f".format(double)
    }

    fun formattedOddsBlock(odds: Odds): String {
        val width = 7
        val places = 1
        return  "  result         " + "num draws      % chance" +
                "\n  ------         ---------      --------" +
                "\n  ADVANCE        ${pad(width, odds.results(ADVANCE))}          ${places(places, odds.percent(ADVANCE))}" +
                "\n  COMPLICATION   ${pad(width, odds.results(COMPLICATION))}          ${places(places, odds.percent(COMPLICATION))}"  +
                "\n  SETBACK        ${pad(width, odds.results(SETBACK))}          ${places(places, odds.percent(SETBACK))}"  +
                "\n  total:         ${pad(width, odds.results.size )}          ${places(places, odds.percent(listOf(ADVANCE, COMPLICATION, SETBACK)))}"
    }

    fun printOddsTableHeading(): Unit {
        println("\n                ADVANCE COMPLICATION SETBACK  Total")
        println("                ------- ------------ -------  -----")
    }

    fun printOddsTableLine(lineDesc: String, deck: Deck, modifiers: List<Card>, pick: Int): Unit {

        val odds = deck.fixedModifiers(modifiers).odds(pick)

        val places = 1
        println  ("${lineDesc}" +
                "    ${places(places, odds.percent(ADVANCE))}" +
                "    ${places(places, odds.percent(COMPLICATION))}"  +
                "    ${places(places, odds.percent(SETBACK))}"  +
                "    ${places(places, odds.percent(listOf(ADVANCE, COMPLICATION, SETBACK)))}")
    }
    private fun printOddsLine(desc: String, deck: Deck, modifiers: List<Card>, pick: Int) {
        println(desc)
        println(formattedOddsBlock(deck.fixedModifiers(modifiers).odds(pick)))
    }

    private fun printOddsBlock(desc: String, deck: Deck, modifiers: List<Card>, pick: Int) {
        println(desc)
        println(formattedOddsBlock(deck.fixedModifiers(modifiers).odds(pick)))
    }

    fun displayDetailedOdds(name:String, cards: List<Card>, deal: Int, cardsForResult: Int, verbose: Boolean = false) {
        val unspeakableDeck = Deck(cards, cardsNeededForResult = cardsForResult)

        println("\n${name}\nDeck: ${unspeakableDeck.cards.size} cards")
        println("Deal $deal cards")
        println("$cardsForResult uncancelled cards needed for a result")

        if (verbose) {
            printOddsBlock("\nWith 2 DISADVANTAGES", unspeakableDeck, listOf(Card(BAD), Card(BAD)), deal)
            printOddsBlock("\nWith 1 DISADVANTAGE ", unspeakableDeck, listOf(Card(BAD)), deal)
            printOddsBlock("\nStraight deal", unspeakableDeck, listOf(), deal)
            printOddsBlock("\nWith 1 ADVANTAGE", unspeakableDeck, listOf(Card(GOOD)), deal)
            printOddsBlock("\nWith 2 ADVANTAGES", unspeakableDeck, listOf(Card(GOOD), Card(GOOD)), deal)
        } else {
            printOddsTableHeading()
            printOddsTableLine("5 DISADVANTAGES", unspeakableDeck, listOf(Card(BAD), Card(BAD), Card(BAD), Card(BAD), Card(BAD)), deal)
            printOddsTableLine("4 DISADVANTAGES", unspeakableDeck, listOf(Card(BAD), Card(BAD), Card(BAD), Card(BAD)), deal)
            printOddsTableLine("3 DISADVANTAGES", unspeakableDeck, listOf(Card(BAD), Card(BAD), Card(BAD)), deal)
            printOddsTableLine("2 DISADVANTAGES", unspeakableDeck, listOf(Card(BAD), Card(BAD)), deal)
            printOddsTableLine("1 DISADVANTAGE ", unspeakableDeck, listOf(Card(BAD)), deal)
            printOddsTableLine("straight       ", unspeakableDeck, listOf(), deal)
            printOddsTableLine("1 ADVANTAGE    ", unspeakableDeck, listOf(Card(GOOD)), deal)
            printOddsTableLine("2 ADVANTAGES   ", unspeakableDeck, listOf(Card(GOOD), Card(GOOD)), deal)
            printOddsTableLine("3 ADVANTAGES   ", unspeakableDeck, listOf(Card(GOOD), Card(GOOD), Card(GOOD)), deal)
            printOddsTableLine("4 ADVANTAGES   ", unspeakableDeck, listOf(Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD)), deal)
            printOddsTableLine("5 ADVANTAGES   ", unspeakableDeck, listOf(Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD)), deal)
            printOddsTableLine("6 ADVANTAGES   ", unspeakableDeck, listOf(Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD)), deal)
            printOddsTableLine("7 ADVANTAGES   ", unspeakableDeck, listOf(Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD)), deal)
            printOddsTableLine("8 ADVANTAGES   ", unspeakableDeck, listOf(Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD), Card(GOOD)), deal)
        }

        println("\n----------------------------------------------------------")
    }

    @Test
    fun `0) check that the simulations are correct`() {

        fun sim(deal: Int, cardsForResult: Int) {
            val sanityCards = listOf<Card>(
                    Card(BAD, BAD),
                    Card(BAD), Card(BAD),
                    Card(),
                    Card(GOOD),
                    Card(GOOD, GOOD)
            )

            val sanityDeck = Deck(sanityCards, cardsNeededForResult = cardsForResult)
            assertEquals("The sanity test deck is 6 cards", 6, sanityDeck.cards.size)
            println("\nSanity test deck: ${sanityDeck.cards.size} cards")
            println("Deal $deal cards")
            println("$cardsForResult uncancelled cards needed for a result")

            printOddsBlock("\nStraight deal", sanityDeck, listOf(), deal)
        }

        sim(3,1)

        sim(3,2)

    }

    @Test
    fun `1) Original Unspeakable Deck, 4 cards, 1 remaining symbol gives result`() {
        assertEquals("The unspeakable deck is 32 cards", 32, unspeakableCardPack.size)
        displayDetailedOdds("Original Unspeakable Deck, 1 card resolve", unspeakableCardPack, 4, 1)
    }

    @Test
    fun `2) Original Unspeakable Deck, 4 cards, 2 remaining symbols gives result`() {
        assertEquals("The unspeakable deck is 32 cards", 32, unspeakableCardPack.size)
        displayDetailedOdds("Original Unspeakable Deck, 2 card resolve", unspeakableCardPack, 4, 2)
    }

    @Test
    fun `3) Balanced Unspeakable Deck, 4 cards, 1 remaining symbol gives result`() {
        assertEquals("The balanced deck is 32 cards", 32, balancedCardPack.size)
        displayDetailedOdds("Balanced Unspeakable Deck, 1 card resolve", balancedCardPack, 4, 1)
    }

    @Test
    fun `4) Balanced Unspeakable Deck, 4 cards, 2 remaining symbols gives result`() {
        assertEquals("The balanced deck is 32 cards", 32, balancedCardPack.size)
        displayDetailedOdds("Balanced Unspeakable Deck, 2 card resolve", balancedCardPack, 4, 2)
    }

    @Test
    fun `5) Experimental Deck, 4 cards, 1 remaining symbol gives result`() {
        displayDetailedOdds("Experimental Deck, 1 card resolve", experimentalCardPack, 4, 1)
    }

    @Test
    fun `6) Experimental Unspeakable Deck, 4 cards, 2 remaining symbols gives result`() {
        displayDetailedOdds("Experimental Deck, 2 card resolve", experimentalCardPack, 4, 2)
    }

}