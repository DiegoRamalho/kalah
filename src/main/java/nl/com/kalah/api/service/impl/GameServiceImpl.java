package nl.com.kalah.api.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import nl.com.kalah.api.config.ApplicationProperties;
import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.domain.Player;
import nl.com.kalah.api.handler.RuleHandler;
import nl.com.kalah.api.rest.dto.GameDTO;
import nl.com.kalah.api.service.GameService;
import nl.com.kalah.api.util.Constants;

/**
 * Game Service implementation.
 *
 * @author Diego
 * @since 13/01/2020
 */
@Service
public class GameServiceImpl implements GameService {

    private static final Player NORTHERN_PLAYER = new Player().name("Northern player");
    private static final Player SOUTHERN_PLAYER = new Player().name("Southern player");
    private static final Logger LOG = LoggerFactory.getLogger(GameServiceImpl.class);

    protected final Map<Long, Game> gameMap;
    protected final List<RuleHandler> rules;
    private final ApplicationProperties applicationProperties;
    private final Environment environment;
    private final AtomicLong atomicLong;
    private final String baseUrl;

    public GameServiceImpl(final ApplicationProperties applicationProperties,
                           final Environment environment,
                           final List<RuleHandler> rules) {
        gameMap = new ConcurrentHashMap<>();
        atomicLong = new AtomicLong();
        this.applicationProperties = applicationProperties;
        this.rules = rules;
        this.environment = environment;
        baseUrl = getURL();
    }

    /**
     * Creates a new Game.
     *
     * The game has unique ID and {@see ApplicationProperties#getAmountOfStones} stones.
     *
     * @return GameDTO.
     */
    @Override
    public GameDTO create() {
        LOG.debug("Creating nem game");
        final Game game = new Game(applicationProperties.getAmountOfStones(), NORTHERN_PLAYER, SOUTHERN_PLAYER);
        final Long gameId = atomicLong.incrementAndGet();
        gameMap.put(gameId, game);
        return new GameDTO().id(gameId).url(baseUrl + gameId);
    }

    /**
     * Makes a move in the game.
     *
     * If the game ID is valid, the system apply all rules in the game and then return the changed game.
     *
     * @param gameId Game ID
     * @param pitId  The ID of the chosen pit.
     * @return GameDTO
     */
    @Override
    public GameDTO move(final Long gameId, final Integer pitId) {
        LOG.debug("Moving pit {} at game {}", pitId, gameId);
        Objects.requireNonNull(gameId, Constants.GAME_ID_NULL_ERROR);
        final Game game = gameMap.computeIfPresent(gameId, (key, value) -> {
            rules.forEach(ruleHandler -> ruleHandler.handle(value, pitId));
            return value;
        });
        if (Objects.isNull(game)) {
            throw new IllegalArgumentException(Constants.GAME_NOT_FOUND_ERROR);
        }
        return new GameDTO()
                .id(gameId)
                .url(baseUrl + gameId)
                .status(game.getStatus())
                .endGameMessage(getEndGameMessage(game));
    }

    /**
     * Invalidates the game.
     *
     * @param gameId Game ID.
     */
    @Override
    public void invalidate(final Long gameId) {
        LOG.debug("Invalidating the game {}", gameId);
        gameMap.remove(gameId);
    }

    /**
     * Invalidates all games.
     */
    @Override
    public void invalidateAll() {
        LOG.debug("Invalidating all games");
        gameMap.clear();
        atomicLong.set(0);
    }

    /**
     * Gets the end game message.
     *
     * @param game The current state of the game.
     * @return String
     */
    protected String getEndGameMessage(final Game game) {
        final String endGameMessage;
        if (Objects.nonNull(game.getWinner())) {
            endGameMessage = String.format(Constants.END_OF_THE_GAME_WINS, game.getWinner());
        } else if (game.isDraw()) {
            endGameMessage = Constants.END_OF_THE_GAME_DRAW;
        } else {
            endGameMessage = null;
        }
        return endGameMessage;
    }

    /**
     * Gets the URL.
     *
     * @return String
     */
    protected String getURL() {
        final String protocol = isHttp() ? "http" : "https";
        final String host = getHost();
        return String.format("%s://%s:%s/games/", protocol, host,
                             getPort());
    }

    /**
     * Gets the application port.
     *
     * @return String
     */
    protected String getPort() {
        return environment.getProperty("server.port");
    }

    /**
     * Gets the application host.
     *
     * @return String
     */
    protected String getHost() {
        String host = "localhost";
        try {
            host = getInetHost();
        } catch (final UnknownHostException exception) {
            LOG.error("Error getting local host.", exception);
        }
        return host;
    }

    /**
     * Get the Internet Protocol (IP) address.
     *
     * @return String
     */
    protected String getInetHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Is http?
     *
     * @return Is http?
     */
    protected boolean isHttp() {
        return Objects.isNull(environment.getProperty("server.ssl.key-store"));
    }
}
