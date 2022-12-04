package com.mgaurat.utils;

import java.util.Collection;

import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;

public class GameBoardUtils {
	
	private final static int X_LENGTH = 1920;
	
	public static boolean isLeftSide(Coordinates coordinates) {
		return coordinates.getX() < X_LENGTH/2;
	}
	
	public static Coordinates getASafeCoordinates(Site myKnightBarracksSite, Collection<Site> allyTowerSites) {
		int yCoordinate = StructuresUtils.getTheSafestTower(allyTowerSites, myKnightBarracksSite).getCoordinates().getY();
		if (isLeftSide(myKnightBarracksSite.getCoordinates())) {
			return new Coordinates(0, yCoordinate);
		} else {
			return new Coordinates(X_LENGTH, yCoordinate);
		}
	}

}
