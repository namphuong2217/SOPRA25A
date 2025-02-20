package service

import entity.*


class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Open game: player gives name and click start
     */
    fun startUpAnDown(players: List<String>){
        check(rootService.currentGame == null) { "There is already a game running!" }
        check(players.size == 2) { "A game includes only 2 players!" }


        //later
//        onAllRefreshables {
//            refreshAfterGameStart()
//        }
    }

    /**
     * 1. Start new game
     */

    fun startNewGame(players: List<String>) {

        //create deck : 52 cards
        val deck = createDeck()

        //divide the deck into 2 piles, each pile consists of 26 cards
        val halfDeck1 = deck.subList(0, 26).toMutableList()
        val halfDeck2 = deck.subList(26, 52).toMutableList()

        //hand out 5 card for each player
        val handCard1 = mutableListOf<Card>()
        repeat(5) { handCard1.add(drawOneCard(halfDeck1)!!) } //!! to avoid null

        val handCard2 = mutableListOf<Card>()
        repeat(5) { handCard2.add(drawOneCard(halfDeck2)!!) } //!! to avoid null

        //create 2 central piles
        val centerDeck1 = mutableListOf(drawOneCard(halfDeck1)!!) //!! to avoid null
        val centerDeck2 = mutableListOf(drawOneCard(halfDeck2)!!) //!! to avoid null

        //draw deck for player is the remaining
        val drawDeck1 = halfDeck1
        val drawDeck2 = halfDeck2

        val player1 = Player(players[0], handCard1, drawDeck1)
        val player2 = Player(players[1], handCard2, drawDeck2)
        val playerList = listOf(player1, player2)


        //currentPlayer: index of the currently active player. By start game player1
        val currentPlayer = playerList.indexOf(player1)

        val game = UpAndDownGame(
            players = playerList,
            currentPlayer = currentPlayer,
            lastPass = false,
            centerDeck1 = centerDeck1,
            centerDeck2 = centerDeck2
        )

        //set the current game of the root service to the created game
        rootService.currentGame = game

        //Trigger refresh to tell the GUI that a new game has started

    }

    /**
     * create a shuffled deck of 52 cards
     */
    fun createDeck(): MutableList<Card> {
        val deck = mutableListOf<Card>()
        for (suit in CardSuit.values()) {
            for (value in CardValue.values()) {
                deck.add(Card(suit, value))
            }
        }
        deck.shuffle()
        return deck
    }

    /**
     * remove 1 card from the pile,
     * can return null when there is no card left in the pile
     */
    fun drawOneCard(drawPile: MutableList<Card>): Card? {
        return if (drawPile.isNotEmpty()) drawPile.removeAt(0) else null
    }

    /**
     * 2. Choose starting player => no need? cuz in startGame let player1 as starting player
     */

    /**
     * 3. Create central pile => no need
     */

    /**
     * 4. Switch player turn : change to next player
     */
    fun startTurn() {
        //Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }

        //caculate the index next player
        val currentPlayer = (game.currentPlayer + 1) % game.players.size

        //update game to other player
        game.currentPlayer = currentPlayer

        //update GUI => later
    }

    /**
     * Show Winner
     * when one of the winner pass
     */

    //NOT YET
    fun showWinner() {
        //Get current game and check if it is currently running
        val game = checkNotNull(rootService.currentGame) { "No game is currently running!" }
    }


}