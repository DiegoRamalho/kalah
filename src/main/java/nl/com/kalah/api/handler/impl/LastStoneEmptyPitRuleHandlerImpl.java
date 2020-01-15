package nl.com.kalah.api.handler.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.domain.Pit;
import nl.com.kalah.api.util.Constants;

/**
 * Handler that checks if the last stone lands in an own empty pit.
 *
 * @author Diego
 * @since 13/01/2020
 */
@Service
public class LastStoneEmptyPitRuleHandlerImpl extends BaseRuleHandlerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(LastStoneEmptyPitRuleHandlerImpl.class);

    /**
     * When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the other
     * players' pit) and puts them in his own Kalah.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    @Override
    protected void handleRequest(final Game game, final Integer pitId) {
        LOG.debug("Checks if the last stone lands in an own empty pit in the game {}.", game);
        final Pit lastPit = game.getLastSownPit();
        final Pit kalah = game.getKalahFrom(game.getCurrentPlayer());
        final Pit oppositePit = game.getPitAt(Constants.FINAL_PIT_ID - lastPit.getId());

        // Adds stones in the current player kalah
        kalah.addStones(lastPit.getStones())
                .addStones(oppositePit.getStones());

        // Removes stones from the last pit and the opposite pit.
        lastPit.addStones(-lastPit.getStones());
        oppositePit.addStones(-oppositePit.getStones());
    }

    /**
     * Checks if the game is not over and the last stone lands in an own empty pit.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    @Override
    protected boolean canHandleRequest(final Game game, final Integer pitId) {
        final Pit lastPit = game.getLastSownPit();
        return super.canHandleRequest(game, pitId) &&
               Objects.nonNull(lastPit) &&
               !lastPit.isKalah() &&
               Objects.equals(lastPit.getStones(), 1) &&
               Objects.equals(lastPit.getPlayer(), game.getCurrentPlayer());
    }

    /**
     * Returns the handle order. It must be executed after the sows method and before the end turn method.
     *
     * @return int
     * @see #VALIDATION_AFTER_SOWS_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return VALIDATION_AFTER_SOWS_PRECEDENCE;
    }
}
