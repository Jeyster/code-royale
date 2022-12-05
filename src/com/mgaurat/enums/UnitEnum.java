package com.mgaurat.enums;

/**
 * Enumeration for the Unit type.
 * 
 * @author mgaurat
 *
 */
public enum UnitEnum {
	
	QUEEN(-1),
	KNIGHT(0),
	ARCHER(1),
	GIANT(2);
	
	private int id;

	private UnitEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
