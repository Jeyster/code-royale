package com.mgaurat.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;
import com.mgaurat.model.Unit;

public final class StructuresUtils {
	
	private StructuresUtils() {
	}
    
    public static int getCurrentGoldProduction(Collection<Site> sites) {
    	int currentGoldProduction = 0;
    	for (Site site : sites) {
			currentGoldProduction += site.getStructure().getParam1();
    	}
    	
    	return currentGoldProduction;
    }
    
    public static boolean isMineNotInFullProduction(Structure structure) {
    	if (structure.getStructureTypeId() != StructureEnum.MINE.getId()) {
    		return false;
    	}
    	
    	return structure.getParam1() < structure.getMaxMineProduction();
    }
    
    public static boolean isTowerNotFullLife(Structure structure) {
    	if (structure.getStructureTypeId() != StructureEnum.TOWER.getId()) {
    		return false;
    	}
    	
    	final int MAX_TOWER_LIFE = 750;
    	return structure.getParam1() < MAX_TOWER_LIFE;
    }
    
    public static boolean isAtLeastOneAllyKnightBarracks(Collection<Site> allyBarracksSites) {
    	for (Site site : allyBarracksSites) {
    		if (site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public static boolean isAtLeastOneAllyGiantBarracks(Collection<Site> allyBarracksSites) {
    	for (Site site : allyBarracksSites) {
    		if (site.getStructure().getParam2() == UnitEnum.GIANT.getId()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public static boolean isItSafeAtCoordinates(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites) {
    	return isItSafeAtCoordinatesRegardingEnemyKnights(coordinates, enemyUnitsByType)
    			&& isItSafeAtCoordinatesRegardingEnemyTowers(coordinates, enemyTowerSites);
    }
    
	public static boolean isItSafeAtCoordinatesRegardingEnemyKnights(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType) {
		final double SAFE_DISTANCE = 500;
		return UnitsUtils.getNearestEnemyKnightDistance(coordinates, enemyUnitsByType) > SAFE_DISTANCE;
	}
	
	public static boolean isItSafeAtCoordinatesRegardingEnemyTowers(Coordinates coordinates, Collection<Site> enemyTowerSites) {
		for (Site site : enemyTowerSites) {
			if (MathUtils.getDistanceBetweenTwoCoordinates(coordinates, site.getCoordinates()) < site.getStructure().getParam2()) {
				return false;
			}
		}
		return true;
	}
	
	public static Site getTheSafestTower(Collection<Site> allyTowerSites, Site myKnightBarracksSite) {
		boolean isLeftSide = GameBoardUtils.isLeftSide(myKnightBarracksSite.getCoordinates());
		
		Iterator<Site> it = allyTowerSites.iterator();
		Site safestTower = it.next();
		int safestTowerXCoordinate= safestTower.getCoordinates().getX();
		Site tower;
		int towerXCoodinate;
		while (it.hasNext()) {
			tower = it.next();
			towerXCoodinate = tower.getCoordinates().getX();
			if ((isLeftSide && towerXCoodinate < safestTowerXCoordinate)
					|| (!isLeftSide && towerXCoodinate > safestTowerXCoordinate)) {
				safestTowerXCoordinate = towerXCoodinate;
				safestTower = tower;
			}
		}
		
		return safestTower;
	}
	
}
