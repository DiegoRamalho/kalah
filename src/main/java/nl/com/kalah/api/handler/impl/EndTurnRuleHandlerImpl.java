package nl.com.kalah.api.handler.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.domain.Pit;
import nl.com.kalah.api.domain.Player;

/**
 * Final phase that defines who is the next player and if the game ended.
 *
 * @author Diego
 * @since 13/01/2020
 */
@Service
public class EndTurnRuleHandlerImpl extends BaseRuleHandlerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(EndTurnRuleHandlerImpl.class);

    /**
     * Defines who is the next player and if the game ended. If the players last stone lands in his own Kalah, he gets another turn.
     * This can be repeated any number of times before it's the other player's turn. The game is over as soon as one of the sides run
     * out of stones. The player who still has stones in his/her pits keeps them and puts them in his/hers Kalah. The winner of the game
     * is the player who has the most stones in his Kalah.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    @Override
    protected void handleRequest(final Game game, final Integer pitId) {
        LOG.debug("Ending game turn");
        final Integer amountOfStonesCurrentPlayer = game.getAmountOfStonesFrom(game.getCurrentPlayer());
        final Integer amountOfStonesOtherPlayer = game.getAmountOfStonesFrom(game.getOtherPlayer());

        // If one of the players ran out of stones in his/her pits, the game end.
        if (Objects.equals(amountOfStonesCurrentPlayer, 0) ||
            Objects.equals(amountOfStonesOtherPlayer, 0)) {
            // Adds all stones in the players kalah
            final Integer totalStonesCurrentPlayer = game.getKalahFrom(game.getCurrentPlayer())
                    .addStones(amountOfStonesCurrentPlayer).getStones();
            final Integer totalStonesOtherPlayer = game.getKalahFrom(game.getOtherPlayer())
                    .addStones(amountOfStonesOtherPlayer).getStones();
            game.clearPits();

            // If they have the same amount of stones, it's a draw.
            if (Objects.equals(totalStonesCurrentPlayer, totalStonesOtherPlayer)) {
                game.setDraw(true);
            } else {
                // The player who has the most stones wins.
                final Player winner =
                        totalStonesCurrentPlayer > totalStonesOtherPlayer ? game.getCurrentPlayer() : game.getOtherPlayer();
                game.setWinner(winner);
            }
        } else {
            // Checks if the last sown pit was in his/her kalah
            final Pit lastPit = game.getLastSownPit();
            if (!lastPit.isKalah()) {
                game.changePlayers();
            }
            // Reset the last sown pit
            game.setLastSownPit(null);
        }
    }

    /**
     * Checks if the game is not over and there was sown.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    @Override
    protected boolean canHandleRequest(final Game game, final Integer pitId) {
        final Pit lastPit = game.getLastSownPit();
        return super.canHandleRequest(game, pitId) &&
               Objects.nonNull(lastPit);
    }

    /**
     * Returns the handle order. It must be the last handler to be executed.
     *
     * @return int
     * @see #END_TURN_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return END_TURN_PRECEDENCE;
    }
}
