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

/**
 * Test class for the SownRuleHandlerImpl.
 *
 * @author Diego
 * @see SownRuleHandlerImpl
 * @since 13/01/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KalahApplication.class)
public class SownRuleHandlerImplTest {

    @Autowired
    private SownRuleHandlerImpl handler;

    @Test
    public void handleRequestValidTest() {
        final Game game = GameTest.getBasicGame();
        handler.handleRequest(game, 3);
        Assert.assertSame("Pit 1 should had 6 stones.", 6, game.getPitAt(1).getStones());
        Assert.assertSame("Pit 2 should had 6 stones.", 6, game.getPitAt(2).getStones());
        Assert.assertSame("Pit 3 should had 0 stones.", 0, game.getPitAt(3).getStones());
        Assert.assertSame("Pit 4 should had 7 stones.", 7, game.getPitAt(4).getStones());
        Assert.assertSame("Pit 5 should had 7 stones.", 7, game.getPitAt(5).getStones());
        Assert.assertSame("Pit 6 should had 7 stones.", 7, game.getPitAt(6).getStones());
        Assert.assertSame("Pit 7 should had 1 stones.", 1, game.getPitAt(7).getStones());
        Assert.assertSame("Pit 8 should had 7 stones.", 7, game.getPitAt(8).getStones());
        Assert.assertSame("Pit 9 should had 7 stones.", 7, game.getPitAt(9).getStones());
        Assert.assertSame("Pit 10 should had 6 stones.", 6, game.getPitAt(10).getStones());
        Assert.assertSame("Pit 11 should had 6 stones.", 6, game.getPitAt(11).getStones());
        Assert.assertSame("Pit 12 should had 6 stones.", 6, game.getPitAt(12).getStones());
        Assert.assertSame("Pit 13 should had 6 stones.", 6, game.getPitAt(13).getStones());
        Assert.assertSame("Pit 14 should had 0 stones.", 0, game.getPitAt(14).getStones());
        handler.handleRequest(game, 8);
        Assert.assertSame("Pit 1 should had 7 stones.", 7, game.getPitAt(1).getStones());
        Assert.assertSame("Pit 2 should had 7 stones.", 7, game.getPitAt(2).getStones());
        Assert.assertSame("Pit 3 should had 0 stones.", 0, game.getPitAt(3).getStones());
        Assert.assertSame("Pit 4 should had 7 stones.", 7, game.getPitAt(4).getStones());
        Assert.assertSame("Pit 5 should had 7 stones.", 7, game.getPitAt(5).getStones());
        Assert.assertSame("Pit 6 should had 7 stones.", 7, game.getPitAt(6).getStones());
        Assert.assertSame("Pit 7 should had 1 stones.", 1, game.getPitAt(7).getStones());
        Assert.assertSame("Pit 8 should had 0 stones.", 0, game.getPitAt(8).getStones());
        Assert.assertSame("Pit 9 should had 8 stones.", 8, game.getPitAt(9).getStones());
        Assert.assertSame("Pit 10 should had 7 stones.", 7, game.getPitAt(10).getStones());
        Assert.assertSame("Pit 11 should had 7 stones.", 7, game.getPitAt(11).getStones());
        Assert.assertSame("Pit 12 should had 7 stones.", 7, game.getPitAt(12).getStones());
        Assert.assertSame("Pit 13 should had 7 stones.", 7, game.getPitAt(13).getStones());
        Assert.assertSame("Pit 14 should had 0 stones.", 0, game.getPitAt(14).getStones());
    }
}