package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.mgaurat.enums.GameBoardQuarterEnum;
import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;
import com.mgaurat.model.Unit;

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
    
    /**
     * Check if Coordinates can be reached by a certain number of towers.
     * 
     * @param coordinates
     * @param towerSites
     * @param towerNumberInRangeMax
     * @return boolean
     */
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
    
    public static Site getNearestSiteFromCoordinatesToBuildAMineInForwardDirection(Collection<Site> sites, Coordinates myQueenCoordinates, 
    		Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> enemyKnightBarrackSites, Coordinates startingAllyQueenCoordinates) { 
        boolean isStartingLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
        final int Y_GAP = 150;
    	double distanceToNearestSite = Double.MAX_VALUE;
        Site nearestSite = null;
        double distanceToSite;
        Coordinates siteCoordinates;
        int siteId;
        Integer remainingGold;
        for (Site site : sites) {
        	if ((isStartingLeftSide && (site.getCoordinates().getX() > startingAllyQueenCoordinates.getX()) 
        			&& (site.getCoordinates().getY() < startingAllyQueenCoordinates.getY() + Y_GAP))
        			|| (!isStartingLeftSide && (site.getCoordinates().getX() < startingAllyQueenCoordinates.getX()))
        			&& (site.getCoordinates().getY() > startingAllyQueenCoordinates.getY() - Y_GAP)) {
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
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteToBuildTowerInCorner(Collection<Site> sites, Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
		GameBoardQuarterEnum siteBoardGameQuarter;
        boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
    	Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {
        	siteBoardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(site.getCoordinates(), new Coordinates(960, 500));
        	if ((isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.BOTTOMLEFT))
        			|| (!isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.TOPRIGHT))) {
        		siteCoordinates = site.getCoordinates();
        		distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, siteCoordinates);
        		if (distanceToSite < distanceToNearestSite) {
        			distanceToNearestSite = distanceToSite;
        			nearestSite = site;
        		}        		
        	}
        }
        return nearestSite;
    }
    
    public static Site getFirstSiteToBuildTowerInCorner(Collection<Site> sites, Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
		GameBoardQuarterEnum siteBoardGameQuarter;
        boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
    	Site chosenSite = null;
        double distanceToSite;
        double distanceToChosenSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {
        	siteBoardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(site.getCoordinates(), new Coordinates(960, 500));
        	if ((isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.BOTTOMLEFT))
        			|| (!isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.TOPRIGHT))) {
        		siteCoordinates = site.getCoordinates();
        		distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(new Coordinates(960, 500), siteCoordinates);
        		if (distanceToSite < distanceToChosenSite) {
        			distanceToChosenSite = distanceToSite;
        			chosenSite = site;
        		}        		
        	}
        }
        return chosenSite;
    }
    
    /**
     * Check if site is close (distance <= SAFE_DISTANCE) to the nearest enemy KNIGHT BARRACKS.
     * 
     * @param site
     * @param enemyKnightBarracksSites
     * @return boolean
     */
    public static boolean isSiteCloseToNearestEnemyKnightBarracksSite(Site site, Collection<Site> enemyKnightBarracksSites) {
    	if (site == null || enemyKnightBarracksSites.isEmpty()) {
    		return false;
    	}
    	
    	Site nearestEnemyKnightBarrackSite = SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarracksSites, site.getCoordinates());
    	return isSiteCloseToAnotherSite(site, nearestEnemyKnightBarrackSite);
    }

    /**
     * Check if the distance between 2 Sites is less or equal to constant SAFE_DISTANCE.
     * 
     * @param site1
     * @param site2
     * @return boolean
     */
    public static boolean isSiteCloseToAnotherSite(Site site1, Site site2) {
    	if (site1 == null || site2 == null) {
    		return false;
    	}
    	
    	final double SAFE_DISTANCE = 300;
    	return MathUtils.getDistanceBetweenTwoCoordinates(site1.getCoordinates(), site2.getCoordinates()) <= SAFE_DISTANCE;
    }
    
    /**
     * Get the Sites from the input BARRACKS Sites that are KNIGHT BARRACKS.
     * 
     * @param barracksSites
     * @return Collection<Site>
     */
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
     * Check if there is a enemy KNIGHT BARRACKS that is dangerous for my QUEEN.
     * It means that my QUEEN should not be close (< SAFE_DISTANCE) to a training enemy KNIGHT BARRACKS.
     * 
     * @param allyQueenCoordinates
     * @param enemyKnightBarracksSites
     * @return boolean
     */
    public static boolean isEnemyKnightBarracksDangerous(Coordinates allyQueenCoordinates, Collection<Site> enemyKnightBarracksSites) {
    	Site nearestEnemyKnightBarracksSite = SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarracksSites, allyQueenCoordinates);
    	final double safeDistance = StructuresUtils.getSafeDistanceWithRespectToKnightBarracks(nearestEnemyKnightBarracksSite);
    	if (nearestEnemyKnightBarracksSite == null) {
    		return false;
    	}
    	
    	boolean isNearestEnemyKnightBarracksSiteInTraining = nearestEnemyKnightBarracksSite.getStructure().isBarracksInTraining();
    	double distanceFromNearestEnemyKnightBarracksSite = MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestEnemyKnightBarracksSite.getCoordinates());
    	
    	return isNearestEnemyKnightBarracksSiteInTraining && distanceFromNearestEnemyKnightBarracksSite < safeDistance;
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
    
    /**
     * Find a safe TOWER Site. The algorithm is defined as followed :
     * 	- for each ally TOWER Site, evaluate the number of ally TOWER (including itself) that cover this Site
     * 	- put in a map the left most (or right most depending on startingAllyQueenCoordinates) for each number of covering ally TOWER
     * 	- return the Site from the map that is the most covered
     * 
     * @param allyTowerSites
     * @param startingAllyQueenCoordinates
     * @return Site
     */
    public static Site getSafestTower(Collection<Site> allyTowerSites, Coordinates startingAllyQueenCoordinates) {
    	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
    	Site safestTowerSite = null;
    	int safestXCoordinate = isLeftSide ? 1920 : 0;
    	for (Site allyTowerSite : allyTowerSites) {
			if ((isLeftSide && allyTowerSite.getCoordinates().getX() < safestXCoordinate)
					|| (!isLeftSide && allyTowerSite.getCoordinates().getX() > safestXCoordinate)) {
				safestXCoordinate = allyTowerSite.getCoordinates().getX();
				safestTowerSite = allyTowerSite;
			}    			
    	}
    	
//    	Map<Integer, Site> safestTowersProtectedByTowers = new HashMap<>();
//    	int safestXCoordinate = isLeftSide ? 1920 : 0;
//    	int numberOfAllyTowersInRangeOfTower;
//    	for (Site allyTowerSite : allyTowerSites) {
//    		numberOfAllyTowersInRangeOfTower = StructuresUtils.getTowerSitesInRangeOfCoordinates(allyTowerSites, allyTowerSite.getCoordinates()).size();
//			if ((isLeftSide && allyTowerSite.getCoordinates().getX() < safestXCoordinate)
//					|| (!isLeftSide && allyTowerSite.getCoordinates().getX() > safestXCoordinate)) {
//				safestXCoordinate = allyTowerSite.getCoordinates().getX();
//    			safestTowersProtectedByTowers.put(numberOfAllyTowersInRangeOfTower, allyTowerSite);
//			}    			
//    	}
//    	
//    	int numberOfProtectedAllyTowerForSafestTower = -1;
//    	Site safestTower = null;
//    	for (Integer numberOfProtectedAllyTower : safestTowersProtectedByTowers.keySet()) {
//    		if (numberOfProtectedAllyTower > numberOfProtectedAllyTowerForSafestTower) {
//    			numberOfProtectedAllyTowerForSafestTower = numberOfProtectedAllyTower;
//    			safestTower = safestTowersProtectedByTowers.get(numberOfProtectedAllyTower);
//    		}
//    	}

    	return safestTowerSite;
    }
    
    /**
     * Get a Coordinates to hide from enemies behind a tower :
     * 	- first evaluate the nearest enemy position with respect to the towerSite
     * 	- then adapt the Coordinates close to the towerSite
     * 
     * If there is no enemy, get the Coordinates that is just at the left side (or right side depending on startingAllyQueenCoordinates) of the input TOWER Site.
     * 
     * @param startingAllyQueenCoordinates
     * @param towerSite
     * @return Coordinates
     */
    public static Coordinates getCoordinatesBehindTower(Unit nearestEnemyKnight, Site towerSite, Coordinates startingAllyQueenCoordinates) {	
    	if (towerSite == null) {
    		return null;
    	}
    	
    	if (nearestEnemyKnight != null) {
    		int xCoordinate, yCoordinate;
    		Coordinates towerCoordinates = towerSite.getCoordinates();
    		Coordinates nearestEnemyKnightCoordinates = nearestEnemyKnight.getCoordinates();
    		int towerXCoordinate = towerCoordinates.getX();
    		int towerYCoordinate = towerCoordinates.getY();
    		int towerRadius = towerSite.getRadius();
    		GameBoardQuarterEnum boardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(nearestEnemyKnightCoordinates, towerCoordinates);
    		switch (boardGameQuarter) {
	    		case TOPLEFT: {
	    			xCoordinate = towerXCoordinate + towerRadius;
	    			yCoordinate = towerYCoordinate;
	    			break;
	    		}
	    		case TOPRIGHT: {
	    			xCoordinate = towerXCoordinate;
	    			yCoordinate = towerYCoordinate + towerRadius;
	    			break;
	    		}
	    		case BOTTOMRIGHT: {
	    			xCoordinate = towerXCoordinate - towerRadius;
	    			yCoordinate = towerYCoordinate;
	    			break;
	    		}
	    		case BOTTOMLEFT: {
	    			xCoordinate = towerXCoordinate;
	    			yCoordinate = towerYCoordinate - towerRadius;
	    			break;
	    		}
	    		// Should not happened
	    		default: {
	    			xCoordinate = towerXCoordinate;
	    			yCoordinate = towerYCoordinate;
	    		}
    		}
    		
    		return new Coordinates(xCoordinate, yCoordinate);    		
    	} else {
        	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
        	Coordinates towerSiteCoordinates = towerSite.getCoordinates();
        	int towerRadius = towerSite.getRadius();
        	int xCoordinate = isLeftSide ? 
        			towerSiteCoordinates.getX() - towerRadius : towerSiteCoordinates.getX() + towerRadius;
        	
        	return new Coordinates(xCoordinate, towerSiteCoordinates.getY());
    	}
    	
    	
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
    
    public static int getSafeDistanceWithRespectToKnightBarracks(Site knightBarracksSite) {
    	if (knightBarracksSite == null) {
    		return 0;
    	}
    	
    	int trainingTurnsRemaining = knightBarracksSite.getStructure().getParam1();
    	if (trainingTurnsRemaining == 0) {
    		return 0;
    	} else {
    		return (6 - trainingTurnsRemaining) * 100;
    	}
    }
	
}
