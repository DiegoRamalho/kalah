package nl.com.kalah.api.rest.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.com.kalah.api.rest.GameRestService;
import nl.com.kalah.api.rest.dto.GameDTO;
import nl.com.kalah.api.service.GameService;
import nl.com.kalah.api.util.Constants;

/**
 * Game Rest Service Implementation.
 *
 * @author Diego
 * @since 13/01/2020
 */
@RestController
@RequestMapping("/games")
public class GameRestServiceImpl implements GameRestService {

    private static final Logger LOG = LoggerFactory.getLogger(GameRestServiceImpl.class);
    private final GameService gameService;

    public GameRestServiceImpl(final GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Creates a new game.
     *
     * @return GameDTO
     */
    @Override
    public ResponseEntity<GameDTO> create() throws URISyntaxException {
        LOG.debug("Request to create a new game");
        final GameDTO game = gameService.create();
        return ResponseEntity
                .created(new URI(game.getUrl()))
                .body(game);
    }

    /**
     * Makes a move in the game.
     *
     * @param gameId Game ID
     * @param pitId  The ID of the chosen pit.
     * @return GameDTO
     */
    @Override
    public ResponseEntity<GameDTO> move(final String gameId, final String pitId) {
        LOG.debug("Request to move game {} at {}", gameId, pitId);
        return ResponseEntity.ok(gameService.move(getGameIdLong(gameId), getPitIdInt(pitId)));
    }

    /**
     * Deletes the game {@param gameId}.
     *
     * @param gameId Game ID
     * @return Game delete with success.
     */
    @Override
    public ResponseEntity<Void> delete(final String gameId) {
        LOG.debug("Request to delete the game {}", gameId);
        gameService.invalidate(getGameIdLong(gameId));
        return ResponseEntity.ok().build();
    }

    /**
     * Delete all games.
     *
     * @return All games deleted with success.
     */
    @Override
    public ResponseEntity<Void> deleteAll() {
        LOG.debug("Request to delete all games");
        gameService.invalidateAll();
        return ResponseEntity.ok().build();
    }

    /**
     * Converts the game ID to Long.
     *
     * @param gameId Game ID
     * @return Long
     */
    protected Long getGameIdLong(final String gameId) {
        if (!NumberUtils.isDigits(gameId)) {
            throw new IllegalArgumentException(Constants.GAME_ID_NOT_NUMBER_ERROR);
        }
        return Long.valueOf(gameId);
    }

    /**
     * Converts the pit ID to Integer
     *
     * @param pitId The ID of the chosen pit.
     * @return Integer
     */
    protected Integer getPitIdInt(final String pitId) {
        if (!NumberUtils.isDigits(pitId)) {
            throw new IllegalArgumentException(Constants.PIT_ID_NOT_NUMBER_ERROR);
        }
        return Integer.valueOf(pitId);
    }
}
