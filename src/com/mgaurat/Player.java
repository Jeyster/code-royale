package com.mgaurat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;
import com.mgaurat.utils.SitesUtils;
import com.mgaurat.utils.StructuresUtils;
import com.mgaurat.utils.SystemOutUtils;
import com.mgaurat.utils.UnitsUtils;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numSites = in.nextInt();

        Map<Integer, Site> sitesById = SitesUtils.getSitesFromInitialInput(in, numSites);

        // game loop
        while (true) {
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none

            SitesUtils.updateSitesFromTurnInput(in, numSites, sitesById);
            Collection<Site> sites = SitesUtils.getSitesCollection(sitesById);

            int numUnits = in.nextInt();
            Map<UnitEnum, List<Unit>> unitsByType = UnitsUtils.getUnitsByTypeFromTurnInput(in, numUnits);
            Unit myQueen = UnitsUtils.getMyQueen(unitsByType);
            Coordinates myQueenCoordinates = myQueen.getCoordinates();
            
            /* 1) First turn action is to MOVE or BUILD.
            *		a) If i have no KNIGHT BARRACKS :
            *			- get the nearest free site and MOVE to it
            *			- if touching this free site, build a KNIGHT BARRACKS
            *		b) else if my gold production is less than MIN_GOLD_PRODUCTION :
            *			- if I touch a MINE of mine that is not in full production, increase the production
            *			- else get the nearest site where a MINE can be built (gold not depleted) and MOVE on it or BUILD a MINE
            *		c) else if my TOWER number is less than MIN_TOWER_NUMBER :
            *			- if I touch a TOWER of mine that is not full life, increase its life
            *			- else get the nearest free site and MOVE on it or BUILD a TOWER
            *		d) else if my gold production is less than MAX_GOLD_PRODUCTION :
            *			- if I touch a MINE of mine that is not in full production, increase the production
            *			- else get the nearest site where a MINE can be built (gold not depleted) and MOVE on it or BUILD a MINE
            *		e) else if my TOWER number is less than MAX_TOWER_NUMBER :
            *			- if I touch a TOWER of mine that is not full life, increase its life
            *			- else get the nearest free site and MOVE on it or BUILD a TOWER
            */
            final int MIN_GOLD_PRODUCTION = 5;
            final int MAX_GOLD_PRODUCTION = 8;
            final int MIN_TOWER_NUMBER = 3;
            final int MAX_TOWER_NUMBER = 6;
            Site targetedSite;
            int targetedSiteId;
            if (!StructuresUtils.isAtLeastOneKnightBarrackOwnedByMe(sites)) {
            	targetedSite = SitesUtils.getNearestFreeSite(sites, myQueenCoordinates);
            	targetedSiteId = targetedSite.getId();
            	if (touchedSite != targetedSiteId) {
                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
            	}
        	} else if (StructuresUtils.getCurrentGoldProduction(sites) < MIN_GOLD_PRODUCTION) {
        		if (touchedSite != -1 
        			&& StructuresUtils.isMineOwnedByMeNotInFullProduction(sitesById.get(touchedSite).getStructure())) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSiteNotOwnedToBuildAMine(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        			}        			
        		}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sites) < MIN_TOWER_NUMBER) {
        		if (touchedSite != -1 
            			&& StructuresUtils.isTowerOwnedByMeNotFullLife(sitesById.get(touchedSite).getStructure())) {
                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        		} else {
        			targetedSite = SitesUtils.getNearestFreeSite(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        			}        			
        		}
        	} else if (StructuresUtils.getCurrentGoldProduction(sites) < MAX_GOLD_PRODUCTION) {
        		if (touchedSite != -1 
        			&& StructuresUtils.isMineOwnedByMeNotInFullProduction(sitesById.get(touchedSite).getStructure())) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSiteNotOwnedToBuildAMine(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        			}        			
        		}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sites) < MAX_TOWER_NUMBER) {
        		if (touchedSite != -1 
            			&& StructuresUtils.isTowerOwnedByMeNotFullLife(sitesById.get(touchedSite).getStructure())) {
                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        		} else {
        			targetedSite = SitesUtils.getNearestFreeSite(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        			}        			
        		}
        	} else {
            	Site knightBarrack = StructuresUtils.getAKnightBarrackOwnedByMe(sites);
            	SystemOutUtils.printMoveAction(knightBarrack.getCoordinates());
        	}

            /* 2) Second turn action is to TRAIN :
            *		- if there is a site available for training, do it
            *		- else train nothing
            */
            Site siteToTrain = SitesUtils.getASiteToTrain(sites);
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(siteToTrain.getId());
            } else {
                SystemOutUtils.printTrainAction(0);
            }
        }
    }

}
