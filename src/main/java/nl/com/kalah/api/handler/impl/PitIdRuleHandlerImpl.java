package nl.com.kalah.api.handler.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.domain.Pit;
import nl.com.kalah.api.util.Constants;

/**
 * Handle that checks if the chosen pit ID is valid.
 *
 * @author Diego
 * @since 13/01/2020
 */
@Service
public class PitIdRuleHandlerImpl extends BaseRuleHandlerImpl {

    private static final Logger LOG = LoggerFactory.getLogger(PitIdRuleHandlerImpl.class);

    /**
     * Checks if the {@param pitId} is valid.
     * <p>The pit ID is valid if it:
     * <ul>
     * <li>is non null;</li>
     * <li>is between {@link Constants#INITIAL_PIT_ID} and {@link Constants#FINAL_PIT_ID};</li>
     * <li>belong to the current player;</li>
     * <li>is not the player kalah;</li>
     * <li>has at least one stone.</li>
     * </ul>
     * </p>
     *
     * @param game  The game.
     * @param pitId The ID of the chosen pit.
     */
    @Override
    protected void handleRequest(final Game game, final Integer pitId) {
        LOG.debug("Checks the validity of the pit ID {} in the game {}.", pitId, game);
        Objects.requireNonNull(game, Constants.GAME_NULL_ERROR);
        Objects.requireNonNull(pitId, Constants.PIT_ID_NULL_ERROR);
        final Pit currentPit = game.getPitAt(pitId);
        if (Objects.isNull(currentPit)) {
            throw new IllegalArgumentException(
                    String.format(Constants.PIT_ID_INVALID_ERROR, pitId, Constants.INITIAL_PIT_ID, Constants.FINAL_PIT_ID));
        }
        if (!Objects.equals(currentPit.getPlayer(), game.getCurrentPlayer())) {
            throw new IllegalArgumentException(Constants.INVALID_PIT_PLAYER_ERROR);
        }
        if (Objects.equals(pitId, Constants.NORTHERN_KALAH_ID) ||
            Objects.equals(pitId, Constants.SOUTHERN_KALAH_ID)) {
            throw new IllegalArgumentException(Constants.KALAH_ID_ERROR);
        }
        if (currentPit.getStones() <= 0) {
            throw new IllegalArgumentException(Constants.PIT_WITHOUT_STONES_ERROR);
        }
    }

    /**
     * Returns the handle order. It's must be executed before the sows method.
     *
     * @return int
     * @see #VALIDATION_BEFORE_SOWS_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return VALIDATION_BEFORE_SOWS_PRECEDENCE;
    }
}
