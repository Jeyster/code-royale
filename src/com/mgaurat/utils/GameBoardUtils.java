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
	 * Get the Coordinates evaluated to be the safest regarding my BARRACKS and TOWER.
	 * 
	 * @param myKnightBarracks
	 * @param allyTowerSites
	 * @param allySites
	 * @return Coordinates
	 */
	public static Coordinates getSafestCoordinates(Site myKnightBarracks, Collection<Site> allyTowerSites, Collection<Site> allySites) {
		Coordinates safestCoordinates;
    	if (allyTowerSites.size() >= 1 && myKnightBarracks != null) {
    		safestCoordinates = GameBoardUtils.getSafestCoordinatesFromMyBarracksAndTowerSites(myKnightBarracks, allyTowerSites);
    	} else if (myKnightBarracks != null) {
    		safestCoordinates = myKnightBarracks.getCoordinates();
    	} else if (allyTowerSites.size() >= 2) {
    		safestCoordinates = SitesUtils.getCoordinatesBetweenTwoRandomSites(allyTowerSites);
    	} else if (allyTowerSites.size() >= 1) {
    		safestCoordinates = SitesUtils.getRandomSiteCoordinates(allyTowerSites);
    	} else {
    		safestCoordinates = SitesUtils.getRandomSiteCoordinates(allySites);
    	}
    	
    	return safestCoordinates;
	}
	
	/**
	 * Get the Coordinates evaluated to be the safest regarding an ally KNIGHT BARRACKS and the ally TOWER.
	 * Y coordinate is the average y coordinate from the the ally TOWER.
	 * X coordinate is the extreme left or ride side of the game board are depending on the side a the KNIGHT BARRACKS.
	 * 
	 * @param myKnightBarracksSite
	 * @param allyTowerSites
	 * @return Coordinates
	 */
	public static Coordinates getSafestCoordinatesFromMyBarracksAndTowerSites(Site myKnightBarracksSite, Collection<Site> allyTowerSites) {
		if (myKnightBarracksSite == null) {
			return null;
		}
		
		int yCoordinate = SitesUtils.getAverageSiteCoordinates(allyTowerSites).getY();
		if (isLeftSide(myKnightBarracksSite.getCoordinates())) {
			return new Coordinates(0, yCoordinate);
		} else {
			return new Coordinates(X_LENGTH, yCoordinate);
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
