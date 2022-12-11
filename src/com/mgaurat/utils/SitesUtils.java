package com.mgaurat.utils;

import java.util.Collection;

import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;

/**
 * Final class for static methods that manipulates Site.
 * It does not refer to any particular Structure type.
 * 
 * @author mgaurat
 *
 */
public final class SitesUtils {
	
	private SitesUtils() {}
	
	/**
	 * Get the nearest Site of the input Site collection from the input Coordinates.
	 * 
	 * @param sites
	 * @param coordinates
	 * @return Site
	 */
    public static Site getNearestSiteFromCoordinates(Collection<Site> sites, Coordinates coordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {
            siteCoordinates = site.getCoordinates();
            distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(coordinates, siteCoordinates);
            if (distanceToSite < distanceToNearestSite) {
                distanceToNearestSite = distanceToSite;
                nearestSite = site;
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteFromCoordinatesInForwardDirection(Collection<Site> sites, Coordinates coordinates, Coordinates startingAllyQueenCoordinates) {
        boolean isStartingLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
        final int Y_GAP = 150;
    	Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {
        	if ((isStartingLeftSide && (site.getCoordinates().getX() > startingAllyQueenCoordinates.getX()) 
        			&& (site.getCoordinates().getY() < startingAllyQueenCoordinates.getY() + Y_GAP))
        			|| (!isStartingLeftSide && (site.getCoordinates().getX() < startingAllyQueenCoordinates.getX()))
        			&& (site.getCoordinates().getY() > startingAllyQueenCoordinates.getY() - Y_GAP)) {
        		siteCoordinates = site.getCoordinates();
        		distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(coordinates, siteCoordinates);
        		if (distanceToSite < distanceToNearestSite) {
        			distanceToNearestSite = distanceToSite;
        			nearestSite = site;
        		}        		
        	}
        }
        return nearestSite;
    }

	/**
	 * Get from the input Sites collection the average Coordinates.
	 * 
	 * @param sites
	 * @return Coordinates
	 */
	public static Coordinates getAverageSiteCoordinates(Collection<Site> sites) {
		if (sites.isEmpty()) {
			return null;
		}

		int xCoordinateSum = 0;
		int yCoordinateSum = 0;
		Coordinates siteCoordinates;
		for (Site site : sites) {
			siteCoordinates = site.getCoordinates();
			xCoordinateSum += siteCoordinates.getX();
			yCoordinateSum += siteCoordinates.getY();
		}

		return new Coordinates(xCoordinateSum/sites.size(), yCoordinateSum/sites.size());
	}
	
	public static boolean isReallyCloseToCoordinates(Coordinates allyQueenCoordinates, Coordinates coordinates) {
		final int REALLY_CLOSE = 150;
		return MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, coordinates) <= REALLY_CLOSE;
	}
    
}
