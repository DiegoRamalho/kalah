package nl.com.kalah.api.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class that represents the Player of the game.
 *
 * @author Diego
 * @since 13/01/2020
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 3170866900132290381L;
    /**
     * Player's name.
     */
    private String name;

    /**
     * Gets the player's name.
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name.
     *
     * @param name Player's name.
     * @return Player
     */
    public Player name(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Player player = (Player) obj;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Player{" +
               "name='" + name + '\'' +
               '}';
    }
}
