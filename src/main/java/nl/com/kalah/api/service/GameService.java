package nl.com.kalah.api.service;

import nl.com.kalah.api.rest.dto.GameDTO;

/**
 * Game Service interface.
 *
 * @author Diego
 * @since 13/01/2020
 */
public interface GameService {

    /**
     * Creates a new game.
     *
     * @return GameDTO
     */
    GameDTO create();

    /**
     * Makes a move in the game.
     *
     * @param gameId Game ID
     * @param pitId  The ID of the chosen pit.
     * @return GameDTO
     */
    GameDTO move(final Long gameId, final Integer pitId);

    /**
     * Invalidates the game.
     *
     * @param gameId Game ID.
     */
    void invalidate(final Long gameId);

    /**
     * Invalidates all games.
     */
    void invalidateAll();
}
