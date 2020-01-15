package nl.com.kalah.api.domain;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Game.
 *
 * @author Diego
 * @see Pit
 * @since 13/01/2020
 */
public class PitTest {

    @Test
    void equalsTest() {
        final Pit pit1 = new Pit().id(1);
        final Pit pit2 = new Pit().id(2);
        final Pit pit3 = new Pit().id(1);

        Assert.assertNotEquals("Pit 1 hashcode should be different from pit 2.", pit2.hashCode(), pit1.hashCode());
        //Test equals method
        Assert.assertFalse("Pit 1 should not be equals to null.", pit1.equals(null));
        Assert.assertFalse("Pit 1 should not be equals to other class.", pit1.equals(new Object()));

        Assert.assertNotEquals("Pit 1 should be different from pit 2.", pit2, pit1);
        Assert.assertEquals("Pit 1 should be equals to pit 1.", pit1, pit1);
        Assert.assertEquals("Pit 1 should be equals to pit 3.", pit3, pit1);
        Assert.assertNotNull("Pit 1 should not be null.", pit1.toString());
    }
}