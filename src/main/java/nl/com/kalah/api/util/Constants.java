package nl.com.kalah.api.util;

/**
 * Class with application constants.
 *
 * @author Diego
 * @since 13/01/2020
 */
public final class Constants {

    public static final Integer INITIAL_PIT_ID = 1;
    public static final Integer FINAL_PIT_ID = 14;
    public static final Integer SOUTHERN_KALAH_ID = FINAL_PIT_ID;
    public static final Integer NORTHERN_KALAH_ID = FINAL_PIT_ID / 2;

    // Error messages
    public static final String GAME_ID_NOT_NUMBER_ERROR = "Error: Game ID should be a number";
    public static final String PIT_ID_NOT_NUMBER_ERROR = "Error: Pit ID should be a number";
    public static final String PIT_ID_NULL_ERROR = "Error: Pit ID shouldn't be null.";
    public static final String GAME_NULL_ERROR = "Error: Game shouldn't be null.";
    public static final String PIT_ID_INVALID_ERROR = "Error: Invalid pit ID (%d). Pit ID must be between %d and %d.";
    public static final String INVALID_PIT_PLAYER_ERROR = "Error: This pit doesn't belong to the current player.";
    public static final String KALAH_ID_ERROR = "Error: You can't sows from your Kalah.";
    public static final String PIT_WITHOUT_STONES_ERROR = "Error: You can't sows from this pit because there is no stone at it.";
    public static final String END_OF_THE_GAME_DRAW = "End of the game: it was a draw!";
    public static final String END_OF_THE_GAME_WINS = "End of the game: %s wins!";
    public static final String GAME_NOT_FOUND_ERROR = "Error: Game not found.";
    public static final String GAME_ID_NULL_ERROR = "Error: Game ID shouldn't be null.";
    private static final String PREFIX = "\n* ";
    public static final String ALL_ERRORS_FMT = PREFIX + GAME_ID_NOT_NUMBER_ERROR
                                                + PREFIX + PIT_ID_NOT_NUMBER_ERROR
                                                + PREFIX + PIT_ID_NULL_ERROR
                                                + PREFIX + GAME_NULL_ERROR
                                                + PREFIX + PIT_ID_INVALID_ERROR
                                                + PREFIX + INVALID_PIT_PLAYER_ERROR
                                                + PREFIX + KALAH_ID_ERROR
                                                + PREFIX + GAME_NOT_FOUND_ERROR
                                                + PREFIX + GAME_ID_NULL_ERROR
                                                + PREFIX + PIT_WITHOUT_STONES_ERROR;

    private Constants() {
    }
}
