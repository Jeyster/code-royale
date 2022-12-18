package com.mgaurat.utils;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

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

	public static Coordinates getMiddleCoordinatesOfTwoCoordinates(Coordinates a, Coordinates b) {
		int xa = a.getX();
		int ya = a.getY();
		int xb = b.getX();
		int yb = b.getY();
		
		return new Coordinates(Math.abs(xb - xa), Math.abs(yb - ya));
	}
	
	/**
	 * Calculate the distance between the input point and the closest point of the line drawn by firstLinePoint and secondLinePoint.
	 * 
	 * @param firstLinePoint
	 * @param secondLinePoint
	 * @param point
	 * @return double
	 */
	public static double getDistanceBetweenLineAndPoint(Coordinates firstLinePoint, Coordinates secondLinePoint, Coordinates point) {
		int xFirstLinePoint = firstLinePoint.getX();
		int yFirstLinePoint = firstLinePoint.getY();
		int xSecondLinePoint = secondLinePoint.getX();
		int ySecondLinePoint = secondLinePoint.getY();
		int xPoint = point.getX();
		int yPoint = point.getY();
		double distanceBetweenTheTwoLinePoints = getDistanceBetweenTwoCoordinates(firstLinePoint, secondLinePoint);
		return Math.abs(((xSecondLinePoint - xFirstLinePoint)*(yFirstLinePoint - yPoint)) - ((xFirstLinePoint - xPoint)*(ySecondLinePoint - yFirstLinePoint))) / distanceBetweenTheTwoLinePoints;
	}
	
	/**
	 * Check if the line between firstLinePoint and secondLinePoint crosses the input circle.
	 * 
	 * @param firstLinePoint
	 * @param secondLinePoint
	 * @param circleCenter
	 * @param circleRadius
	 * @return boolean
	 */
	public static boolean isLineCrossingCircle(Coordinates firstLinePoint, Coordinates secondLinePoint, Coordinates circleCenter, int circleRadius) {
		double distanceBetweenLineAndCircleCenter = getDistanceBetweenLineAndPoint(firstLinePoint, secondLinePoint, circleCenter);
		if (distanceBetweenLineAndCircleCenter >= circleRadius) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Get the closest Coordinates from p of the line drawn by a and b.
	 * 
	 * @param a
	 * @param b
	 * @param p
	 * @return Coordinates
	 */
	public static Coordinates getClosestCoordinatesOfLineFromPoint(Coordinates a, Coordinates b, Coordinates p) {
		int xa = a.getX();
		int ya = a.getY();
		int xb = b.getX();
		int yb = b.getY();
		int xp = p.getX();
		int yp = p.getY();
		
	    int xpMinusXa = xp - xa;
	    int ypMinusYa = yp - ya;
	    int xbMinusXa = xb - xa;
	    int ybMinusYa = yb - ya;

	    int xCoordinate = xa + (xbMinusXa * ((xpMinusXa * xbMinusXa) + (ypMinusYa * ybMinusYa)) / ((xbMinusXa * xbMinusXa) + (ybMinusYa * ybMinusYa)));
		int yCoordinate = ya + (ybMinusYa * ((xpMinusXa * xbMinusXa) + (ypMinusYa * ybMinusYa)) / ((xbMinusXa * xbMinusXa) + (ybMinusYa * ybMinusYa)));
	    return new Coordinates(xCoordinate, yCoordinate);
	}
	
	/**
	 * Get the two Coordinates of the line drawn by firstLinePoint and secondLinePoint that crosses the input circle.
	 * It calls a method that I copy and that uses Point2D. I thus convert Point2D into Coordinates.
	 * 
	 * @param firstLinePoint
	 * @param secondLinePoint
	 * @param circleCenter
	 * @param circleRadius
	 * @return List<Coordinates>
	 */
	public static List<Coordinates> getIntersectionsOfLineWithCircle(Coordinates firstLinePoint, Coordinates secondLinePoint, Coordinates circleCenter, int circleRadius) {
		List<Coordinates> intersections = new ArrayList<>();
		Point2D firstLinePoint2D = new Point2D.Double(firstLinePoint.getX(), firstLinePoint.getY());
		Point2D secondLinePoint2D = new Point2D.Double(secondLinePoint.getX(), secondLinePoint.getY());
		Point2D circleCenter2D = new Point2D.Double(circleCenter.getX(), circleCenter.getY());
		try {
			List<Point2D> intersections2D = intersection(firstLinePoint2D, secondLinePoint2D, circleCenter2D, circleRadius, false);
			for (Point2D intersection2D : intersections2D) {
	        	//System.err.println("Intersection coordinates : (" + intersection2D.getX() + ", " + intersection2D.getY() + ")");
				intersections.add(new Coordinates((int) Math.round(intersection2D.getX()), (int) Math.round(intersection2D.getY())));
			}
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		
		return intersections;
	}

	/**
     * If center of the circle is at the origin and the line is horizontal,
     * it's easy to calculate the points of intersection, so to handle the
     * general case, we convert the input to a coordinate system where the
     * center of the circle is at the origin and the line is horizontal,
     * then convert the points of intersection back to the original
     * coordinate system.
     *
	 * @param p1
	 * @param p2
	 * @param center
	 * @param radius
	 * @param isSegment
	 * @return
	 * @throws NoninvertibleTransformException
	 */
    public static List<Point2D> intersection(Point2D p1, Point2D p2, Point2D center,
            double radius, boolean isSegment) throws NoninvertibleTransformException {
        List<Point2D> result = new ArrayList<>();
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        AffineTransform trans = AffineTransform.getRotateInstance(dx, dy);
        trans.invert();
        trans.translate(-center.getX(), -center.getY());
        Point2D p1a = trans.transform(p1, null);
        Point2D p2a = trans.transform(p2, null);
        double y = p1a.getY();
        double minX = Math.min(p1a.getX(), p2a.getX());
        double maxX = Math.max(p1a.getX(), p2a.getX());
        if (y == radius || y == -radius) {
            if (!isSegment || (0 <= maxX && 0 >= minX)) {
                p1a.setLocation(0, y);
                trans.inverseTransform(p1a, p1a);
                result.add(p1a);
            }
        } else if (y < radius && y > -radius) {
            double x = Math.sqrt(radius * radius - y * y);
            if (!isSegment || (-x <= maxX && -x >= minX)) {
                p1a.setLocation(-x, y);
                trans.inverseTransform(p1a, p1a);
                result.add(p1a);
            }
            if (!isSegment || (x <= maxX && x >= minX)) {
                p2a.setLocation(x, y);
                trans.inverseTransform(p2a, p2a);
                result.add(p2a);
            }
        }
        return result;
    }
}
