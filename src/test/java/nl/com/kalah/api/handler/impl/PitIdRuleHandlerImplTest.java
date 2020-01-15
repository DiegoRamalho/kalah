package nl.com.kalah.api.handler.impl;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.com.kalah.api.KalahApplication;
import nl.com.kalah.api.domain.Game;
import nl.com.kalah.api.domain.GameTest;
import nl.com.kalah.api.util.Constants;

/**
 * Test class for the PitIdRuleHandlerImpl.
 *
 * @author Diego
 * @see PitIdRuleHandlerImpl
 * @since 13/01/2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KalahApplication.class)
public class PitIdRuleHandlerImplTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Autowired
    private PitIdRuleHandlerImpl handler;

    @Test
    public void handleRequestGameNullTest() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(Constants.GAME_NULL_ERROR);
        handler.handleRequest(null, 1);
        Assert.fail("Shouldn't accept game null.");
    }

    @Test
    public void handleRequestPitIdNullTest() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(Constants.PIT_ID_NULL_ERROR);
        final Game game = GameTest.getBasicGame();
        handler.handleRequest(game, null);
        Assert.fail("Shouldn't accept pit ID null.");
    }

    @Test
    public void handleRequestInvalidPitIdTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException
                .expectMessage(String.format(Constants.PIT_ID_INVALID_ERROR, 0, Constants.INITIAL_PIT_ID, Constants.FINAL_PIT_ID));
        final Game game = GameTest.getBasicGame();
        handler.handleRequest(game, 0);
        Assert.fail("Shouldn't accept pit ID 0.");
    }

    @Test
    public void handleRequestPitIdOtherPlayerTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Constants.INVALID_PIT_PLAYER_ERROR);
        final Game game = GameTest.getBasicGame();
        handler.handleRequest(game, Constants.FINAL_PIT_ID - 1);
        Assert.fail("Shouldn't accept pit ID from other player.");
    }

    @Test
    public void handleRequestPitIdNorthernKalahTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Constants.KALAH_ID_ERROR);
        final Game game = GameTest.getBasicGame();
        handler.handleRequest(game, Constants.NORTHERN_KALAH_ID);
        Assert.fail("Shouldn't accept pit ID " + Constants.NORTHERN_KALAH_ID);
    }

    @Test
    public void handleRequestPitIdSouthernKalahTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Constants.KALAH_ID_ERROR);
        final Game game = GameTest.getBasicGame();
        game.changePlayers();
        handler.handleRequest(game, Constants.SOUTHERN_KALAH_ID);
        Assert.fail("Shouldn't accept pit ID " + Constants.SOUTHERN_KALAH_ID);
    }

    @Test
    public void handleRequestPitIdEmptyStonesTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Constants.PIT_WITHOUT_STONES_ERROR);
        final Game game = GameTest.getBasicGame();
        game.clearPits();
        handler.handleRequest(game, 1);
        Assert.fail("Shouldn't accept from empty pit");
    }

    @Test
    public void handleRequestValidTest() {
        final Game game = GameTest.getBasicGame();
        handler.handleRequest(game, 1);
    }

}