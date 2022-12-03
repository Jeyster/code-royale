package com.mgaurat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.OwnerEnum;
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

            Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = SitesUtils.updateSitesFromTurnInput(in, sitesById);
            Map<Integer, Site> emptySitesById = sitesByIdAndStructureAndOwner.get(OwnerEnum.NOBODY).get(StructureEnum.NOTHING);
            Collection<Site> emptySites = SitesUtils.getSitesCollection(emptySitesById);
            
            Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ALLY);
    		Map<Integer, Site> allyMineSitesById = allySitesByIdAndStructure.get(StructureEnum.MINE);
            Collection<Site> allyMineSites = SitesUtils.getSitesCollection(allyMineSitesById);
    		Map<Integer, Site> allyTowerSitesById = allySitesByIdAndStructure.get(StructureEnum.TOWER);
    		Map<Integer, Site> allyBarracksSitesById = allySitesByIdAndStructure.get(StructureEnum.BARRACKS);
            Collection<Site> allyBarracksSites = SitesUtils.getSitesCollection(allyBarracksSitesById);

    		Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ENEMY);
    		Map<Integer, Site> enemyMineSitesById = enemySitesByIdAndStructure.get(StructureEnum.MINE);
    		Map<Integer, Site> enemyTowerSitesById = enemySitesByIdAndStructure.get(StructureEnum.TOWER);
    		Map<Integer, Site> enemyBarracksSitesById = enemySitesByIdAndStructure.get(StructureEnum.BARRACKS);

            int numUnits = in.nextInt();
            Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = UnitsUtils.getUnitsByTypeAndOwnerFromTurnInput(in, numUnits);
            Map<UnitEnum, List<Unit>> allyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ALLY);
            Map<UnitEnum, List<Unit>> enemyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ENEMY);
            Unit myQueen = UnitsUtils.getMyQueen(allyUnitsByType);
            Coordinates myQueenCoordinates = myQueen.getCoordinates();
            
            /* 1) First turn action is to MOVE or BUILD.
            *		a) If i have no KNIGHT BARRACKS :
            *			- get the nearest free site and MOVE to it
            *			- if touching this free site, build a KNIGHT BARRACKS
            *		b) else if my gold production is less than MIN_GOLD_PRODUCTION and it is safe to build a MINE :
            *			- if I touch a MINE of mine that is not in full production, increase the production
            *			- else get the nearest site where a MINE can be built (gold not depleted) and MOVE on it or BUILD a MINE
            *		c) else if my TOWER number is less than MIN_TOWER_NUMBER :
            *			- if I touch a TOWER of mine that is not full life, increase its life
            *			- else get the nearest free site and MOVE on it or BUILD a TOWER
            *		d) else if my gold production is less than MAX_GOLD_PRODUCTION and it is safe to build a MINE :
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
            if (allyBarracksSites.isEmpty()) {
            	targetedSite = SitesUtils.getNearestSite(emptySites, myQueenCoordinates);
            	targetedSiteId = targetedSite.getId();
            	if (touchedSite != targetedSiteId) {
                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
            	}
        	} else if (StructuresUtils.getCurrentGoldProduction(allyMineSites) < MIN_GOLD_PRODUCTION
        			&& UnitsUtils.isItSafeToBuildAMine(myQueenCoordinates, enemyUnitsByType)) {
        		if (!allyMineSitesById.isEmpty() && allyMineSitesById.get(touchedSite) != null
        			&& StructuresUtils.isMineNotInFullProduction(allyMineSitesById.get(touchedSite).getStructure())) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSiteToBuildAMine(emptySites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        			}        			
        		}
            } else if (allyTowerSitesById.size() < MIN_TOWER_NUMBER) {
        		if (!allyTowerSitesById.isEmpty() && allyTowerSitesById.get(touchedSite) != null
            			&& StructuresUtils.isTowerNotFullLife(allyTowerSitesById.get(touchedSite).getStructure())) {
                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSite(emptySites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        			}        			
        		}
        	} else if (StructuresUtils.getCurrentGoldProduction(allyMineSites) < MAX_GOLD_PRODUCTION
        			&& UnitsUtils.isItSafeToBuildAMine(myQueenCoordinates, enemyUnitsByType)) {
        		if (!allyMineSitesById.isEmpty() && allyMineSitesById.get(touchedSite) != null
        			&& StructuresUtils.isMineNotInFullProduction(allyMineSitesById.get(touchedSite).getStructure())) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSiteToBuildAMine(emptySites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        			}        			
        		}
            } else if (allyTowerSitesById.size() < MAX_TOWER_NUMBER) {
        		if (!allyTowerSitesById.isEmpty() && allyTowerSitesById.get(touchedSite) != null
            			&& StructuresUtils.isTowerNotFullLife(allyTowerSitesById.get(touchedSite).getStructure())) {
                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSite(emptySites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        			}        			
        		}
        	} else {
            	Site knightBarrack = allyBarracksSitesById.values().iterator().next();
            	SystemOutUtils.printMoveAction(knightBarrack.getCoordinates());
        	}

            /* 2) Second turn action is to TRAIN :
            *		- if there is a site available for training, do it
            *		- else train nothing
            */
            Site siteToTrain = SitesUtils.getSiteToTrain(allyBarracksSites, gold);
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(siteToTrain.getId());
            } else {
                SystemOutUtils.printTrainAction(0);
            }
        }
    }

}
