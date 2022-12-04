package com.mgaurat.utils;

import java.util.Collection;
import java.util.Iterator;

import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;

public class GameBoardUtils {
	
	private final static int X_LENGTH = 1920;
	
	public static boolean isLeftSide(Coordinates coordinates) {
		return coordinates.getX() < X_LENGTH/2;
	}
	
	public static Coordinates getASafeCoordinates(Site myKnightBarracks, Collection<Site> allyTowerSites, Collection<Site> allySites) {
		Coordinates safestCoordinates;
    	if (allyTowerSites.size() >= 1 && myKnightBarracks != null) {
    		safestCoordinates = GameBoardUtils.getASafeCoordinatesFromMyBarracksAndTowerSites(myKnightBarracks, allyTowerSites);
    	} else if (myKnightBarracks != null) {
    		safestCoordinates = myKnightBarracks.getCoordinates();
    	} else if (allyTowerSites.size() >= 2) {
    		safestCoordinates = GameBoardUtils.getCoordinatesBetweenTwoSites(allyTowerSites);
    	} else if (allyTowerSites.size() >= 1) {
    		safestCoordinates = GameBoardUtils.getASiteCoordinates(allyTowerSites);
    	} else {
    		safestCoordinates = GameBoardUtils.getASiteCoordinates(allySites);
    	}
    	
    	return safestCoordinates;
	}
	
	public static Coordinates getASafeCoordinatesFromMyBarracksAndTowerSites(Site myKnightBarracksSite, Collection<Site> allyTowerSites) {
		int yCoordinate = StructuresUtils.getTheSafestTower(allyTowerSites, myKnightBarracksSite).getCoordinates().getY();
		if (isLeftSide(myKnightBarracksSite.getCoordinates())) {
			return new Coordinates(0, yCoordinate);
		} else {
			return new Coordinates(X_LENGTH, yCoordinate);
		}
	}
	
	public static Coordinates getCoordinatesBetweenTwoSites(Collection<Site> sites) {
		if (sites.size() < 2) {
			return null;
		}
		
		Iterator<Site> it = sites.iterator();
		Site site1 = it.next();
		Site site2 = it.next();
		return MathUtils.getCoordinatesBetweenTwoCoordinates(site1.getCoordinates(), site2.getCoordinates());
	}
	
	public static Coordinates getASiteCoordinates(Collection<Site> sites) {
		Site site = sites.iterator().next();
		return site.getCoordinates();
	}
	
}
