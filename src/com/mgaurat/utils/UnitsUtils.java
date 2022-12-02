package com.mgaurat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.Owner;
import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Unit;

public final class UnitsUtils {
	
	private UnitsUtils() {
	}
	
	public static Map<UnitType, List<Unit>> getUnitsByTypeFromTurnInput(Scanner in, int numUnits) {
		Map<UnitType, List<Unit>> unitsByType = buildUnitsByType();
		List<Unit> queens = unitsByType.get(UnitType.QUEEN);
		List<Unit> knights = unitsByType.get(UnitType.KNIGHT);
		List<Unit> archers = unitsByType.get(UnitType.ARCHER);
		List<Unit> giants = unitsByType.get(UnitType.GIANT);
		
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
            
            if (unitType == UnitType.QUEEN.getUnitTypeId()) {
				queens.add(unit);
			} else if (unitType == UnitType.KNIGHT.getUnitTypeId()) {
				knights.add(unit);
			} else if (unitType == UnitType.ARCHER.getUnitTypeId()) {
				archers.add(unit);
			} else if (unitType == UnitType.GIANT.getUnitTypeId()) {
				giants.add(unit);
			}
        }
        
        return unitsByType;
	}
	
	private static Map<UnitType, List<Unit>> buildUnitsByType() {
		Map<UnitType, List<Unit>> unitsByType = new HashMap<>();
		List<Unit> queens = new ArrayList<>();
		List<Unit> knights = new ArrayList<>();
		List<Unit> archers = new ArrayList<>();
		List<Unit> giants = new ArrayList<>();
		
		unitsByType.put(UnitType.QUEEN, queens);
		unitsByType.put(UnitType.KNIGHT, knights);
		unitsByType.put(UnitType.ARCHER, archers);
		unitsByType.put(UnitType.GIANT, giants);
		
		return unitsByType;
	}
	
	public static Unit getMyQueen(Map<UnitType, List<Unit>> unitsByType) {
		List<Unit> queens = unitsByType.get(UnitType.QUEEN);
		for (Unit queen : queens) {
			if (queen.getOwner() == Owner.ALLY.getOwnerId()) {
				return queen;
			}
		}
		
		return null;
	}

}
