package com.mgaurat.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.Owner;
import com.mgaurat.enums.StructureType;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;

public final class SitesUtils {
	
	private SitesUtils() {
	}
	
	public static Map<Integer, Site> getSitesFromInitialInput(Scanner in, int numSites) {
		Map<Integer, Site> sitesById = new HashMap<>();
        Coordinates coordinates;
        Site site;
        
        for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int radius = in.nextInt();
            coordinates = new Coordinates(x, y);
            site = new Site(siteId, coordinates, radius);
            sitesById.put(siteId, site);
        }
        
        return sitesById;
	}
	
	public static void updateSitesFromTurnInput(Scanner in, int numSites, Map<Integer, Site> sitesById) {
		Structure structure;
		Site site;
		for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int ignore1 = in.nextInt();
            int ignore2 = in.nextInt();
            int structureType = in.nextInt();
            int owner = in.nextInt();
            int param1 = in.nextInt();
            int param2 = in.nextInt();

            structure = new Structure(ignore1, ignore2, structureType, owner, param1, param2);
            site = sitesById.get(siteId);
            site.setStructure(structure);
        }
	}
	
	public static Collection<Site> getSitesCollection(Map<Integer, Site> sitesById) {
		return sitesById.values();
	}
	
	/**
	 * Choose a Site to target :
	 * 	- if at least one Site owned by me :
	 * 		- if my QUEEN touched a MINE of mine that is not in full production and my total gold production is not reached, choose it
	 *  	- else choose the nearest not owned by me
	 *  - else choose the nearest
	 *  
	 * @param sites
	 * @param myQueenCoordinates
	 * @return
	 */
	public static Site getSiteToTarget(Map<Integer, Site> sitesById, Coordinates myQueenCoordinates, int touchedSite, int maxGoldProduction) {
		Collection<Site> sites = SitesUtils.getSitesCollection(sitesById);
		Site targetedSite;
        if (SitesUtils.isAtLeastOneSiteOwnedByMe(sites)) {
        	if (touchedSite != -1 
        			&& StructuresUtils.isMineOwnedByMeNotInFullProduction(sitesById.get(touchedSite).getStructure())
        			&& StructuresUtils.getCurrentGoldProduction(sites) < maxGoldProduction) {
        		targetedSite = sitesById.get(touchedSite);
        	} else {
        		targetedSite = SitesUtils.getNearestSiteNotOwnedByMe(sites, myQueenCoordinates);        		
        	}
        } else {
        	targetedSite = SitesUtils.getNearestSite(sites, myQueenCoordinates);            	
        }
        
        return targetedSite;
	}
	
    public static Site getNearestSite(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {
            siteCoordinates = site.getCoordinates();
            distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            if (distanceToSite < distanceToNearestSite) {
                distanceToNearestSite = distanceToSite;
                nearestSite = site;
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestFreeSite(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
            if (site.getStructure().getOwner() == Owner.NOBODY.getOwnerId()) {
            	siteCoordinates = site.getCoordinates();
            	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteNotOwnedToBuildAMine(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
            if (site.getStructure().getOwner() == Owner.NOBODY.getOwnerId()
            		&& site.getStructure().getMineGold() != 0) {
            	siteCoordinates = site.getCoordinates();
            	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteNotOwnedByMe(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
            if (site.getStructure().getOwner() != Owner.ALLY.getOwnerId()) {
            	siteCoordinates = site.getCoordinates();
            	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static boolean isAtLeastOneSiteOwnedByMe(Collection<Site> sites) {
        for (Site site : sites) {            
        	if (site.getStructure().isOwnedByMe()) {
        		return true;
        	}
        }
        return false;

    }
    
    public static Site getASiteToTrain(Collection<Site> sites) {
        for (Site site : sites) {            
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getParam1() == 0 
        			&& site.getStructure().getStructureTypeId() == StructureType.BARRACKS.getStructureTypeId()) {
        		return site;
        	}
        }
        return null;
    }

}
