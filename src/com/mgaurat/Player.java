package com.mgaurat;
import java.util.*;

import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;
import com.mgaurat.utils.GameBoardUtil;

import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numSites = in.nextInt();

        Map<Integer, Site> sitesById = GameBoardUtil.getSitesFromInitialInput(in, numSites);

        // game loop
        while (true) {
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none

            GameBoardUtil.updateSitesFromTurnInput(in, numSites, sitesById);

            int numUnits = in.nextInt();
            Map<UnitType, List<Unit>> unitsByType = GameBoardUtil.getUnitsByType(in, numUnits);
            Unit myQueen = GameBoardUtil.getMyQueen(unitsByType);
            Coordinates myQueenCordinates = myQueen.getCoordinates();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            // First line: A valid queen action
            // Second line: A set of training instructions
            //System.out.println("WAIT");
            Site nearestSiteToMoveOn;
            if (GameBoardUtil.isAtLeastOneSiteOwned(sitesById)) {
            	nearestSiteToMoveOn = GameBoardUtil.getNearestSiteNotOwned(sitesById, myQueenCordinates);
            } else {
            	nearestSiteToMoveOn = GameBoardUtil.getNearestSite(sitesById, myQueenCordinates);            	
            }
            int nearestSiteId = nearestSiteToMoveOn.getId();
            Coordinates nearestSiteCoordinates = nearestSiteToMoveOn.getCoordinates();
            int xTarget = nearestSiteCoordinates.getX();
            int yTarget = nearestSiteCoordinates.getY();
            
            if (touchedSite == nearestSiteId) {
            	if (!GameBoardUtil.isAtLeastOneKnightBarrackOwnedByMe(sitesById)) {
            		System.out.println("BUILD " + nearestSiteId + " BARRACKS-KNIGHT");
            	} else {
            		System.out.println("BUILD " + nearestSiteId + " TOWER");
            	}
            } else if (GameBoardUtil.getNumberOfTowerOwnedByMe(sitesById) == 6) {
            	Site knightBarrack = GameBoardUtil.getAKnightBarrackOwnedByMe(sitesById);
                System.out.println("MOVE " + knightBarrack.getCoordinates().getX() + " " + knightBarrack.getCoordinates().getY());
            } else {	
                System.out.println("MOVE " + xTarget + " " + yTarget);
            }

            Site siteToTrain = GameBoardUtil.getASiteToTrain(sitesById);
            if (siteToTrain != null) {
                System.out.println("TRAIN " + siteToTrain.getId());
            } else {
                System.out.println("TRAIN 0");
            }
        }
    }

}
