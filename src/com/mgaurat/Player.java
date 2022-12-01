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

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            // First line: A valid queen action
            // Second line: A set of training instructions
            //System.out.println("WAIT");
            Site nearestSite = GameBoardUtil.getNearestSite(sitesById, myQueen.getCoordinates());
            int nearestSiteId = nearestSite.getId();
            Coordinates nearestSiteCoordinates = nearestSite.getCoordinates();
            int xTarget = nearestSiteCoordinates.getX();
            int yTarget = nearestSiteCoordinates.getY();
            if (touchedSite == nearestSiteId) {
                System.out.println("BUILD " + nearestSiteId + " BARRACKS-KNIGHT");
            } else {
                System.out.println("MOVE " + xTarget + " " + yTarget);
            }

            if (nearestSite.getStructure().getParam1() == 0) {
                System.out.println("TRAIN " + nearestSiteId);
            } else {
                System.out.println("TRAIN 0");
            }
        }
    }

}
