package com.mgaurat.enums;

/**
 * Enumeration for the Structure type.
 * 
 * @author mgaurat
 *
 */
public enum StructureEnum {
	
	NOTHING(-1),
	MINE(0),
	TOWER(1),
	BARRACKS(2);
	
	private int id;

	private StructureEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
