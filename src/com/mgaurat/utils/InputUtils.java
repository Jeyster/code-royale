package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.OwnerEnum;
import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;
import com.mgaurat.model.Unit;

/**
 * Final class for utilitaries methods that initialize the game and each turn from input.
 * 
 * @author mgaurat
 *
 */
public class InputUtils {
	
	private InputUtils() {}
	
	/**
	 * Initialize the Sites at the start of the game thanks to input.
	 * 
	 * @param in
	 * @param numSites
	 * @return Map<Integer, Site>
	 */
	public static Map<Integer, Site> getSitesFromInitialInput(Scanner in, int numSites) {		
		Map<Integer, Site> sitesById = new HashMap<>();
        Coordinates coordinates;
        Site site;
        
        for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int radius = in.nextInt();
            coordinates = new Coordinates(x, y);
            site = new Site(siteId, coordinates, radius);
            sitesById.put(siteId, site);
        }
        
        return sitesById;
	}
	
	/**
	 * Update the Sites of the game start thanks to turn input. Launched at the beginning of each turn.
	 * It classify the Sites by Site ID, Structure types and Owner.
	 * 
	 * @param in
	 * @param sitesById
	 * @return Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>>
	 */
	public static Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> updateSitesFromTurnInput(Scanner in, Map<Integer, Site> sitesById) {
		Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = new HashMap<>();
		
		Map<StructureEnum, Map<Integer, Site>> emptySitesByIdAndStructure = new HashMap<>();
		Map<Integer, Site> emptySitesById = new HashMap<>();
		emptySitesByIdAndStructure.put(StructureEnum.NOTHING, emptySitesById);
		sitesByIdAndStructureAndOwner.put(OwnerEnum.NOBODY, emptySitesByIdAndStructure);
		
		Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = new HashMap<>();
		Map<Integer, Site> allyMineSitesById = new HashMap<>();
		Map<Integer, Site> allyTowerSitesById = new HashMap<>();
		Map<Integer, Site> allyBarracksSitesById = new HashMap<>();
		allySitesByIdAndStructure.put(StructureEnum.MINE, allyMineSitesById);
		allySitesByIdAndStructure.put(StructureEnum.TOWER, allyTowerSitesById);
		allySitesByIdAndStructure.put(StructureEnum.BARRACKS, allyBarracksSitesById);
		sitesByIdAndStructureAndOwner.put(OwnerEnum.ALLY, allySitesByIdAndStructure);
		
		Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = new HashMap<>();
		Map<Integer, Site> enemyMineSitesById = new HashMap<>();
		Map<Integer, Site> enemyTowerSitesById = new HashMap<>();
		Map<Integer, Site> enemyBarracksSitesById = new HashMap<>();
		enemySitesByIdAndStructure.put(StructureEnum.MINE, enemyMineSitesById);
		enemySitesByIdAndStructure.put(StructureEnum.TOWER, enemyTowerSitesById);
		enemySitesByIdAndStructure.put(StructureEnum.BARRACKS, enemyBarracksSitesById);
		sitesByIdAndStructureAndOwner.put(OwnerEnum.ENEMY, enemySitesByIdAndStructure);
		
		Structure structure;
		Site site;
		for (int i = 0; i < sitesById.size(); i++) {
            int siteId = in.nextInt();
            int ignore1 = in.nextInt();
            int ignore2 = in.nextInt();
            int structureType = in.nextInt();
            int owner = in.nextInt();
            int param1 = in.nextInt();
            int param2 = in.nextInt();

            structure = new Structure(ignore1, ignore2, structureType, owner, param1, param2);
            site = sitesById.get(siteId);
            site.setStructure(structure);
            
            if (structure.getOwner() == OwnerEnum.NOBODY.getId()) {
            	emptySitesById.put(siteId, site);
            } else if (structure.getOwner() == OwnerEnum.ALLY.getId()) {
            	if (structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
            		allyMineSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
            		allyTowerSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()) {
            		allyBarracksSitesById.put(siteId, site);
            	}
            } else if (structure.getOwner() == OwnerEnum.ENEMY.getId()) {
            	if (structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
            		enemyMineSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
            		enemyTowerSitesById.put(siteId, site);
            	} else if (structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()) {
            		enemyBarracksSitesById.put(siteId, site);
            	}
            }
        }
		
		return sitesByIdAndStructureAndOwner;
	}
	
	/**
	 * Get all the Units thanks to turn input. Launched at the beginning of each turn.
	 * It classify the Units by Unit type and Owner.
	 * 
	 * @param in
	 * @param numUnits
	 * @return Map<OwnerEnum, Map<UnitEnum, List<Unit>>>
	 */
	public static Map<OwnerEnum, Map<UnitEnum, List<Unit>>> getUnitsByTypeAndOwnerFromTurnInput(Scanner in, int numUnits) {
		Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = buildUnitsByTypeAndOwner();
		
		Map<UnitEnum, List<Unit>> allyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ALLY);
		List<Unit> allyQueens = allyUnitsByType.get(UnitEnum.QUEEN);
		List<Unit> allyKnights = allyUnitsByType.get(UnitEnum.KNIGHT);
		List<Unit> allyArchers = allyUnitsByType.get(UnitEnum.ARCHER);
		List<Unit> allyGiants = allyUnitsByType.get(UnitEnum.GIANT);
		
		Map<UnitEnum, List<Unit>> enemyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ENEMY);
		List<Unit> enemyQueens = enemyUnitsByType.get(UnitEnum.QUEEN);
		List<Unit> enemyKnights = enemyUnitsByType.get(UnitEnum.KNIGHT);
		List<Unit> enemyArchers = enemyUnitsByType.get(UnitEnum.ARCHER);
		List<Unit> enemyGiants = enemyUnitsByType.get(UnitEnum.GIANT);
		
		Coordinates unitCoordinates;
		Unit unit;
        for (int i = 0; i < numUnits; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            unitCoordinates = new Coordinates(x, y);
            int owner = in.nextInt();
            int unitType = in.nextInt();
            int health = in.nextInt();

            unit = new Unit(unitCoordinates, owner, unitType, health);
            
            if (unit.getOwner() == OwnerEnum.ALLY.getId()) {
            	if (unitType == UnitEnum.QUEEN.getId()) {
            		allyQueens.add(unit);
            	} else if (unitType == UnitEnum.KNIGHT.getId()) {
            		allyKnights.add(unit);
            	} else if (unitType == UnitEnum.ARCHER.getId()) {
            		allyArchers.add(unit);
            	} else if (unitType == UnitEnum.GIANT.getId()) {
            		allyGiants.add(unit);
            	}            	
            } else if (unit.getOwner() == OwnerEnum.ENEMY.getId()) {
            	if (unitType == UnitEnum.QUEEN.getId()) {
            		enemyQueens.add(unit);
            	} else if (unitType == UnitEnum.KNIGHT.getId()) {
            		enemyKnights.add(unit);
            	} else if (unitType == UnitEnum.ARCHER.getId()) {
            		enemyArchers.add(unit);
            	} else if (unitType == UnitEnum.GIANT.getId()) {
            		enemyGiants.add(unit);
            	}  
            }
        }
        
        return unitsByTypeAndOwner;
	}
	
	/**
	 * Initialize the map used to get the Units from turns input.
	 * 
	 * @return
	 */
	private static Map<OwnerEnum, Map<UnitEnum, List<Unit>>> buildUnitsByTypeAndOwner() {
		Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = new HashMap<>();
		
		Map<UnitEnum, List<Unit>> allyUnitsByType = new HashMap<>();
		List<Unit> allyQueens = new ArrayList<>();
		List<Unit> allyKnights = new ArrayList<>();
		List<Unit> allyArchers = new ArrayList<>();
		List<Unit> allyGiants = new ArrayList<>();
		allyUnitsByType.put(UnitEnum.QUEEN, allyQueens);
		allyUnitsByType.put(UnitEnum.KNIGHT, allyKnights);
		allyUnitsByType.put(UnitEnum.ARCHER, allyArchers);
		allyUnitsByType.put(UnitEnum.GIANT, allyGiants);
		
		Map<UnitEnum, List<Unit>> enemyUnitsByType = new HashMap<>();
		List<Unit> enemyQueens = new ArrayList<>();
		List<Unit> enemyKnights = new ArrayList<>();
		List<Unit> enemyArchers = new ArrayList<>();
		List<Unit> enemyGiants = new ArrayList<>();
		enemyUnitsByType.put(UnitEnum.QUEEN, enemyQueens);
		enemyUnitsByType.put(UnitEnum.KNIGHT, enemyKnights);
		enemyUnitsByType.put(UnitEnum.ARCHER, enemyArchers);
		enemyUnitsByType.put(UnitEnum.GIANT, enemyGiants);
		
		unitsByTypeAndOwner.put(OwnerEnum.ALLY, allyUnitsByType);
		unitsByTypeAndOwner.put(OwnerEnum.ENEMY, enemyUnitsByType);
		
		return unitsByTypeAndOwner;
	}

}
