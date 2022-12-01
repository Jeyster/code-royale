package com.mgaurat.utils;

import com.mgaurat.model.Coordinates;

public final class MathUtils {
	
	private MathUtils() {
	}
	
	public static double getDistanceBetweenTwoCoordinates(Coordinates a, Coordinates b) {
		int xa = a.getX();
		int ya = a.getY();
		int xb = b.getX();
		int yb = b.getY();
		
		return Math.sqrt(Math.pow(xa - xb, 2) + Math.pow(ya - yb, 2));
	}

}
