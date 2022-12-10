package com.mgaurat.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;

/**
 * Final class for static methods that gives informations about the game board area.
 * Basically it provides Coordinates.
 * 
 * @author mgaurat
 *
 */
public final class GameBoardUtils {
	
	private final static int X_LENGTH = 1920;
	private final static int Y_LENGTH = 1000;
	
	/**
	 * Check if the input Coordinates in the left half side of the game board area.
	 * 
	 * @param coordinates
	 * @return boolean
	 */
	public static boolean isLeftSide(Coordinates coordinates) {
		return coordinates.getX() < X_LENGTH/2;
	}
	
	/**
	 * Get the Coordinates evaluated to be the safest regarding my starting ally QUEEN Coordinates and the ally TOWER.
	 * 
	 * @param startingAllyQueenCoordinates
	 * @param allyTowerSites
	 * @param allySites
	 * @return Coordinates
	 */
	public static Coordinates getSafestCoordinates(Coordinates startingAllyQueenCoordinates, Collection<Site> allyTowerSites, Collection<Site> allySites) {
		Coordinates safestCoordinates;
    	if (allyTowerSites.size() >= 3) {
    		Site safestAllyTower = StructuresUtils.getSafestTower(allyTowerSites, startingAllyQueenCoordinates);
    		safestCoordinates = StructuresUtils.getCoordinatesBehindTower(startingAllyQueenCoordinates, safestAllyTower);
    	} else {
    		safestCoordinates = getSafestCoordinatesFromStartingAllyQueen(startingAllyQueenCoordinates);
    	}
    	
    	return safestCoordinates;
	}
	
	/**
	 * Get the Coordinates evaluated to be the safest regarding the starting ally QUEEN Coordinates.
	 * If we start at left side, the safest Coordinates is the bottom left corner.
	 * If we start at right side, the safest Coordinates is the top right corner.
	 * 
	 * @param startingAllyQueenCoordinates
	 * @return Coordinates
	 */
	public static Coordinates getSafestCoordinatesFromStartingAllyQueen(Coordinates startingAllyQueenCoordinates) {
		if (isLeftSide(startingAllyQueenCoordinates)) {
			return new Coordinates(0, Y_LENGTH);
		} else {
			return new Coordinates(X_LENGTH, 0);
		}
	}
	
	/**
	 * Check if the input Coordinates is considered as safe.
	 * It depends on enemy KNIGHT, enemy TOWER and KNIGHT BARRACKS.
	 * 
	 * @param coordinates
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @return boolean
	 */
    public static boolean isItSafeAtCoordinates(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType, 
    		Collection<Site> enemyTowerSites, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
    	return UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(coordinates, enemyUnitsByType, safeDistance)
    			&& !StructuresUtils.isCoordinatesInRangeOfTowers(coordinates, enemyTowerSites, 1)
    			&& !StructuresUtils.isEnemyKnightBarracksDangerous(coordinates, enemyKnightBarracksSites);
    }
    
    /**
     * a is the Coordinates to evaluate.
     * b is the Coordinates to go.
     * c is the QUEEN Coordinates.
     * 
     * @param a
     * @param b
     * @param c
     * @return boolean
     */
    public static boolean isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(Coordinates a, Coordinates b, Coordinates c) {
    	int xa = a.getX();
    	int ya = a.getY();
    	int xb = b.getX();
    	int yb = b.getY();
    	int xc = c.getX();
    	int yc = c.getY();
    	
    	boolean isXcLessThanXb = xc < xb;
    	boolean isYcLessThanYb = yc < yb;
    	boolean isXaLessThanXb = xa < xb;
    	boolean isYaLessThanYb = ya < yb;
    	
    	if (isXcLessThanXb && isYcLessThanYb) {
    		return isXaLessThanXb && isYaLessThanYb;
    	} else if (isXcLessThanXb && !isYcLessThanYb) {
    		return isXaLessThanXb && !isYaLessThanYb;
    	} else if (!isXcLessThanXb && isYcLessThanYb) {
    		return !isXaLessThanXb && isYaLessThanYb;
    	} else {
    		return !isXaLessThanXb && !isYaLessThanYb;
    	}
    }
    	
}
