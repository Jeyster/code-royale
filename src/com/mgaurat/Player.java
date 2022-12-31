package com.mgaurat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mgaurat.enums.OwnerEnum;
import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.UnitEnum;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Unit;
import com.mgaurat.utils.GameBoardUtils;
import com.mgaurat.utils.InputUtils;
import com.mgaurat.utils.MathUtils;
import com.mgaurat.utils.PrintUtils;
import com.mgaurat.utils.SitesUtils;
import com.mgaurat.utils.StructuresUtils;
import com.mgaurat.utils.TurnStrategyUtils;
import com.mgaurat.utils.UnitsUtils;

/**
 * Main class
 * 
 * @author mgaurat
 *
 */
class Player {
	
	// Static attributes that refer to global game changes
	private static Integer turn = 0;
	private static Integer startingQueenHealth = null;
	private static Coordinates startingAllyQueenCoordinates = null;
	private static Map<Integer, Integer> remainingGoldBySiteId = new HashMap<>();
	private static boolean isFirstBuildDone = false;
	private static boolean isFirstMinesBuild = false;
	private static boolean isFirstKnightBarracksBuilt = false;
	private static int towersBuilt = 0;
	private static Site secondMine = null;
	private static Site firstKnightBarracks = null;
	private static Site firstTower = null;
	private static Site secondTower = null;
	
    // Constants
    private static final int MIN_ALLY_TOWERS_NUMBER = 3;
    private static final int MAX_ALLY_GOLD_PRODUCTION = 8;
    private static final int ENEMY_TOWERS_NUMBER_THRESHOLD = 3;
    private static final int SAFE_DISTANCE = 500;
    private static final int SAFE_DISTANCE_TO_BUILD_TOWER = 300;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numSites = in.nextInt();

        // Initialize Sites with start of game input
        Map<Integer, Site> sitesById = InputUtils.getSitesFromInitialInput(in, numSites);

        // Game loop
        while (true) {
        	turn++;
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none

            /* --- Sites --- */
            // Update Sites by creating Structure thanks to the the turn input
            Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = InputUtils.updateSitesFromTurnInput(in, sitesById);
            
            // Create Sites collections
            Map<Integer, Site> emptySitesById = sitesByIdAndStructureAndOwner.get(OwnerEnum.NOBODY).get(StructureEnum.NOTHING);
            Collection<Site> emptySites = emptySitesById.values();
            int emptySitesNumber = emptySites.size();
            
            Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ALLY);
    		Map<Integer, Site> allyMineSitesById = allySitesByIdAndStructure.get(StructureEnum.MINE);
            Collection<Site> allyMineSites = allyMineSitesById.values();
            int goldProduction = StructuresUtils.getGoldProduction(allyMineSites);
    		Map<Integer, Site> allyTowerSitesById = allySitesByIdAndStructure.get(StructureEnum.TOWER);
    		Collection<Site> allyTowerSites = allyTowerSitesById.values();
    		int allyTowersNumber = allyTowerSites.size();
    		Map<Integer, Site> allyBarracksSitesById = allySitesByIdAndStructure.get(StructureEnum.BARRACKS);
            Collection<Site> allyBarracksSites = allyBarracksSitesById.values();
            Collection<Site> allyKnightBarracksSites = StructuresUtils.getKnightBarracksSites(allyBarracksSites);
            Collection<Site> allyGiantBarracksSites = StructuresUtils.getGiantBarracksSites(allyBarracksSites);
            Collection<Site> allySites = Stream.of(allyMineSites, allyTowerSites, allyBarracksSites)
            		.flatMap(Collection::stream)
            		.collect(Collectors.toList());
            
            Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ENEMY);
            Map<Integer, Site> enemyMineSitesById = enemySitesByIdAndStructure.get(StructureEnum.MINE);
            Collection<Site> enemyMineSites = enemyMineSitesById.values();
            Map<Integer, Site> enemyTowerSitesById = enemySitesByIdAndStructure.get(StructureEnum.TOWER);
            Collection<Site> enemyTowerSites = enemyTowerSitesById.values();
            int enemyTowersNumber = enemyTowerSites.size();
            Map<Integer, Site> enemyBarracksSitesById = enemySitesByIdAndStructure.get(StructureEnum.BARRACKS);
            Collection<Site> enemyBarracksSites = enemyBarracksSitesById.values();
            Collection<Site> enemyKnightBarracksSites = StructuresUtils.getKnightBarracksSites(enemyBarracksSites);
            Collection<Site> enemyNotInTrainingBarracksSites = StructuresUtils.getNotInTrainingBarracksSites(enemyBarracksSites);
            Collection<Site> enemySites = Stream.of(enemyMineSites, enemyTowerSites, enemyBarracksSites)
            		.flatMap(Collection::stream)
            		.collect(Collectors.toList());

            Collection<Site> allSites = Stream.of(emptySites, allySites, enemySites)
            		.flatMap(Collection::stream)
            		.collect(Collectors.toList());
            
            Collection<Site> enemyAndEmptySites = Stream.of(emptySites, enemySites)
            		.flatMap(Collection::stream)
            		.collect(Collectors.toList());
            
            Collection<Site> allyMineAndNotTrainingBarracksAndTowerSites = StructuresUtils.getMineAndNotTrainingBarracksAndTowerSites(allySites);
            Collection<Site> emptyAndEnemyMineAndNotInTraingBarracksSites = StructuresUtils.getEmptyAndMineAndNotTrainingBarracks(enemyAndEmptySites);
            Collection<Site> emptyAndMineAndNotInTraingBarracksSites = Stream.of(allyMineSites, emptyAndEnemyMineAndNotInTraingBarracksSites)
            		.flatMap(Collection::stream)
            		.collect(Collectors.toList());
            Collection<Site> emptyAndEnemyMineAndObsoleteAllyTowerAndNotInTraingBarracksSites = new ArrayList<>(emptyAndEnemyMineAndNotInTraingBarracksSites);
            
            // Update the remaining gold in each known Site
            StructuresUtils.updateRemaingGoldBySiteId(remainingGoldBySiteId, allSites);

            /* --- Units --- */
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
    		Unit nearestEnemyKnight = UnitsUtils.getNearestUnit(allyQueenCoordinates, enemyKnights);
            Collection<Unit> enemyGiants = enemyUnitsByType.get(UnitEnum.GIANT);
            Unit enemyQueen = UnitsUtils.getQueen(enemyUnitsByType);
            
            /* --- Initialize start of game static parameters --- */
            if (startingQueenHealth == null) {
            	startingQueenHealth = allyQueenHealth;
            }
            if (startingAllyQueenCoordinates == null) {
            	startingAllyQueenCoordinates = allyQueenCoordinates;
            }
            
            int minAllyFirstMines;
            if (startingQueenHealth > 40) {
            	minAllyFirstMines = 2;
            } else {
            	minAllyFirstMines = 1;
            }
            
            if (!isFirstMinesBuild) {
            	isFirstMinesBuild = allyMineSites.size() == minAllyFirstMines;
            }
            
            /* --- Possible Site to MOVE or to BUILD -- */
    		Collection<Site> obsoleteAllyTowerSites = StructuresUtils.getObsoleteAllyTowers(allyTowerSites, startingAllyQueenCoordinates, enemyMineSites);
            emptyAndEnemyMineAndObsoleteAllyTowerAndNotInTraingBarracksSites.addAll(obsoleteAllyTowerSites);
            
            int targetedSiteId;
            Site nearestSite;
            Site nearestSiteToBuildAMine;
            Site nearestSiteToBuildAMineOnObsoleteTowers = null;
            if (!isFirstKnightBarracksBuilt && isFirstBuildDone) {
            	nearestSite = SitesUtils.getNearestSiteFromCoordinatesInBandForwardDirection(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, startingAllyQueenCoordinates);
            	nearestSiteToBuildAMine = StructuresUtils.getNearestSiteFromCoordinatesToBuildAMineInBandForwardDirection(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, remainingGoldBySiteId, enemyKnightBarracksSites, startingAllyQueenCoordinates, allyTowerSites);
            } else {
            	if (isFirstKnightBarracksBuilt && towersBuilt == 0) {
            		System.err.println("First tower");
            		if (firstTower == null) {
            			firstTower = StructuresUtils.getPerfectSiteForFirstTower(emptySites, allyQueenCoordinates, startingAllyQueenCoordinates);
            		}
            		nearestSite = firstTower;
            	} else if (towersBuilt == 1) {
            		System.err.println("Second tower");

            		if (secondTower == null) {
            			secondTower = StructuresUtils.getPerfectSiteForSecondTower(emptySites, firstTower, startingAllyQueenCoordinates);
            		}
            		nearestSite = secondTower;
            	} else {
            		nearestSite = SitesUtils.getNearestSiteToBuild(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueen, startingAllyQueenCoordinates, enemyTowerSites, nearestEnemyKnight);
            	}
            	nearestSiteToBuildAMine = StructuresUtils.getNearestSiteFromCoordinatesToBuildAMine(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueen, startingAllyQueenCoordinates, remainingGoldBySiteId, enemyKnightBarracksSites, allyTowerSites, enemyTowerSites, enemyMineSites, nearestEnemyKnight);
            	nearestSiteToBuildAMineOnObsoleteTowers = StructuresUtils.getNearestSiteFromCoordinatesToBuildAMine(emptyAndEnemyMineAndObsoleteAllyTowerAndNotInTraingBarracksSites, allyQueen, startingAllyQueenCoordinates, remainingGoldBySiteId, enemyKnightBarracksSites, allyTowerSites, enemyTowerSites, enemyMineSites, nearestEnemyKnight);
            }
            
            Site nearestEnemyBarracksSiteToBuildATower = SitesUtils.getNearestSiteToBuild(enemyNotInTrainingBarracksSites, allyQueen, startingAllyQueenCoordinates, enemyTowerSites, nearestEnemyKnight);
            Site nearestSiteToBuildTowerInForward = SitesUtils.getNearestSiteToBuildInForwardDirection(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueen, startingAllyQueenCoordinates, nearestEnemyKnight, enemyTowerSites);
            Site nearestAllyTowerSiteWithNotSufficientLife = SitesUtils.getNearestSiteToBuild(StructuresUtils.getAllyTowerSitesWithNotSufficientLife(allyTowerSites), allyQueen, startingAllyQueenCoordinates, enemyTowerSites, nearestEnemyKnight);
            Site nearestAllySiteNotInTraining = SitesUtils.getNearestSiteToBuild(allyMineAndNotTrainingBarracksAndTowerSites, allyQueen, startingAllyQueenCoordinates, enemyTowerSites, nearestEnemyKnight);
            Site nearestSiteToBuildATowerWhenRunningAway;
        	if (isFirstKnightBarracksBuilt && towersBuilt == 0) {
        		if (firstTower == null) {
        			firstTower = StructuresUtils.getPerfectSiteForFirstTower(emptySites, allyQueenCoordinates, startingAllyQueenCoordinates);
        		}
        		nearestSiteToBuildATowerWhenRunningAway = firstTower;
        	} else if (isFirstKnightBarracksBuilt && towersBuilt <= 3) {
                nearestSiteToBuildATowerWhenRunningAway = SitesUtils.getNearestSiteFromCoordinates(emptySites, allyQueenCoordinates);
        	} else {
                nearestSiteToBuildATowerWhenRunningAway = SitesUtils.getNearestSiteToBuild(emptyAndMineAndNotInTraingBarracksSites, allyQueen, startingAllyQueenCoordinates, enemyTowerSites, nearestEnemyKnight);
        	}
            
            /* --- Calculate coordinates to go for the second MINE built --- */
            Coordinates coordinatesForSecondBuild = null;
            if (minAllyFirstMines == 2 && isFirstBuildDone && !isFirstMinesBuild) {
            	if (secondMine == null) {
            		secondMine = nearestSiteToBuildAMine;            		
            	}
            	if (firstKnightBarracks == null) {
            		Collection<Site> sites = emptyAndEnemyMineAndNotInTraingBarracksSites
            				.stream()
            				.filter(site -> site.getId() != secondMine.getId())
            				.collect(Collectors.toList());
            		firstKnightBarracks = SitesUtils.getNearestSiteFromCoordinatesInBandForwardDirection(sites, secondMine.getCoordinates(), startingAllyQueenCoordinates);            		
            	}
            	coordinatesForSecondBuild = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, firstKnightBarracks.getCoordinates(), allSites);
            }
            
            /* --- Booleans that could be use to choose what to do during this turn --- */
            boolean isCampMode = turn > 100 && allyQueenHealth > enemyQueen.getHealth()
            		&& allyMineSites.size() < enemyMineSites.size();
            
            boolean isTouchingAMineToImprove = false;
            boolean isTouchingATowerToImprove = false;
            if (touchedSite != -1) {
            	if (allyMineSitesById.get(touchedSite) != null
            			&& UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(allyQueenCoordinates, enemyUnitsByType, SAFE_DISTANCE)
            			&& StructuresUtils.isMineNotInFullProduction(allyMineSitesById.get(touchedSite).getStructure())) {
            		isTouchingAMineToImprove = true;
            	}
        		if (allyTowerSitesById.get(touchedSite) != null
            			&& StructuresUtils.isTowerLifeNotSufficient(allyTowerSitesById.get(touchedSite).getStructure())
            			&& (isCampMode || !SitesUtils.isSiteIdInCollection(obsoleteAllyTowerSites, touchedSite))) {
            		isTouchingATowerToImprove = true;
            	}
            }
            
            /* 1) First turn action is to MOVE or BUILD. Generally, if ally QUEEN is low life, adopt a safest strategy.
            *		a) MOVE to a safe place when the ally QUEEN is in danger.
            *		   Can BUILD TOWER on the way to go.
            *		b) else if touching a MINE I owned not in full production, improve it
            *		c) else if MOVE to a reachable enemy BARRACKS Site and BUILD a TOWER
            *		d) else if touching a TOWER I owned not with full range, improve it
            *		e) else if MOVE to the chosen Site and BUILD a MINE until 1 or 2 MINE are built
            *		f) else if MOVE to the nearest empty Site and BUILD an only one KNIGHT BARRACKS
            *		g) else if MOVE to the chosen Site and BUILD an only one KNIGHT BARRACKS
            *		h) else if MOVE to the nearest empty Site and BUILD a TOWER until MIN_ALLY_TOWERS_NUMBER is reached
            *		i) else if MOVE to the nearest empty Site and BUILD an only one GIANT BARRACKS
            *		j) else if MOVE to the chosen Site and BUILD a MINE until MAX_ALLY_GOLD_PRODUCTION is reached
            *		k) else if MOVE to the chosen Site (could be obsolete TOWER) and BUILD a MINE until MAX_ALLY_GOLD_PRODUCTION is reached
            *		l) else if MOVE safely to the chosen Site and BUILD a MINE
            *		m) else if MOVE safely to the chosen Site (could be obsolete TOWER) and BUILD a MINE
            *		n) else if MOVE to the to the chosen Site in front and BUILD a TOWER
            *		o) else if MOVE to the nearest ally TOWER with not enough life points
            *		p) else if MOVE to the to the chosen Site and BUILD a TOWER
            *		q) else MOVE to a safe place
            */
            Coordinates coordinatesToGo;
            if (TurnStrategyUtils.isRunAwayStrategyOk(allyQueenHealth, allyQueenCoordinates, enemyUnitsByType, enemyTowerSites, emptySitesNumber, enemyKnightsNumber, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites)
            		&& towersBuilt > 0) {
            	System.err.println("Strategy a)");
            	Coordinates safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, enemyKnights, allyQueenCoordinates, allyQueen); 
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, safestCoordinates, allSites);
            	if (TurnStrategyUtils.isBuildTowerWhenRunningAwayStrategyOk(allyQueenCoordinates, safestCoordinates, nearestSiteToBuildATowerWhenRunningAway, enemyGiants)
            			&& allyQueenHealth > 5) {
            		if (touchedSite == nearestSiteToBuildATowerWhenRunningAway.getId()) {
            			towersBuilt++;
                		PrintUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
            		} else {
            			safestCoordinates = nearestSiteToBuildATowerWhenRunningAway.getCoordinates();
            			PrintUtils.printMoveAction(safestCoordinates);            			
            		}
            	} else {
            		Site safestTower = StructuresUtils.getSafestTower(allyTowerSites, startingAllyQueenCoordinates, allyQueen, nearestEnemyKnight);
            		if (safestTower != null && touchedSite == safestTower.getId()
            				&& StructuresUtils.isTowerLifeNotSufficient(safestTower.getStructure())
            				&& (nearestEnemyKnight == null || 
            				(MathUtils.getDistanceBetweenTwoCoordinates(nearestEnemyKnight.getCoordinates(), allyQueenCoordinates) > 200
            				&& MathUtils.isLineCrossingCircle(allyQueenCoordinates, nearestEnemyKnight.getCoordinates(), safestTower.getCoordinates(), safestTower.getRadius())
            				&& safestTower.getCoordinates().isBetweenTwoXCoordinates(allyQueenCoordinates, nearestEnemyKnight.getCoordinates())))) {
                		PrintUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
            		} else {
            			PrintUtils.printMoveAction(coordinatesToGo);            			
            		}
            	}
            } else if (isTouchingAMineToImprove) {
            	System.err.println("Strategy b)");
        		PrintUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
            } else if (TurnStrategyUtils.isTowerMoveOrBuildOnEnemyBarracksStrategyOk(allyQueenHealth, nearestEnemyBarracksSiteToBuildATower, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE_TO_BUILD_TOWER, enemyKnightBarracksSites, allyQueenCoordinates, enemyMineSites, touchedSite)) {
            	System.err.println("Strategy c)");
        		targetedSiteId = nearestEnemyBarracksSiteToBuildATower.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestEnemyBarracksSiteToBuildATower.getCoordinates(), allSites);
        		if (touchedSite != targetedSiteId) {
        			PrintUtils.printMoveAction(coordinatesToGo);
        		} else {
        			towersBuilt++;
        			PrintUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        		}   
        	} else if (isTouchingATowerToImprove) {
            	System.err.println("Strategy d)");
            	PrintUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, Integer.MAX_VALUE, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites, touchedSite)
        			&& !isFirstMinesBuild && !isCampMode) {
            	System.err.println("Strategy e)");
        		targetedSiteId = nearestSiteToBuildAMine.getId();
        		if (coordinatesForSecondBuild != null) {
        			coordinatesToGo = coordinatesForSecondBuild;
        		} else {
        			coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates(), allSites);        			
        		}
        		if (touchedSite != targetedSiteId) {
        			PrintUtils.printMoveAction(coordinatesToGo);
        		} else {
        			if (!isFirstBuildDone) {
        				isFirstBuildDone = true;
        			}
        			PrintUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        		}
            } else if (TurnStrategyUtils.isKnightBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestSite, allyKnightBarracksSites, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites, touchedSite, goldProduction)
            		&& !isCampMode) {
            	System.err.println("Strategy f)");
            	targetedSiteId = nearestSite.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSite.getCoordinates(), allSites);
            	if (touchedSite != targetedSiteId) {
                	PrintUtils.printMoveAction(coordinatesToGo);
            	} else {
            		if (!isFirstKnightBarracksBuilt) {
            			isFirstKnightBarracksBuilt = true;
            		}
            		PrintUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
            	}
            } else if (TurnStrategyUtils.isKnightBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestAllySiteNotInTraining, allyKnightBarracksSites, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites, touchedSite, goldProduction)
            		&& !isCampMode) {
            	System.err.println("Strategy g)");
            	targetedSiteId = nearestAllySiteNotInTraining.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestAllySiteNotInTraining.getCoordinates(), allSites);
            	if (touchedSite != targetedSiteId) {
                	PrintUtils.printMoveAction(coordinatesToGo);
            	} else {
            		PrintUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
            	}
            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestSite, allyTowersNumber, MIN_ALLY_TOWERS_NUMBER, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE_TO_BUILD_TOWER, enemyKnightBarracksSites, enemyMineSites, touchedSite)
            		&& towersBuilt < 3) {
            	System.err.println("Strategy h)");
        		targetedSiteId = nearestSite.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSite.getCoordinates(), allSites);
        		if (touchedSite != targetedSiteId) {
        			PrintUtils.printMoveAction(coordinatesToGo);
        		} else {
        			towersBuilt++;
        			PrintUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        		}   
            } else if (TurnStrategyUtils.isGiantBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestSite, enemyTowersNumber, allyGiantBarracksSites, enemyUnitsByType, enemyTowerSites, ENEMY_TOWERS_NUMBER_THRESHOLD, SAFE_DISTANCE, enemyKnightBarracksSites, allyMineSites, enemyMineSites, touchedSite)
            		&& !isCampMode) {
            	System.err.println("Strategy i)");
            	targetedSiteId = nearestSite.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSite.getCoordinates(), allSites);
            	if (touchedSite != targetedSiteId) {
                	PrintUtils.printMoveAction(coordinatesToGo);
            	} else {
            		PrintUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.GIANT);
            	}
        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, MAX_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites, touchedSite)
        			&& !isCampMode) {
            	System.err.println("Strategy j)");
        		targetedSiteId = nearestSiteToBuildAMine.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates(), allSites);
    			if (touchedSite != targetedSiteId) {
    				PrintUtils.printMoveAction(coordinatesToGo);
    			} else {
    				PrintUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
    			}
        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMineOnObsoleteTowers, allyMineSites, MAX_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites, touchedSite)
        			&& !isCampMode) {
            	System.err.println("Strategy k)");
        		targetedSiteId = nearestSiteToBuildAMineOnObsoleteTowers.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMineOnObsoleteTowers.getCoordinates(), allSites);
    			if (touchedSite != targetedSiteId) {
    				PrintUtils.printMoveAction(coordinatesToGo);
    			} else {
    				PrintUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
    			}
        	} else if (nearestSiteToBuildAMine != null && !isCampMode && (touchedSite == nearestSiteToBuildAMine.getId()
        			|| GameBoardUtils.isItSafeAtCoordinates(nearestSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites))) {
        		System.err.println("Strategy l)");
        		targetedSiteId = nearestSiteToBuildAMine.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates(), allSites);
        		if (touchedSite != targetedSiteId) {
        			PrintUtils.printMoveAction(coordinatesToGo);
        		} else {
        			PrintUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        		}
        	} else if (nearestSiteToBuildAMineOnObsoleteTowers != null && !isCampMode && (touchedSite == nearestSiteToBuildAMineOnObsoleteTowers.getId() 
        			|| GameBoardUtils.isItSafeAtCoordinates(nearestSiteToBuildAMineOnObsoleteTowers.getCoordinates(), enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites))) {
        		System.err.println("Strategy m)");
        		targetedSiteId = nearestSiteToBuildAMineOnObsoleteTowers.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMineOnObsoleteTowers.getCoordinates(), allSites);
        		if (touchedSite != targetedSiteId) {
        			PrintUtils.printMoveAction(coordinatesToGo);
        		} else {
        			PrintUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        		} 
        	} else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildTowerInForward, allyTowersNumber, Integer.MAX_VALUE, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE_TO_BUILD_TOWER, enemyKnightBarracksSites, enemyMineSites, touchedSite)
        			&& !isCampMode) {
        		System.err.println("Strategy n)");
        		targetedSiteId = nearestSiteToBuildTowerInForward.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildTowerInForward.getCoordinates(), allSites);
        		if (touchedSite != targetedSiteId) {
        			PrintUtils.printMoveAction(coordinatesToGo);
        		} else {
        			towersBuilt++;
        			PrintUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        		} 
        	} else if (nearestAllyTowerSiteWithNotSufficientLife != null 
        			&& !SitesUtils.isSiteIdInCollection(obsoleteAllyTowerSites, nearestAllyTowerSiteWithNotSufficientLife.getId())) {
        		System.err.println("Strategy o)");
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestAllyTowerSiteWithNotSufficientLife.getCoordinates(), allSites);
        		PrintUtils.printMoveAction(coordinatesToGo);
        	} else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestSite, allyTowersNumber, Integer.MAX_VALUE, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE_TO_BUILD_TOWER, enemyKnightBarracksSites, enemyMineSites, touchedSite)
        			&& isCampMode) {
        		System.err.println("Strategy p)");
        		targetedSiteId = nearestSite.getId();
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSite.getCoordinates(), allSites);
        		if (touchedSite != targetedSiteId) {
        			PrintUtils.printMoveAction(coordinatesToGo);
        		} else {
        			towersBuilt++;
        			PrintUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        		} 
            } else {
            	System.err.println("Strategy q)");
            	Coordinates safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, enemyKnights, allyQueenCoordinates, allyQueen);
        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, safestCoordinates, allSites);
            	PrintUtils.printMoveAction(coordinatesToGo);
        	}

            /* 2) Second turn action is to TRAIN.
            *		a) TRAIN a GIANT id needed
            *		b) else TRAIN a KNIGHT
            */
            Optional<Site> siteToTrain = Optional.empty();
            if (TurnStrategyUtils.isGiantTrainStrategyOk(enemyTowersNumber, ENEMY_TOWERS_NUMBER_THRESHOLD + 2, allyMineSites, allyGiants, allyGiantBarracksSites)) {
            	siteToTrain = StructuresUtils.getBarracksSiteToTrain(allyGiantBarracksSites);
            } else if (!allyKnightBarracksSites.isEmpty()) {
            	siteToTrain = StructuresUtils.getBarracksSiteToTrain(allyKnightBarracksSites);            	
            }
            
            if (siteToTrain.isPresent()) {
                PrintUtils.printTrainAction(siteToTrain.get().getId());
            } else {
                PrintUtils.printTrainAction(-1);
            }
        }
    }

}
