package nl.com.kalah.api.handler.impl;

import java.util.Objects;

import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.handler.RuleHandler;

/**
 * Abstract class that defines the flow of request handling.
 *
 * @author Diego
 * @since 13/01/2020
 */
public abstract class BaseRuleHandlerImpl implements RuleHandler {

    /**
     * Handle the current request.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    protected abstract void handleRequest(final Game game, final Integer pitId);

    /**
     * Checks if the game is not over.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     * @return Is the game not over?
     */
    protected boolean canHandleRequest(final Game game, final Integer pitId) {
        return Objects.isNull(game.getWinner()) && !game.isDraw();
    }

    /**
     * Handles the game request.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    @Override
    public void handle(final Game game, final Integer pitId) {
        if (canHandleRequest(game, pitId)) {
            handleRequest(game, pitId);
        }
    }
}
