package com.mgaurat.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;

/**
 * Final class for utilitaries methods that gives informations about the game board area.
 * Basically it provides Coordinates.
 * 
 * @author mgaurat
 *
 */
public class GameBoardUtils {
	
	private final static int X_LENGTH = 1920;
	private final static int Y_LENGTH = 1000;
	
	/**
	 * Is the input Coodinates in the left half side of the game board area.
	 * 
	 * @param coordinates
	 * @return boolean
	 */
	public static boolean isLeftSide(Coordinates coordinates) {
		return coordinates.getX() < X_LENGTH/2;
	}
	
	/**
	 * Get the Coordinates evaluated to be the safest regarding my starting ally QUEEN Coordinates and TOWER.
	 * 
	 * @param startingAllyQueenCoordinates
	 * @param allyTowerSites
	 * @param allySites
	 * @return Coordinates
	 */
	public static Coordinates getSafestCoordinates(Coordinates startingAllyQueenCoordinates, Collection<Site> allyTowerSites, Collection<Site> allySites) {
		Coordinates safestCoordinates;
    	if (allyTowerSites.size() >= 1) {
//    		safestCoordinates = GameBoardUtils.getSafestCoordinatesFromStartingAllyQueenAndTowerSites(startingAllyQueenCoordinates, allyTowerSites);
    		safestCoordinates = GameBoardUtils.getSafestCoordinatesFromStartingAllyQueen(startingAllyQueenCoordinates);
    	} else {
    		safestCoordinates = startingAllyQueenCoordinates;
//    	} else if (allyTowerSites.size() >= 2) {
//    		safestCoordinates = SitesUtils.getCoordinatesBetweenTwoRandomSites(allyTowerSites);
//    	} else if (allyTowerSites.size() >= 1) {
//    		safestCoordinates = SitesUtils.getRandomSiteCoordinates(allyTowerSites);
//    	} else {
//    		safestCoordinates = SitesUtils.getRandomSiteCoordinates(allySites);
    	}
    	
    	return safestCoordinates;
	}
	
	/**
	 * Get the Coordinates evaluated to be the safest regarding the starting ally QUEEN Coordinates and the ally TOWER.
	 * Y coordinate is the average y coordinate from the the ally TOWER.
	 * X coordinate is the extreme left or ride side of the game board are depending on the starting ally QUEEN Coordinates.
	 * 
	 * @param startingAllyQueenCoordinates
	 * @param allyTowerSites
	 * @return Coordinates
	 */
	public static Coordinates getSafestCoordinatesFromStartingAllyQueenAndTowerSites(Coordinates startingAllyQueenCoordinates, Collection<Site> allyTowerSites) {
		int yCoordinate = SitesUtils.getAverageSiteCoordinates(allyTowerSites).getY();
		if (isLeftSide(startingAllyQueenCoordinates)) {
			return new Coordinates(0, yCoordinate);
		} else {
			return new Coordinates(X_LENGTH, yCoordinate);
		}
	}
	
	public static Coordinates getSafestCoordinatesFromStartingAllyQueen(Coordinates startingAllyQueenCoordinates) {
		if (isLeftSide(startingAllyQueenCoordinates)) {
			return new Coordinates(0, Y_LENGTH);
		} else {
			return new Coordinates(X_LENGTH, 0);
		}
	}
	
	/**
	 * Check if the input Coordinates is considered as safe.
	 * It depends on enemy KNIGHT and TOWER.
	 * 
	 * @param coordinates
	 * @param enemyUnitsByType
	 * @param enemyTowerSites
	 * @return boolean
	 */
    public static boolean isItSafeAtCoordinates(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites) {
    	return UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(coordinates, enemyUnitsByType)
    			&& StructuresUtils.isCoordinatesInRangeOfAnyTower(coordinates, enemyTowerSites);
    }
	
}
