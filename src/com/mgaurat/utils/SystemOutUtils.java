package com.mgaurat.utils;

import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Coordinates;

public final class SystemOutUtils {
	
	private SystemOutUtils() {
	}
	
	public static void printBuildAction(int targetedSiteId, StructureType structureType, UnitType unitType) {
		StringBuilder sb = new StringBuilder();
		sb.append("BUILD");
		sb.append(" ");
		sb.append(targetedSiteId);
		sb.append(" ");
		sb.append(structureType.toString());
		
		if (unitType != null) {
			sb.append("-");
			sb.append(unitType.toString());
		}
		
		System.out.println(sb.toString());
	}
	
	public static void printMoveAction(Coordinates coordinates) {
		StringBuilder sb = new StringBuilder();
		sb.append("MOVE");
		sb.append(" ");
		sb.append(coordinates.getX());
		sb.append(" ");
		sb.append(coordinates.getY());
		
		System.out.println(sb.toString());
	}
	
	public static void printTrainAction(int siteId) {
		StringBuilder sb = new StringBuilder();
		sb.append("TRAIN");
		sb.append(" ");
		sb.append(siteId);
		
		System.out.println(sb.toString());
	}

}
