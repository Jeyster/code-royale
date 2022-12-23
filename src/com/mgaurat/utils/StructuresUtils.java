package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
	
	private static final Coordinates boardGameCenter = new Coordinates(960, 500);
	
	private StructuresUtils() {}
    
	/**
	 * Get the sum of the gold production of each MINE of the input collection.
	 * 
	 * @param sites
	 * @return int
	 */
    public static int getGoldProduction(Collection<Site> sites) {
    	return sites
    			.stream()
    			.filter(site -> site.getStructure().isMine())
    			.mapToInt(site -> site.getStructure().getParam1())
    			.sum();
    }
    
    public static boolean isMineNotInFullProduction(Structure structure) {
    	if (!structure.isMine()) {
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
    	if (!structure.isTower()) {
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
    	return allyTowerSites
    			.stream()
    			.filter(site -> isTowerLifeNotSufficient(site.getStructure()))
    			.collect(Collectors.toList());
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
		return towerSites
				.stream()
				.filter(site -> MathUtils.getDistanceBetweenTwoCoordinates(coordinates, site.getCoordinates()) <= site.getStructure().getParam2())
    			.collect(Collectors.toList());
	}
	
	/**
	 * Get the nearest Site from the input Coordinates of the ally QUEEN in which a MINE can be built.
	 * remainingGoldBySiteId tells us if we know the remaining gold in Sites.
	 * 
	 * @param sites
	 * @param myQueenCoordinates
	 * @return Site
	 */
    public static Site getNearestSiteFromCoordinatesToBuildAMine(Collection<Site> sites, Coordinates myQueenCoordinates, 
    		Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> enemyKnightBarrackSites) {    	
        return sites
        		.stream()
        		.filter(site -> site.isAllowedToBuildMine(enemyKnightBarrackSites, remainingGoldBySiteId.get(site.getId())))
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, site.getCoordinates()))))
        		.orElse(null);
    }
    
    /**
	 * Get the nearest Site from the input Coordinates of the ally QUEEN in which a MINE can be built.
	 * Only Sites that are towards the enemy camp are considered.
	 * If there is no gold left in the Site, a MINE cannot be built.
	 * remainingGoldBySiteId tells us if we know the remaining gold in Sites.
	 *
     * @param sites
     * @param myQueenCoordinates
     * @param remainingGoldBySiteId
     * @param enemyKnightBarrackSites
     * @param startingAllyQueenCoordinates
     * @return Site
     */
    public static Site getNearestSiteFromCoordinatesToBuildAMineInForwardDirection(Collection<Site> sites, Coordinates myQueenCoordinates, 
    		Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> enemyKnightBarrackSites, Coordinates startingAllyQueenCoordinates) { 
        return sites
        		.stream()
        		.filter(site -> site.isAllowedToBuildMine(enemyKnightBarrackSites, remainingGoldBySiteId.get(site.getId())) 
        				&& site.isInForwardDirection(startingAllyQueenCoordinates))
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, site.getCoordinates()))))
        		.orElse(null);
    }
    
    /**
     * Get the Site to BUILD the first TOWER.
     * It would be the one that is the closest to the middle and in the "safe" game board corner.
     * 
     * @param sites
     * @param allyQueenCoordinates
     * @param startingAllyQueenCoordinates
     * @return Site
     */
    public static Site getFirstSiteToBuildTowerInCorner(Collection<Site> sites, Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
        return sites
        		.stream()
        		.filter(site -> site.isInFirstsTowersBuildCorner(startingAllyQueenCoordinates, boardGameCenter))
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(boardGameCenter, site.getCoordinates()))))
        		.orElse(null);
    }
    
    /**
     * Get the nearest Site to BUILD a TOWER in the "safe" game board corner.
     * 
     * @param sites
     * @param allyQueenCoordinates
     * @param startingAllyQueenCoordinates
     * @return Site
     */
    public static Site getNearestSiteToBuildTowerInCorner(Collection<Site> sites, Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
        return sites
        		.stream()
        		.filter(site -> site.isInFirstsTowersBuildCorner(startingAllyQueenCoordinates, boardGameCenter))
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, site.getCoordinates()))))
        		.orElse(null);
    }
    
    /**
     * Get the Sites from the input BARRACKS Sites that are KNIGHT BARRACKS.
     * 
     * @param barracksSites
     * @return Collection<Site>
     */
    public static Collection<Site> getKnightBarracksSites(Collection<Site> barracksSites) {
    	return barracksSites
    			.stream()
    			.filter(site -> site.getStructure().isKnightBarracks())
    			.collect(Collectors.toList());
    }
    
    public static Collection<Site> getGiantBarracksSites(Collection<Site> barracksSites) {
    	return barracksSites
    			.stream()
    			.filter(site -> site.getStructure().isGiantBarracks())
    			.collect(Collectors.toList());
    }
    
    public static Collection<Site> getNotInTrainingBarracksSites(Collection<Site> barracksSites) {
    	return barracksSites
    			.stream()
    			.filter(site -> site.getStructure().isBarracks() && !site.getStructure().isBarracksInTraining())
    			.collect(Collectors.toList());
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
     * Get the first BARRACKS Site of the input Sites collection that can be TRAIN.
     * 
     * @param barracksSites
     * @return Site
     */
    public static Optional<Site> getBarracksSiteToTrain(Collection<Site> barracksSites) {
    	return barracksSites
    			.stream()
    			.filter(site -> site.getStructure().getParam1() == 0)
    			.findFirst();
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
    	sites
    	.stream()
    	.filter(site -> site.getStructure().getMineGold() > -1)
    	.forEach(site -> remainingGoldBySiteId.put(site.getId(), site.getStructure().getMineGold()));
    }
    
    /**
     * Get Sites that are empty, or a MINE or a not training BARRACKS.
     * 
     * @param sites
     * @return Collection<Site>
     */
    public static Collection<Site> getEmptyAndMineAndNotTrainingBarracks(Collection<Site> sites) {
    	return sites
    			.stream()
    			.filter(site -> site.isEmpty() || site.getStructure().isMine() || 
    	    				(site.getStructure().isBarracks() && !site.getStructure().isBarracksInTraining()))
    			.collect(Collectors.toList());
    }
    
    /**
     * Get Sites that are a MINE or a TOWER or a not training BARRACKS.
     * 
     * @param sites
     * @return Collection<Site>
     */
    public static Collection<Site> getMineAndNotTrainingBarracksAndTowerSites(Collection<Site> sites) {
    	return sites
    			.stream()
    			.filter(site -> {
    				Structure structure = site.getStructure();
    				return structure.isMine() || structure.isTower() || 
    	    				(structure.isBarracks() && !structure.isBarracksInTraining());
    			})
    			.collect(Collectors.toList());
	}
    
    /**
     * Get the safest TOWER Site that is at left or right most location depending on startingAllyQueenCoordinates.
     * 
     * @param allyTowerSites
     * @param startingAllyQueenCoordinates
     * @return Site
     */
    public static Site getSafestTower(Collection<Site> allyTowerSites, Coordinates startingAllyQueenCoordinates) {
    	if (GameBoardUtils.isLeftSide(startingAllyQueenCoordinates)) {
    		return allyTowerSites
    				.stream()
    				.collect(Collectors.minBy(Comparator.comparingInt(site -> site.getCoordinates().getX())))
    				.orElse(null);    		
    	} else {
    		return allyTowerSites
    				.stream()
    				.collect(Collectors.maxBy(Comparator.comparingInt(site -> site.getCoordinates().getX())))
    				.orElse(null);    
    	}
    }
    
    /**
     * Get a Coordinates to hide from enemies behind a TOWER.
     * Choose the coordinates on the input TOWER Site circle that is opposite to the nearest enemy KNIGHT.
     * 
     * If there is no enemy, get the Coordinates that is just at the left side (or right side depending on startingAllyQueenCoordinates) of the input TOWER Site.
     * 
     * @param nearestEnemyKnight
     * @param towerSite
     * @param startingAllyQueenCoordinates
     * @return Coordinates
     */
    public static Coordinates getCoordinatesBehindTowerOppositeToNearestEnemyKnight(Unit nearestEnemyKnight, Site towerSite, Coordinates startingAllyQueenCoordinates) {	
    	if (towerSite == null) {
    		return null;
    	}
    	
    	if (nearestEnemyKnight != null) {
    		Coordinates towerCoordinates = towerSite.getCoordinates();
    		Coordinates nearestEnemyKnightCoordinates = nearestEnemyKnight.getCoordinates();
    		int towerRadius = towerSite.getRadius();
        	int distanceBetweenNearestEnemyKnightAndTower = (int) Math.round(MathUtils.getDistanceBetweenTwoCoordinates(nearestEnemyKnightCoordinates, towerCoordinates));
        	int xDifferenceBetweenNearestEnemyKnightAndTower = towerSite.getCoordinates().getX() - nearestEnemyKnightCoordinates.getX();
        	int yDifferenceBetweenNearestEnemyKnightAndTower = towerSite.getCoordinates().getY() - nearestEnemyKnightCoordinates.getY();

        	int deltaX = towerRadius * xDifferenceBetweenNearestEnemyKnightAndTower / distanceBetweenNearestEnemyKnightAndTower;
        	int deltaY = towerRadius * yDifferenceBetweenNearestEnemyKnightAndTower / distanceBetweenNearestEnemyKnightAndTower;
        	
        	return new Coordinates(towerCoordinates.getX() + deltaX, towerCoordinates.getY() + deltaY); 		
    	} else {
        	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
        	Coordinates towerSiteCoordinates = towerSite.getCoordinates();
        	int towerRadius = towerSite.getRadius();
        	int xCoordinate = isLeftSide ? towerSiteCoordinates.getX() - towerRadius : towerSiteCoordinates.getX() + towerRadius;
        	
        	return new Coordinates(xCoordinate, towerSiteCoordinates.getY());
    	}
    	
    }
    
    /**
     * Evaluate a safe distance with a KNIGHT BARRACKS.
     * It depends on the turns that left to finish a TRAIN and on an arbitrary safe distance.
     * 
     * @param knightBarracksSite
     * @return int
     */
    public static int getSafeDistanceWithRespectToKnightBarracks(Site knightBarracksSite) {
    	final int TRAINING_KNIGHT_TURNS_NUMBER = 5;
    	final int SAFE_DISTANCE_PER_REMAINING_TURN = 100;
    	
    	if (knightBarracksSite == null) {
    		return 0;
    	}
    	
    	int trainingTurnsRemaining = knightBarracksSite.getStructure().getParam1();
    	if (trainingTurnsRemaining == 0) {
    		return 0;
    	} else {
    		return (TRAINING_KNIGHT_TURNS_NUMBER + 1 - trainingTurnsRemaining) * SAFE_DISTANCE_PER_REMAINING_TURN;
    	}
    }
    
    /**
     * Get TOWER on which we consider something else could be built.
     * We consider a TOWER as obsolete if there are 3 or more other TOWER in front of it.
     * 
     * @param allyTowers
     * @param startingAllyQueenCoordinates
     * @return Collection<Site>
     */
    public static Collection<Site> getObsoleteAllyTowers(Collection<Site> allyTowers, Coordinates startingAllyQueenCoordinates) {
    	if (allyTowers.size() <= 4) {
    		return new ArrayList<>();
    	}

    	return allyTowers
    			.stream()
    			.filter(allyTower -> getTowerSitesInFrontOfCoordinates(allyTowers, allyTower.getCoordinates(), GameBoardUtils.isLeftSide(startingAllyQueenCoordinates)).size() >= 3)
    			.collect(Collectors.toList());
    }
    
    /**
     * Get TOWER in front of coordinates.
     * 
     * @param towers
     * @param coordinates
     * @param isStartingLeftSide
     * @return Collection<Site>
     */
    public static Collection<Site> getTowerSitesInFrontOfCoordinates(Collection<Site> towers, Coordinates coordinates, boolean isStartingLeftSide) {	
    	return towers
    			.stream()
    			.filter(tower -> (isStartingLeftSide && tower.getCoordinates().getX() > coordinates.getX())
    				|| (!isStartingLeftSide && tower.getCoordinates().getX() < coordinates.getX()))
    			.collect(Collectors.toList());
    }
    
    /**
     * Check if enemyKnightBarracksSite can be reach by the QUEEN before ending TRAIN.
     * 
     * @param enemyKnightBarracksSite
     * @param allyQueenCoordinates
     * @return boolean
     */
    public static boolean isEnemyKnightBarracksReachable(Site enemyKnightBarracksSite, Coordinates allyQueenCoordinates) {
    	final int QUEEN_SPEED = 60;
    	final int TRAINING_KNIGHT_TURNS = 5;
    	double distanceFromQueenToSite = MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, enemyKnightBarracksSite.getCoordinates());
    	
    	return distanceFromQueenToSite < (QUEEN_SPEED * TRAINING_KNIGHT_TURNS);
    }
	
}
