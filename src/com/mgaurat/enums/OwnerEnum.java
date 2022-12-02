package com.mgaurat.enums;

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
