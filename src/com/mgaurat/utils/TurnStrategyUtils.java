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
    final static int ENEMY_KNIGHTS_THRESHOLD = 7;
		
	private TurnStrategyUtils() {}
	
	/**
     * Check if we have to MOVE to a safe place. Depends on :
     *	- ally QUEEN health 
     *	- ally QUEEN coordinates
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
			int emptySitesNumber, int enemyKnightsNumber, int safeDistance, 
			Collection<Site> enemyKnightBarracksSites, Collection<Site> enemyMines) {
		Collection<Unit> enemyKnights = enemyUnitsByType.get(UnitEnum.KNIGHT);
		return UnitsUtils.getNearestUnits(allyQueenCoordinates, enemyKnights, 200).size() > 0
				|| UnitsUtils.getNearestUnits(allyQueenCoordinates, enemyKnights, 500).size() > 2
				|| UnitsUtils.getNearestUnits(allyQueenCoordinates, enemyKnights, 1200).size() > 4
				|| (queenHealth < LOW_HEALTH_QUEEN && !GameBoardUtils.isItSafeAtCoordinates(allyQueenCoordinates, enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites, enemyMines))
				|| (queenHealth < 40 && queenHealth >= LOW_HEALTH_QUEEN 
					&& !UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(allyQueenCoordinates, enemyUnitsByType, 100))
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
		
		if (SitesUtils.isReallyCloseToCoordinates(allyQueenCoordinates, nearestSiteToBuildATower.getCoordinates())) {
			return true;
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
				&& GameBoardUtils.isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(nearestSiteToBuildATowerCoordinates, safestCoordinates, allyQueenCoordinates)) {
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
			int goldProductionIWant, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, int safeDistance, 
			Collection<Site> enemyKnightBarracksSites, Collection<Site> enemyMines, int touchedSite) {
		
		if (targetedSiteToBuildAMine == null || StructuresUtils.getGoldProduction(allyMineSites) >= goldProductionIWant ) {
			return false;
		}
		
		if (touchedSite == targetedSiteToBuildAMine.getId()) {
			return true;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, 200)
					&& !StructuresUtils.isCoordinatesInRangeOfTowers(targetedSiteToBuildAMine.getCoordinates(), enemyTowerSites, 2);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites, enemyMines);
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
			int safeDistance, Collection<Site> enemyKnightBarracksSites, Collection<Site> enemyMines, int touchedSite) {
		
		if (nearestEmptySite == null || allyTowersNumber >= allyTowersNumberIWant || enemyMines.isEmpty()) {
			return false;
		}
		
		if (allyTowersNumber < 3 || touchedSite == nearestEmptySite.getId()) {
			return true;
		} else if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isCoordinatesInRangeOfTowers(nearestEmptySite.getCoordinates(), enemyTowerSites, 2);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites, enemyMines);
		}
	}
	
	public static boolean isTowerMoveOrBuildOnEnemyBarracksStrategyOk(int queenHealth, Site nearestSite,
			Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, int safeDistance, 
			Collection<Site> enemyKnightBarracksSites, Coordinates allyQueenCoordinates, Collection<Site> enemyMines, int touchedSite) {
		
		if (nearestSite == null || !StructuresUtils.isEnemyKnightBarracksReachable(nearestSite, allyQueenCoordinates)) {
			return false;
		}
		
		if (touchedSite == nearestSite.getId()) {
			return true;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isCoordinatesInRangeOfTowers(nearestSite.getCoordinates(), enemyTowerSites, 2)
					&& enemyUnitsByType.get(UnitEnum.KNIGHT).size() < 2;			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(nearestSite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites, enemyMines);
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
	 * @param allyKnightBarracks
	 * @param enemyUnitsByType
	 * @param enemyTowers
	 * @return boolean
	 */
	public static boolean isKnightBarracksMoveOrBuildStrategyOk(int queenHealth, Site nearestSite, Collection<Site> allyKnightBarracks, 
			Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowers, int safeDistance, 
			Collection<Site> enemyKnightBarracks, Collection<Site> enemyMines, int touchedSite, int goldProduction) {
		
		if (nearestSite == null || allyKnightBarracks.size() >= 2 
				|| (allyKnightBarracks.size() == 1 && goldProduction < 15)) {
			return false;
		}
		
		if (touchedSite == nearestSite.getId()) {
			return true;
		}

		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return !StructuresUtils.isCoordinatesInRangeOfTowers(nearestSite.getCoordinates(), enemyTowers, 2);			
		} else {
			return GameBoardUtils.isItSafeAtCoordinates(nearestSite.getCoordinates(), enemyUnitsByType, enemyTowers, safeDistance, enemyKnightBarracks, enemyMines);
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
	 * @param allyGiantBarracksSites
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @param enemyTowersNumberThreshold
	 * @return boolean
	 */
	public static boolean isGiantBarracksMoveOrBuildStrategyOk(int queenHealth, Site nearestEmptySite, int enemyTowersNumber,
			Collection<Site> allyGiantBarracksSites, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, 
			int enemyTowersNumberThreshold, int safeDistance, Collection<Site> enemyKnightBarracksSites, Collection<Site> allyMineSites, 
			Collection<Site> enemyMines, int touchedSite) {
		
		if (nearestEmptySite == null 
				|| enemyTowersNumber < enemyTowersNumberThreshold
				|| StructuresUtils.getGoldProduction(allyMineSites) < 10) {
			return false;
		}
		
		if (touchedSite == nearestEmptySite.getId()) {
			return true;
		}
		
		if (queenHealth >= LOW_HEALTH_QUEEN) {
			return allyGiantBarracksSites.isEmpty()
            		&& !StructuresUtils.isCoordinatesInRangeOfTowers(nearestEmptySite.getCoordinates(), enemyTowerSites, 2);			
		} else {
			return allyGiantBarracksSites.isEmpty()
            		&& GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites, enemyMines);
		}
	}
	
	/**
	 * We have to train a GIANT if :
	 * 	- there are more enemy TOWER than a given threshold or there is an enemy TOWER and we produce enough gold
	 * 	- there is less than 2 ally GIANT
	 * 	- there is at least 1 ally GIANT BARRACKS
	 * 
	 * @param enemyTowersNumber
	 * @param enemyTowersNumberThreshold
	 * @param allyMineSites
	 * @param allyGiants
	 * @param allyGiantBarracksSites
	 * @return boolean
	 */
	public static boolean isGiantTrainStrategyOk(int enemyTowersNumber, int enemyTowersNumberThreshold,
			Collection<Site> allyMineSites, Collection<Unit> allyGiants, Collection<Site> allyGiantBarracksSites) {
		return (enemyTowersNumber > enemyTowersNumberThreshold ||
        		(enemyTowersNumber > 1 && StructuresUtils.getGoldProduction(allyMineSites) >= 8))
        		&& allyGiants.size() < 2
        		&& !allyGiantBarracksSites.isEmpty();
	}
	

}
