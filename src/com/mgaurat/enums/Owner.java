package com.mgaurat.enums;

public enum Owner {
	
	ALLY(0),
	ENEMY(1);
	
	private int ownerId;

	private Owner(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getOwnerId() {
		return ownerId;
	}

}
