package com.mgaurat.utils;

import java.util.Collection;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
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
        			&& structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()
        			&& structure.getParam2() == UnitEnum.KNIGHT.getId()) {
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
        			&& structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()
        			&& structure.getParam2() == UnitEnum.KNIGHT.getId()) {
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
        			&& structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
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
    			&& structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
    			currentGoldProduction += structure.getParam1();
    		}
    	}
    	
    	return currentGoldProduction;
    }
    
    public static boolean isMineOwnedByMeNotInFullProduction(Structure structure) {
    	return structure.getStructureTypeId() == StructureEnum.MINE.getId()
    			&& structure.getParam1() < structure.getMaxMineProduction();
    }
    
    public static boolean isTowerOwnedByMeNotFullLife(Structure structure) {
    	final int MAX_TOWER_LIFE = 750;
    	return structure.getStructureTypeId() == StructureEnum.TOWER.getId()
    			&& structure.getParam1() < MAX_TOWER_LIFE;
    }

}
