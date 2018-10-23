package unspeakable

import org.junit.Assert.*
import org.junit.Test
import org.slf4j.LoggerFactory
import unspeakable.ResultType.*
import unspeakable.SymbolType.*

class DeckTest {

    private val logger = LoggerFactory.getLogger(DeckTest::class.java)

    @Test
    fun `1) a deck can be empty`() {
        val noCards = listOf<Card>()
        val deck = Deck(noCards)
        assertEquals(0, deck.cards.size)
    }

    @Test
    fun `3) a deck can provide a list of all the possible draws that can be made from it`() {
        val fourCards = listOf<Card>(Card(BAD))
        val deck = Deck(fourCards)
        val draws: List<Draw> = deck.allPossibleDraws(1)
        assertEquals(1, draws.size)
    }

    @Test
    fun `4) when draw size is 4 a deck of 0 cards cant create draws`() {
        val noCards = listOf<Card>()
        val deckNoCards = Deck(noCards)
        val draws: List<Draw> = deckNoCards.allPossibleDraws(4)
        assertEquals(0, draws.size)
    }

    @Test
    fun `5) when draw size is 4 a deck of 1 card cant create draws`() {
        val cards = listOf<Card>(Card(BAD))
        val deck = Deck(cards)
        val draws: List<Draw> = deck.allPossibleDraws(4)
        assertEquals(0, draws.size)
    }

    @Test
    fun `6) when draw size is 4 a deck of 3 card cant create draws`() {
        val cards = listOf<Card>(Card(BAD), Card(), Card(BAD, BAD))
        val deck = Deck(cards)
        val draws: List<Draw> = deck.allPossibleDraws(4)
        assertEquals(0, draws.size)
    }

    @Test
    fun `7) when draw size is 4 a deck of 4 card can only create 1 draw`() {
        val cards = listOf<Card>(Card(BAD), Card(), Card(BAD, BAD), Card())
        val deck = Deck(cards)
        val draws: List<Draw> = deck.allPossibleDraws(4)
        assertEquals(1, draws.size)
    }

    @Test
    fun `8) when draw size is 3 a deck of 4 card can create 4 draws`() {
        val cards = listOf<Card>(Card(BAD), Card(), Card(BAD, BAD), Card())
        val deck = Deck(cards)
        val draws: List<Draw> = deck.allPossibleDraws(3)
        assertEquals(4, draws.size)
    }

    @Test
    fun `12) if you perm 0 cards from an empty deck you get 0 perms`() {
        //When
        val deck = Deck(listOf<Card>())
        assertEquals("Setup failed", 0, deck.cards.size)
        // Then
        val resultDecks = deck.allPossibleDraws(0)
        assertEquals(0, resultDecks.size)
    }

    @Test
    fun `13) if you perm 1 card from an empty deck you get 0 perms`() {
        //When
        val deck = Deck(listOf<Card>())
        assertEquals("Setup failed", 0, deck.cards.size)
        // Then
        val resultDecks = deck.allPossibleDraws(1)
        assertEquals(0, resultDecks.size)
    }

    @Test
    fun `14) if you perm 0 cards from a 1 card deck you get 0 perms`() {
        //When
        val deck = Deck(listOf<Card>(Card()))
        assertEquals("Setup failed", 1, deck.cards.size)
        // Then
        val resultDecks = deck.allPossibleDraws(0)
        assertEquals(0, resultDecks.size)
    }

    @Test
    fun `15) if you perm 1 card from a 1 card deck you get 1 perm with the 1 card`() {
        //When
        val deck = Deck(listOf<Card>(Card()))
        assertEquals("Setup failed", 1, deck.cards.size)
        // Then
        val resultDecks = deck.allPossibleDraws(1)
        assertEquals("Expected 1 perm",1, resultDecks.size)
        assertEquals("Expected the only perm to have 1 card", 1, resultDecks[0].cards.size)
    }

    @Test
    fun `16) if you perm 4 card from a 4 card deck you get 1 perm with all 4 cards`() {
        //When
        val deck = Deck(listOf<Card>(Card(),Card(),Card(),Card()))
        assertEquals("Setup failed", 4, deck.cards.size)
        // Then
        val resultDecks = deck.allPossibleDraws(4)
        assertEquals("Expected 1 perm",1, resultDecks.size)
        assertEquals("Expected the only perm to have 4 cards", 4, resultDecks[0].cards.size)
    }

    @Test
    fun `17) if you perm 1 card from a 3 card deck you get 3 perms, each with one of the cards`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(GOOD)))
        assertEquals("Setup failed", 3, deck.cards.size)
        // Then
        val resultDraws = deck.allPossibleDraws(1)
        assertEquals("Expected 3 perms",3, resultDraws.size)
        assertEquals("Expected first perm to have 1 card", 1, resultDraws[0].cards.size)
        assertEquals("Expected second perm to have 1 card", 1, resultDraws[1].cards.size)
        assertEquals("Expected third perm to have 1 card", 1, resultDraws[2].cards.size)
    }

    @Test
    fun `18) if you perm 2 cards from a 3 card deck you get 3 perms, each with 2 of the cards`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(GOOD)))
        assertEquals("Setup failed", 3, deck.cards.size)
        // Then
        val resultDraws = deck.allPossibleDraws(2)
        assertEquals("Expected 3 perms",3, resultDraws.size)
        assertEquals("Expected first perm to have 2 cards", 2, resultDraws[0].cards.size)
        assertEquals("Expected second perm to have 2 cards", 2, resultDraws[1].cards.size)
        assertEquals("Expected third perm to have 2 cards", 2, resultDraws[2].cards.size)
    }

    @Test
    fun `19) a deck can list the results of all its possible draws`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(GOOD)))
        assertEquals("Setup failed", 3, deck.cards.size)
        // Then
        val results = deck.results(2)
        assertEquals(3, results.size)
    }

    @Test
    fun `20) if you perm 2 cards from the set GOOD BAD GOOD you get 1 ADVANCE 2 COMPLICATIONS and 0 SETBACKS`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(GOOD)))
        assertEquals("Setup failed", 3, deck.cards.size)
        // Then
        val odds = deck.odds(2)
        assertEquals("Expect 3 permutations",3, odds.size())
        assertEquals("Expect 1 ADVANCE", 1, odds.results(ADVANCE))
        assertEquals("Expect 2 COMPLICATIONS", 2, odds.results(COMPLICATION))
        assertEquals("Expect 0 SETBACKS", 0, odds.results(SETBACK))
    }

    @Test
    fun `21) calculating and counting result types works with cards with multiple symbols`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(BAD), Card(GOOD, GOOD)))
        assertEquals("Setup failed", 4, deck.cards.size)
        // Then
        val odds = deck.odds(2)
        assertEquals("Expect 6 permutations",6, odds.size())
        assertEquals("Expect 3 ADVANCE", 3, odds.results(ADVANCE))
        assertEquals("Expect 2 COMPLICATIONS", 2, odds.results(COMPLICATION))
        assertEquals("Expect 1 SETBACKS", 1, odds.results(SETBACK))
    }

    @Test
    fun `22) we can add face up ADVANTAGE and DISADVANTAGE cards and they are always in the draw`() {
        //When normal deck..
        val straightDeck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(BAD), Card(GOOD, GOOD)))
        println("\nSTRAIGHT DECK...")
        val possibleDrawsFromStraightDeck = straightDeck.allPossibleDraws(2)
        assertEquals("Setup failed: possible draws",6, possibleDrawsFromStraightDeck.size)
        val firstDrawFromStraightDeck = possibleDrawsFromStraightDeck[0]
        assertEquals("Setup failed: draw size",2, firstDrawFromStraightDeck.cards.size)
        // with two ADVANTAGES added..
        println("\n\nADD TWO DISADVANTAGE CARDS...")
        val advantageousDeck = straightDeck.fixedModifiers(listOf(Card(BAD), Card(BAD)))
        assertEquals("Setup failed: deck size", 4, advantageousDeck.cards.size)
        // Then
        val possibleDrawsFromAdvantageousDeck = advantageousDeck.allPossibleDraws(2)
        assertEquals("Expect unchanged number of possible draws",6, possibleDrawsFromAdvantageousDeck.size)
        val firstDrawFromAdvantageous = possibleDrawsFromAdvantageousDeck[0]
        assertEquals("Expect all draws to be 2 cards bigger and so have 4 cards",4, firstDrawFromAdvantageous.cards.size)
    }

    @Test
    fun `23) we can add face up DISADVANTAGE cards and they alter the odds`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(BAD), Card(GOOD, GOOD)))
        val deckWithTwoDisadvantages = deck.fixedModifiers(listOf(Card(BAD), Card(BAD)))
        assertEquals("Setup failed", 4, deckWithTwoDisadvantages.cards.size)
        // Then
        val odds = deckWithTwoDisadvantages.odds(2)
        assertEquals("Expect 6 permutations",6, odds.size())
        assertEquals("Expect 1 ADVANCE", 1, odds.results(ADVANCE))
        assertEquals("Expect 0 COMPLICATIONS", 0, odds.results(COMPLICATION))
        assertEquals("Expect 5 SETBACKS", 5, odds.results(SETBACK))
    }

    @Test
    fun `24) we can add face up ADVANTAGE cards and they alter the odds`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(BAD), Card(GOOD, GOOD)))
        val deckWithTwoDisadvantages = deck.fixedModifiers(listOf(Card(GOOD)))
        assertEquals("Setup failed", 4, deckWithTwoDisadvantages.cards.size)
        // Then
        val odds = deckWithTwoDisadvantages.odds(2)
        assertEquals("Expect 6 permutations",6, odds.size())
        assertEquals("Expect 5 ADVANCE", 5, odds.results(ADVANCE))
        assertEquals("Expect 0 COMPLICATIONS", 0, odds.results(COMPLICATION))
        assertEquals("Expect 1 SETBACKS", 1, odds.results(SETBACK))
    }

    @Test
    fun `25) neutral cards are definately affecting the odds`() {
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(), Card(BAD), Card(GOOD, GOOD)))
        assertEquals("Setup failed", 5, deck.cards.size)
        // Then
        val odds = deck.odds(2)
        assertEquals("Expect 10 permutations",10, odds.size())
        assertEquals("Expect 5 ADVANCE", 5, odds.results(ADVANCE))
        assertEquals("Expect 2 COMPLICATIONS", 2, odds.results(COMPLICATION))
        assertEquals("Expect 3 SETBACKS", 3, odds.results(SETBACK))
    }

    @Test
    fun `26) when you create a deck you can tell it one cards is enough for an ADVANCE or SETBACK`() {
        // and this flows down to the draws that are created and hence how they calculate results
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(), Card(BAD), Card(GOOD, GOOD)), cardsNeededForResult = 1)
        assertEquals("Setup failed", 5, deck.cards.size)
        // Then
        val odds = deck.odds(2)
        assertEquals("Expect 10 permutations",10, odds.size())
        assertEquals("Expect 5 ADVANCE", 5, odds.results(ADVANCE))
        assertEquals("Expect 2 COMPLICATIONS", 2, odds.results(COMPLICATION))
        assertEquals("Expect 3 SETBACKS", 3, odds.results(SETBACK))
    }

    @Test
    fun `26) when you create a deck you can tell it two cards are needed for an ADVANCE or SETBACK`() {
        // and this flows down to the draws that are created and hence how they calculate results
        //When
        val deck = Deck(listOf<Card>(Card(GOOD), Card(BAD), Card(), Card(BAD), Card(GOOD, GOOD)), cardsNeededForResult = 2)
        assertEquals("Setup failed", 5, deck.cards.size)
        // Then
        val odds = deck.odds(2)
        assertEquals("Expect 10 permutations",10, odds.size())
        assertEquals("Expect 2 ADVANCE", 2, odds.results(ADVANCE))
        assertEquals("Expect 7 COMPLICATIONS", 7, odds.results(COMPLICATION))
        assertEquals("Expect 1 SETBACKS", 1, odds.results(SETBACK))
    }

    // cut debug lines
    //println(deck)
    //println("Draws:" + deck.allPossibleDraws(2))
    //println(odds)
}