package com.mgaurat.utils;

import java.util.Collection;

import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;

public final class StructuresUtils {
	
	private StructuresUtils() {
	}
	
    public static boolean isAtLeastOneKnightBarrackOwnedByMe(Collection<Site> sites) {
    	Structure structure;
        for (Site site : sites) {
        	structure = site.getStructure();
        	if (structure.isOwnedByMe() 
        			&& structure.getStructureTypeId() == StructureType.BARRACKS.getStructureTypeId()
        			&& structure.getParam2() == UnitType.KNIGHT.getUnitTypeId()) {
        		return true;
        	}
        }
        return false;
    }
    
    public static Site getAKnightBarrackOwnedByMe(Collection<Site> sites) {
    	Structure structure;
        for (Site site : sites) {
        	structure = site.getStructure();
        	if (structure.isOwnedByMe() 
        			&& structure.getStructureTypeId() == StructureType.BARRACKS.getStructureTypeId()
        			&& structure.getParam2() == UnitType.KNIGHT.getUnitTypeId()) {
        		return site;
        	}
        }
        return null;
    }
    
    public static int getNumberOfTowerOwnedByMe(Collection<Site> sites) {
    	int towerNumber = 0;
    	Structure structure;
        for (Site site : sites) {
        	structure = site.getStructure();
        	if (structure.isOwnedByMe() 
        			&& structure.getStructureTypeId() == StructureType.TOWER.getStructureTypeId()) {
        		towerNumber++;
        	}
        }
        
        return towerNumber;
    }
    
    public static int getCurrentGoldProduction(Collection<Site> sites) {
    	int currentGoldProduction = 0;
    	Structure structure;
    	for (Site site : sites) {
        	structure = site.getStructure();
    		if (structure.isOwnedByMe()
    			&& structure.getStructureTypeId() == StructureType.MINE.getStructureTypeId()) {
    			currentGoldProduction += structure.getParam1();
    		}
    	}
    	
    	return currentGoldProduction;
    }
    
    public static boolean isMineOwnedByMeNotInFullProduction(Structure structure) {
    	return structure.getStructureTypeId() == StructureType.MINE.getStructureTypeId()
    			&& structure.getParam1() < structure.getMaxMineProduction();
    }
    
    public static boolean isTowerOwnedByMeNotFullLife(Structure structure) {
    	final int MAX_TOWER_LIFE = 750;
    	return structure.getStructureTypeId() == StructureType.TOWER.getStructureTypeId()
    			&& structure.getParam1() < MAX_TOWER_LIFE;
    }

}
