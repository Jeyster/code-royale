package com.mgaurat.model;

public class Unit {
	
	private Coordinates coordinates;
	
    // 0 : ally
    // 1 : enemy	
	private int owner;
	
	// -1 : QUEEN
	// 0 : KNIGHT
	// 1 : ARCHER
	// 2 : GIANT
	private int unitType;
	
	private int health;

	public Unit(Coordinates coordinates, int owner, int unitType, int health) {
		super();
		this.coordinates = coordinates;
		this.owner = owner;
		this.unitType = unitType;
		this.health = health;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public int getOwner() {
		return owner;
	}

	public int getUnitType() {
		return unitType;
	}

	public int getHealth() {
		return health;
	}
 
}
