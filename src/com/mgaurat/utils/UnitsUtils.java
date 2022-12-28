package com.mgaurat.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;

/**
 * Final class for static methods that manipulates Unit.
 * 
 * @author mgaurat
 *
 */
public final class UnitsUtils {
	
	private static final int LOW_HEALTH_QUEEN = 20;
	private static final int MID_HEALTH_QUEEN = 40;
	
	private UnitsUtils() {}
	
	/**
	 * Get the QUEEN from the input map that provides a list of the Unit for each unit type.
	 * The list of QUEEN must be of size 1 because the input map must be for only one player.
	 * 
	 * @param unitsByType
	 * @return Unit
	 */
	public static Unit getQueen(Map<UnitEnum, List<Unit>> unitsByType) {		
		return unitsByType.get(UnitEnum.QUEEN).get(0);
	}
	
	/**
	 * Check if the Coordinates is considered as safe regarding enemy KNIGHT.
	 * Safe distance is defined in a constant.
	 * 
	 * @param coordinates
	 * @param enemyUnitsByType
	 * @return boolean
	 */
	public static boolean isItSafeAtCoordinatesRegardingEnemyKnights(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType, int safeDistance) {
		return UnitsUtils.getDistanceBetweenNearestKnightAndCoordinates(coordinates, enemyUnitsByType) > safeDistance;
	}
	
	/**
	 * Get the distance between the input Coordinates and the nearest KNIGHT form the input map.
	 * 
	 * @param coordinates
	 * @param unitsByType
	 * @return double
	 */
	public static double getDistanceBetweenNearestKnightAndCoordinates(Coordinates coordinates, Map<UnitEnum, List<Unit>> unitsByType) {
		List<Unit> knights = unitsByType.get(UnitEnum.KNIGHT);
		Unit nearestKnight = getNearestUnit(coordinates, knights);
		return nearestKnight == null ? Double.MAX_VALUE : MathUtils.getDistanceBetweenTwoCoordinates(coordinates, nearestKnight.getCoordinates());
	}
	
	public static Unit getNearestUnit(Coordinates coordinates, Collection<Unit> units) {
		return units
				.stream()
				.collect(Collectors.minBy(Comparator.comparingDouble(unit -> MathUtils.getDistanceBetweenTwoCoordinates(coordinates, unit.getCoordinates()))))
				.orElse(null);
	}
	
	/**
	 * Check if a GIANT is close to the Coordinates. The distance considered as "close to" is defined in a constant.
	 * 
	 * @param giants
	 * @param coordinates
	 * @return boolean
	 */
	public static boolean isGiantCloseToCoordinates(Collection<Unit> giants, Coordinates coordinates) {
		final int GIANT_SAFE_ZONE = 200;
		return giants
				.stream()
				.anyMatch(giant -> MathUtils.getDistanceBetweenTwoCoordinates(coordinates, giant.getCoordinates()) <= GIANT_SAFE_ZONE);
	}
	
	public static boolean canAllyQueenReachSiteSafely(Unit allyQueen, Coordinates startingAllyQueenCoordinates, Site site, 
			Unit nearestEnemyKnight, Collection<Site> enemyTowers, boolean isNotMineBuild) {
		return !site.isBehindEnemyLine(allyQueen, enemyTowers)
				&& (!site.isInForwardDirection(allyQueen.getCoordinates(), startingAllyQueenCoordinates)
				|| (allyQueen.getHealth() < LOW_HEALTH_QUEEN && !StructuresUtils.isCoordinatesInRangeOfTowers(site.getCoordinates(), enemyTowers, 1)
				&& allyQueen.canReachSiteSomeTurnsBeforeUnit(site, nearestEnemyKnight, isNotMineBuild ? 0 : 8))
				|| (allyQueen.getHealth() < MID_HEALTH_QUEEN && allyQueen.getHealth() >= LOW_HEALTH_QUEEN 
				&& allyQueen.canReachSiteSomeTurnsBeforeUnit(site, nearestEnemyKnight, isNotMineBuild ? -1 : 1.5))
				|| (allyQueen.getHealth() >= MID_HEALTH_QUEEN && !StructuresUtils.isCoordinatesInRangeOfTowers(site.getCoordinates(), enemyTowers, 2)));
	}
	
}
