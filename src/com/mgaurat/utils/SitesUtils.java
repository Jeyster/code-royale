package com.mgaurat.utils;

import java.util.ArrayList;
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
    
    /**
     * Get the nearest Site that is towards the enemy camp.
     * 
     * @param sites
     * @param coordinates
     * @param startingAllyQueenCoordinates
     * @return
     */
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
	
	/**
	 * Get the Sites that are on the path to go from allyQueenCoordinates to targetCoordinates.
	 * For each sites, check if the site is between allyQueenCoordinates to targetCoordinates
	 * and if the line between allyQueenCoordinates and targetCoordinates cross the site area.
	 * 
	 * @param allyQueenCoordinates
	 * @param targetCoordinates
	 * @param sites
	 * @return Collection<Site>
	 */
	public static Collection<Site> getSitesOnPath(Coordinates allyQueenCoordinates, Coordinates targetCoordinates, Collection<Site> sites) {
		Collection<Site> sitesOnPath = new ArrayList<>();
		for (Site site : sites) {
			if (GameBoardUtils.isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(site.getCoordinates(), targetCoordinates, allyQueenCoordinates) 
					&& MathUtils.isLineCrossingCircle(allyQueenCoordinates, targetCoordinates, site.getCoordinates(), site.getRadius())
					&& !site.isItsCoordinates(targetCoordinates)) {
				sitesOnPath.add(site);
			}
		}
		
		return sitesOnPath;
	}
	
	/**
	 * Get the closest Coordinates that in on the path to go from allyQueenCoordinates to targetCoordinates.
	 * 
	 * @param allyQueenCoordinates
	 * @param targetCoordinates
	 * @param sites
	 * @return Site
	 */
	public static Site getClosestSiteOnPath(Coordinates allyQueenCoordinates, Coordinates targetCoordinates, Collection<Site> sites) {
		Collection<Site> sitesOnPath = getSitesOnPath(allyQueenCoordinates, targetCoordinates, sites);
//		for (Site site : sitesOnPath) {
//			System.err.println("Site on path ID : " + site.getId());
//		}
		return getNearestSiteFromCoordinates(sitesOnPath, allyQueenCoordinates);
	}
	
	public static boolean isSiteIdInCollection(Collection<Site> sites, int siteId) {
		for (Site site : sites) {
			if (site.getId() == siteId) {
				return true;
			}
		}
		
		return false;
	}
    
}
