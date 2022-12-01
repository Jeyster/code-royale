package com.mgaurat.enums;

public enum StructureType {
	
	TOWER(1),
	BARRACK(2);
	
	private int structureType;

	private StructureType(int structureType) {
		this.structureType = structureType;
	}

	public int getStructureType() {
		return structureType;
	}

}
