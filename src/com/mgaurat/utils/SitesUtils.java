package com.mgaurat.utils;

import java.util.Collection;
import java.util.Iterator;

import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;

/**
 * Final class for utilitaries methods that manipulates Site.
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
    
    /**
     * Get the Coordinates in the middle of the first two Site of the input list.
     * 
     * @param sites
     * @return Coordinates
     */
    public static Coordinates getCoordinatesBetweenTwoRandomSites(Collection<Site> sites) {
    	if (sites.size() < 2) {
    		return null;
    	}
    	
    	Iterator<Site> it = sites.iterator();
    	Site site1 = it.next();
    	Site site2 = it.next();
    	return MathUtils.getCoordinatesBetweenTwoCoordinates(site1.getCoordinates(), site2.getCoordinates());
    }

    /**
     * Get the Coordinates of the first Site of the input list.
     * 
     * @param sites
     * @return Coordinates
     */
    public static Coordinates getRandomSiteCoordinates(Collection<Site> sites) {
    	Site site = sites.iterator().next();
    	return site.getCoordinates();
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
    
}
