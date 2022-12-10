package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;

/**
 * Final class for static methods that manipulates Site with Structure informations.
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
    
    public static boolean isCoordinatesInRangeOfTowers(Coordinates coordinates, Collection<Site> towerSites, int towerNumberInRangeMax) {
    	return getTowerSitesInRangeOfCoordinates(towerSites, coordinates).size() >= towerNumberInRangeMax;
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
	
	/**
	 * Get the nearest Site from the input Coordinates of the ally QUEEN in which a MINE can be built.
	 * If there is no gold left in the Site, a MINE cannot be built.
	 * remainingGoldBySiteId tells us if we know the remaining gold in Sites.
	 * 
	 * @param sites
	 * @param myQueenCoordinates
	 * @return Site
	 */
    public static Site getNearestSiteFromCoordinatesToBuildAMine(Collection<Site> sites, Coordinates myQueenCoordinates, 
    		Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> enemyKnightBarrackSites) {    	
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
            if (!isSiteCloseToNearestEnemyKnightBarracksSite(site, enemyKnightBarrackSites) 
            		&& ((remainingGold == null && site.getStructure().getMineGold() != 0)
            		|| (remainingGold != null && remainingGold > 0))) {
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static boolean isSiteCloseToAnotherSite(Site site1, Site site2) {
    	if (site1 == null || site2 == null) {
    		return false;
    	}
    	
    	final double DISTANCE = 300;
    	return MathUtils.getDistanceBetweenTwoCoordinates(site1.getCoordinates(), site2.getCoordinates()) <= DISTANCE;
    }
    
    public static boolean isSiteCloseToNearestEnemyKnightBarracksSite(Site site, Collection<Site> enemyKnightBarracksSites) {
    	if (site == null || enemyKnightBarracksSites.isEmpty()) {
    		return false;
    	}
    	
    	Site nearestEnemyKnightBarrackSite = SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarracksSites, site.getCoordinates());
    	return isSiteCloseToAnotherSite(site, nearestEnemyKnightBarrackSite);
    }
    
    public static Collection<Site> getKnightBarracksSites(Collection<Site> barracksSites) {
    	Collection<Site> knightBarracksSites = new ArrayList<>();
    	for (Site barracksSite : barracksSites) {
    		if (barracksSite.getStructure().isKnightBarracks()) {
    			knightBarracksSites.add(barracksSite);
    		}
    	}
    	return knightBarracksSites;
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
    
    /**
     * Update a map that lists siteId by remaining gold at each turn.
     * At the first turn of the game, the map is empty.
     * Each time a Site is sufficiently close to show its gold content, the Site is added or updated.
     * 
     * @param remainingGoldBySiteId
     * @param sites
     */
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
    
    /**
     * Get Sites that are empty, or a MINE or a not training BARRACKS.
     * 
     * @param sites
     * @return Collection<Site>
     */
    public static Collection<Site> getEmptyAndMineAndNotTrainingBarracks(Collection<Site> sites) {
    	Collection<Site> emptyAndMineAndNotTrainingBarracks = new ArrayList<>();
    	Structure structure;
    	for (Site site : sites) {
    		structure = site.getStructure();
    		if (site.isEmpty() || structure.isMine() || 
    				(structure.isBarrack() && !structure.isBarracksInTraining())) {
    			emptyAndMineAndNotTrainingBarracks.add(site);
    		}
    	}
    	
    	return emptyAndMineAndNotTrainingBarracks;
    }
    
    /**
     * Get Sites that are a MINE or a TOWER or a not training BARRACKS.
     * 
     * @param sites
     * @return Collection<Site>
     */
    public static Collection<Site> getMineAndNotTrainingBarracksAndTowerSites(Collection<Site> sites) {
    	Collection<Site> mineAndNotTrainingBarracksAndTowerSites = new ArrayList<>();
    	Structure structure;
    	for (Site site : sites) {
    		structure = site.getStructure();
    		if (structure.isMine() || structure.isTower() || 
    				(structure.isBarrack() && !structure.isBarracksInTraining())) {
    			mineAndNotTrainingBarracksAndTowerSites.add(site);
    		}
    	}
    	
    	return mineAndNotTrainingBarracksAndTowerSites;
    }
    
    public static Site getSafestTower(Collection<Site> allyTowerSites, Coordinates startingAllyQueenCoordinates) {
    	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
    	Map<Integer, Site> safestTowersProtectedByTowers = new HashMap<>();
    	int safestXCoordinate = isLeftSide ? 1920 : 0;
    	int numberOfAllyTowersInRangeOfTower;
    	for (Site allyTowerSite : allyTowerSites) {
    		numberOfAllyTowersInRangeOfTower = StructuresUtils.getTowerSitesInRangeOfCoordinates(allyTowerSites, allyTowerSite.getCoordinates()).size();
			if ((isLeftSide && allyTowerSite.getCoordinates().getX() < safestXCoordinate)
					|| (!isLeftSide && allyTowerSite.getCoordinates().getX() > safestXCoordinate)) {
				safestXCoordinate = allyTowerSite.getCoordinates().getX();
    			safestTowersProtectedByTowers.put(numberOfAllyTowersInRangeOfTower, allyTowerSite);
			}    			
    	}
    	
    	int numberOfProtectedAllyTowerForSafestTower = -1;
    	Site safestTower = null;
    	for (Integer numberOfProtectedAllyTower : safestTowersProtectedByTowers.keySet()) {
    		if (numberOfProtectedAllyTower > numberOfProtectedAllyTowerForSafestTower) {
    			numberOfProtectedAllyTowerForSafestTower = numberOfProtectedAllyTower;
    			safestTower = safestTowersProtectedByTowers.get(numberOfProtectedAllyTower);
    		}
    	}

    	return safestTower;
    }
    
    public static Coordinates getCoordinatesBehindTower(Coordinates startingAllyQueenCoordinates, Site towerSite) {
    	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
    	Coordinates towerSiteCoordinates = towerSite.getCoordinates();
    	int towerRadius = towerSite.getRadius();
    	int xCoordinate = isLeftSide ? 
    			towerSiteCoordinates.getX() - towerRadius : towerSiteCoordinates.getX() + towerRadius;
    	
    	return new Coordinates(xCoordinate, towerSiteCoordinates.getY());
    	
//    	Coordinates towerSiteCoordinates = towerSite.getCoordinates();
//    	int distanceBetweenQueenAndTower = (int) Math.round(MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, towerSiteCoordinates));
//    	int towerRadius = towerSite.getRadius();
//    	int xDifferenceBetweenQueenAndTower = towerSite.getCoordinates().getX() - allyQueenCoordinates.getX();
//    	int yDifferenceBetweenQueenAndTower = towerSite.getCoordinates().getY() - allyQueenCoordinates.getY();
//
//    	int deltaX = towerRadius * xDifferenceBetweenQueenAndTower / distanceBetweenQueenAndTower;
//    	int deltaY = towerRadius * yDifferenceBetweenQueenAndTower / distanceBetweenQueenAndTower;
//    	
//    	return new Coordinates(towerSiteCoordinates.getX() + deltaX, towerSiteCoordinates.getY() + deltaY);
    }
	
}
