package com.mgaurat.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mgaurat.enums.GameBoardQuarterEnum;
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
	 * Get the Coordinates evaluated to be the safest regarding my starting ally QUEEN Coordinates, the ally TOWER and the enemy KNIGHT.
	 * 
	 * @param startingAllyQueenCoordinates
	 * @param allyTowerSites
	 * @param allySites
	 * @return Coordinates
	 */
	public static Coordinates getSafestCoordinates(Coordinates startingAllyQueenCoordinates, Collection<Site> allyTowerSites, Collection<Unit> enemyKnights, Coordinates allyQueenCoordinates) {
		Coordinates safestCoordinates;
    	System.err.println("---Get safest coordinates---");
		if (allyTowerSites.size() >= 3) {
    		Site safestAllyTower = StructuresUtils.getSafestTower(allyTowerSites, startingAllyQueenCoordinates);
    		System.err.println("Safest TOWER ID : " + safestAllyTower.getId());
    		
    		Unit nearestEnemyKnight = UnitsUtils.getNearestUnit(safestAllyTower.getCoordinates(), enemyKnights);
    		if (nearestEnemyKnight != null) {
    			System.err.println("Nearest enemy knight X : " + nearestEnemyKnight.getCoordinates().getX());    			
    			System.err.println("Nearest enemy knight Y : " + nearestEnemyKnight.getCoordinates().getY());    			
    		}
    		
    		safestCoordinates = StructuresUtils.getCoordinatesBehindTowerOppositeToNearestEnemyKnight(nearestEnemyKnight, safestAllyTower, startingAllyQueenCoordinates);
    		System.err.println("Safest X : " + safestCoordinates.getX());
    		System.err.println("Safest Y : " + safestCoordinates.getY());
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
    
    /**
     * Says in which cardinal corner is the coordinates with respect to the referenceCoordinates.
     * 
     * @param coordinates
     * @param referenceCoordinates
     * @return GameBoardQuarterEnum
     */
    public static GameBoardQuarterEnum getQuarterOfCoordinatesWithRespectToAnotherCoordinates(Coordinates coordinates, Coordinates referenceCoordinates) {
    	int xCoordinate = coordinates.getX();  
    	int yCoordinate = coordinates.getY();
    	int xReferenceCoordinate = referenceCoordinates.getX();
    	int yReferenceCoordinate = referenceCoordinates.getY();
    	if (xCoordinate < xReferenceCoordinate && yCoordinate < yReferenceCoordinate) {
    		return GameBoardQuarterEnum.TOPLEFT;
    	} else if (xCoordinate < xReferenceCoordinate && yCoordinate >= yReferenceCoordinate) {
    		return GameBoardQuarterEnum.BOTTOMLEFT;
    	} else if (xCoordinate >= xReferenceCoordinate && yCoordinate < yReferenceCoordinate) {
    		return GameBoardQuarterEnum.TOPRIGHT;
    	} else {
    		return GameBoardQuarterEnum.BOTTOMRIGHT;
    	}
    }
    	
}
