package com.mgaurat.utils;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;

public final class SystemOutUtils {
	
	private SystemOutUtils() {
	}
	
	public static void printBuildAction(int targetedSiteId, StructureEnum structureType, UnitEnum unitType) {
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
		
		if (siteId > -1) {
			sb.append(" ");
			sb.append(siteId);			
		}
		
		System.out.println(sb.toString());
	}

}
