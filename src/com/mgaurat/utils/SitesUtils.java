package com.mgaurat.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.OwnerEnum;
import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;

public final class SitesUtils {
	
	private SitesUtils() {
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
	
	public static Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> updateSitesFromTurnInput(Scanner in, Map<Integer, Site> sitesById) {
		Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = new HashMap<>();
		
		Map<StructureEnum, Map<Integer, Site>> emptySitesByIdAndStructure = new HashMap<>();
		Map<Integer, Site> emptySitesById = new HashMap<>();
		emptySitesByIdAndStructure.put(StructureEnum.NOTHING, emptySitesById);
		sitesByIdAndStructureAndOwner.put(OwnerEnum.NOBODY, emptySitesByIdAndStructure);
		
		Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = new HashMap<>();
		Map<Integer, Site> allyMineSitesById = new HashMap<>();
		Map<Integer, Site> allyTowerSitesById = new HashMap<>();
		Map<Integer, Site> allyBarracksSitesById = new HashMap<>();
		allySitesByIdAndStructure.put(StructureEnum.MINE, allyMineSitesById);
		allySitesByIdAndStructure.put(StructureEnum.TOWER, allyTowerSitesById);
		allySitesByIdAndStructure.put(StructureEnum.BARRACKS, allyBarracksSitesById);
		sitesByIdAndStructureAndOwner.put(OwnerEnum.ALLY, allySitesByIdAndStructure);
		
		Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = new HashMap<>();
		Map<Integer, Site> enemyMineSitesById = new HashMap<>();
		Map<Integer, Site> enemyTowerSitesById = new HashMap<>();
		Map<Integer, Site> enemyBarracksSitesById = new HashMap<>();
		enemySitesByIdAndStructure.put(StructureEnum.MINE, enemyMineSitesById);
		enemySitesByIdAndStructure.put(StructureEnum.TOWER, enemyTowerSitesById);
		enemySitesByIdAndStructure.put(StructureEnum.BARRACKS, enemyBarracksSitesById);
		sitesByIdAndStructureAndOwner.put(OwnerEnum.ENEMY, enemySitesByIdAndStructure);
		
		Structure structure;
		Site site;
		for (int i = 0; i < sitesById.size(); i++) {
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
            
            if (structure.getOwner() == OwnerEnum.NOBODY.getId()) {
            	emptySitesById.put(siteId, site);
            } else if (structure.getOwner() == OwnerEnum.ALLY.getId()) {
            	if (structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
            		allyMineSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
            		allyTowerSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()) {
            		allyBarracksSitesById.put(siteId, site);
            	}
            } else if (structure.getOwner() == OwnerEnum.ENEMY.getId()) {
            	if (structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
            		enemyMineSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
            		enemyTowerSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()) {
            		enemyBarracksSitesById.put(siteId, site);
            	}
            }
        }
		
		return sitesByIdAndStructureAndOwner;
	}
	
    public static Site getNearestSite(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {
            siteCoordinates = site.getCoordinates();
            distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            if (distanceToSite < distanceToNearestSite) {
                distanceToNearestSite = distanceToSite;
                nearestSite = site;
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteToBuildAMine(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        final int GOLD_VISIBILTY_DISTANCE = 300;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
        	siteCoordinates = site.getCoordinates();
        	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            if (site.getStructure().getMineGold() != 0
            		|| (distanceToSite < GOLD_VISIBILTY_DISTANCE && site.getStructure().getMineGold() > 0)) {
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static Site getAKnightSite(Collection<Site> sites) {
        for (Site site : sites) {     
        	if (site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()) {
        		return site;
        	}
        }
        return null;
    }
    
    public static Site getKnightSiteToTrain(Collection<Site> sites) {
        for (Site site : sites) {     
        	if (site.getStructure().getParam1() == 0
        			&& site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()) {
        		return site;
        	}
        }
        return null;
    }
    
    public static Site getGiantSiteToTrain(Collection<Site> sites) {
        for (Site site : sites) {     
        	if (site.getStructure().getParam1() == 0
        			&& site.getStructure().getParam2() == UnitEnum.GIANT.getId()) {
        		return site;
        	}
        }
        return null;
    }
    
    public static boolean isSiteInTheMap(int siteId, Map<Integer, Site> sitesById) {
    	return !sitesById.isEmpty() && sitesById.get(siteId) != null;
    }

}
