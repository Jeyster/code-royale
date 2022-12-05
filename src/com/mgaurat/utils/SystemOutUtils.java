package com.mgaurat.utils;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;

/**
 * Final class for utilitaries methods that print outputs.
 * 
 * @author mgaurat
 *
 */
public final class SystemOutUtils {
	
	private SystemOutUtils() {}
	
	/**
	 * Print a BUILD action thanks to a Site ID, a Structure type 
	 * and eventually a Unit type (for BARRACKS Structure).
	 * 
	 * @param siteId
	 * @param structureType
	 * @param unitType
	 */
	public static void printBuildAction(int siteId, StructureEnum structureType, UnitEnum unitType) {
		StringBuilder sb = new StringBuilder();
		sb.append("BUILD");
		sb.append(" ");
		sb.append(siteId);
		sb.append(" ");
		sb.append(structureType.toString());
		
		if (unitType != null) {
			sb.append("-");
			sb.append(unitType.toString());
		}
		
		System.out.println(sb.toString());
	}
	
	/**
	 * Print a MOVE action thanks to input Coordinates.
	 * 
	 * @param coordinates
	 */
	public static void printMoveAction(Coordinates coordinates) {
		StringBuilder sb = new StringBuilder();
		sb.append("MOVE");
		sb.append(" ");
		sb.append(coordinates.getX());
		sb.append(" ");
		sb.append(coordinates.getY());
		
		System.out.println(sb.toString());
	}
	
	/**
	 * Print a TRAIN action thanks to input Site ID.
	 * If we do not want to TRAIN a Site, the input must be equal to -1;
	 * 
	 * @param siteId
	 */
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
