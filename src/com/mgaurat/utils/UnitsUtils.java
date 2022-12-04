package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.OwnerEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Unit;

public final class UnitsUtils {
	
	private UnitsUtils() {
	}
	
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
	
	public static Unit getMyQueen(Map<UnitEnum, List<Unit>> allyUnitsByType) {
		List<Unit> queens = allyUnitsByType.get(UnitEnum.QUEEN);
		for (Unit queen : queens) {
			if (queen.getOwner() == OwnerEnum.ALLY.getId()) {
				return queen;
			}
		}
		
		return null;
	}
	
	public static double getNearestEnemyKnightDistance(Coordinates myQueenCoordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType) {
        double distanceToKnight;
        double distanceToNearestKnight = Double.MAX_VALUE;
		List<Unit> enemyKnights = enemyUnitsByType.get(UnitEnum.KNIGHT);
		Coordinates enemyKnightCoordinates;
		for (Unit enemyKnight : enemyKnights) {
			enemyKnightCoordinates = enemyKnight.getCoordinates();
			distanceToKnight = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, enemyKnightCoordinates);
			if (distanceToKnight < distanceToNearestKnight) {
				distanceToNearestKnight = distanceToKnight;
			}
		}
		
		return distanceToNearestKnight;
	}
}
