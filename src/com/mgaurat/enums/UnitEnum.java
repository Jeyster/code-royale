package com.mgaurat.enums;

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
