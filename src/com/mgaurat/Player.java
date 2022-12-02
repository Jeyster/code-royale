package com.mgaurat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;
import com.mgaurat.utils.SitesUtils;
import com.mgaurat.utils.StructuresUtils;
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

            // First line: A valid queen action
            // Second line: A set of training instructions
            //System.out.println("WAIT");
            Site nearestSiteToMoveOn;
            if (SitesUtils.isAtLeastOneSiteOwned(sites)) {
            	nearestSiteToMoveOn = SitesUtils.getNearestSiteNotOwned(sites, myQueenCordinates);
            } else {
            	nearestSiteToMoveOn = SitesUtils.getNearestSite(sites, myQueenCordinates);            	
            }
            int nearestSiteId = nearestSiteToMoveOn.getId();
            Coordinates nearestSiteCoordinates = nearestSiteToMoveOn.getCoordinates();
            int xTarget = nearestSiteCoordinates.getX();
            int yTarget = nearestSiteCoordinates.getY();
            
            if (touchedSite == nearestSiteId) {
            	if (!StructuresUtils.isAtLeastOneKnightBarrackOwnedByMe(sitesById)) {
            		System.out.println("BUILD " + nearestSiteId + " BARRACKS-KNIGHT");
            	} else {
            		System.out.println("BUILD " + nearestSiteId + " TOWER");
            	}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sitesById) == 6) {
            	Site knightBarrack = StructuresUtils.getAKnightBarrackOwnedByMe(sitesById);
                System.out.println("MOVE " + knightBarrack.getCoordinates().getX() + " " + knightBarrack.getCoordinates().getY());
            } else {	
                System.out.println("MOVE " + xTarget + " " + yTarget);
            }

            Site siteToTrain = SitesUtils.getASiteToTrain(sites);
            if (siteToTrain != null) {
                System.out.println("TRAIN " + siteToTrain.getId());
            } else {
                System.out.println("TRAIN 0");
            }
        }
    }

}
