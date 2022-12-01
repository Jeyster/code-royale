package com.mgaurat;
import java.util.*;
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

        Map<Integer, Coordinates> sites = new HashMap<>();

        for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int radius = in.nextInt();
            sites.put(siteId, new Coordinates(x, y));
        }

        // game loop
        while (true) {
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none

            Map<Integer, Integer> sitesParam1 = new HashMap<>();
            for (int i = 0; i < numSites; i++) {
                int siteId = in.nextInt();
                int ignore1 = in.nextInt(); // used in future leagues
                int ignore2 = in.nextInt(); // used in future leagues
                int structureType = in.nextInt(); // -1 = No structure, 2 = Barracks
                int owner = in.nextInt(); // -1 = No structure, 0 = Friendly, 1 = Enemy
                int param1 = in.nextInt();
                int param2 = in.nextInt();

                sitesParam1.put(siteId, param1);
            }

            int numUnits = in.nextInt();
            int myQueenXCoordinate;
            int myQueenYCoordinate;
            Coordinates myQueenCoordinates = new Coordinates(0, 0);
            for (int i = 0; i < numUnits; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                int owner = in.nextInt();
                int unitType = in.nextInt(); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
                int health = in.nextInt();

                if (unitType == -1 && owner == 0) {
                    myQueenXCoordinate = x;
                    myQueenYCoordinate = y;
                    myQueenCoordinates = new Coordinates(myQueenXCoordinate, myQueenYCoordinate);
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // First line: A valid queen action
            // Second line: A set of training instructions
            //System.out.println("WAIT");
            int nearestSiteId = findNearestSite(sites, myQueenCoordinates);
            Coordinates nearestSiteCoordinates = sites.get(nearestSiteId);
            int xTarget = nearestSiteCoordinates.getX();
            int yTarget = nearestSiteCoordinates.getY();
            if (touchedSite == nearestSiteId) {
                System.out.println("BUILD " + nearestSiteId + " BARRACKS-KNIGHT");
            } else {
                System.out.println("MOVE " + xTarget + " " + yTarget);
            }

            if (sitesParam1.get(nearestSiteId) == 0) {
                System.out.println("TRAIN " + nearestSiteId);
            } else {
                System.out.println("TRAIN 0");
            }
        }
    }

    public static int findNearestSite(Map<Integer, Coordinates> sites, Coordinates myQueenCoordinates) {
        int nearestSiteId = 1;
        double distanceToCurrentSite = 10000000000.;
        double distanceToNearestSite = distanceToCurrentSite;
        Coordinates currentSite;
        for (int i = 0; i < sites.size(); i++) {
            currentSite = sites.get(i);
            distanceToCurrentSite = Math.sqrt(((currentSite.getX() - myQueenCoordinates.getX()) * (currentSite.getX() - myQueenCoordinates.getX())) + ((currentSite.getY() - myQueenCoordinates.getY()) * (currentSite.getY() - myQueenCoordinates.getY())));
            if (distanceToCurrentSite < distanceToNearestSite) {
                distanceToNearestSite = distanceToCurrentSite;
                nearestSiteId = i;
            }
        }
        return nearestSiteId;
    }

}
