package com.mgaurat.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;

/**
 * Final class for utilitaries methods that help to choose a turn strategy.
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
			int emptySitesNumber, int enemyKnightsNumber) {
		return (queenHealth < LOW_HEALTH_QUEEN 
        		&& !GameBoardUtils.isItSafeAtCoordinates(allyQueenCoordinates, enemyUnitsByType, enemyTowerSites))
        		|| emptySitesNumber == 0
        		|| enemyKnightsNumber > ENEMY_KNIGHTS_THRESHOLD;
	}
	
	public static boolean isBuildTowerWhenRunningAwayStrategyOk(Coordinates allyQueenCoordinates, Site nearestSiteToBuildATower) {
		if (nearestSiteToBuildATower == null) {
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
				&& Math.abs(yNearestSiteToBuildATowerCoordinates - yAllyQueenCoordinates) <= Y_RANGE) {
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
			int goldProductionIWant, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites) {
		
		if (targetedSiteToBuildAMine == null || StructuresUtils.getGoldProduction(allyMineSites) >= goldProductionIWant ) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType)
					&& !StructuresUtils.isCoordinatesInRangeOfAtLeastTwoTowers(targetedSiteToBuildAMine.getCoordinates(), enemyTowerSites);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites);
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
			int allyTowersNumberIWant, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites) {
		
		if (nearestEmptySite == null || allyTowersNumber >= allyTowersNumberIWant) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isCoordinatesInRangeOfAtLeastTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites);
		}
	}
	
	/**
	 * Check if we choose to BUILD a KNIGHT BARRACKS. Depends on :
	 *	- ally QUEEN health
     *	- nearest empty Site
     *	- ally KNIGHT BARRACKS Sites
     *	- enemy UNIT location
     *	- enemy TOWER location
     *
	 * @param queenHealth
	 * @param nearestEmptySite
	 * @param allyBarracksSites
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @return boolean
	 */
	public static boolean isKnightBarracksMoveOrBuildStrategyOk(int queenHealth, Site nearestEmptySite, 
			Collection<Site> allyBarracksSites, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites) {
		
		if (nearestEmptySite == null || !allyBarracksSites.isEmpty()) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isCoordinatesInRangeOfAtLeastTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites);
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
			Collection<Site> enemyTowerSites, int enemyTowersNumberThreshold) {
		
		if (nearestEmptySite == null || enemyTowersNumber <= enemyTowersNumberThreshold) {
			return false;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)
            		&& !StructuresUtils.isCoordinatesInRangeOfAtLeastTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites);			
		} else {
			return !StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)
            		&& GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites);
		}
	}

}
