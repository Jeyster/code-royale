package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.Owner;
import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;
import com.mgaurat.model.Unit;

public final class GameBoardUtil {
	
	private GameBoardUtil() {
	}
	
	public static Map<Integer, Site> getSitesFromInitialInput(Scanner in, int numSites) {
		Map<Integer, Site> sitesById = new HashMap<>();
        Coordinates coordinates;
        Site site;
        
        for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int radius = in.nextInt();
            coordinates = new Coordinates(x, y);
            site = new Site(siteId, coordinates, radius);
            sitesById.put(siteId, site);
        }
        
        return sitesById;
	}
	
	public static void updateSitesFromTurnInput(Scanner in, int numSites, Map<Integer, Site> sitesById) {
		Structure structure;
		Site site;
		for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int ignore1 = in.nextInt();
            int ignore2 = in.nextInt();
            int structureType = in.nextInt();
            int owner = in.nextInt();
            int param1 = in.nextInt();
            int param2 = in.nextInt();

            structure = new Structure(ignore1, ignore2, structureType, owner, param1, param2);
            site = sitesById.get(siteId);
            site.setStructure(structure);
        }
	}
	
	public static Map<UnitType, List<Unit>> getUnitsByType(Scanner in, int numUnits) {
		Map<UnitType, List<Unit>> unitsByType = buildUnitsByType();
		List<Unit> queens = unitsByType.get(UnitType.QUEEN);
		List<Unit> knights = unitsByType.get(UnitType.KNIGHT);
		List<Unit> archers = unitsByType.get(UnitType.ARCHER);
		List<Unit> giants = unitsByType.get(UnitType.GIANT);
		
		Coordinates unitCoordinates;
		Unit unit;
        for (int i = 0; i < numUnits; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            unitCoordinates = new Coordinates(x, y);
            int owner = in.nextInt();
            int unitType = in.nextInt();
            int health = in.nextInt();

            unit = new Unit(unitCoordinates, owner, unitType, health);
            
            if (unitType == UnitType.QUEEN.getUnitTypeId()) {
				queens.add(unit);
			} else if (unitType == UnitType.KNIGHT.getUnitTypeId()) {
				knights.add(unit);
			} else if (unitType == UnitType.ARCHER.getUnitTypeId()) {
				archers.add(unit);
			} else if (unitType == UnitType.GIANT.getUnitTypeId()) {
				giants.add(unit);
			}
        }
        
        return unitsByType;
	}
	
	private static Map<UnitType, List<Unit>> buildUnitsByType() {
		Map<UnitType, List<Unit>> unitsByType = new HashMap<>();
		List<Unit> queens = new ArrayList<>();
		List<Unit> knights = new ArrayList<>();
		List<Unit> archers = new ArrayList<>();
		List<Unit> giants = new ArrayList<>();
		
		unitsByType.put(UnitType.QUEEN, queens);
		unitsByType.put(UnitType.KNIGHT, knights);
		unitsByType.put(UnitType.ARCHER, archers);
		unitsByType.put(UnitType.GIANT, giants);
		
		return unitsByType;
	}
	
	public static Unit getMyQueen(Map<UnitType, List<Unit>> unitsByType) {
		List<Unit> queens = unitsByType.get(UnitType.QUEEN);
		for (Unit queen : queens) {
			if (queen.getOwner() == Owner.ALLY.getOwnerId()) {
				return queen;
			}
		}
		
		return null;
	}
	
    public static Site getNearestSite(Map<Integer, Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Site site;
        Coordinates siteCoordinates;
        for (int i = 0; i < sites.size(); i++) {
            site = sites.get(i);
            siteCoordinates = site.getCoordinates();
            distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            if (distanceToSite < distanceToNearestSite) {
                distanceToNearestSite = distanceToSite;
                nearestSite = site;
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteNotOwned(Map<Integer, Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Site site;
        Coordinates siteCoordinates;
        for (int i = 0; i < sites.size(); i++) {
            site = sites.get(i);
            
            if (site.getStructure().getOwner() != Owner.ALLY.getOwnerId()) {
            	siteCoordinates = site.getCoordinates();
            	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static boolean isAtLeastOneSiteOwned(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe()) {
        		return true;
        	}
        }
        return false;

    }
    
    public static Site getASiteToTrain(Map<Integer, Site> sites) {
    	Site site = null;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getParam1() == 0 
        			&& site.getStructure().getStructureType() == StructureType.BARRACK.getStructureType()) {
        		return site;
        	}
        }
        return site;
    }
    
    public static boolean isAtLeastOneKnightBarrackOwnedByMe(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.BARRACK.getStructureType()
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
        			&& site.getStructure().getStructureType() == StructureType.BARRACK.getStructureType()
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
