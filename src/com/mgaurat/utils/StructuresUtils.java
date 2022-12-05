package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.Collection;
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
    			&& isCoordinatesInRangeOfAnyTower(coordinates, enemyTowerSites);
    }
    
	public static boolean isItSafeAtCoordinatesRegardingEnemyKnights(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType) {
		final double SAFE_DISTANCE = 500;
		return UnitsUtils.getNearestEnemyKnightDistance(coordinates, enemyUnitsByType) > SAFE_DISTANCE;
	}
	
	public static Collection<Site> getTowerSitesInRangeOfCoordinates(Collection<Site> towerSites, Coordinates coordinates) {
		Collection<Site> towerSitesInRange = new ArrayList<>();
		for (Site towerSite : towerSites) {
			if (MathUtils.getDistanceBetweenTwoCoordinates(coordinates, towerSite.getCoordinates()) < towerSite.getStructure().getParam2()) {
				towerSitesInRange.add(towerSite);
			}
		}
		
		return towerSitesInRange;
	}

	public static boolean isCoordinatesInRangeOfAnyTower(Coordinates coordinates, Collection<Site> towerSites) {
		return getTowerSitesInRangeOfCoordinates(towerSites, coordinates).size() == 0;
	}
	
	public static boolean isCoordinatesInRangeOfTwoTowers(Coordinates coordinates, Collection<Site> towerSites) {
		return getTowerSitesInRangeOfCoordinates(towerSites, coordinates).size() >= 2;
	}
	
	public static Coordinates getAverageSiteCoordinates(Collection<Site> sites) {
		if (sites.isEmpty()) {
			return null;
		}

		int xCoordinateSum = 0;
		int yCoordinateSum = 0;
		Coordinates siteCoordinates;
		for (Site site : sites) {
			siteCoordinates = site.getCoordinates();
			xCoordinateSum += siteCoordinates.getX();
			yCoordinateSum += siteCoordinates.getY();
		}

		return new Coordinates(xCoordinateSum/sites.size(), yCoordinateSum/sites.size());
	}
	
}
