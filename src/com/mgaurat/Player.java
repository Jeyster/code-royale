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
            Coordinates myQueenCordinates = myQueen.getCoordinates();

            // Choose a target Site :
            //	- if no Site owned by me, choose the nearest
            //	- else choose the nearest not owned by me
            Site targetedSite;
            if (SitesUtils.isAtLeastOneSiteOwnedByMe(sites)) {
            	targetedSite = SitesUtils.getNearestSiteNotOwnedByMe(sites, myQueenCordinates);
            } else {
            	targetedSite = SitesUtils.getNearestSite(sites, myQueenCordinates);            	
            }
            int targetedSiteId = targetedSite.getId();
            
            // 1) First turn action is to BUILD if possible. Else is MOVE.
            //		a) BUILD when touching the targeted Site :
            //			- if I don't owned a Knight Barrack, build it
            //			- else build a Tower
            //		b) else MOVE :
            //			- if already build a sufficient number of Structures, move back to the Knight Barrack
            //			- else move to the targeted site
            if (touchedSite == targetedSiteId) {
            	if (!StructuresUtils.isAtLeastOneKnightBarrackOwnedByMe(sitesById)) {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureType.BARRACKS, UnitType.KNIGHT);
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureType.TOWER, null);
            	}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sitesById) == 6) {
            	Site knightBarrack = StructuresUtils.getAKnightBarrackOwnedByMe(sitesById);
            	SystemOutUtils.printMoveAction(knightBarrack.getCoordinates());
            } else {	
            	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            }

            // 2) Second turn action is to TRAIN :
            //		- if there is a site available for training, do it
            //		- else train nothing
            Site siteToTrain = SitesUtils.getASiteToTrain(sites);
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(targetedSiteId);
            } else {
                SystemOutUtils.printTrainAction(0);
            }
        }
    }

}
