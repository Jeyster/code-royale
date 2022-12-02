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
	
	public static Map<UnitEnum, List<Unit>> getUnitsByTypeFromTurnInput(Scanner in, int numUnits) {
		Map<UnitEnum, List<Unit>> unitsByType = buildUnitsByType();
		List<Unit> queens = unitsByType.get(UnitEnum.QUEEN);
		List<Unit> knights = unitsByType.get(UnitEnum.KNIGHT);
		List<Unit> archers = unitsByType.get(UnitEnum.ARCHER);
		List<Unit> giants = unitsByType.get(UnitEnum.GIANT);
		
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
            
            if (unitType == UnitEnum.QUEEN.getId()) {
				queens.add(unit);
			} else if (unitType == UnitEnum.KNIGHT.getId()) {
				knights.add(unit);
			} else if (unitType == UnitEnum.ARCHER.getId()) {
				archers.add(unit);
			} else if (unitType == UnitEnum.GIANT.getId()) {
				giants.add(unit);
			}
        }
        
        return unitsByType;
	}
	
	private static Map<UnitEnum, List<Unit>> buildUnitsByType() {
		Map<UnitEnum, List<Unit>> unitsByType = new HashMap<>();
		List<Unit> queens = new ArrayList<>();
		List<Unit> knights = new ArrayList<>();
		List<Unit> archers = new ArrayList<>();
		List<Unit> giants = new ArrayList<>();
		
		unitsByType.put(UnitEnum.QUEEN, queens);
		unitsByType.put(UnitEnum.KNIGHT, knights);
		unitsByType.put(UnitEnum.ARCHER, archers);
		unitsByType.put(UnitEnum.GIANT, giants);
		
		return unitsByType;
	}
	
	public static Unit getMyQueen(Map<UnitEnum, List<Unit>> unitsByType) {
		List<Unit> queens = unitsByType.get(UnitEnum.QUEEN);
		for (Unit queen : queens) {
			if (queen.getOwner() == OwnerEnum.ALLY.getId()) {
				return queen;
			}
		}
		
		return null;
	}

}
