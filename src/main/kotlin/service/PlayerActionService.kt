package service

import entity.*


/**
 * PlayerActionService describes all possible actions of a specific player, such as
 * play a card, draw a card, change hand cards, pass
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * play a card
     * if a player has a valid card on their hand, they may place it face-up onto one of the two central piles
     * @throws IllegalArgumentException if the card is not valid
     */


    fun playCard(card : Card) {
        //Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        val cardValid1 = isValidCard1(card)
        val cardValid2 = isValidCard2(card)
        require(cardValid1)
        require(cardValid2)

        //add the valid card to center deck 1
        if (cardValid1) {
            actPlayer.handCard.remove(card)
            game.centerDeck1.add(card)

        //add the valid card to center deck 2
        } else if (cardValid2) {
            actPlayer.handCard.remove(card)
            game.centerDeck2.add(card)
        } else {
            IllegalArgumentException("The card is not valid!")
        }
    }

    /**
     * draw a card
     * player has at least 9 hand cards and their own draw pile is not empty,
     * they may draw one card from their own draw pile
     *
     */
    fun drawCard() {
        //Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        if (actPlayer.drawDeck.isNotEmpty()) {
            if (actPlayer.handCard.size <= 9) {

                //Get the top card of the draw stack and add it to the current player's hand
                val drawnCard = actPlayer.drawDeck.removeFirst()
                actPlayer.handCard.add(drawnCard)
            } else {
                throw IllegalArgumentException("The player already has 9 cards in their hand!")
            }
        } else {
            throw IllegalArgumentException("The draw stack is empty!")
        }
    }

    /**
     * Replace hand cards
     * if player has at least 8 hand cards and their own draw pile is not empty
     * they may have all their hand cards put back into their draw pile,
     * then get 5 new cards from the shuffled draw pile
     *
     */

    fun redrawHand() {
        //Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

        if (actPlayer.handCard.size >= 8 &&
            actPlayer.drawDeck.isNotEmpty()
        ) {
            //Player puts all his hand cards into his own draw pile

            //HMMMM not yettttt
            val putCard = actPlayer.drawDeck.add(actPlayer.handCard)

            val totalShuffledCards = putCard.shufflle()
            actPlayer.handCard.clear()
        }
        repeat(5) {
            actPlayer.handCard.add(actPlayer.drawDeck.drawCard())
        }
    }


    /**
     * Pass
     */

    fun pass() {
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
        val actPlayer = game.players[game.currentPlayer]

    }

    //valid for center deck 1?
    fun isValidCard1(card: Card): Boolean {
        //Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }

        //val actPlayer = game.players[game.currentPlayer]

        val cardValues = CardValue.entries
        val topPileCard = game.centerDeck1.first()
        val topPileCardIndex = cardValues.indexOf(topPileCard.value)
        val cardIndex = cardValues.indexOf(card.value)

        /**
         * setOf() : returns a new read-only set with the given elements.
         */

        // 1 level
        val oneLevel = setOf(
            (topPileCardIndex + 1) % cardValues.size, //1 level up
            (topPileCardIndex - 1) % cardValues.size //1 level down
        )
        // 2 levels

        val twoLevels = if (card.suit == topPileCard.suit) {
            setOf(
                (topPileCardIndex + 2) % cardValues.size, //2 levels up
                (topPileCardIndex - 2) % cardValues.size //2 levels down
            )
        } else {
            emptySet()
        }

        //check if the cardIndex matches a valid move
        return cardIndex in (oneLevel + twoLevels)
    }

    //valid for center deck 1?
    fun isValidCard2(card: Card): Boolean {
        //Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }

        //val actPlayer = game.players[game.currentPlayer]

        val cardValues = CardValue.entries
        val topPileCard = game.centerDeck2.first()
        val topPileCardIndex = cardValues.indexOf(topPileCard.value)
        val cardIndex = cardValues.indexOf(card.value)

        /**
         * setOf() : returns a new read-only set with the given elements.
         */

        // 1 level
        val oneLevel = setOf(
            (topPileCardIndex + 1) % cardValues.size, //1 level up
            (topPileCardIndex - 1) % cardValues.size //1 level down
        )
        // 2 levels

        val twoLevels = if (card.suit == topPileCard.suit) {
            setOf(
                (topPileCardIndex + 2) % cardValues.size, //2 levels up
                (topPileCardIndex - 2) % cardValues.size //2 levels down
            )
        } else {
            emptySet()
        }

        //check if the cardIndex matches a valid move
        return cardIndex in (oneLevel + twoLevels)
    }

}



