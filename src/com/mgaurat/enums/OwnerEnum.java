package com.mgaurat.enums;

/**
 * Enumeration for the Owner type.
 * 
 * @author mgaurat
 *
 */
public enum OwnerEnum {
	
	NOBODY(-1),
	ALLY(0),
	ENEMY(1);
	
	private int id;

	private OwnerEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
