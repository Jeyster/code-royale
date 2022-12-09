package com.mgaurat.model;

import com.mgaurat.enums.OwnerEnum;
import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;

/**
 * Structure that is built on a Site.
 * It is a bean that is instanciated from the game turn input.
 * 
 * @author mgaurat
 *
 */
public class Structure {
	
    // Gold left in a MINE (-1 if unknown or N/A)
    private int mineGold;
    
    // Maximum MINE gold production (-1 if unknown or N/A)
    private int maxMineProduction;
    
    // -1 : nothing is built
    // 0 : MINE
    // 1 : TOWER 
    // 2 : BARRACKS
    int structureTypeId;
    
    // -1 : nothing is built
    // 0 : ALLY structure
    // 1 : ENEMY structure
    int owner;
    
    // if nothing is built = -1
    // if MINE = current gold production (-1 if ENEMY MINE)
    // if TOWER = life points
    // if BARRACKS = turns left before TRAIN ending (0 if ready to TRAIN)
    int param1;
    
    // if nothing or a MINE is built = -1
    // if TOWER = range radius
    // if BARRACKS = 0 if it produces KNIGHT
    //            = 1 if it produces ARCHER
    //            = 2 if it produces GIANT
    int param2;

	public Structure(int mineGold, int maxMineProduction, int structureType, int owner, int param1, int param2) {
		this.mineGold = mineGold;
		this.maxMineProduction = maxMineProduction;
		this.structureTypeId = structureType;
		this.owner = owner;
		this.param1 = param1;
		this.param2 = param2;
	}

	public int getMineGold() {
		return mineGold;
	}

	public int getMaxMineProduction() {
		return maxMineProduction;
	}

	public int getStructureTypeId() {
		return structureTypeId;
	}

	public int getOwner() {
		return owner;
	}

	public int getParam1() {
		return param1;
	}

	public int getParam2() {
		return param2;
	}
	
	public boolean isOwnedByMe() {
		return getOwner() == OwnerEnum.ALLY.getId();
	}
	
	public boolean isMine() {
		return this.getStructureTypeId() == StructureEnum.MINE.getId();
	}
	
	public boolean isBarrack() {
		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId();
	}
	
	public boolean isKnightBarracks() {
		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId()
				&& this.getParam2() == UnitEnum.KNIGHT.getId();
	}
	
	public boolean isArcherBarracks() {
		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId()
				&& this.getParam2() == UnitEnum.ARCHER.getId();
	}
	
	public boolean isGiantBarracks() {
		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId()
				&& this.getParam2() == UnitEnum.GIANT.getId();
	}
	
	public boolean isTower() {
		return this.getStructureTypeId() == StructureEnum.TOWER.getId();
	}
	
	public boolean isBarracksInTraining() {
		return this.isBarrack() && this.getParam1() > 0;
	}
}
