package nl.com.kalah.api.handler.impl;

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
 * Test class for the LastStoneEmptyPitRuleHandlerImpl.
 *
 * @author Diego
 * @see LastStoneEmptyPitRuleHandlerImpl
 * @since 13/01/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KalahApplication.class)
public class LastStoneEmptyPitRuleHandlerImplTest {

    @Autowired
    private LastStoneEmptyPitRuleHandlerImpl handler;

    @Test
    public void canHandleRequestNoMoveDoneTest() {
        final Game game = GameTest.getBasicGame();
        Assert.assertFalse("Shouldn't handler request without previous move", handler.canHandleRequest(game, 1));
    }

    @Test
    public void canHandleRequestWithWinnerTest() {
        final Game game = GameTest.getBasicGame();
        game.setWinner(GameTest.NORTHERN_PLAYER);
        Assert.assertFalse("Shouldn't handler request with a draw", handler.canHandleRequest(game, 1));
    }

    @Test
    public void canHandleRequestKalahTest() {
        final Game game = GameTest.getBasicGame();
        game.setLastSownPit(game.getKalahFrom(GameTest.NORTHERN_PLAYER));
        Assert.assertFalse("Shouldn't handler request that end in a kalah",
                           handler.canHandleRequest(game, Constants.NORTHERN_KALAH_ID));
    }

    @Test
    public void canHandleRequestMoreThanOneStoneTest() {
        final Game game = GameTest.getBasicGame();
        game.setLastSownPit(game.getPitAt(1));
        Assert.assertFalse("Shouldn't handler request that ends in a pit with more than 1 stone", handler.canHandleRequest(game, 1));
    }

    @Test
    public void canHandleRequestLastStoneInOtherPlayerTest() {
        final Game game = GameTest.getBasicGame();
        game.clearPits();
        game.setLastSownPit(game.getPitAt(Constants.FINAL_PIT_ID - 1).stones(1));
        game.setLastSownPit(game.getPitAt(Constants.FINAL_PIT_ID - 1));
        Assert.assertFalse("Shouldn't handler request that ends in a other player pit",
                           handler.canHandleRequest(game, Constants.FINAL_PIT_ID - 1));
    }

    @Test
    public void canHandleRequestValidTest() {
        final Game game = GameTest.getBasicGame();
        game.setLastSownPit(game.getPitAt(1).stones(1));
        Assert.assertTrue("Should handler valid request",
                          handler.canHandleRequest(game, 1));
    }

    @Test
    public void handleRequestValidTest() {
        final Game game = GameTest.getBasicGame();
        final Pit pit1 = game.getPitAt(1).stones(1);
        final Pit finalPit = game.getPitAt(Constants.FINAL_PIT_ID - 1).stones(12);
        game.setLastSownPit(pit1);
        handler.handleRequest(game, 1);
        Assert.assertSame("Northern kalah should had 13 stones", 13, game.getPitAt(Constants.NORTHERN_KALAH_ID).getStones());
        Assert.assertSame("Pit 1 should had 0 stones", 0, pit1.getStones());
        Assert.assertSame("Pit 13 kalah should had 0 stones", 0, finalPit.getStones());
    }
}