package nl.com.kalah.api.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.com.kalah.api.util.Constants;

/**
 * Class that represents the Game board.
 *
 * @author Diego
 * @since 13/01/2020
 */
public class Game implements Serializable {

    private static final long serialVersionUID = -3835609877119429912L;
    /**
     * Board of the game.
     *
     * It's a circular list of pits like bellow:
     * <pre>
     *              <b>Northern Player pits</b>
     *  <-  06  <-  05  <-  04  <-  03  <-  02  <-  01 <-
     *  |                                               / \
     * \ /                                               |
     * 07 << Northern Kalah            Southern Kalah >> 14
     *  |                                               / \
     * \ /                                               |
     * ->   08  ->  09  ->  10  ->  11  ->  12  ->  13  ->
     *              <b>Southern Player pits</b>
     * </pre>
     */
    protected final List<Pit> board;
    /**
     * The current player.
     */
    private Player currentPlayer;
    /**
     * The other player.
     */
    private Player otherPlayer;
    /**
     * The winner.
     */
    private Player winner;
    /**
     * Was it a draw?
     */
    private boolean draw;
    /**
     * Last sown pit.
     */
    private Pit lastSownPit;

    /**
     * Creates a new game. The northernPlayer always starts the game.
     *
     * @param amountOfStones Amount of stones to be put in each pit
     * @param northernPlayer Northern player
     * @param southernPlayer Southern player
     */
    public Game(final Integer amountOfStones,
                final Player northernPlayer,
                final Player southernPlayer) {
        board = new LinkedList<>();
        currentPlayer = northernPlayer;
        otherPlayer = southernPlayer;
        populateBoard(amountOfStones, northernPlayer, southernPlayer);
    }

    /**
     * Populates the board.
     *
     * Rules: Each of the two players has 'six pits' in front of him/her. To the right of the six pits, each player has a larger pit,
     * his Kalah or house ({@code pit.kalah == true}). At the start of the game, {@param amountOfStones} stones are put in each pit.
     *
     * @param amountOfStones Amount of stones to be put in each pit
     * @param northernPlayer Northern player
     * @param southernPlayer Southern player
     */
    private void populateBoard(final Integer amountOfStones,
                               final Player northernPlayer,
                               final Player southernPlayer) {
        // Populates the board
        for (int index = Constants.INITIAL_PIT_ID; index <= Constants.FINAL_PIT_ID; index++) {
            final Player player = index <= Constants.NORTHERN_KALAH_ID ? northernPlayer : southernPlayer;
            final boolean isKalah = index == Constants.NORTHERN_KALAH_ID || index == Constants.SOUTHERN_KALAH_ID;
            final int stones = isKalah ? 0 : amountOfStones;
            board.add(new Pit().id(index)
                              .kalah(isKalah)
                              .player(player)
                              .stones(stones)
            );
        }
        // Creates the circular dependency
        for (int index = 0; index < Constants.FINAL_PIT_ID; index++) {
            final Pit next;
            if (index < Constants.FINAL_PIT_ID - 1) {
                next = board.get(index + 1);
            } else {
                next = board.get(0);
            }
            board.get(index).next(next);
        }
    }

    /**
     * Gets the board as a Map, where key is the pit ID and value is the amount of stones in the pit.
     *
     * @return Map
     */
    public Map<Integer, Integer> getStatus() {
        return board.parallelStream().collect(Collectors.toMap(Pit::getId, Pit::getStones));
    }

    /**
     * Gets the pit at {@param id}. If the ID is not valid, returns null.
     *
     * @param id ID of the Pit.
     * @return Pit
     */
    public Pit getPitAt(final Integer id) {
        final Pit result;
        if (Objects.nonNull(id) && id >= Constants.INITIAL_PIT_ID && id <= Constants.FINAL_PIT_ID) {
            result = board.get(id - 1);
        } else {
            result = null;
        }
        return result;
    }

    /**
     * Gets {@param player}' kalah.
     *
     * @param player Player who owns the kalah.
     * @return Pit
     */
    public Pit getKalahFrom(final Player player) {
        final Pit northernKalahPit = getPitAt(Constants.NORTHERN_KALAH_ID);
        return Objects.equals(northernKalahPit.getPlayer(), player) ? northernKalahPit : getPitAt(Constants.SOUTHERN_KALAH_ID);
    }

    /**
     * Gets the amount of stones of the {@param player}.
     *
     * @param player Player
     * @return Amount of stones
     */
    public Integer getAmountOfStonesFrom(final Player player) {
        return board.parallelStream()
                .filter(it -> Objects.equals(player, it.getPlayer()) && !it.isKalah())
                .map(Pit::getStones)
                .reduce(0, Integer::sum);
    }

    /**
     * Remove all stones from the pits
     */
    public void clearPits() {
        board.parallelStream()
                .filter(it -> !it.isKalah())
                .forEach(pit -> pit.stones(0));
    }

    /**
     * Change the players.
     */
    public void changePlayers() {
        final Player current = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = current;
    }

    /**
     * Gets the current player.
     *
     * @return Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the other player.
     *
     * @return Player
     */
    public Player getOtherPlayer() {
        return otherPlayer;
    }

    /**
     * Gets the winner.
     *
     * @return Player.
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Sets the winner.
     *
     * @param winner Winner
     */
    public void setWinner(final Player winner) {
        this.winner = winner;
    }

    /**
     * Gets the last sown pit.
     *
     * @return Pit
     */
    public Pit getLastSownPit() {
        return lastSownPit;
    }

    /**
     * Sets the last sown pit.
     *
     * @param lastSownPit The last sown pit.
     */
    public void setLastSownPit(final Pit lastSownPit) {
        this.lastSownPit = lastSownPit;
    }

    /**
     * Was a draw?
     *
     * @return boolean
     */
    public boolean isDraw() {
        return draw;
    }

    /**
     * Sets if was a draw.
     *
     * @param draw Was a draw?
     */
    public void setDraw(final boolean draw) {
        this.draw = draw;
    }

    @Override
    public String toString() {
        return "Game{" +
               "board=" + board +
               ", currentPlayer=" + currentPlayer +
               ", otherPlayer=" + otherPlayer +
               ", winner=" + winner +
               ", lastSownPit=" + lastSownPit +
               '}';
    }
}
