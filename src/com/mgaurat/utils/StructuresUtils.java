package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;

/**
 * Final class for utilitaries methods that manipulates Site with Structure informations.
 * 
 * @author mgaurat
 *
 */
public final class StructuresUtils {
	
	private StructuresUtils() {}
    
	/**
	 * Get the sum of the gold production of each MINE of the input collection.
	 * 
	 * @param sites
	 * @return int
	 */
    public static int getGoldProduction(Collection<Site> sites) {
    	int goldProduction = 0;
    	for (Site site : sites) {
    		if (site.getStructure().getStructureTypeId() == StructureEnum.MINE.getId()) {
    			goldProduction += site.getStructure().getParam1();    			
    		}
    	}
    	
    	return goldProduction;
    }
    
    public static boolean isMineNotInFullProduction(Structure structure) {
    	if (structure.getStructureTypeId() != StructureEnum.MINE.getId()) {
    		return false;
    	}
    	
    	return structure.getParam1() < structure.getMaxMineProduction();
    }
    
    /**
     * Check if the input Structure is a TOWER and has enough life points.
     * The life points considered as sufficient is defined in a constant.
     * 
     * @param structure
     * @return boolean
     */
    public static boolean isTowerLifeNotSufficient(Structure structure) {
    	if (structure.getStructureTypeId() != StructureEnum.TOWER.getId()) {
    		return false;
    	}
    	
    	final int SUFFICIENT_TOWER_LIFE = 650;
    	return structure.getParam1() < SUFFICIENT_TOWER_LIFE;
    }
    
    /**
     * Get the ally TOWER Sites that do not have enough life points.
     * 
     * @param allyTowerSites
     * @return
     */
    public static Collection<Site> getAllyTowerSitesWithNotSufficientLife(Collection<Site> allyTowerSites) {
    	Collection<Site> allyTowerSitesWithNotSufficientLife = new ArrayList<>();
    	for (Site allyTowerSite : allyTowerSites) {
    		if (isTowerLifeNotSufficient(allyTowerSite.getStructure())) {
    			allyTowerSitesWithNotSufficientLife.add(allyTowerSite);
    		}
    	}
    	
    	return allyTowerSitesWithNotSufficientLife;
    }
    
    /**
     * Check if the input Sites collection holds a KNIGHT BARRACKS.
     * 
     * @param sites
     * @return boolean
     */
    public static boolean isAtLeastOneKnightBarracks(Collection<Site> sites) {
    	for (Site site : sites) {
    		if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
    				&& site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Check if the input Sites collection holds a GIANT BARRACKS.
     * 
     * @param sites
     * @return boolean
     */
    public static boolean isAtLeastOneGiantBarracks(Collection<Site> sites) {
    	for (Site site : sites) {
    		if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
    				&& site.getStructure().getParam2() == UnitEnum.GIANT.getId()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
	
    public static boolean isCoordinatesInRangeOfAnyTower(Coordinates coordinates, Collection<Site> towerSites) {
    	return getTowerSitesInRangeOfCoordinates(towerSites, coordinates).size() == 0;
    }
    
    public static boolean isCoordinatesInRangeOfAtLeastTwoTowers(Coordinates coordinates, Collection<Site> towerSites) {
    	return getTowerSitesInRangeOfCoordinates(towerSites, coordinates).size() >= 2;
    }

    /**
     * Get the collection of TOWER Sites that can reach the input Coordinates.
     * A TOWER can reach a coordinates if the distance between the TOWER and the coordinates is less or equal to the TOWER range.
     * 
     * @param towerSites
     * @param coordinates
     * @return Collection<Site>
     */
	public static Collection<Site> getTowerSitesInRangeOfCoordinates(Collection<Site> towerSites, Coordinates coordinates) {
		Collection<Site> towerSitesInRange = new ArrayList<>();
		for (Site towerSite : towerSites) {
			if (MathUtils.getDistanceBetweenTwoCoordinates(coordinates, towerSite.getCoordinates()) <= towerSite.getStructure().getParam2()) {
				towerSitesInRange.add(towerSite);
			}
		}
		
		return towerSitesInRange;
	}
	
	public static Collection<Site> getSitesExceptKnightBarracksAndTower(Collection<Site> sites) {
		Collection<Site> sitesExceptKnightBarracksAndTower = new ArrayList<>();
		for (Site site : sites) {
			if (site.getStructure().getStructureTypeId() != StructureEnum.TOWER.getId()
					&& site.getStructure().getStructureTypeId() != StructureEnum.BARRACKS.getId()
					&& site.getStructure().getParam2() != UnitEnum.KNIGHT.getId()) {
				sitesExceptKnightBarracksAndTower.add(site);
			}
		}
		
		return sitesExceptKnightBarracksAndTower;
	}
	
	/**
	 * Get the nearest Site from the input Coordinates of the ally QUEEN in which a MINE can be built.
	 * If there is no gold left in the Site, a MINE cannot be built.
	 * But we can only know it it remains gold in a Site if the ally QUEEN is sufficiently close to the Site (distance less than 300).
	 * 
	 * Here we consider that if the gold that remains in a Site is unknown (= -1), this Site can be chosen.
	 * But we cannot be sure of that until the QUEEN is close enough.
	 * 
	 * @param sites
	 * @param myQueenCoordinates
	 * @return Site
	 */
    public static Site getNearestSiteFromCoordinatesToBuildAMine(Collection<Site> sites, Coordinates myQueenCoordinates, Map<Integer, Integer> remainingGoldBySiteId) {    	
    	double distanceToNearestSite = Double.MAX_VALUE;
        Site nearestSite = null;
        double distanceToSite;
        Coordinates siteCoordinates;
        int siteId;
        Integer remainingGold;
        for (Site site : sites) {
        	siteId = site.getId();
        	remainingGold = remainingGoldBySiteId.get(siteId);
        	siteCoordinates = site.getCoordinates();
        	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            if ((remainingGold == null && site.getStructure().getMineGold() != 0)
            		|| (remainingGold != null && remainingGold > 0)) {
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    /**
     * Get the first KNIGHT BARRACKS Site of the input Sites collection that can be TRAIN.
     * 
     * @param sites
     * @return Site
     */
    public static Site getAKnightSiteToTrain(Collection<Site> sites) {
        for (Site site : sites) {     
        	if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
        			&& site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()
        			&& site.getStructure().getParam1() == 0) {
        		return site;
        	}
        }
        return null;
    }
    
    /**
     * Get the first GIANT BARRACKS Site of the input Sites collection that can be TRAIN.
     * 
     * @param sites
     * @return Site
     */
    public static Site getGiantSiteToTrain(Collection<Site> sites) {
        for (Site site : sites) {     
        	if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
        			&& site.getStructure().getParam2() == UnitEnum.GIANT.getId()
        			&& site.getStructure().getParam1() == 0) {
        		return site;
        	}
        }
        return null;
    }
    
    public static void updateRemaingGoldBySiteId(Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> sites) {
    	int siteId;
    	int remainingGold;
    	for (Site site : sites) {
    		siteId = site.getId();
    		remainingGold = site.getStructure().getMineGold();
    		if (remainingGold > -1) {
    			remainingGoldBySiteId.put(siteId, remainingGold);
    		}
    	}
    }
	
}
