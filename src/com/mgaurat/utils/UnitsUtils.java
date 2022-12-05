package com.mgaurat.utils;

import java.util.List;
import java.util.Map;

import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Unit;

/**
 * Final class for utilitaries methods that manipulates Unit.
 * 
 * @author mgaurat
 *
 */
public final class UnitsUtils {
	
	private UnitsUtils() {}
	
	/**
	 * Get the QUEEN from the input map that provides a list of the Unit for each unit type.
	 * The list of QUEEN must be of size 1 because the input map must be for only one player.
	 * 
	 * @param unitsByType
	 * @return Unit
	 */
	public static Unit getQueen(Map<UnitEnum, List<Unit>> unitsByType) {
		List<Unit> queens = unitsByType.get(UnitEnum.QUEEN);
		if (queens.size() > 1) {
			return null;
		}
		
		return queens.get(0);
	}
	
	/**
	 * Check if the Coordinates is considered as safe regarding enemy KNIGHT.
	 * Safe distance is defined in a constant.
	 * 
	 * @param coordinates
	 * @param enemyUnitsByType
	 * @return boolean
	 */
	public static boolean isItSafeAtCoordinatesRegardingEnemyKnights(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType) {
		final double SAFE_DISTANCE = 500;
		return UnitsUtils.getDistanceBetweenNearestKnightAndCoordinates(coordinates, enemyUnitsByType) > SAFE_DISTANCE;
	}
	
	/**
	 * Get the distance between the input Coordinates and the nearest KNIGHT form the input map.
	 * 
	 * @param coordinates
	 * @param unitsByType
	 * @return double
	 */
	public static double getDistanceBetweenNearestKnightAndCoordinates(Coordinates coordinates, Map<UnitEnum, List<Unit>> unitsByType) {
        double distanceToKnight;
        double distanceToNearestKnight = Double.MAX_VALUE;
		List<Unit> knights = unitsByType.get(UnitEnum.KNIGHT);
		Coordinates knightCoordinates;
		for (Unit knight : knights) {
			knightCoordinates = knight.getCoordinates();
			distanceToKnight = MathUtils.getDistanceBetweenTwoCoordinates(coordinates, knightCoordinates);
			if (distanceToKnight < distanceToNearestKnight) {
				distanceToNearestKnight = distanceToKnight;
			}
		}
		
		return distanceToNearestKnight;
	}
	
}
