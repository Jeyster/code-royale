package com.mgaurat.enums;

public enum StructureType {
	
	MINE(0),
	TOWER(1),
	BARRACKS(2);
	
	private int structureTypeId;

	private StructureType(int structureTypeId) {
		this.structureTypeId = structureTypeId;
	}

	public int getStructureTypeId() {
		return structureTypeId;
	}

}
