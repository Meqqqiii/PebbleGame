package org.example;

/**
 * Represents a player in the pebble game.
 */
public class Player {
    private String name;
    private int numPebbles;

    /**
     * Creates a new player with a specified name and number of pebbles.
     *
     * @param name       the name of the player
     * @param numPebbles the initial number of pebbles the player has
     */
    Player(String name, int numPebbles) {
        this.name = name;
        this.numPebbles = numPebbles;
    }

    public int getNumPebbles() {
        return this.numPebbles;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Sets the player's name.
     *
     * @param name the new name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the number of pebbles the player has.
     *
     * @param numPebbles the new number of pebbles
     */
    public void setNumPebbles(int numPebbles) {
        this.numPebbles = numPebbles;
    }
}
