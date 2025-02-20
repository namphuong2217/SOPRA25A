package service

import entity.UpAndDownGame
import gui.Refreshable

/**
 * This base service class provide access to all other services
 *
 * @property gameService The connected [GameService]
 * @property playerActionService The connected [PlayerActionService]
 * @property currentGame The currently active [entity.UpAndDownGame]
 */

class RootService {
    private val gameService = GameService(this)
    private val playerActionService = PlayerActionService(this)

    //The currently active game. Can be `null`, if no game has started yet.
    var currentGame: UpAndDownGame? = null

    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}
