package com.mgaurat;
import java.util.ArrayList;
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
import com.mgaurat.utils.GameBoardUtils;
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
            Collection<Site> emptySites = emptySitesById.values();
            
            Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ALLY);
    		Map<Integer, Site> allyMineSitesById = allySitesByIdAndStructure.get(StructureEnum.MINE);
            Collection<Site> allyMineSites = allyMineSitesById.values();
    		Map<Integer, Site> allyTowerSitesById = allySitesByIdAndStructure.get(StructureEnum.TOWER);
    		Collection<Site> allyTowerSites = allyTowerSitesById.values();
    		Map<Integer, Site> allyBarracksSitesById = allySitesByIdAndStructure.get(StructureEnum.BARRACKS);
            Collection<Site> allyBarracksSites = allyBarracksSitesById.values();
            Collection<Site> allySites = new ArrayList<>();
            allySites.addAll(allyMineSites);
            allySites.addAll(allyTowerSites);
            allySites.addAll(allyBarracksSites);

    		Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ENEMY);
    		Map<Integer, Site> enemyMineSitesById = enemySitesByIdAndStructure.get(StructureEnum.MINE);
    		Collection<Site> enemyMineSites = enemyMineSitesById.values();
    		Map<Integer, Site> enemyTowerSitesById = enemySitesByIdAndStructure.get(StructureEnum.TOWER);
    		Collection<Site> enemyTowerSites = enemyTowerSitesById.values();
    		Map<Integer, Site> enemyBarracksSitesById = enemySitesByIdAndStructure.get(StructureEnum.BARRACKS);
    		Collection<Site> enemyBarracksSites = enemyBarracksSitesById.values();
    		
    		Collection<Site> ownedSites = new ArrayList<>();
    		ownedSites.addAll(allySites);
    		ownedSites.addAll(enemyMineSites);
    		ownedSites.addAll(enemyTowerSites);
    		ownedSites.addAll(enemyBarracksSites);

            int numUnits = in.nextInt();
            Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = UnitsUtils.getUnitsByTypeAndOwnerFromTurnInput(in, numUnits);
            Map<UnitEnum, List<Unit>> allyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ALLY);
            Collection<Unit> allyGiants = allyUnitsByType.get(UnitEnum.GIANT);
            Map<UnitEnum, List<Unit>> enemyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ENEMY);
            Collection<Unit> enemyKnights = enemyUnitsByType.get(UnitEnum.KNIGHT);
            Unit myQueen = UnitsUtils.getMyQueen(allyUnitsByType);
            Coordinates myQueenCoordinates = myQueen.getCoordinates();
            
            boolean isTouchingAMineToImprove = false;
            boolean isTouchingATowerToImprove = false;
            if (touchedSite != -1) {
            	if (SitesUtils.isSiteInTheMap(touchedSite, allyMineSitesById)
            			&& StructuresUtils.isItSafeAtCoordinatesRegardingEnemyKnights(myQueenCoordinates, enemyUnitsByType)
            			&& StructuresUtils.isMineNotInFullProduction(allyMineSitesById.get(touchedSite).getStructure())) {
            		isTouchingAMineToImprove = true;
            	} else if (SitesUtils.isSiteInTheMap(touchedSite, allyTowerSitesById)
            			&& StructuresUtils.isTowerNotFullLife(allyTowerSitesById.get(touchedSite).getStructure())) {
            		isTouchingATowerToImprove = true;
            	}
            }
            
            int minAllyGoldProduction;
            if (myQueen.getHealth() < 50) {
            	minAllyGoldProduction = 3;
            } else {
            	minAllyGoldProduction = 4;            	
            }
            final int MAX_ALLY_GOLD_PRODUCTION = 8;
            
            int minAllyTowerNumber;
            if (myQueen.getHealth() < 50) {
                minAllyTowerNumber = 1;
            } else {
            	minAllyTowerNumber = 2;            	
            }
            final int AVERAGE_ALLY_TOWER_NUMBER = 3;
            final int MAX_ALLY_TOWER_NUMBER = 6;
            final int ENEMY_TOWER_NUMBER_THRESHOLD = 3;
            final int LOW_LIFE_QUEEN = 20;
            final int ENEMY_KNIGHTS_THRESHOLD = 8;
            Site targetedSite;
            int targetedSiteId;
            Site targetedSiteToBuildAMine = SitesUtils.getNearestSiteToBuildAMine(emptySites, myQueenCoordinates);
        	Site nearestEmptySite = SitesUtils.getNearestSite(emptySites, myQueenCoordinates);

            
            /* 1) First turn action is to MOVE or BUILD.
            *		a) If my QUEEN is low life and threatened or if there is no free site left, MOVE to a safe place
            *		b) else if touching a MINE I owned not in full production, improve it
            *		c) else if touching a TOWER I owned not with full range, improve it
            *		d) else if i have no KNIGHT BARRACKS :
            *			- get the nearest free site and MOVE to it
            *			- or if touching this free site, build a KNIGHT BARRACKS
            *		e) else if my gold production is less than MIN_GOLD_PRODUCTION and it is safe to build a MINE :
            *			- get the nearest free site where a MINE can be built (gold not depleted) MOVE on it 
            *			- or if touching this free site, BUILD a MINE
            *		f) else if my TOWER number is less than MIN_TOWER_NUMBER :
            *			- get the nearest free site and MOVE on it 
            *			- or if touching this free site, BUILD a TOWER
            *		g) else if enemy TOWER number is more than ENEMY_TOWER_NUMBER_THRESHOLD and I do not already owned a GIANT BARRACKS
            *			- get the nearest free site and MOVE on it 
            *			- or if touching this free site, BUILD a GIANT BARRACKS
            *		h) else if my gold production is less than MAX_GOLD_PRODUCTION and it is safe to build a MINE :
            *			- get the nearest free site where a MINE can be built (gold not depleted) MOVE on it 
            *			- or if touching this free site, BUILD a MINE
            *		i) else if my TOWER number is less than MAX_TOWER_NUMBER :
            *			- get the nearest free site and MOVE on it 
            *			- or if touching this free site, BUILD a TOWER
            *		j) else if the nearest free site where a MINE can be built (gold not depleted) is totally safe :
            *			- MOVE on it 
            *			- or if touching this free site, BUILD a MINE
            *		k) else MOVE to a safe coordinates
            */
            if ((myQueen.getHealth() < LOW_LIFE_QUEEN 
            		&& !StructuresUtils.isItSafeAtCoordinates(myQueenCoordinates, enemyUnitsByType, enemyTowerSites))
            		|| emptySites.size() == 0
            		|| enemyKnights.size() > ENEMY_KNIGHTS_THRESHOLD) {
            	Site myKnightBarracks = SitesUtils.getAKnightSite(allyBarracksSites);
            	Coordinates safestCoordinates = GameBoardUtils.getASafeCoordinates(myKnightBarracks, allyTowerSites, allySites);
            	SystemOutUtils.printMoveAction(safestCoordinates);
            } else if (isTouchingAMineToImprove) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        	} else if (isTouchingATowerToImprove) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        	} else if (targetedSiteToBuildAMine != null && (myQueen.getHealth() >= LOW_LIFE_QUEEN && StructuresUtils.getCurrentGoldProduction(allyMineSites) < minAllyGoldProduction 
        			&& StructuresUtils.isItSafeAtCoordinatesRegardingEnemyKnights(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType)
        			&& !StructuresUtils.isCoordinatesInRangeOfTwoTowers(targetedSiteToBuildAMine.getCoordinates(), enemyTowerSites))
        			|| (myQueen.getHealth() < LOW_LIFE_QUEEN && StructuresUtils.getCurrentGoldProduction(allyMineSites) < minAllyGoldProduction 
                			&& StructuresUtils.isItSafeAtCoordinates(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites))) {
        		targetedSiteId = targetedSiteToBuildAMine.getId();
        		if (touchedSite != targetedSiteId) {
        			SystemOutUtils.printMoveAction(targetedSiteToBuildAMine.getCoordinates());
        		} else {
        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        		}        			
        	} else if (nearestEmptySite != null && (myQueen.getHealth() >= LOW_LIFE_QUEEN && allyTowerSitesById.size() < minAllyTowerNumber
        			&& !StructuresUtils.isCoordinatesInRangeOfTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites))
        			|| (myQueen.getHealth() < LOW_LIFE_QUEEN && allyTowerSitesById.size() < minAllyTowerNumber)
        			&& StructuresUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites)) {
        		targetedSite = nearestEmptySite;
        		targetedSiteId = targetedSite.getId();
        		if (touchedSite != targetedSiteId) {
        			SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        		} else {
        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        		}        			
            } else if (nearestEmptySite != null && (myQueen.getHealth() >= LOW_LIFE_QUEEN && allyBarracksSites.isEmpty()
            		&& !StructuresUtils.isCoordinatesInRangeOfTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites))
            		|| (myQueen.getHealth() < LOW_LIFE_QUEEN && allyBarracksSites.isEmpty()
            		&& StructuresUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites))) {
            	targetedSite = nearestEmptySite;
            	targetedSiteId = targetedSite.getId();
            	if (touchedSite != targetedSiteId) {
                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
            	}
            } else if (nearestEmptySite != null && (myQueen.getHealth() >= LOW_LIFE_QUEEN && allyTowerSitesById.size() < AVERAGE_ALLY_TOWER_NUMBER
            		&& !StructuresUtils.isCoordinatesInRangeOfTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites))
            		|| (myQueen.getHealth() < LOW_LIFE_QUEEN && allyTowerSitesById.size() < AVERAGE_ALLY_TOWER_NUMBER
            		&& StructuresUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites))) {
        		targetedSite = nearestEmptySite;
        		targetedSiteId = targetedSite.getId();
        		if (touchedSite != targetedSiteId) {
        			SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        		} else {
        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        		}   
            } else if (nearestEmptySite != null && (myQueen.getHealth() >= LOW_LIFE_QUEEN && enemyTowerSitesById.size() > ENEMY_TOWER_NUMBER_THRESHOLD
            		&& !StructuresUtils.isAtLeastOneAllyGiantBarracks(allyBarracksSites)
            		&& !StructuresUtils.isCoordinatesInRangeOfTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites))
            		|| (myQueen.getHealth() < LOW_LIFE_QUEEN && enemyTowerSitesById.size() > ENEMY_TOWER_NUMBER_THRESHOLD
            		&& !StructuresUtils.isAtLeastOneAllyGiantBarracks(allyBarracksSites)
            		&& StructuresUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites))) {
            	targetedSite = nearestEmptySite;
            	targetedSiteId = targetedSite.getId();
            	if (touchedSite != targetedSiteId) {
                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.GIANT);
            	}
        	} else if (targetedSiteToBuildAMine != null 
        			&& (myQueen.getHealth() >= LOW_LIFE_QUEEN && StructuresUtils.getCurrentGoldProduction(allyMineSites) < MAX_ALLY_GOLD_PRODUCTION 
        			&& !StructuresUtils.isCoordinatesInRangeOfTwoTowers(targetedSiteToBuildAMine.getCoordinates(), enemyTowerSites)
        			&& StructuresUtils.isItSafeAtCoordinatesRegardingEnemyKnights(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType))
        			|| (myQueen.getHealth() < LOW_LIFE_QUEEN && StructuresUtils.getCurrentGoldProduction(allyMineSites) < MAX_ALLY_GOLD_PRODUCTION 
        					&& StructuresUtils.isItSafeAtCoordinates(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites))) {
    			targetedSiteId = targetedSiteToBuildAMine.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(targetedSiteToBuildAMine.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
    			}        			
            } else if (nearestEmptySite != null && (myQueen.getHealth() >= LOW_LIFE_QUEEN && allyTowerSitesById.size() < MAX_ALLY_TOWER_NUMBER
            		&& !StructuresUtils.isCoordinatesInRangeOfTwoTowers(nearestEmptySite.getCoordinates(), enemyTowerSites))
            		|| (myQueen.getHealth() < LOW_LIFE_QUEEN && allyTowerSitesById.size() < MAX_ALLY_TOWER_NUMBER
            		&& StructuresUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites))) {
    			targetedSite = nearestEmptySite;
    			targetedSiteId = targetedSite.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
    			}        			
        	} else if (targetedSiteToBuildAMine != null && StructuresUtils.isItSafeAtCoordinates(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites)) {
    			targetedSiteId = targetedSiteToBuildAMine.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(targetedSiteToBuildAMine.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
    			}  
            } else if (nearestEmptySite != null && StructuresUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites)) {
    			targetedSite = nearestEmptySite;
    			targetedSiteId = targetedSite.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
    			} 
            } else {
            	Site myKnightBarracks = SitesUtils.getAKnightSite(allyBarracksSites);
            	Coordinates safestCoordinates = GameBoardUtils.getASafeCoordinates(myKnightBarracks, allyTowerSites, allySites);
            	SystemOutUtils.printMoveAction(safestCoordinates);
        	}

            /* 2) Second turn action is to TRAIN.
            *		a) if enemy TOWER number is more than ENEMY_TOWER_NUMBER_THRESHOLD and I owned a GIANT BARRACKS, TRAIN a GIANT
            *		b) else TRAIN a KNIGHT
            */
            Site siteToTrain = null;
            if (enemyTowerSitesById.size() > ENEMY_TOWER_NUMBER_THRESHOLD
            		&& allyGiants.size() < 2
            		&& StructuresUtils.isAtLeastOneAllyGiantBarracks(allyBarracksSites)) {
            	siteToTrain = SitesUtils.getGiantSiteToTrain(allyBarracksSites);
            } else if (StructuresUtils.isAtLeastOneAllyKnightBarracks(allyBarracksSites)) {
            	siteToTrain = SitesUtils.getKnightSiteToTrain(allyBarracksSites);            	
            }
            
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(siteToTrain.getId());
            } else {
                SystemOutUtils.printTrainAction(-1);
            }
        }
    }

}
