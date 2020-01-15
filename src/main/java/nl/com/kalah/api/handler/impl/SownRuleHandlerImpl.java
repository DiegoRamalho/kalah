package nl.com.kalah.api.handler.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.domain.Pit;

/**
 * Second handle that sows the stones on to the right, one in each of the following pits.
 *
 * @author Diego
 * @since 13/01/2020
 */
@Service
public class SownRuleHandlerImpl extends BaseRuleHandlerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(SownRuleHandlerImpl.class);

    /**
     * Sows the stones on to the right, one in each of the following pits. No stones are put in the opponent's' Kalah.
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    @Override
    protected void handleRequest(final Game game, final Integer pitId) {
        LOG.debug("Sown the pit ID {} in the game {}.", pitId, game);
        Pit currentPit = game.getPitAt(pitId);
        // Gets all stones from the current pit and...
        Integer numberOfStones = currentPit.getStones();
        currentPit.addStones(-numberOfStones);

        // ... sows the stones on to the right
        while (numberOfStones > 0) {
            currentPit = currentPit.getNext();
            // Just do not sow the stone in the other player's kalah
            if (!currentPit.isKalah() ||
                Objects.equals(currentPit.getPlayer(), game.getCurrentPlayer())) {
                currentPit.addStones(1);
                numberOfStones--;
            }
        }
        game.setLastSownPit(currentPit);
    }

    /**
     * Returns the handle order. It must be executed before the sows method.
     *
     * @return int
     * @see #SOWS_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return SOWS_PRECEDENCE;
    }
}
