package com.mgaurat.enums;

public enum UnitType {
	
	QUEEN(-1),
	KNIGHT(0),
	ARCHER(1),
	GIANT(2);
	
	private int unitTypeId;

	private UnitType(int unitTypeId) {
		this.unitTypeId = unitTypeId;
	}

	public int getUnitTypeId() {
		return unitTypeId;
	}

}
