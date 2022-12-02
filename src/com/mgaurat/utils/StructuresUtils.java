package com.mgaurat.utils;

import java.util.Map;

import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Site;

public final class StructuresUtils {
	
	private StructuresUtils() {
	}
	
    public static boolean isAtLeastOneKnightBarrackOwnedByMe(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.BARRACKS.getStructureType()
        			&& site.getStructure().getParam2() == UnitType.KNIGHT.getUnitTypeId()) {
        		return true;
        	}
        }
        return false;
    }
    
    public static Site getAKnightBarrackOwnedByMe(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.BARRACKS.getStructureType()
        			&& site.getStructure().getParam2() == UnitType.KNIGHT.getUnitTypeId()) {
        		return site;
        	}
        }
        return null;
    }
    
    public static int getNumberOfTowerOwnedByMe(Map<Integer, Site> sites) {
    	int towerNumber = 0;
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.TOWER.getStructureType()) {
        		towerNumber++;
        	}
        }
        
        return towerNumber;
    }

}
