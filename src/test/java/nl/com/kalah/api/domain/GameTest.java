package nl.com.kalah.api.domain;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import nl.com.kalah.api.util.Constants;

/**
 * Test class for the Game.
 *
 * @author Diego
 * @see Game
 * @since 13/01/2020
 */
public class GameTest {

    public static final Integer AMOUNT_OF_STONES = 6;
    public static final Player NORTHERN_PLAYER = new Player().name("Player 1");
    public static final Player SOUTHERN_PLAYER = new Player().name("Player 2");
    private static final Integer MIN_VALUE = 0;
    private static final Integer TOTAL_OF_STONES = AMOUNT_OF_STONES * 6;

    public static Game getBasicGame() {
        return new Game(AMOUNT_OF_STONES, NORTHERN_PLAYER, SOUTHERN_PLAYER);
    }

    @Test
    public void populateBoardTest() {
        final Game game = getBasicGame();
        Assert.assertSame("The current player should be the " + NORTHERN_PLAYER, NORTHERN_PLAYER, game.getCurrentPlayer());
        Assert.assertSame("The other player should be the " + SOUTHERN_PLAYER, SOUTHERN_PLAYER, game.getOtherPlayer());
        Assert.assertNull("The winner should be null", game.getWinner());
        Assert.assertNull("The last sown pit should be null", game.getLastSownPit());

        // Check the board
        Assert.assertEquals("The board size should be 14", 14, game.board.size());
        Assert.assertEquals("None of the pits should not have next", 0, game.board.stream()
                .filter(it -> Objects.isNull(it.getNext()))
                .collect(Collectors.toList()).size());
    }

    @Test
    public void statusTest() {
        final Game game = getBasicGame();

        // Check the status
        final Map<Integer, Integer> status = game.getStatus();
        Assert.assertNotNull("Status shouldn't be null", status);
        Assert.assertEquals("The board size should be 14", 14, status.size());
        Assert.assertEquals("The number of stones in the pit 1 should be " + AMOUNT_OF_STONES, AMOUNT_OF_STONES, status.get(1));
        Assert.assertEquals("The number of stones in the pit 7 should be " + MIN_VALUE, MIN_VALUE, status.get(7));
        Assert.assertEquals("The number of stones in the pit 13 should be " + AMOUNT_OF_STONES, AMOUNT_OF_STONES, status.get(13));
        Assert.assertEquals("The number of stones in the pit 14 should be " + MIN_VALUE, MIN_VALUE, status.get(14));
    }

    @Test
    public void getPitAtTest() {
        final Game game = getBasicGame();
        Assert.assertNull("Should not exists pit with ID 0", game.getPitAt(null));
        final int idBefore = Constants.INITIAL_PIT_ID - 1;
        Assert.assertNull("Should not exists pit with ID " + idBefore, game.getPitAt(idBefore));
        final int idAfter = Constants.FINAL_PIT_ID + 1;
        Assert.assertNull("Should not exists pit with ID " + idAfter, game.getPitAt(idAfter));
        for (Integer index = Constants.INITIAL_PIT_ID; index < Constants.FINAL_PIT_ID; index++) {
            final Pit pitAt = game.getPitAt(index);
            Assert.assertNotNull("Should exists pit with ID " + index, pitAt);
            Assert.assertEquals("Pit ID should match index" + index, index, pitAt.getId());
        }
    }

    @Test
    public void getKalahFromTest() {
        final Game game = getBasicGame();
        final Pit northernKalahPit = game.getKalahFrom(NORTHERN_PLAYER);
        final Pit southernKalahPit = game.getKalahFrom(SOUTHERN_PLAYER);
        Assert.assertNotNull("Northern Kalah should not be null", northernKalahPit);
        Assert.assertNotNull("Southern Kalah should not be null", southernKalahPit);

        Assert.assertNotEquals("Southern Kalah should not be equals to Northern Kalah", northernKalahPit, southernKalahPit);
        Assert.assertEquals("Northern Kalah ID should be " + Constants.NORTHERN_KALAH_ID, Constants.NORTHERN_KALAH_ID,
                            northernKalahPit.getId());
        Assert.assertEquals("Southern Kalah ID should be " + Constants.SOUTHERN_KALAH_ID, Constants.SOUTHERN_KALAH_ID,
                            southernKalahPit.getId());
    }

    @Test
    public void getAmountOfStonesFromTest() {
        final Game game = getBasicGame();
        Assert.assertEquals("Amount of stones should be 18", TOTAL_OF_STONES, game.getAmountOfStonesFrom(NORTHERN_PLAYER));
        Assert.assertEquals("Amount of stones should be 18", TOTAL_OF_STONES, game.getAmountOfStonesFrom(SOUTHERN_PLAYER));

        // Adds a new stone in the northern kalah
        final Integer amountOfStones = 1;
        final Pit northernKalahPit = game.getKalahFrom(NORTHERN_PLAYER)
                .addStones(amountOfStones);
        final Pit southernKalahPit = game.getKalahFrom(SOUTHERN_PLAYER);
        Assert.assertEquals("Amount of stones in the northern kalah should be 1", amountOfStones, northernKalahPit.getStones());
        Assert.assertEquals("Amount of stones in the southern kalah should be 0", MIN_VALUE, southernKalahPit.getStones());

        Assert.assertEquals("Amount of stones should be 18", TOTAL_OF_STONES, game.getAmountOfStonesFrom(NORTHERN_PLAYER));
        Assert.assertEquals("Amount of stones should be 18", TOTAL_OF_STONES, game.getAmountOfStonesFrom(SOUTHERN_PLAYER));
    }

    @Test
    public void clearPitsTest() {
        final Game game = getBasicGame();

        // Adds a new stone in the northern kalah
        final Integer amountOfStones = 1;
        final Pit northernKalahPit = game.getKalahFrom(NORTHERN_PLAYER)
                .stones(amountOfStones);
        Assert.assertEquals("Amount of stones in the northern kalah should be 1", amountOfStones, northernKalahPit.getStones());

        game.clearPits();
        Assert.assertEquals("Amount of stones should be 0", MIN_VALUE, game.getAmountOfStonesFrom(NORTHERN_PLAYER));
        Assert.assertEquals("Amount of stones should be 0", MIN_VALUE, game.getAmountOfStonesFrom(SOUTHERN_PLAYER));

        Assert.assertEquals("Amount of stones in the northern kalah should be 1", amountOfStones, northernKalahPit.getStones());
    }

    @Test
    public void changePlayersTest() {
        final Game game = getBasicGame();
        game.changePlayers();
        Assert.assertSame("The current player should be the " + SOUTHERN_PLAYER.getName(), SOUTHERN_PLAYER, game.getCurrentPlayer());
        Assert.assertSame("The other player should be the " + NORTHERN_PLAYER.getName(), NORTHERN_PLAYER, game.getOtherPlayer());
    }
}