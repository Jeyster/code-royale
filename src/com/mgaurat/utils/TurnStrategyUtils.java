package com.mgaurat.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;

/**
 * Final class for static methods that help to choose a turn strategy.
 * 
 * @author mgaurat
 *
 */
public final class TurnStrategyUtils {
	
    final static int LOW_HEALTH_QUEEN = 20;
    final static int ENEMY_KNIGHTS_THRESHOLD = 8;
		
	private TurnStrategyUtils() {}
	
	/**
     * Check if we have to MOVE to a safe place. Depends on :
     *	- ally QUEEN health 
     *	- ally QUEEN coordinates
     *	- free sites that left
     *	- number of enemy KNIGHT
     *
	 * @param queenHealth
	 * @param allyQueenCoordinates
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @param emptySitesNumber
	 * @param enemyKnightsNumber
	 * @return
	 */
	public static boolean isRunAwayStrategyOk(int queenHealth, Coordinates allyQueenCoordinates,
			Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites,
			int emptySitesNumber, int enemyKnightsNumber, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
		return (queenHealth < LOW_HEALTH_QUEEN 
        		&& !GameBoardUtils.isItSafeAtCoordinates(allyQueenCoordinates, enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites))
				|| (queenHealth >= 40 && !UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(allyQueenCoordinates, enemyUnitsByType, 10))
				|| (queenHealth < 40 && queenHealth >= LOW_HEALTH_QUEEN && !UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(allyQueenCoordinates, enemyUnitsByType, 100))
				|| enemyKnightsNumber > ENEMY_KNIGHTS_THRESHOLD;
	}
	
	/**
	 * Check if it is possible to BUILD a TOWER when running away.
	 * If an enemy GIANT is too close, do not BUILD a TOWER.
	 * If nearestSiteToBuildATower is not so far, do it.
	 * The distance "not so far" is defined by constants.
	 * 
	 * @param allyQueenCoordinates
	 * @param nearestSiteToBuildATower
	 * @param enemyGiants
	 * @return boolean
	 */
	public static boolean isBuildTowerWhenRunningAwayStrategyOk(Coordinates allyQueenCoordinates, Coordinates safestCoordinates,
			Site nearestSiteToBuildATower, Collection<Unit> enemyGiants) {
		if (nearestSiteToBuildATower == null
				|| UnitsUtils.isGiantCloseToCoordinates(enemyGiants, allyQueenCoordinates)) {
			return false;
		}
		
		final int X_RANGE = 200;
		final int Y_RANGE = 200;
		
		Coordinates nearestSiteToBuildATowerCoordinates = nearestSiteToBuildATower.getCoordinates();
		int xNearestSiteToBuildATowerCoordinates = nearestSiteToBuildATowerCoordinates.getX();
		int yNearestSiteToBuildATowerCoordinates = nearestSiteToBuildATowerCoordinates.getY();
		int xAllyQueenCoordinates = allyQueenCoordinates.getX();
		int yAllyQueenCoordinates = allyQueenCoordinates.getY();
		if (Math.abs(xNearestSiteToBuildATowerCoordinates - xAllyQueenCoordinates) <= X_RANGE
				&& Math.abs(yNearestSiteToBuildATowerCoordinates - yAllyQueenCoordinates) <= Y_RANGE
				&& GameBoardUtils.isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(nearestSiteToBuildATowerCoordinates, allyQueenCoordinates, safestCoordinates)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if we choose to BUILD a MINE. Depends on :
	 *	- ally QUEEN health
     *	- current gold production
     *	- gold production we want
     *	- nearest empty Site where a MINE can be built (gold not depleted)
     *	- enemy UNIT location (because enemy KNIGHT destroy MINE)
     *	- enemy TOWER location
     *
	 * @param queenHealth
	 * @param targetedSiteToBuildAMine
	 * @param allyMineSites
	 * @param goldProductionIWant
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @return boolean
	 */
	public static boolean isMineMoveOrBuildStrategyOk(int queenHealth, Site targetedSiteToBuildAMine, Collection<Site> allyMineSites, 
			int goldProductionIWant, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
		
		if (targetedSiteToBuildAMine == null || StructuresUtils.getGoldProduction(allyMineSites) >= goldProductionIWant ) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, safeDistance)
					&& !StructuresUtils.isCoordinatesInRangeOfTowers(targetedSiteToBuildAMine.getCoordinates(), enemyTowerSites, 2);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
		}
	}

	/**
	 * Check if we choose to BUILD a TOWER. Depends on :
	 *	- ally QUEEN health
     *	- ally TOWER number
     *	- number of TOWER we want
     *	- nearest empty Site
     *	- enemy UNIT location
     *	- enemy TOWER location
     *
	 * @param queenHealth
	 * @param nearestEmptySite
	 * @param allyTowersNumber
	 * @param allyTowersNumberIWant
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @return boolean
	 */
	public static boolean isTowerMoveOrBuildStrategyOk(int queenHealth, Site nearestEmptySite, int allyTowersNumber,
			int allyTowersNumberIWant, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, 
			int safeDistance, Collection<Site> enemyKnightBarracksSites) {
		
		if (nearestEmptySite == null || allyTowersNumber >= allyTowersNumberIWant) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isCoordinatesInRangeOfTowers(nearestEmptySite.getCoordinates(), enemyTowerSites, 2);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
		}
	}
	
	/**
	 * Check if we choose to BUILD a KNIGHT BARRACKS. Depends on :
	 *	- ally QUEEN health
     *	- nearest Site
     *	- ally KNIGHT BARRACKS Sites
     *	- enemy UNIT location
     *	- enemy TOWER location
     *
	 * @param queenHealth
	 * @param nearestSite
	 * @param allyBarracksSites
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @return boolean
	 */
	public static boolean isKnightBarracksMoveOrBuildStrategyOk(int queenHealth, Site nearestSite, Collection<Site> allyBarracksSites, 
			Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
		
		if (nearestSite == null || !allyBarracksSites.isEmpty()) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isCoordinatesInRangeOfTowers(nearestSite.getCoordinates(), enemyTowerSites, 2);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(nearestSite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
		}
	}
	
	/**
	 * Check if we choose to BUILD a GIANT BARRACKS. Depends on :
	 *	- ally QUEEN health
     *	- nearest empty Site
     *	- enemy TOWER number -> there is a threshold
     *	- ally GIANT BARRACKS Sites
     *	- enemy UNIT location
     *	- enemy TOWER location
	 * 
	 * @param queenHealth
	 * @param nearestEmptySite
	 * @param enemyTowersNumber
	 * @param allyBarracksSites
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @param enemyTowersNumberThreshold
	 * @return boolean
	 */
	public static boolean isGiantBarracksMoveOrBuildStrategyOk(int queenHealth, Site nearestEmptySite, int enemyTowersNumber,
			Collection<Site> allyBarracksSites, Map<UnitEnum, List<Unit>> enemyUnitsByType, 
			Collection<Site> enemyTowerSites, int enemyTowersNumberThreshold, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
		
		if (nearestEmptySite == null || enemyTowersNumber <= enemyTowersNumberThreshold) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)
            		&& !StructuresUtils.isCoordinatesInRangeOfTowers(nearestEmptySite.getCoordinates(), enemyTowerSites, 2);			
		} else {
			return !StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)
            		&& GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
		}
	}

}
