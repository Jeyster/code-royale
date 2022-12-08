package com.mgaurat.utils;

import com.mgaurat.model.Coordinates;

/**
 * Final class for static mathematical methods.
 * 
 * @author mgaurat
 *
 */
public final class MathUtils {
	
	private MathUtils() {}
	
	public static double getDistanceBetweenTwoCoordinates(Coordinates a, Coordinates b) {
		int xa = a.getX();
		int ya = a.getY();
		int xb = b.getX();
		int yb = b.getY();
		
		return Math.sqrt(Math.pow(xa - xb, 2) + Math.pow(ya - yb, 2));
	}

	public static Coordinates getCoordinatesBetweenTwoCoordinates(Coordinates a, Coordinates b) {
		int xa = a.getX();
		int ya = a.getY();
		int xb = b.getX();
		int yb = b.getY();
		
		return new Coordinates(Math.abs(xb - xa), Math.abs(yb - ya));
	}
}
