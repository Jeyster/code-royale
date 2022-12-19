package com.mgaurat.model;

/**
 * Site from the game board.
 * Geometrical properties from the inital input (do not change during the game).
 * Hold a Structure (does change during the game).
 * 
 * @author mgaurat
 *
 */
public class Site {
	
	private int id;
    private Coordinates coordinates;
    private int radius;
    private Structure structure;
    
	public Site(int id, Coordinates coordinates, int radius) {
		this.id = id;
		this.coordinates = coordinates;
		this.radius = radius;
	}
	
	public int getId() {
		return id;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	public int getRadius() {
		return radius;
	}

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}
	
	public boolean isEmpty() {
		return this.structure.getParam1() == -1;
	}
	
	public boolean isItsCoordinates(Coordinates coordinates) {
		return this.getCoordinates().equals(coordinates);
	}

}
