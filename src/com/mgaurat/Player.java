package com.mgaurat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitType;
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
            Map<UnitType, List<Unit>> unitsByType = UnitsUtils.getUnitsByType(in, numUnits);
            Unit myQueen = UnitsUtils.getMyQueen(unitsByType);
            Coordinates myQueenCoordinates = myQueen.getCoordinates();

            // Choose a target Site :
            final int MAX_GOLD_PRODUCTION = 8;
            Site targetedSite = SitesUtils.getSiteToTarget(sitesById, myQueenCoordinates, touchedSite, MAX_GOLD_PRODUCTION);
            int targetedSiteId = targetedSite.getId();
            
            // 1) First turn action is to BUILD if possible. Else is MOVE.
            //		a) BUILD when touching the targeted Site :
            //			- if I don't owned a KNIGHT BARRACKS, build it
            //			- if my gold production is not sufficient (< MAX_GOLD_PRODUCTION), build a MINE or improve it
            //			- else build a TOWER
            //		b) else MOVE :
            //			- if already build a sufficient number of TOWER (MAX_TOWER_NUMBER), move back to the KNIGHT BARRACKS
            //			- else move to the targeted site
            
            final int MAX_TOWER_NUMBER = 4;
            if (touchedSite == targetedSiteId) {
            	if (!StructuresUtils.isAtLeastOneKnightBarrackOwnedByMe(sites)) {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureType.BARRACKS, UnitType.KNIGHT);
            	} else if (StructuresUtils.getCurrentGoldProduction(sites) < MAX_GOLD_PRODUCTION) {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureType.MINE, null);
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureType.TOWER, null);
            	}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sites) == MAX_TOWER_NUMBER) {
            	Site knightBarrack = StructuresUtils.getAKnightBarrackOwnedByMe(sites);
            	SystemOutUtils.printMoveAction(knightBarrack.getCoordinates());
            } else {	
            	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            }

            // 2) Second turn action is to TRAIN :
            //		- if there is a site available for training, do it
            //		- else train nothing
            Site siteToTrain = SitesUtils.getASiteToTrain(sites);
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(siteToTrain.getId());
            } else {
                SystemOutUtils.printTrainAction(0);
            }
        }
    }

}
