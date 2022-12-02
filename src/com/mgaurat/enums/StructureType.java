package com.mgaurat.enums;

public enum StructureType {
	
	TOWER(1),
	BARRACKS(2);
	
	private int structureTypeId;

	private StructureType(int structureTypeId) {
		this.structureTypeId = structureTypeId;
	}

	public int getStructureType() {
		return structureTypeId;
	}

}
