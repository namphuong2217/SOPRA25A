package entity
/**
 * Represents a card in a deck of cards.
 *
 * @property suit The [CardSuit] of the card
 * @property value The [CardValue] of the card
 */

class Card(val suit: CardSuit, val value: CardValue){

    //return a string representation of the card
    override fun toString() = "$suit$value"
}

