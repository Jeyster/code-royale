package com.mgaurat.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;

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
        return sites
        		.stream()
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(coordinates, site.getCoordinates()))))
        		.orElse(null);
    }
    
    public static Site getNearestSiteToBuild(Collection<Site> sites, Unit allyQueen, Coordinates startingAllyQueenCoordinates,
    		Collection<Site> enemyTowers, Unit nearestEnemyKnight) {
        return sites
        		.stream()
        		.filter(site -> UnitsUtils.canAllyQueenReachSiteSafely(allyQueen, startingAllyQueenCoordinates, site, nearestEnemyKnight, enemyTowers, true))
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(allyQueen.getCoordinates(), site.getCoordinates()))))
        		.orElse(null);
    }
        
    /**
     * Get the nearest Site that is towards the enemy camp and limited in Y.
     * 
     * @param sites
     * @param coordinates
     * @param startingAllyQueenCoordinates
     * @return Site
     */
    public static Site getNearestSiteFromCoordinatesInBandForwardDirection(Collection<Site> sites, Coordinates coordinates, Coordinates startingAllyQueenCoordinates) {
        return sites
        		.stream()
        		.filter(site -> site.isInBandForwardDirection(startingAllyQueenCoordinates))
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(coordinates, site.getCoordinates()))))
        		.orElse(null);
    }
    
    /**
     * Get the nearest Site that is towards the enemy camp.
     * 
     * @param sites
     * @param coordinates
     * @param startingAllyQueenCoordinates
     * @return Site
     */
    public static Site getNearestSiteToBuildInForwardDirection(Collection<Site> sites, Unit allyQueen, Coordinates startingAllyQueenCoordinates,
    		Unit nearestEnemyKnight, Collection<Site> enemyTowers) {
        return sites
        		.stream()
        		.filter(site -> site.isInForwardDirection(allyQueen.getCoordinates(), startingAllyQueenCoordinates)
        				&& UnitsUtils.canAllyQueenReachSiteSafely(allyQueen, startingAllyQueenCoordinates, site, nearestEnemyKnight, enemyTowers, true))
        		.collect(Collectors.minBy(Comparator.comparingDouble(site -> MathUtils.getDistanceBetweenTwoCoordinates(allyQueen.getCoordinates(), site.getCoordinates()))))
        		.orElse(null);
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
		return sites
				.stream()
				.filter(site -> GameBoardUtils.isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(site.getCoordinates(), targetCoordinates, allyQueenCoordinates) 
						&& MathUtils.isLineCrossingCircle(allyQueenCoordinates, targetCoordinates, site.getCoordinates(), site.getRadius()) 
						&& !site.isItsCoordinates(targetCoordinates))
				.collect(Collectors.toList());
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
		return getNearestSiteFromCoordinates(sitesOnPath, allyQueenCoordinates);
	}
	
	public static boolean isSiteIdInCollection(Collection<Site> sites, int siteId) {
		return sites
				.stream()
				.anyMatch(site -> site.getId() == siteId);
	}
	
}
