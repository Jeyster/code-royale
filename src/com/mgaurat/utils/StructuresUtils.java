package com.mgaurat.utils;

import java.util.Collection;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;

public final class StructuresUtils {
	
	private StructuresUtils() {
	}
    
    public static int getCurrentGoldProduction(Collection<Site> sites) {
    	int currentGoldProduction = 0;
    	for (Site site : sites) {
			currentGoldProduction += site.getStructure().getParam1();
    	}
    	
    	return currentGoldProduction;
    }
    
    public static boolean isMineNotInFullProduction(Structure structure) {
    	if (structure.getStructureTypeId() != StructureEnum.MINE.getId()) {
    		return false;
    	}
    	
    	return structure.getParam1() < structure.getMaxMineProduction();
    }
    
    public static boolean isTowerNotFullLife(Structure structure) {
    	if (structure.getStructureTypeId() != StructureEnum.TOWER.getId()) {
    		return false;
    	}
    	
    	final int MAX_TOWER_LIFE = 750;
    	return structure.getParam1() < MAX_TOWER_LIFE;
    }
    
    public static boolean isAtLeastOneAllyKnightBarracks(Collection<Site> allyBarracksSites) {
    	for (Site site : allyBarracksSites) {
    		if (site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public static boolean isAtLeastOneAllyGiantBarracks(Collection<Site> allyBarracksSites) {
    	for (Site site : allyBarracksSites) {
    		if (site.getStructure().getParam2() == UnitEnum.GIANT.getId()) {
    			return true;
    		}
    	}
    	
    	return false;
    }

}
