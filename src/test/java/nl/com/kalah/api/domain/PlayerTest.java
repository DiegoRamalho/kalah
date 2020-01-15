package nl.com.kalah.api.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Game.
 *
 * @author Diego
 * @see Player
 * @since 13/01/2020
 */
public class PlayerTest {

    @Test
    void equalsTest() {
        final Player player1 = new Player().name("Player 1");
        final Player player2 = new Player().name("Player 2");
        final Player player3 = new Player().name("Player 1");

        Assert.assertNotEquals("Player 1 hashcode should be different from player 2.", player2.hashCode(), player1.hashCode());
        //Test equals method
        Assert.assertFalse("Player 1 should not be equals to null.", player1.equals(null));
        Assert.assertFalse("Player 1 should not be equals to other class.", player1.equals(new Object()));

        Assert.assertNotEquals("Player 1 should be different from player 2.", player2, player1);
        Assert.assertEquals("Player 1 should be equals to player 1.", player1, player1);
        Assert.assertEquals("Player 1 should be equals to player 3.", player3, player1);
    }
}