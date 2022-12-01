package com.mgaurat.model;

/**
 * Coordinates of a point of the game board.
 * The game board is a 1900 x 1000 length rectangle.
 * The game board origin defined by coordinates (0, 0) is the top left corner.
 * 
 * @author mgaurat
 *
 */
public class Coordinates {
	
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
}
