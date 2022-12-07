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
import com.mgaurat.utils.InputUtils;
import com.mgaurat.utils.SitesUtils;
import com.mgaurat.utils.StructuresUtils;
import com.mgaurat.utils.SystemOutUtils;
import com.mgaurat.utils.TurnStrategyUtils;
import com.mgaurat.utils.UnitsUtils;

/**
 * Main class
 * 
 * @author mgaurat
 *
 */
class Player {
	
	private static Integer startingQueenHealth = null;
	private static Coordinates startingAllyQueenCoordinates = null;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numSites = in.nextInt();

        // Initialize Sites with start of game input
        Map<Integer, Site> sitesById = InputUtils.getSitesFromInitialInput(in, numSites);

        // Game loop
        while (true) {
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none

            // Update Sites by creating Structure thanks to the the turn input
            Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = InputUtils.updateSitesFromTurnInput(in, sitesById);
            
            Map<Integer, Site> emptySitesById = sitesByIdAndStructureAndOwner.get(OwnerEnum.NOBODY).get(StructureEnum.NOTHING);
            Collection<Site> emptySites = emptySitesById.values();
            int emptySitesNumber = emptySites.size();
            
            Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ALLY);
    		Map<Integer, Site> allyMineSitesById = allySitesByIdAndStructure.get(StructureEnum.MINE);
            Collection<Site> allyMineSites = allyMineSitesById.values();
    		Map<Integer, Site> allyTowerSitesById = allySitesByIdAndStructure.get(StructureEnum.TOWER);
    		Collection<Site> allyTowerSites = allyTowerSitesById.values();
    		int allyTowersNumber = allyTowerSites.size();
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
    		int enemyTowersNumber = enemyTowerSites.size();
    		Map<Integer, Site> enemyBarracksSitesById = enemySitesByIdAndStructure.get(StructureEnum.BARRACKS);
            Collection<Site> enemyBarracksSites = enemyBarracksSitesById.values();
            Collection<Site> enemySites = new ArrayList<>();
            enemySites.addAll(enemyMineSites);
            enemySites.addAll(enemyTowerSites);
            enemySites.addAll(enemyBarracksSites);
            
            Collection<Site> allyAndEmptySites = new ArrayList<>();
            allyAndEmptySites.addAll(emptySites);
            allyAndEmptySites.addAll(allySites);
            Collection<Site> allyAndEmptySitesExceptKnightBarracksAndTower = StructuresUtils.getSitesExceptKnightBarracksAndTower(allyAndEmptySites);
            
            Collection<Site> allSites = new ArrayList<>();
            allSites.addAll(emptySites);
            allSites.addAll(allySites);
            allSites.addAll(enemySites);

            // Get the Units thanks to the turn input
            int numUnits = in.nextInt();
            Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = InputUtils.getUnitsByTypeAndOwnerFromTurnInput(in, numUnits);
            
            Map<UnitEnum, List<Unit>> allyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ALLY);
            Collection<Unit> allyGiants = allyUnitsByType.get(UnitEnum.GIANT);
            Unit allyQueen = UnitsUtils.getQueen(allyUnitsByType);
            Coordinates allyQueenCoordinates = allyQueen.getCoordinates();
            int allyQueenHealth = allyQueen.getHealth();
                        
            Map<UnitEnum, List<Unit>> enemyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ENEMY);
            Collection<Unit> enemyKnights = enemyUnitsByType.get(UnitEnum.KNIGHT);
            int enemyKnightsNumber = enemyKnights.size();
            
            // Initialize start of game parameters
            if (startingQueenHealth == null) {
            	startingQueenHealth = allyQueenHealth;
            }
            if (startingAllyQueenCoordinates == null) {
            	startingAllyQueenCoordinates = allyQueenCoordinates;
            }
            
            // Constants
            final int AVERAGE_ALLY_TOWERS_NUMBER = 3;
            final int MAX_ALLY_TOWERS_NUMBER = 6;
            final int MAX_ALLY_GOLD_PRODUCTION = 8;
            final int ENEMY_TOWERS_NUMBER_THRESHOLD = 3;
            
            // Depending on ally QUEEN health, choose the best values
            int minAllyGoldProduction;
            if (allyQueen.getHealth() < 50) {
            	minAllyGoldProduction = 4;
            } else {
            	minAllyGoldProduction = 6;            	
            }
            
            int minAllyTowerNumber;
            if (allyQueen.getHealth() < 50) {
                minAllyTowerNumber = 1;
            } else {
            	minAllyTowerNumber = 2;            	
            }
            
            // Possible Site to MOVE or to BUILD
            Site targetedSite;
            int targetedSiteId;
            Site nearestEmptySite = SitesUtils.getNearestSiteFromCoordinates(emptySites, allyQueenCoordinates);
            Site nearestAllyTowerSiteWithNotSufficientLife = SitesUtils.getNearestSiteFromCoordinates(StructuresUtils.getAllyTowerSitesWithNotSufficientLife(allyTowerSites), allyQueenCoordinates);
            Site nearestSiteToBuildAMine = StructuresUtils.getNearestSiteFromCoordinatesToBuildAMine(emptySites, allyQueenCoordinates);
            Site nearestSiteToBuildATower = SitesUtils.getNearestSiteFromCoordinates(allyAndEmptySitesExceptKnightBarracksAndTower, allyQueenCoordinates);
            
            // Booleans that could be use to choose what to do during this turn
            boolean isTouchingAMineToImprove = false;
            boolean isTouchingATowerToImprove = false;
            if (touchedSite != -1) {
            	if (allyMineSitesById.get(touchedSite) != null
            			&& UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(allyQueenCoordinates, enemyUnitsByType)
            			&& StructuresUtils.isMineNotInFullProduction(allyMineSitesById.get(touchedSite).getStructure())) {
            		isTouchingAMineToImprove = true;
            	}
        		if (allyTowerSitesById.get(touchedSite) != null
            			&& StructuresUtils.isTowerLifeNotSufficient(allyTowerSitesById.get(touchedSite).getStructure())) {
            		isTouchingATowerToImprove = true;
            	}
            }
            
            
            /* 1) First turn action is to MOVE or BUILD. Generally, if ally QUEEN is low life, adopt a safest strategy.
            *		a) MOVE to a safe place when the ally QUEEN is in danger
            *		b) else if touching a MINE I owned not in full production, improve it
            *		c) else if touching a TOWER I owned not with full range, improve it
            *		d) else if MOVE to a free Site and BUILD a MINE until minAllyGoldProduction is reached
            *		e) else if MOVE to a free Site and BUILD a TOWER until minAllyTowerNumber is reached
            *		f) else if MOVE to a free Site and BUILD an only one KNIGHT BARRACKS
            *		g) else if MOVE to a free Site and BUILD a TOWER until AVERAGE_ALLY_TOWERS_NUMBER is reached
            *		h) else if MOVE to a free Site and BUILD an only one GIANT BARRACKS
            *		i) else if MOVE to a free Site and BUILD a MINE until MAX_ALLY_GOLD_PRODUCTION is reached
            *		j) else if MOVE to a free Site and BUILD a TOWER until MAX_ALLY_TOWERS_NUMBER is reached
            *		k) else if MOVE to a free Site and BUILD a MINE
            *		l) else if MOVE to a free Site and BUILD a TOWER
            *		m) else if MOVE to the nearest ally TOWER with not enough life points
            *		n) else MOVE to a safe place
            */
            if (TurnStrategyUtils.isRunAwayStrategyOk(allyQueenHealth, allyQueenCoordinates, enemyUnitsByType, enemyTowerSites, emptySitesNumber, enemyKnightsNumber)) {
            	Coordinates safestCoordinates;
            	if (TurnStrategyUtils.isBuildTowerWhenRunningAwayStrategyOk(allyQueenCoordinates, nearestSiteToBuildATower)) {
            		if (touchedSite == nearestSiteToBuildATower.getId()) {
                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
            		} else {
            			safestCoordinates = nearestSiteToBuildATower.getCoordinates();
            			SystemOutUtils.printMoveAction(safestCoordinates);            			
            		}
            	} else {
            		safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, allySites);            		
            		SystemOutUtils.printMoveAction(safestCoordinates);
            	}
            } else if (isTouchingAMineToImprove) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        	} else if (isTouchingATowerToImprove) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, minAllyGoldProduction, enemyUnitsByType, enemyTowerSites)) {
        		targetedSiteId = nearestSiteToBuildAMine.getId();
        		if (touchedSite != targetedSiteId) {
        			SystemOutUtils.printMoveAction(nearestSiteToBuildAMine.getCoordinates());
        		} else {
        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        		}        			      			
            } else if (TurnStrategyUtils.isKnightBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyBarracksSites, enemyUnitsByType, enemyTowerSites)) {
            	targetedSite = nearestEmptySite;
            	targetedSiteId = targetedSite.getId();
            	if (touchedSite != targetedSiteId) {
                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
            	}
            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, AVERAGE_ALLY_TOWERS_NUMBER, enemyUnitsByType, enemyTowerSites)) {
        		targetedSite = nearestEmptySite;
        		targetedSiteId = targetedSite.getId();
        		if (touchedSite != targetedSiteId) {
        			SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        		} else {
        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        		}   
            } else if (TurnStrategyUtils.isGiantBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, enemyTowersNumber, allyBarracksSites, enemyUnitsByType, enemyTowerSites, ENEMY_TOWERS_NUMBER_THRESHOLD)) {
            	targetedSite = nearestEmptySite;
            	targetedSiteId = targetedSite.getId();
            	if (touchedSite != targetedSiteId) {
                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.GIANT);
            	}
        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, MAX_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites)) {
    			targetedSiteId = nearestSiteToBuildAMine.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(nearestSiteToBuildAMine.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
    			}        			
            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, MAX_ALLY_TOWERS_NUMBER, enemyUnitsByType, enemyTowerSites)) {
    			targetedSite = nearestEmptySite;
    			targetedSiteId = targetedSite.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
    			}        			
        	} else if (nearestSiteToBuildAMine != null && GameBoardUtils.isItSafeAtCoordinates(nearestSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites)) {
    			targetedSiteId = nearestSiteToBuildAMine.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(nearestSiteToBuildAMine.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
    			}  
            } else if (nearestEmptySite != null && GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites)) {
    			targetedSite = nearestEmptySite;
    			targetedSiteId = targetedSite.getId();
    			if (touchedSite != targetedSiteId) {
    				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
    			} else {
    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
    			} 
            } else if (nearestAllyTowerSiteWithNotSufficientLife != null) {
            	SystemOutUtils.printMoveAction(nearestAllyTowerSiteWithNotSufficientLife.getCoordinates()); 
            } else {
            	Coordinates safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, allySites);
            	SystemOutUtils.printMoveAction(safestCoordinates);
        	}

            /* 2) Second turn action is to TRAIN.
            *		a) if enemy TOWER number is more than ENEMY_TOWER_NUMBER_THRESHOLD and I owned a GIANT BARRACKS, TRAIN a GIANT
            *		b) else TRAIN a KNIGHT
            */
            Site siteToTrain = null;
            if (enemyTowerSitesById.size() > ENEMY_TOWERS_NUMBER_THRESHOLD
            		&& allyGiants.size() < 2
            		&& StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)) {
            	siteToTrain = StructuresUtils.getGiantSiteToTrain(allyBarracksSites);
            } else if (StructuresUtils.isAtLeastOneKnightBarracks(allyBarracksSites)) {
            	siteToTrain = StructuresUtils.getAKnightSiteToTrain(allyBarracksSites);            	
            }
            
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(siteToTrain.getId());
            } else {
                SystemOutUtils.printTrainAction(-1);
            }
        }
    }

}
