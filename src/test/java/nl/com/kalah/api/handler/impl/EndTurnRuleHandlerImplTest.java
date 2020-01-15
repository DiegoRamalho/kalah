package nl.com.kalah.api.handler.impl;

import static nl.com.kalah.api.domain.GameTest.NORTHERN_PLAYER;
import static nl.com.kalah.api.domain.GameTest.SOUTHERN_PLAYER;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.com.kalah.api.KalahApplication;
import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.domain.GameTest;
import nl.com.kalah.api.domain.Pit;
import nl.com.kalah.api.util.Constants;

/**
 * Test class for the EndTurnRuleHandlerImpl.
 *
 * @author Diego
 * @see EndTurnRuleHandlerImpl
 * @since 13/01/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KalahApplication.class)
public class EndTurnRuleHandlerImplTest {

    @Autowired
    private EndTurnRuleHandlerImpl handler;

    @Test
    public void canHandleRequestNoMoveDoneTest() {
        final Game game = GameTest.getBasicGame();
        Assert.assertFalse("Shouldn't handler request without previous move", handler.canHandleRequest(game, 1));
    }

    @Test
    public void canHandleRequestWithWinnerTest() {
        final Game game = GameTest.getBasicGame();
        game.setWinner(NORTHERN_PLAYER);
        Assert.assertFalse("Shouldn't handler request with a winner", handler.canHandleRequest(game, 1));
    }

    @Test
    public void canHandleRequestWithDrawTest() {
        final Game game = GameTest.getBasicGame();
        game.setDraw(true);
        Assert.assertFalse("Shouldn't handler request with a draw", handler.canHandleRequest(game, 1));
    }

    @Test
    public void handleRequestLastStonePitTest() {
        final Game game = GameTest.getBasicGame();
        Assert.assertSame("The current player should be the " + NORTHERN_PLAYER, NORTHERN_PLAYER, game.getCurrentPlayer());
        // Simulates the movement
        game.setLastSownPit(game.getPitAt(1));
        handler.handleRequest(game, 1);
        // Changes the current player
        Assert.assertSame("The current player should be the " + SOUTHERN_PLAYER, SOUTHERN_PLAYER, game.getCurrentPlayer());
    }

    @Test
    public void handleRequestLastStoneKalahTest() {
        final Game game = GameTest.getBasicGame();
        Assert.assertSame("The current player should be the " + NORTHERN_PLAYER, NORTHERN_PLAYER, game.getCurrentPlayer());
        // Simulates the movement
        game.setLastSownPit(game.getKalahFrom(NORTHERN_PLAYER));
        handler.handleRequest(game, Constants.NORTHERN_KALAH_ID);
        // Don't change the current player
        Assert.assertSame("The current player should be the " + NORTHERN_PLAYER, NORTHERN_PLAYER, game.getCurrentPlayer());
    }

    @Test
    public void handleRequestCurrentPlayerWinsTest() {
        final Game game = GameTest.getBasicGame();
        game.clearPits();
        // Simulates the movement
        // Northern/current player run out of stones
        final Pit northernKalah = game.getKalahFrom(NORTHERN_PLAYER);
        northernKalah.stones(10);
        game.getPitAt(1).stones(2);

        final Pit southernKalah = game.getKalahFrom(SOUTHERN_PLAYER);
        southernKalah.stones(5);

        game.setLastSownPit(northernKalah);

        handler.handleRequest(game, Constants.NORTHERN_KALAH_ID);

        Assert.assertSame("The southern kalah should have 5 stones", 5, southernKalah.getStones());
        Assert.assertSame("The northern kalah should have 12 stones", 12, northernKalah.getStones());
        Assert.assertSame("The southern player should have 0 stones in his/her pits", 0, game
                .getAmountOfStonesFrom(SOUTHERN_PLAYER));
        Assert.assertSame("The northern kalah should have 0 stones in his/her pits", 0, game
                .getAmountOfStonesFrom(NORTHERN_PLAYER));

        Assert.assertSame("The winning player should be the " + NORTHERN_PLAYER, NORTHERN_PLAYER, game.getWinner());
    }

    @Test
    public void handleRequestOtherPlayerWinsTest() {
        final Game game = GameTest.getBasicGame();
        game.clearPits();
        game.changePlayers();
        // Simulates the movement
        // Southern/current player run out of stones
        final Pit northernKalah = game.getKalahFrom(NORTHERN_PLAYER);
        northernKalah.stones(10);

        final Pit southernKalah = game.getKalahFrom(SOUTHERN_PLAYER);
        southernKalah.stones(5);
        game.getPitAt(Constants.FINAL_PIT_ID - 1).stones(2);

        game.setLastSownPit(southernKalah);

        handler.handleRequest(game, Constants.FINAL_PIT_ID);

        Assert.assertSame("The southern kalah should have 7 stones", 7, southernKalah.getStones());
        Assert.assertSame("The northern kalah should have 10 stones", 10, northernKalah.getStones());
        Assert.assertSame("The southern player should have 0 stones in his/her pits", 0, game
                .getAmountOfStonesFrom(SOUTHERN_PLAYER));
        Assert.assertSame("The northern kalah should have 0 stones in his/her pits", 0, game
                .getAmountOfStonesFrom(NORTHERN_PLAYER));

        Assert.assertSame("The winning player should be the " + NORTHERN_PLAYER, NORTHERN_PLAYER, game.getWinner());
    }

    @Test
    public void handleRequestDrawTest() {
        final Game game = GameTest.getBasicGame();
        game.clearPits();

        // Simulates the movement
        game.setLastSownPit(game.getPitAt(1));
        handler.handleRequest(game, 1);

        Assert.assertTrue("It should be a draw", game.isDraw());
    }
}