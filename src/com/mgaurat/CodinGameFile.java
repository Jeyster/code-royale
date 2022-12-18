//import java.awt.geom.AffineTransform;
//import java.awt.geom.NoninvertibleTransformException;
//import java.awt.geom.Point2D;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//
///**
// * Main class
// * 
// * @author mgaurat
// *
// */
//class Player {
//	
//	// Static attributes that refer to global game changes
//	private static Integer startingQueenHealth = null;
//	private static Coordinates startingAllyQueenCoordinates = null;
//	private static Map<Integer, Integer> remainingGoldBySiteId = new HashMap<>();
//	private static boolean isFirstBuildDone = false;
//	private static boolean isTwoFirstMinesBuild = false;
//	private static boolean isFirstKnightBarracksBuilt = false;
//	private static int towersBuilt = 0;
//	
//    // Constants
//    private static final int MIN_ALLY_GOLD_PRODUCTION = 2;
//    private static final int MIN_ALLY_TOWERS_NUMBER = 3;
//    private static final int MAX_ALLY_TOWERS_NUMBER = 4;
//    private static final int MAX_ALLY_GOLD_PRODUCTION = 8;
//    private static final int ENEMY_TOWERS_NUMBER_THRESHOLD = 3;
//    private static final int SAFE_DISTANCE = 500;
//
//    public static void main(String args[]) {
//        Scanner in = new Scanner(System.in);
//        int numSites = in.nextInt();
//
//        // Initialize Sites with start of game input
//        Map<Integer, Site> sitesById = InputUtils.getSitesFromInitialInput(in, numSites);
//
//        // Game loop
//        while (true) {
//            int gold = in.nextInt();
//            int touchedSite = in.nextInt(); // -1 if none
//
//            /* --- Sites --- */
//            // Update Sites by creating Structure thanks to the the turn input
//            Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = InputUtils.updateSitesFromTurnInput(in, sitesById);
//            
//            // Create Sites collections
//            Map<Integer, Site> emptySitesById = sitesByIdAndStructureAndOwner.get(OwnerEnum.NOBODY).get(StructureEnum.NOTHING);
//            Collection<Site> emptySites = emptySitesById.values();
//            int emptySitesNumber = emptySites.size();
//            
//            Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ALLY);
//    		Map<Integer, Site> allyMineSitesById = allySitesByIdAndStructure.get(StructureEnum.MINE);
//            Collection<Site> allyMineSites = allyMineSitesById.values();
//    		Map<Integer, Site> allyTowerSitesById = allySitesByIdAndStructure.get(StructureEnum.TOWER);
//    		Collection<Site> allyTowerSites = allyTowerSitesById.values();
//    		int allyTowersNumber = allyTowerSites.size();
//    		Map<Integer, Site> allyBarracksSitesById = allySitesByIdAndStructure.get(StructureEnum.BARRACKS);
//            Collection<Site> allyBarracksSites = allyBarracksSitesById.values();
//            Collection<Site> allyKnightBarracksSites = StructuresUtils.getKnightBarracksSites(allyBarracksSites);
//            Collection<Site> allySites = new ArrayList<>();
//            allySites.addAll(allyMineSites);
//            allySites.addAll(allyTowerSites);
//            allySites.addAll(allyBarracksSites);
//            
//            Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ENEMY);
//            Map<Integer, Site> enemyMineSitesById = enemySitesByIdAndStructure.get(StructureEnum.MINE);
//            Collection<Site> enemyMineSites = enemyMineSitesById.values();
//            Map<Integer, Site> enemyTowerSitesById = enemySitesByIdAndStructure.get(StructureEnum.TOWER);
//            Collection<Site> enemyTowerSites = enemyTowerSitesById.values();
//            int enemyTowersNumber = enemyTowerSites.size();
//            Map<Integer, Site> enemyBarracksSitesById = enemySitesByIdAndStructure.get(StructureEnum.BARRACKS);
//            Collection<Site> enemyBarracksSites = enemyBarracksSitesById.values();
//            Collection<Site> enemyKnightBarracksSites = StructuresUtils.getKnightBarracksSites(enemyBarracksSites);
//            Collection<Site> enemySites = new ArrayList<>();
//            enemySites.addAll(enemyMineSites);
//            enemySites.addAll(enemyTowerSites);
//            enemySites.addAll(enemyBarracksSites);
//
//            Collection<Site> allSites = new ArrayList<>();
//            allSites.addAll(emptySites);
//            allSites.addAll(allySites);
//            allSites.addAll(enemySites);
//            
//            Collection<Site> allyAndEmptySites = new ArrayList<>();
//            allyAndEmptySites.addAll(emptySites);
//            allyAndEmptySites.addAll(allySites);
//            Collection<Site> enemyAndEmptySites = new ArrayList<>();
//            enemyAndEmptySites.addAll(emptySites);
//            enemyAndEmptySites.addAll(enemySites);
//            Collection<Site> allyMineAndNotTrainingBarracksAndTowerSites = StructuresUtils.getMineAndNotTrainingBarracksAndTowerSites(allySites);
//            Collection<Site> emptyAndEnemyMineAndNotInTraingBarracksSites = StructuresUtils.getEmptyAndMineAndNotTrainingBarracks(enemyAndEmptySites);
//            
//            // Update the remaining gold in each known Site
//            StructuresUtils.updateRemaingGoldBySiteId(remainingGoldBySiteId, allSites);
//
//            /* --- Units --- */
//            // Get the Units thanks to the turn input
//            int numUnits = in.nextInt();
//            Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = InputUtils.getUnitsByTypeAndOwnerFromTurnInput(in, numUnits);
//            
//            Map<UnitEnum, List<Unit>> allyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ALLY);
//            Collection<Unit> allyGiants = allyUnitsByType.get(UnitEnum.GIANT);
//            Unit allyQueen = UnitsUtils.getQueen(allyUnitsByType);
//            Coordinates allyQueenCoordinates = allyQueen.getCoordinates();
//            int allyQueenHealth = allyQueen.getHealth();
//                        
//            Map<UnitEnum, List<Unit>> enemyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ENEMY);
//            Collection<Unit> enemyKnights = enemyUnitsByType.get(UnitEnum.KNIGHT);
//            int enemyKnightsNumber = enemyKnights.size();
//            Collection<Unit> enemyGiants = enemyUnitsByType.get(UnitEnum.GIANT);
//            
//            /* --- Initialize start of game static parameters --- */
//            if (startingQueenHealth == null) {
//            	startingQueenHealth = allyQueenHealth;
//            }
//            if (startingAllyQueenCoordinates == null) {
//            	startingAllyQueenCoordinates = allyQueenCoordinates;
//            }
//            
//            int minAllyFirstMines;
//            if (startingQueenHealth < 50) {
//            	minAllyFirstMines = 1;
//            } else {
//            	minAllyFirstMines = 2;            	
//            }
//            
//            if (!isTwoFirstMinesBuild) {
//            	isTwoFirstMinesBuild = allyMineSites.size() == minAllyFirstMines;
//            }
//            
//            /* --- Possible Site to MOVE or to BUILD -- */
//            int targetedSiteId;
//            Site nearestEmptySite;
//            Site nearestSiteToBuildAMine;
//            if (!isFirstKnightBarracksBuilt && isFirstBuildDone) {
//            	nearestEmptySite = SitesUtils.getNearestSiteFromCoordinatesInForwardDirection(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, startingAllyQueenCoordinates);              		
//            	nearestSiteToBuildAMine = StructuresUtils.getNearestSiteFromCoordinatesToBuildAMineInForwardDirection(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, remainingGoldBySiteId, enemyKnightBarracksSites, startingAllyQueenCoordinates);
//            } else {
//            	if (towersBuilt == 0 && startingQueenHealth >= 50) {
//            		nearestEmptySite = StructuresUtils.getFirstSiteToBuildTowerInCorner(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, startingAllyQueenCoordinates);
//            	} else if (towersBuilt <= 3) {
//                	nearestEmptySite = StructuresUtils.getNearestSiteToBuildTowerInCorner(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, startingAllyQueenCoordinates);
//            	} else {
//            		nearestEmptySite = SitesUtils.getNearestSiteFromCoordinates(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates);            	            		
//            	}
//            	nearestSiteToBuildAMine = StructuresUtils.getNearestSiteFromCoordinatesToBuildAMine(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, remainingGoldBySiteId, enemyKnightBarracksSites);
//            }
//            
//            Site nearestSiteToBuildATowerWhenRunningAway;
//        	if (isFirstKnightBarracksBuilt && towersBuilt == 0  && startingQueenHealth >= 50) {
//        		nearestSiteToBuildATowerWhenRunningAway = StructuresUtils.getFirstSiteToBuildTowerInCorner(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, startingAllyQueenCoordinates);
//        	} else if (isFirstKnightBarracksBuilt && towersBuilt <= 3) {
//            	nearestSiteToBuildATowerWhenRunningAway = StructuresUtils.getNearestSiteToBuildTowerInCorner(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates, startingAllyQueenCoordinates);
//        	} else {
//                nearestSiteToBuildATowerWhenRunningAway = SitesUtils.getNearestSiteFromCoordinates(emptyAndEnemyMineAndNotInTraingBarracksSites, allyQueenCoordinates);
//        	}
//
//            Site nearestAllyTowerSiteWithNotSufficientLife = SitesUtils.getNearestSiteFromCoordinates(StructuresUtils.getAllyTowerSitesWithNotSufficientLife(allyTowerSites), allyQueenCoordinates);
//            Site nearestAllySiteNotInTraining = SitesUtils.getNearestSiteFromCoordinates(allyMineAndNotTrainingBarracksAndTowerSites, allyQueenCoordinates);
//            
//            /* --- Booleans that could be use to choose what to do during this turn --- */
//            boolean isTouchingAMineToImprove = false;
//            boolean isTouchingATowerToImprove = false;
//            if (touchedSite != -1) {
//            	if (allyMineSitesById.get(touchedSite) != null
//            			&& UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(allyQueenCoordinates, enemyUnitsByType, SAFE_DISTANCE)
//            			&& StructuresUtils.isMineNotInFullProduction(allyMineSitesById.get(touchedSite).getStructure())) {
//            		isTouchingAMineToImprove = true;
//            	}
//        		if (allyTowerSitesById.get(touchedSite) != null
//            			&& StructuresUtils.isTowerLifeNotSufficient(allyTowerSitesById.get(touchedSite).getStructure())) {
//            		isTouchingATowerToImprove = true;
//            	}
//            }
//            
//            
//            /* 1) First turn action is to MOVE or BUILD. Generally, if ally QUEEN is low life, adopt a safest strategy.
//            *		a) MOVE to a safe place when the ally QUEEN is in danger.
//            *		   Can BUILD TOWER on the way to go.
//            *		b) else if touching a MINE I owned not in full production, improve it
//            *		c) else if touching a TOWER I owned not with full range, improve it
//            *		d) else if MOVE to the chosen Site and BUILD a MINE until 2 TOWER are built
//            *		e) else if MOVE to the chosen Site and BUILD a MINE until MIN_ALLY_GOLD_PRODUCTION is reached
//            *		f) else if MOVE to the nearest empty Site and BUILD an only one KNIGHT BARRACKS
//            *		g) else if MOVE to the chosen Site and BUILD an only one KNIGHT BARRACKS
//            *		h) else if MOVE to the nearest empty Site and BUILD a TOWER until MIN_ALLY_TOWERS_NUMBER is reached
//            *		i) else if MOVE to the nearest empty Site and BUILD an only one GIANT BARRACKS
//            *		j) else if MOVE to the chosen Site and BUILD a MINE until MAX_ALLY_GOLD_PRODUCTION is reached
//            *		k) else if MOVE to the nearest empty Site and BUILD a TOWER until MAX_ALLY_TOWERS_NUMBER is reached
//            *		l) else if MOVE to the chosen Site and BUILD a MINE
//            *		m) else if MOVE to the nearest ally TOWER with not enough life points
//            *		n) else if MOVE to the nearest empty Site and BUILD a TOWER
//            *		o) else MOVE to a safe place
//            */
//            Coordinates coordinatesToGo;
//            if (TurnStrategyUtils.isRunAwayStrategyOk(allyQueenHealth, allyQueenCoordinates, enemyUnitsByType, enemyTowerSites, emptySitesNumber, enemyKnightsNumber, SAFE_DISTANCE, enemyKnightBarracksSites)
//            		&& towersBuilt > 0) {
//            	System.err.println("Strategy a)");
//            	Coordinates safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, enemyKnights, allyQueenCoordinates); 
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, safestCoordinates, allSites);
//            	if (TurnStrategyUtils.isBuildTowerWhenRunningAwayStrategyOk(allyQueenCoordinates, safestCoordinates, nearestSiteToBuildATowerWhenRunningAway, enemyGiants)) {
//            		if (touchedSite == nearestSiteToBuildATowerWhenRunningAway.getId()) {
//            			towersBuilt++;
//                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
//            		} else {
//            			safestCoordinates = nearestSiteToBuildATowerWhenRunningAway.getCoordinates();
//            			SystemOutUtils.printMoveAction(safestCoordinates);            			
//            		}
//            	} else {
//            		SystemOutUtils.printMoveAction(coordinatesToGo);
//            	}
//            } else if (isTouchingAMineToImprove) {
//            	System.err.println("Strategy b)");
//        		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
//        	} else if (isTouchingATowerToImprove) {
//            	System.err.println("Strategy c)");
//            	SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
//        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, MAX_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)
//        			&& !isTwoFirstMinesBuild) {
//            	System.err.println("Strategy d)");
//        		targetedSiteId = nearestSiteToBuildAMine.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates(), allSites);
//        		if (touchedSite != targetedSiteId) {
//        			SystemOutUtils.printMoveAction(coordinatesToGo);
//        		} else {
//        			if (!isFirstBuildDone) {
//        				isFirstBuildDone = true;
//        			}
//        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//        		}   
//        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, MIN_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy e)");
//        		targetedSiteId = nearestSiteToBuildAMine.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates(), allSites);
//        		if (touchedSite != targetedSiteId) {
//        			SystemOutUtils.printMoveAction(coordinatesToGo);
//        		} else {
//        			if (!isFirstBuildDone) {
//        				isFirstBuildDone = true;
//        			}
//        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//        		}   
//            } else if (TurnStrategyUtils.isKnightBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyKnightBarracksSites, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy f)");
//            	targetedSiteId = nearestEmptySite.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestEmptySite.getCoordinates(), allSites);
//            	if (touchedSite != targetedSiteId) {
//                	SystemOutUtils.printMoveAction(coordinatesToGo);
//            	} else {
//            		if (!isFirstKnightBarracksBuilt) {
//            			isFirstKnightBarracksBuilt = true;
//            		}
//            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
//            	}
//            } else if (TurnStrategyUtils.isKnightBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestAllySiteNotInTraining, allyKnightBarracksSites, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy g)");
//            	targetedSiteId = nearestAllySiteNotInTraining.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestAllySiteNotInTraining.getCoordinates(), allSites);
//            	if (touchedSite != targetedSiteId) {
//                	SystemOutUtils.printMoveAction(coordinatesToGo);
//            	} else {
//            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
//            	}
//            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, MIN_ALLY_TOWERS_NUMBER, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy h)");
//        		targetedSiteId = nearestEmptySite.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestEmptySite.getCoordinates(), allSites);
//        		if (touchedSite != targetedSiteId) {
//        			SystemOutUtils.printMoveAction(coordinatesToGo);
//        		} else {
//        			towersBuilt++;
//        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
//        		}   
//            } else if (TurnStrategyUtils.isGiantBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, enemyTowersNumber, allyBarracksSites, enemyUnitsByType, enemyTowerSites, ENEMY_TOWERS_NUMBER_THRESHOLD, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites, allyMineSites)) {
//            	System.err.println("Strategy i)");
//            	targetedSiteId = nearestEmptySite.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestEmptySite.getCoordinates(), allSites);
//            	if (touchedSite != targetedSiteId) {
//                	SystemOutUtils.printMoveAction(coordinatesToGo);
//            	} else {
//            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.GIANT);
//            	}
//        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, MAX_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy j)");
//        		targetedSiteId = nearestSiteToBuildAMine.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates(), allSites);
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(coordinatesToGo);
//    			} else {
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//    			}        			
//            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, MAX_ALLY_TOWERS_NUMBER, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy k)");
//    			targetedSiteId = nearestEmptySite.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestEmptySite.getCoordinates(), allSites);
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(coordinatesToGo);
//    			} else {
//        			towersBuilt++;
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
//    			}        			
//        	} else if (nearestSiteToBuildAMine != null && GameBoardUtils.isItSafeAtCoordinates(nearestSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy l)");
//        		targetedSiteId = nearestSiteToBuildAMine.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates(), allSites);
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(coordinatesToGo);
//    			} else {
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//    			}  
//        	} else if (nearestAllyTowerSiteWithNotSufficientLife != null 
//        			&& (nearestSiteToBuildAMine == null || MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestAllyTowerSiteWithNotSufficientLife.getCoordinates()) < MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates()))
//        			&& (nearestEmptySite == null || MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestAllyTowerSiteWithNotSufficientLife.getCoordinates()) < MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestEmptySite.getCoordinates()))) {
//            	System.err.println("Strategy m)");
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestAllyTowerSiteWithNotSufficientLife.getCoordinates(), allSites);
//        		SystemOutUtils.printMoveAction(coordinatesToGo); 
//            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, Integer.MAX_VALUE, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	System.err.println("Strategy n)");
//    			targetedSiteId = nearestEmptySite.getId();
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, nearestEmptySite.getCoordinates(), allSites);
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(coordinatesToGo);
//    			} else {
//        			towersBuilt++;
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
//    			} 
//            } else {
//            	System.err.println("Strategy o)");
//            	Coordinates safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, enemyKnights, allyQueenCoordinates);
//        		coordinatesToGo = GameBoardUtils.getTargetCoordinatesAvoidingSitesCollisions(allyQueenCoordinates, safestCoordinates, allSites);
//            	SystemOutUtils.printMoveAction(coordinatesToGo);
//        	}
//
//            /* 2) Second turn action is to TRAIN.
//            *		a) TRAIN a GIANT id needed
//            *		b) else TRAIN a KNIGHT
//            */
//            Site siteToTrain = null;
//            if (TurnStrategyUtils.isGiantTrainStrategyOk(enemyTowersNumber, ENEMY_TOWERS_NUMBER_THRESHOLD + 2, allyMineSites, allyGiants, allyBarracksSites)) {
//            	siteToTrain = StructuresUtils.getGiantSiteToTrain(allyBarracksSites);
//            } else if (StructuresUtils.isAtLeastOneKnightBarracks(allyBarracksSites)) {
//            	siteToTrain = StructuresUtils.getAKnightSiteToTrain(allyBarracksSites);            	
//            }
//            
//            if (siteToTrain != null) {
//                SystemOutUtils.printTrainAction(siteToTrain.getId());
//            } else {
//                SystemOutUtils.printTrainAction(-1);
//            }
//        }
//    }
//
//}
//
//class Coordinates {
//	
//    private int x;
//    private int y;
//
//    public Coordinates(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    public int getX() {
//        return this.x;
//    }
//
//    public int getY() {
//        return this.y;
//    }
//    
//}
//
//class Site {
//	
//	private int id;
//    private Coordinates coordinates;
//    private int radius;
//    private Structure structure;
//    
//	public Site(int id, Coordinates coordinates, int radius) {
//		this.id = id;
//		this.coordinates = coordinates;
//		this.radius = radius;
//	}
//	
//	public int getId() {
//		return id;
//	}
//	
//	public Coordinates getCoordinates() {
//		return coordinates;
//	}
//	
//	public int getRadius() {
//		return radius;
//	}
//
//	public Structure getStructure() {
//		return structure;
//	}
//
//	public void setStructure(Structure structure) {
//		this.structure = structure;
//	}
//	
//	public boolean isEmpty() {
//		return this.structure.getParam1() == -1;
//	}
//
//}
//
//class Structure {
//	
//    // Gold left in a MINE (-1 if unknown or N/A)
//    private int mineGold;
//    
//    // Maximum MINE gold production (-1 if unknown or N/A)
//    private int maxMineProduction;
//    
//    // -1 : nothing is built
//    // 0 : MINE
//    // 1 : TOWER 
//    // 2 : BARRACKS
//    int structureTypeId;
//    
//    // -1 : nothing is built
//    // 0 : ALLY structure
//    // 1 : ENEMY structure
//    int owner;
//    
//    // if nothing is built = -1
//    // if MINE = current gold production (-1 if ENEMY MINE)
//    // if TOWER = life points
//    // if BARRACKS = turns left before TRAIN ending (0 if ready to TRAIN)
//    int param1;
//    
//    // if nothing or a MINE is built = -1
//    // if TOWER = range radius
//    // if BARRACKS = 0 if it produces KNIGHT
//    //            = 1 if it produces ARCHER
//    //            = 2 if it produces GIANT
//    int param2;
//
//	public Structure(int mineGold, int maxMineProduction, int structureType, int owner, int param1, int param2) {
//		this.mineGold = mineGold;
//		this.maxMineProduction = maxMineProduction;
//		this.structureTypeId = structureType;
//		this.owner = owner;
//		this.param1 = param1;
//		this.param2 = param2;
//	}
//
//	public int getMineGold() {
//		return mineGold;
//	}
//
//	public int getMaxMineProduction() {
//		return maxMineProduction;
//	}
//
//	public int getStructureTypeId() {
//		return structureTypeId;
//	}
//
//	public int getOwner() {
//		return owner;
//	}
//
//	public int getParam1() {
//		return param1;
//	}
//
//	public int getParam2() {
//		return param2;
//	}
//	
//	public boolean isOwnedByMe() {
//		return getOwner() == OwnerEnum.ALLY.getId();
//	}
//	
//	public boolean isMine() {
//		return this.getStructureTypeId() == StructureEnum.MINE.getId();
//	}
//	
//	public boolean isBarrack() {
//		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId();
//	}
//	
//	public boolean isKnightBarracks() {
//		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId()
//				&& this.getParam2() == UnitEnum.KNIGHT.getId();
//	}
//	
//	public boolean isArcherBarracks() {
//		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId()
//				&& this.getParam2() == UnitEnum.ARCHER.getId();
//	}
//	
//	public boolean isGiantBarracks() {
//		return this.getStructureTypeId() == StructureEnum.BARRACKS.getId()
//				&& this.getParam2() == UnitEnum.GIANT.getId();
//	}
//	
//	public boolean isTower() {
//		return this.getStructureTypeId() == StructureEnum.TOWER.getId();
//	}
//	
//	public boolean isBarracksInTraining() {
//		return this.isBarrack() && this.getParam1() > 0;
//	}
//}
//
//class Unit {
//	
//	private Coordinates coordinates;
//	
//    // 0 : ally
//    // 1 : enemy	
//	private int owner;
//	
//	// -1 : QUEEN
//	// 0 : KNIGHT
//	// 1 : ARCHER
//	// 2 : GIANT
//	private int unitType;
//	
//	private int health;
//
//	public Unit(Coordinates coordinates, int owner, int unitType, int health) {
//		super();
//		this.coordinates = coordinates;
//		this.owner = owner;
//		this.unitType = unitType;
//		this.health = health;
//	}
//
//	public Coordinates getCoordinates() {
//		return coordinates;
//	}
//
//	public int getOwner() {
//		return owner;
//	}
//
//	public int getUnitType() {
//		return unitType;
//	}
//
//	public int getHealth() {
//		return health;
//	}
// 
//}
//
//final class SitesUtils {
//	
//	private SitesUtils() {}
//	
//	/**
//	 * Get the nearest Site of the input Site collection from the input Coordinates.
//	 * 
//	 * @param sites
//	 * @param coordinates
//	 * @return Site
//	 */
//    public static Site getNearestSiteFromCoordinates(Collection<Site> sites, Coordinates coordinates) {
//        Site nearestSite = null;
//        double distanceToSite;
//        double distanceToNearestSite = Double.MAX_VALUE;
//        Coordinates siteCoordinates;
//        for (Site site : sites) {
//            siteCoordinates = site.getCoordinates();
//            distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(coordinates, siteCoordinates);
//            if (distanceToSite < distanceToNearestSite) {
//                distanceToNearestSite = distanceToSite;
//                nearestSite = site;
//            }
//        }
//        return nearestSite;
//    }
//    
//    /**
//     * Get the nearest Site that is towards the enemy camp.
//     * 
//     * @param sites
//     * @param coordinates
//     * @param startingAllyQueenCoordinates
//     * @return
//     */
//    public static Site getNearestSiteFromCoordinatesInForwardDirection(Collection<Site> sites, Coordinates coordinates, Coordinates startingAllyQueenCoordinates) {
//        boolean isStartingLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//        final int Y_GAP = 150;
//    	Site nearestSite = null;
//        double distanceToSite;
//        double distanceToNearestSite = Double.MAX_VALUE;
//        Coordinates siteCoordinates;
//        for (Site site : sites) {
//        	if ((isStartingLeftSide && (site.getCoordinates().getX() > startingAllyQueenCoordinates.getX()) 
//        			&& (site.getCoordinates().getY() < startingAllyQueenCoordinates.getY() + Y_GAP))
//        			|| (!isStartingLeftSide && (site.getCoordinates().getX() < startingAllyQueenCoordinates.getX()))
//        			&& (site.getCoordinates().getY() > startingAllyQueenCoordinates.getY() - Y_GAP)) {
//        		siteCoordinates = site.getCoordinates();
//        		distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(coordinates, siteCoordinates);
//        		if (distanceToSite < distanceToNearestSite) {
//        			distanceToNearestSite = distanceToSite;
//        			nearestSite = site;
//        		}        		
//        	}
//        }
//        return nearestSite;
//    }
//
//	/**
//	 * Get from the input Sites collection the average Coordinates.
//	 * 
//	 * @param sites
//	 * @return Coordinates
//	 */
//	public static Coordinates getAverageSiteCoordinates(Collection<Site> sites) {
//		if (sites.isEmpty()) {
//			return null;
//		}
//
//		int xCoordinateSum = 0;
//		int yCoordinateSum = 0;
//		Coordinates siteCoordinates;
//		for (Site site : sites) {
//			siteCoordinates = site.getCoordinates();
//			xCoordinateSum += siteCoordinates.getX();
//			yCoordinateSum += siteCoordinates.getY();
//		}
//
//		return new Coordinates(xCoordinateSum/sites.size(), yCoordinateSum/sites.size());
//	}
//	
//	public static boolean isReallyCloseToCoordinates(Coordinates allyQueenCoordinates, Coordinates coordinates) {
//		final int REALLY_CLOSE = 150;
//		return MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, coordinates) <= REALLY_CLOSE;
//	}
//	
//	/**
//	 * Get the Sites that are on the path to go from allyQueenCoordinates to targetCoordinates.
//	 * For each sites, check if the site is between allyQueenCoordinates to targetCoordinates
//	 * and if the line between allyQueenCoordinates and targetCoordinates cross the site area.
//	 * 
//	 * @param allyQueenCoordinates
//	 * @param targetCoordinates
//	 * @param sites
//	 * @return Collection<Site>
//	 */
//	public static Collection<Site> getSitesOnPath(Coordinates allyQueenCoordinates, Coordinates targetCoordinates, Collection<Site> sites) {
//		Collection<Site> sitesOnPath = new ArrayList<>();
//		for (Site site : sites) {
//			if (GameBoardUtils.isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(site.getCoordinates(), targetCoordinates, allyQueenCoordinates) 
//					&& MathUtils.isLineCrossingCircle(allyQueenCoordinates, targetCoordinates, site.getCoordinates(), site.getRadius())) {
//				sitesOnPath.add(site);
//			}
//		}
//		
//		return sitesOnPath;
//	}
//	
//	/**
//	 * Get the closest Coordinates that in on the path to go from allyQueenCoordinates to targetCoordinates.
//	 * 
//	 * @param allyQueenCoordinates
//	 * @param targetCoordinates
//	 * @param sites
//	 * @return Site
//	 */
//	public static Site getClosestSiteOnPath(Coordinates allyQueenCoordinates, Coordinates targetCoordinates, Collection<Site> sites) {
//		Collection<Site> sitesOnPath = getSitesOnPath(allyQueenCoordinates, targetCoordinates, sites);
////		for (Site site : sitesOnPath) {
////			System.err.println("Site on path ID : " + site.getId());
////		}
//		return getNearestSiteFromCoordinates(sitesOnPath, allyQueenCoordinates);
//	}
//    
//}
//
//final class StructuresUtils {
//	
//	private StructuresUtils() {}
//    
//	/**
//	 * Get the sum of the gold production of each MINE of the input collection.
//	 * 
//	 * @param sites
//	 * @return int
//	 */
//    public static int getGoldProduction(Collection<Site> sites) {
//    	int goldProduction = 0;
//    	for (Site site : sites) {
//    		if (site.getStructure().getStructureTypeId() == StructureEnum.MINE.getId()) {
//    			goldProduction += site.getStructure().getParam1();    			
//    		}
//    	}
//    	
//    	return goldProduction;
//    }
//    
//    public static boolean isMineNotInFullProduction(Structure structure) {
//    	if (structure.getStructureTypeId() != StructureEnum.MINE.getId()) {
//    		return false;
//    	}
//    	
//    	return structure.getParam1() < structure.getMaxMineProduction();
//    }
//    
//    /**
//     * Check if the input Structure is a TOWER and has enough life points.
//     * The life points considered as sufficient is defined in a constant.
//     * 
//     * @param structure
//     * @return boolean
//     */
//    public static boolean isTowerLifeNotSufficient(Structure structure) {
//    	if (structure.getStructureTypeId() != StructureEnum.TOWER.getId()) {
//    		return false;
//    	}
//    	
//    	final int SUFFICIENT_TOWER_LIFE = 650;
//    	return structure.getParam1() < SUFFICIENT_TOWER_LIFE;
//    }
//    
//    /**
//     * Get the ally TOWER Sites that do not have enough life points.
//     * 
//     * @param allyTowerSites
//     * @return
//     */
//    public static Collection<Site> getAllyTowerSitesWithNotSufficientLife(Collection<Site> allyTowerSites) {
//    	Collection<Site> allyTowerSitesWithNotSufficientLife = new ArrayList<>();
//    	for (Site allyTowerSite : allyTowerSites) {
//    		if (isTowerLifeNotSufficient(allyTowerSite.getStructure())) {
//    			allyTowerSitesWithNotSufficientLife.add(allyTowerSite);
//    		}
//    	}
//    	
//    	return allyTowerSitesWithNotSufficientLife;
//    }
//    
//    /**
//     * Check if the input Sites collection holds a KNIGHT BARRACKS.
//     * 
//     * @param sites
//     * @return boolean
//     */
//    public static boolean isAtLeastOneKnightBarracks(Collection<Site> sites) {
//    	for (Site site : sites) {
//    		if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
//    				&& site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()) {
//    			return true;
//    		}
//    	}
//    	
//    	return false;
//    }
//    
//    /**
//     * Check if the input Sites collection holds a GIANT BARRACKS.
//     * 
//     * @param sites
//     * @return boolean
//     */
//    public static boolean isAtLeastOneGiantBarracks(Collection<Site> sites) {
//    	for (Site site : sites) {
//    		if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
//    				&& site.getStructure().getParam2() == UnitEnum.GIANT.getId()) {
//    			return true;
//    		}
//    	}
//    	
//    	return false;
//    }
//    
//    /**
//     * Check if Coordinates can be reached by a certain number of towers.
//     * 
//     * @param coordinates
//     * @param towerSites
//     * @param towerNumberInRangeMax
//     * @return boolean
//     */
//    public static boolean isCoordinatesInRangeOfTowers(Coordinates coordinates, Collection<Site> towerSites, int towerNumberInRangeMax) {
//    	return getTowerSitesInRangeOfCoordinates(towerSites, coordinates).size() >= towerNumberInRangeMax;
//    }
//
//    /**
//     * Get the collection of TOWER Sites that can reach the input Coordinates.
//     * A TOWER can reach a coordinates if the distance between the TOWER and the coordinates is less or equal to the TOWER range.
//     * 
//     * @param towerSites
//     * @param coordinates
//     * @return Collection<Site>
//     */
//	public static Collection<Site> getTowerSitesInRangeOfCoordinates(Collection<Site> towerSites, Coordinates coordinates) {
//		Collection<Site> towerSitesInRange = new ArrayList<>();
//		for (Site towerSite : towerSites) {
//			if (MathUtils.getDistanceBetweenTwoCoordinates(coordinates, towerSite.getCoordinates()) <= towerSite.getStructure().getParam2()) {
//				towerSitesInRange.add(towerSite);
//			}
//		}
//		
//		return towerSitesInRange;
//	}
//	
//	/**
//	 * Get the nearest Site from the input Coordinates of the ally QUEEN in which a MINE can be built.
//	 * If there is no gold left in the Site, a MINE cannot be built.
//	 * remainingGoldBySiteId tells us if we know the remaining gold in Sites.
//	 * 
//	 * @param sites
//	 * @param myQueenCoordinates
//	 * @return Site
//	 */
//    public static Site getNearestSiteFromCoordinatesToBuildAMine(Collection<Site> sites, Coordinates myQueenCoordinates, 
//    		Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> enemyKnightBarrackSites) {    	
//    	double distanceToNearestSite = Double.MAX_VALUE;
//        Site nearestSite = null;
//        double distanceToSite;
//        Coordinates siteCoordinates;
//        int siteId;
//        Integer remainingGold;
//        for (Site site : sites) {
//        	siteId = site.getId();
//        	remainingGold = remainingGoldBySiteId.get(siteId);
//        	siteCoordinates = site.getCoordinates();
//        	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
//            if (!isSiteCloseToNearestEnemyKnightBarracksSite(site, enemyKnightBarrackSites) 
//            		&& ((remainingGold == null && site.getStructure().getMineGold() != 0)
//            		|| (remainingGold != null && remainingGold > 0))) {
//            	if (distanceToSite < distanceToNearestSite) {
//            		distanceToNearestSite = distanceToSite;
//            		nearestSite = site;
//            	}            	
//            }
//        }
//        return nearestSite;
//    }
//    
//    /**
//	 * Get the nearest Site from the input Coordinates of the ally QUEEN in which a MINE can be built.
//	 * Only Sites that are towards the enemy camp are considered.
//	 * If there is no gold left in the Site, a MINE cannot be built.
//	 * remainingGoldBySiteId tells us if we know the remaining gold in Sites.
//	 *
//     * @param sites
//     * @param myQueenCoordinates
//     * @param remainingGoldBySiteId
//     * @param enemyKnightBarrackSites
//     * @param startingAllyQueenCoordinates
//     * @return Site
//     */
//    public static Site getNearestSiteFromCoordinatesToBuildAMineInForwardDirection(Collection<Site> sites, Coordinates myQueenCoordinates, 
//    		Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> enemyKnightBarrackSites, Coordinates startingAllyQueenCoordinates) { 
//        boolean isStartingLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//        final int Y_GAP = 150;
//    	double distanceToNearestSite = Double.MAX_VALUE;
//        Site nearestSite = null;
//        double distanceToSite;
//        Coordinates siteCoordinates;
//        int siteId;
//        Integer remainingGold;
//        for (Site site : sites) {
//        	if ((isStartingLeftSide && (site.getCoordinates().getX() > startingAllyQueenCoordinates.getX()) 
//        			&& (site.getCoordinates().getY() < startingAllyQueenCoordinates.getY() + Y_GAP))
//        			|| (!isStartingLeftSide && (site.getCoordinates().getX() < startingAllyQueenCoordinates.getX()))
//        			&& (site.getCoordinates().getY() > startingAllyQueenCoordinates.getY() - Y_GAP)) {
//        		siteId = site.getId();
//        		remainingGold = remainingGoldBySiteId.get(siteId);
//        		siteCoordinates = site.getCoordinates();
//        		distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
//        		if (!isSiteCloseToNearestEnemyKnightBarracksSite(site, enemyKnightBarrackSites) 
//        				&& ((remainingGold == null && site.getStructure().getMineGold() != 0)
//        						|| (remainingGold != null && remainingGold > 0))) {
//        			if (distanceToSite < distanceToNearestSite) {
//        				distanceToNearestSite = distanceToSite;
//        				nearestSite = site;
//        			}            	
//        		}        		
//        	}
//        }
//        return nearestSite;
//    }
//    
//    /**
//     * Get the Site to BUILD the first TOWER.
//     * It would be the one that is the closest to the middle and in the "safe" game board corner.
//     * 
//     * @param sites
//     * @param allyQueenCoordinates
//     * @param startingAllyQueenCoordinates
//     * @return Site
//     */
//    public static Site getFirstSiteToBuildTowerInCorner(Collection<Site> sites, Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
//    	GameBoardQuarterEnum siteBoardGameQuarter;
//    	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//    	Site chosenSite = null;
//    	double distanceToSite;
//    	double distanceToChosenSite = Double.MAX_VALUE;
//    	Coordinates siteCoordinates;
//    	for (Site site : sites) {
//    		siteBoardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(site.getCoordinates(), new Coordinates(960, 500));
//    		if ((isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.BOTTOMLEFT))
//    				|| (!isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.TOPRIGHT))) {
//    			siteCoordinates = site.getCoordinates();
//    			distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(new Coordinates(960, 500), siteCoordinates);
//    			if (distanceToSite < distanceToChosenSite) {
//    				distanceToChosenSite = distanceToSite;
//    				chosenSite = site;
//    			}        		
//    		}
//    	}
//    	return chosenSite;
//    }
//    
//    /**
//     * Get the nearest Site to BUILD a TOWER in the "safe" game board corner.
//     * 
//     * @param sites
//     * @param allyQueenCoordinates
//     * @param startingAllyQueenCoordinates
//     * @return Site
//     */
//    public static Site getNearestSiteToBuildTowerInCorner(Collection<Site> sites, Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
//		GameBoardQuarterEnum siteBoardGameQuarter;
//        boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//    	Site nearestSite = null;
//        double distanceToSite;
//        double distanceToNearestSite = Double.MAX_VALUE;
//        Coordinates siteCoordinates;
//        for (Site site : sites) {
//        	siteBoardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(site.getCoordinates(), new Coordinates(960, 500));
//        	if ((isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.BOTTOMLEFT))
//        			|| (!isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.TOPRIGHT))) {
//        		siteCoordinates = site.getCoordinates();
//        		distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, siteCoordinates);
//        		if (distanceToSite < distanceToNearestSite) {
//        			distanceToNearestSite = distanceToSite;
//        			nearestSite = site;
//        		}        		
//        	}
//        }
//        return nearestSite;
//    }
//    
//    /**
//     * Check if site is close (distance <= SAFE_DISTANCE) to the nearest enemy KNIGHT BARRACKS.
//     * 
//     * @param site
//     * @param enemyKnightBarracksSites
//     * @return boolean
//     */
//    public static boolean isSiteCloseToNearestEnemyKnightBarracksSite(Site site, Collection<Site> enemyKnightBarracksSites) {
//    	if (site == null || enemyKnightBarracksSites.isEmpty()) {
//    		return false;
//    	}
//    	
//    	Site nearestEnemyKnightBarrackSite = SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarracksSites, site.getCoordinates());
//    	return isSiteCloseToAnotherSite(site, nearestEnemyKnightBarrackSite);
//    }
//
//    /**
//     * Check if the distance between 2 Sites is less or equal to constant SAFE_DISTANCE.
//     * 
//     * @param site1
//     * @param site2
//     * @return boolean
//     */
//    public static boolean isSiteCloseToAnotherSite(Site site1, Site site2) {
//    	if (site1 == null || site2 == null) {
//    		return false;
//    	}
//    	
//    	final double SAFE_DISTANCE = 300;
//    	return MathUtils.getDistanceBetweenTwoCoordinates(site1.getCoordinates(), site2.getCoordinates()) <= SAFE_DISTANCE;
//    }
//    
//    /**
//     * Get the Sites from the input BARRACKS Sites that are KNIGHT BARRACKS.
//     * 
//     * @param barracksSites
//     * @return Collection<Site>
//     */
//    public static Collection<Site> getKnightBarracksSites(Collection<Site> barracksSites) {
//    	Collection<Site> knightBarracksSites = new ArrayList<>();
//    	for (Site barracksSite : barracksSites) {
//    		if (barracksSite.getStructure().isKnightBarracks()) {
//    			knightBarracksSites.add(barracksSite);
//    		}
//    	}
//    	return knightBarracksSites;
//    }
//    
//    /**
//     * Check if there is a enemy KNIGHT BARRACKS that is dangerous for my QUEEN.
//     * It means that my QUEEN should not be close (< SAFE_DISTANCE) to a training enemy KNIGHT BARRACKS.
//     * 
//     * @param allyQueenCoordinates
//     * @param enemyKnightBarracksSites
//     * @return boolean
//     */
//    public static boolean isEnemyKnightBarracksDangerous(Coordinates allyQueenCoordinates, Collection<Site> enemyKnightBarracksSites) {
//    	Site nearestEnemyKnightBarracksSite = SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarracksSites, allyQueenCoordinates);
//    	final double safeDistance = StructuresUtils.getSafeDistanceWithRespectToKnightBarracks(nearestEnemyKnightBarracksSite);
//    	if (nearestEnemyKnightBarracksSite == null) {
//    		return false;
//    	}
//    	
//    	boolean isNearestEnemyKnightBarracksSiteInTraining = nearestEnemyKnightBarracksSite.getStructure().isBarracksInTraining();
//    	double distanceFromNearestEnemyKnightBarracksSite = MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestEnemyKnightBarracksSite.getCoordinates());
//    	
//    	return isNearestEnemyKnightBarracksSiteInTraining && distanceFromNearestEnemyKnightBarracksSite < safeDistance;
//    }
//        
//    /**
//     * Get the first KNIGHT BARRACKS Site of the input Sites collection that can be TRAIN.
//     * 
//     * @param sites
//     * @return Site
//     */
//    public static Site getAKnightSiteToTrain(Collection<Site> sites) {
//        for (Site site : sites) {     
//        	if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
//        			&& site.getStructure().getParam2() == UnitEnum.KNIGHT.getId()
//        			&& site.getStructure().getParam1() == 0) {
//        		return site;
//        	}
//        }
//        return null;
//    }
//    
//    /**
//     * Get the first GIANT BARRACKS Site of the input Sites collection that can be TRAIN.
//     * 
//     * @param sites
//     * @return Site
//     */
//    public static Site getGiantSiteToTrain(Collection<Site> sites) {
//        for (Site site : sites) {     
//        	if (site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()
//        			&& site.getStructure().getParam2() == UnitEnum.GIANT.getId()
//        			&& site.getStructure().getParam1() == 0) {
//        		return site;
//        	}
//        }
//        return null;
//    }
//    
//    /**
//     * Update a map that lists siteId by remaining gold at each turn.
//     * At the first turn of the game, the map is empty.
//     * Each time a Site is sufficiently close to show its gold content, the Site is added or updated.
//     * 
//     * @param remainingGoldBySiteId
//     * @param sites
//     */
//    public static void updateRemaingGoldBySiteId(Map<Integer, Integer> remainingGoldBySiteId, Collection<Site> sites) {
//    	int siteId;
//    	int remainingGold;
//    	for (Site site : sites) {
//    		siteId = site.getId();
//    		remainingGold = site.getStructure().getMineGold();
//    		if (remainingGold > -1) {
//    			remainingGoldBySiteId.put(siteId, remainingGold);
//    		}
//    	}
//    }
//    
//    /**
//     * Get Sites that are empty, or a MINE or a not training BARRACKS.
//     * 
//     * @param sites
//     * @return Collection<Site>
//     */
//    public static Collection<Site> getEmptyAndMineAndNotTrainingBarracks(Collection<Site> sites) {
//    	Collection<Site> emptyAndMineAndNotTrainingBarracks = new ArrayList<>();
//    	Structure structure;
//    	for (Site site : sites) {
//    		structure = site.getStructure();
//    		if (site.isEmpty() || structure.isMine() || 
//    				(structure.isBarrack() && !structure.isBarracksInTraining())) {
//    			emptyAndMineAndNotTrainingBarracks.add(site);
//    		}
//    	}
//    	
//    	return emptyAndMineAndNotTrainingBarracks;
//    }
//    
//    /**
//     * Get Sites that are a MINE or a TOWER or a not training BARRACKS.
//     * 
//     * @param sites
//     * @return Collection<Site>
//     */
//    public static Collection<Site> getMineAndNotTrainingBarracksAndTowerSites(Collection<Site> sites) {
//    	Collection<Site> mineAndNotTrainingBarracksAndTowerSites = new ArrayList<>();
//    	Structure structure;
//    	for (Site site : sites) {
//    		structure = site.getStructure();
//    		if (structure.isMine() || structure.isTower() || 
//    				(structure.isBarrack() && !structure.isBarracksInTraining())) {
//    			mineAndNotTrainingBarracksAndTowerSites.add(site);
//    		}
//    	}
//    	
//    	return mineAndNotTrainingBarracksAndTowerSites;
//    }
//    
//    /**
//     * Find a safe TOWER Site. The algorithm is defined as followed :
//     * 	- for each ally TOWER Site, evaluate the number of ally TOWER (including itself) that cover this Site
//     * 	- put in a map the left most (or right most depending on startingAllyQueenCoordinates) for each number of covering ally TOWER
//     * 	- return the Site from the map that is the most covered
//     * 
//     * @param allyTowerSites
//     * @param startingAllyQueenCoordinates
//     * @return Site
//     */
//    public static Site getSafestTower(Collection<Site> allyTowerSites, Coordinates startingAllyQueenCoordinates) {
//    	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//    	Site safestTowerSite = null;
//    	int safestXCoordinate = isLeftSide ? 1920 : 0;
//    	for (Site allyTowerSite : allyTowerSites) {
//			if ((isLeftSide && allyTowerSite.getCoordinates().getX() < safestXCoordinate)
//					|| (!isLeftSide && allyTowerSite.getCoordinates().getX() > safestXCoordinate)) {
//				safestXCoordinate = allyTowerSite.getCoordinates().getX();
//				safestTowerSite = allyTowerSite;
//			}    			
//    	}
//    	
////    	Map<Integer, Site> safestTowersProtectedByTowers = new HashMap<>();
////    	int safestXCoordinate = isLeftSide ? 1920 : 0;
////    	int numberOfAllyTowersInRangeOfTower;
////    	for (Site allyTowerSite : allyTowerSites) {
////    		numberOfAllyTowersInRangeOfTower = StructuresUtils.getTowerSitesInRangeOfCoordinates(allyTowerSites, allyTowerSite.getCoordinates()).size();
////			if ((isLeftSide && allyTowerSite.getCoordinates().getX() < safestXCoordinate)
////					|| (!isLeftSide && allyTowerSite.getCoordinates().getX() > safestXCoordinate)) {
////				safestXCoordinate = allyTowerSite.getCoordinates().getX();
////    			safestTowersProtectedByTowers.put(numberOfAllyTowersInRangeOfTower, allyTowerSite);
////			}    			
////    	}
////    	
////    	int numberOfProtectedAllyTowerForSafestTower = -1;
////    	Site safestTower = null;
////    	for (Integer numberOfProtectedAllyTower : safestTowersProtectedByTowers.keySet()) {
////    		if (numberOfProtectedAllyTower > numberOfProtectedAllyTowerForSafestTower) {
////    			numberOfProtectedAllyTowerForSafestTower = numberOfProtectedAllyTower;
////    			safestTower = safestTowersProtectedByTowers.get(numberOfProtectedAllyTower);
////    		}
////    	}
//
//    	return safestTowerSite;
//    }
//    
//    /**
//     * Get a Coordinates to hide from enemies behind a TOWER.
//     * Choose a cardinal point (N, E, S or W) on the input TOWER that is opposite to the nearest enemy KNIGHT.
//     * 
//     * If there is no enemy, get the Coordinates that is just at the left side (or right side depending on startingAllyQueenCoordinates) of the input TOWER Site.
//     * 
//     * @param startingAllyQueenCoordinates
//     * @param towerSite
//     * @return Coordinates
//     */
//    public static Coordinates getCoordinatesBehindTower(Unit nearestEnemyKnight, Site towerSite, Coordinates startingAllyQueenCoordinates) {	
//    	if (towerSite == null) {
//    		return null;
//    	}
//    	
//    	if (nearestEnemyKnight != null) {
//    		int xCoordinate, yCoordinate;
//    		Coordinates towerCoordinates = towerSite.getCoordinates();
//    		Coordinates nearestEnemyKnightCoordinates = nearestEnemyKnight.getCoordinates();
//    		int towerXCoordinate = towerCoordinates.getX();
//    		int towerYCoordinate = towerCoordinates.getY();
//    		int towerRadius = towerSite.getRadius();
//    		GameBoardQuarterEnum boardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(nearestEnemyKnightCoordinates, towerCoordinates);
//    		switch (boardGameQuarter) {
//	    		case TOPLEFT: {
//	    			xCoordinate = towerXCoordinate + towerRadius;
//	    			yCoordinate = towerYCoordinate;
//	    			break;
//	    		}
//	    		case TOPRIGHT: {
//	    			xCoordinate = towerXCoordinate;
//	    			yCoordinate = towerYCoordinate + towerRadius;
//	    			break;
//	    		}
//	    		case BOTTOMRIGHT: {
//	    			xCoordinate = towerXCoordinate - towerRadius;
//	    			yCoordinate = towerYCoordinate;
//	    			break;
//	    		}
//	    		case BOTTOMLEFT: {
//	    			xCoordinate = towerXCoordinate;
//	    			yCoordinate = towerYCoordinate - towerRadius;
//	    			break;
//	    		}
//	    		// Should not happened
//	    		default: {
//	    			xCoordinate = towerXCoordinate;
//	    			yCoordinate = towerYCoordinate;
//	    		}
//    		}
//    		
//    		return new Coordinates(xCoordinate, yCoordinate);    		
//    	} else {
//        	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//        	Coordinates towerSiteCoordinates = towerSite.getCoordinates();
//        	int towerRadius = towerSite.getRadius();
//        	int xCoordinate = isLeftSide ? 
//        			towerSiteCoordinates.getX() - towerRadius : towerSiteCoordinates.getX() + towerRadius;
//        	
//        	return new Coordinates(xCoordinate, towerSiteCoordinates.getY());
//    	}
//    	
//    	
////    	Coordinates towerSiteCoordinates = towerSite.getCoordinates();
////    	int distanceBetweenQueenAndTower = (int) Math.round(MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, towerSiteCoordinates));
////    	int towerRadius = towerSite.getRadius();
////    	int xDifferenceBetweenQueenAndTower = towerSite.getCoordinates().getX() - allyQueenCoordinates.getX();
////    	int yDifferenceBetweenQueenAndTower = towerSite.getCoordinates().getY() - allyQueenCoordinates.getY();
////
////    	int deltaX = towerRadius * xDifferenceBetweenQueenAndTower / distanceBetweenQueenAndTower;
////    	int deltaY = towerRadius * yDifferenceBetweenQueenAndTower / distanceBetweenQueenAndTower;
////    	
////    	return new Coordinates(towerSiteCoordinates.getX() + deltaX, towerSiteCoordinates.getY() + deltaY);
//    }
//    
//    /**
//     * Get a Coordinates to hide from enemies behind a TOWER.
//     * Choose the coordinates on the input TOWER Site circle that is opposite to the nearest enemy KNIGHT.
//     * 
//     * If there is no enemy, get the Coordinates that is just at the left side (or right side depending on startingAllyQueenCoordinates) of the input TOWER Site.
//     * 
//     * @param nearestEnemyKnight
//     * @param towerSite
//     * @param startingAllyQueenCoordinates
//     * @return Coordinates
//     */
//    public static Coordinates getCoordinatesBehindTowerOppositeToNearestEnemyKnight(Unit nearestEnemyKnight, Site towerSite, Coordinates startingAllyQueenCoordinates) {	
//    	if (towerSite == null) {
//    		return null;
//    	}
//    	
//    	if (nearestEnemyKnight != null) {
//    		Coordinates towerCoordinates = towerSite.getCoordinates();
//    		Coordinates nearestEnemyKnightCoordinates = nearestEnemyKnight.getCoordinates();
//    		int towerRadius = towerSite.getRadius();
//        	int distanceBetweenNearestEnemyKnightAndTower = (int) Math.round(MathUtils.getDistanceBetweenTwoCoordinates(nearestEnemyKnightCoordinates, towerCoordinates));
//        	int xDifferenceBetweenNearestEnemyKnightAndTower = towerSite.getCoordinates().getX() - nearestEnemyKnightCoordinates.getX();
//        	int yDifferenceBetweenNearestEnemyKnightAndTower = towerSite.getCoordinates().getY() - nearestEnemyKnightCoordinates.getY();
//
//        	int deltaX = towerRadius * xDifferenceBetweenNearestEnemyKnightAndTower / distanceBetweenNearestEnemyKnightAndTower;
//        	int deltaY = towerRadius * yDifferenceBetweenNearestEnemyKnightAndTower / distanceBetweenNearestEnemyKnightAndTower;
//        	
//        	return new Coordinates(towerCoordinates.getX() + deltaX, towerCoordinates.getY() + deltaY); 		
//    	} else {
//        	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//        	Coordinates towerSiteCoordinates = towerSite.getCoordinates();
//        	int towerRadius = towerSite.getRadius();
//        	int xCoordinate = isLeftSide ? 
//        			towerSiteCoordinates.getX() - towerRadius : towerSiteCoordinates.getX() + towerRadius;
//        	
//        	return new Coordinates(xCoordinate, towerSiteCoordinates.getY());
//    	}
//    	
//    }
//    
//    /**
//     * Evaluate a safe distance with a KNIGHT BARRACKS.
//     * It depends on the turns that left to finish a TRAIN and on an arbitrary safe distance.
//     * 
//     * @param knightBarracksSite
//     * @return int
//     */
//    public static int getSafeDistanceWithRespectToKnightBarracks(Site knightBarracksSite) {
//    	final int TRAINING_KNIGHT_TURNS_NUMBER = 5;
//    	final int SAFE_DISTANCE_PER_REMAINING_TURN = 100;
//    	
//    	if (knightBarracksSite == null) {
//    		return 0;
//    	}
//    	
//    	int trainingTurnsRemaining = knightBarracksSite.getStructure().getParam1();
//    	if (trainingTurnsRemaining == 0) {
//    		return 0;
//    	} else {
//    		return (TRAINING_KNIGHT_TURNS_NUMBER + 1 - trainingTurnsRemaining) * SAFE_DISTANCE_PER_REMAINING_TURN;
//    	}
//    }
//	
//}
//
//final class TurnStrategyUtils {
//	
//    final static int LOW_HEALTH_QUEEN = 20;
//    final static int ENEMY_KNIGHTS_THRESHOLD = 7;
//		
//	private TurnStrategyUtils() {}
//	
//	/**
//     * Check if we have to MOVE to a safe place. Depends on :
//     *	- ally QUEEN health 
//     *	- ally QUEEN coordinates
//     *	- number of enemy KNIGHT
//     *
//	 * @param queenHealth
//	 * @param allyQueenCoordinates
//	 * @param enemyUnitsByType
//	 * @param enemyTowerSites
//	 * @param emptySitesNumber
//	 * @param enemyKnightsNumber
//	 * @return
//	 */
//	public static boolean isRunAwayStrategyOk(int queenHealth, Coordinates allyQueenCoordinates,
//			Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites,
//			int emptySitesNumber, int enemyKnightsNumber, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
//		return (queenHealth < LOW_HEALTH_QUEEN 
//        		&& !GameBoardUtils.isItSafeAtCoordinates(allyQueenCoordinates, enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites))
//				|| (queenHealth < 40 && queenHealth >= LOW_HEALTH_QUEEN && !UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(allyQueenCoordinates, enemyUnitsByType, 100))
//    			|| StructuresUtils.isEnemyKnightBarracksDangerous(allyQueenCoordinates, enemyKnightBarracksSites)
//				|| enemyKnightsNumber > ENEMY_KNIGHTS_THRESHOLD;
//	}
//	
//	/**
//	 * Check if it is possible to BUILD a TOWER when running away.
//	 * If an enemy GIANT is too close, do not BUILD a TOWER.
//	 * If nearestSiteToBuildATower is not so far, do it.
//	 * The distance "not so far" is defined by constants.
//	 * 
//	 * @param allyQueenCoordinates
//	 * @param nearestSiteToBuildATower
//	 * @param enemyGiants
//	 * @return boolean
//	 */
//	public static boolean isBuildTowerWhenRunningAwayStrategyOk(Coordinates allyQueenCoordinates, Coordinates safestCoordinates,
//			Site nearestSiteToBuildATower, Collection<Unit> enemyGiants) {
//		if (nearestSiteToBuildATower == null
//				|| UnitsUtils.isGiantCloseToCoordinates(enemyGiants, allyQueenCoordinates)) {
//			System.err.println("No Site to build Tower when running away");
//			return false;
//		}
//		
//		if (SitesUtils.isReallyCloseToCoordinates(allyQueenCoordinates, nearestSiteToBuildATower.getCoordinates())) {
//			return true;
//		}
//		
//		final int X_RANGE = 200;
//		final int Y_RANGE = 200;
//		
//		Coordinates nearestSiteToBuildATowerCoordinates = nearestSiteToBuildATower.getCoordinates();
//    	System.err.println("nearestSiteToBuildATowerCoordinates : (" + nearestSiteToBuildATowerCoordinates.getX() + ", " + nearestSiteToBuildATowerCoordinates.getY() + ")");
//		int xNearestSiteToBuildATowerCoordinates = nearestSiteToBuildATowerCoordinates.getX();
//		int yNearestSiteToBuildATowerCoordinates = nearestSiteToBuildATowerCoordinates.getY();
//		int xAllyQueenCoordinates = allyQueenCoordinates.getX();
//		int yAllyQueenCoordinates = allyQueenCoordinates.getY();
//		if (Math.abs(xNearestSiteToBuildATowerCoordinates - xAllyQueenCoordinates) <= X_RANGE
//				&& Math.abs(yNearestSiteToBuildATowerCoordinates - yAllyQueenCoordinates) <= Y_RANGE
//				&& GameBoardUtils.isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(nearestSiteToBuildATowerCoordinates, safestCoordinates, allyQueenCoordinates)) {
//			return true;
//		}
//		
//		return false;
//	}
//	
//	/**
//	 * Check if we choose to BUILD a MINE. Depends on :
//	 *	- ally QUEEN health
//     *	- current gold production
//     *	- gold production we want
//     *	- nearest empty Site where a MINE can be built (gold not depleted)
//     *	- enemy UNIT location (because enemy KNIGHT destroy MINE)
//     *	- enemy TOWER location
//     *
//	 * @param queenHealth
//	 * @param targetedSiteToBuildAMine
//	 * @param allyMineSites
//	 * @param goldProductionIWant
//	 * @param enemyUnitsByType
//	 * @param enemyTowerSites
//	 * @return boolean
//	 */
//	public static boolean isMineMoveOrBuildStrategyOk(int queenHealth, Site targetedSiteToBuildAMine, Collection<Site> allyMineSites, 
//			int goldProductionIWant, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
//		
//		if (targetedSiteToBuildAMine == null || StructuresUtils.getGoldProduction(allyMineSites) >= goldProductionIWant ) {
//			return false;
//		}
//		
//		if (queenHealth >= LOW_HEALTH_QUEEN) {
//			return UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, safeDistance)
//					&& !StructuresUtils.isCoordinatesInRangeOfTowers(targetedSiteToBuildAMine.getCoordinates(), enemyTowerSites, 2);			
//		} else {
//			return GameBoardUtils.isItSafeAtCoordinates(targetedSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
//		}
//	}
//
//	/**
//	 * Check if we choose to BUILD a TOWER. Depends on :
//	 *	- ally QUEEN health
//     *	- ally TOWER number
//     *	- number of TOWER we want
//     *	- nearest empty Site
//     *	- enemy UNIT location
//     *	- enemy TOWER location
//     *
//	 * @param queenHealth
//	 * @param nearestEmptySite
//	 * @param allyTowersNumber
//	 * @param allyTowersNumberIWant
//	 * @param enemyUnitsByType
//	 * @param enemyTowerSites
//	 * @return boolean
//	 */
//	public static boolean isTowerMoveOrBuildStrategyOk(int queenHealth, Site nearestEmptySite, int allyTowersNumber,
//			int allyTowersNumberIWant, Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, 
//			int safeDistance, Collection<Site> enemyKnightBarracksSites) {
//		
//		if (nearestEmptySite == null || allyTowersNumber >= allyTowersNumberIWant) {
//			return false;
//		}
//		
//		if (allyTowersNumber < 3) {
//			return true;
//		} else if (queenHealth >= LOW_HEALTH_QUEEN) {
//			return !StructuresUtils.isCoordinatesInRangeOfTowers(nearestEmptySite.getCoordinates(), enemyTowerSites, 2);			
//		} else {
//			return GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
//		}
//	}
//	
//	/**
//	 * Check if we choose to BUILD a KNIGHT BARRACKS. Depends on :
//	 *	- ally QUEEN health
//     *	- nearest Site
//     *	- ally KNIGHT BARRACKS Sites
//     *	- enemy UNIT location
//     *	- enemy TOWER location
//     *
//	 * @param queenHealth
//	 * @param nearestSite
//	 * @param allyBarracksSites
//	 * @param enemyUnitsByType
//	 * @param enemyTowerSites
//	 * @return boolean
//	 */
//	public static boolean isKnightBarracksMoveOrBuildStrategyOk(int queenHealth, Site nearestSite, Collection<Site> allyBarracksSites, 
//			Map<UnitEnum, List<Unit>> enemyUnitsByType, Collection<Site> enemyTowerSites, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
//		
//		if (nearestSite == null || !allyBarracksSites.isEmpty()) {
//			return false;
//		}
//		
//		if (queenHealth >= LOW_HEALTH_QUEEN) {
//			return !StructuresUtils.isCoordinatesInRangeOfTowers(nearestSite.getCoordinates(), enemyTowerSites, 2);			
//		} else {
//			return GameBoardUtils.isItSafeAtCoordinates(nearestSite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
//		}
//	}
//	
//	/**
//	 * Check if we choose to BUILD a GIANT BARRACKS. Depends on :
//	 *	- ally QUEEN health
//     *	- nearest empty Site
//     *	- enemy TOWER number -> there is a threshold
//     *	- ally GIANT BARRACKS Sites
//     *	- enemy UNIT location
//     *	- enemy TOWER location
//	 * 
//	 * @param queenHealth
//	 * @param nearestEmptySite
//	 * @param enemyTowersNumber
//	 * @param allyBarracksSites
//	 * @param enemyUnitsByType
//	 * @param enemyTowerSites
//	 * @param enemyTowersNumberThreshold
//	 * @return boolean
//	 */
//	public static boolean isGiantBarracksMoveOrBuildStrategyOk(int queenHealth, Site nearestEmptySite, int enemyTowersNumber,
//			Collection<Site> allyBarracksSites, Map<UnitEnum, List<Unit>> enemyUnitsByType, 
//			Collection<Site> enemyTowerSites, int enemyTowersNumberThreshold, int safeDistance, 
//			Collection<Site> enemyKnightBarracksSites, Collection<Site> enemyMineSites, Collection<Site> allyMineSites) {
//		
//		if (nearestEmptySite == null 
//				|| enemyTowersNumber < enemyTowersNumberThreshold
//				|| StructuresUtils.getGoldProduction(allyMineSites) < 10) {
//			return false;
//		}
//		
//		if (queenHealth >= LOW_HEALTH_QUEEN) {
//			return !StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)
//            		&& !StructuresUtils.isCoordinatesInRangeOfTowers(nearestEmptySite.getCoordinates(), enemyTowerSites, 2);			
//		} else {
//			return !StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)
//            		&& GameBoardUtils.isItSafeAtCoordinates(nearestEmptySite.getCoordinates(), enemyUnitsByType, enemyTowerSites, safeDistance, enemyKnightBarracksSites);
//		}
//	}
//	
//	/**
//	 * We have to train a GIANT if :
//	 * 	- there are more enemy TOWER than a given threshold or there is an enemy TOWER and we produce enough gold
//	 * 	- there is less than 2 ally GIANT
//	 * 	- there is at least 1 ally GIANT BARRACKS
//	 * 
//	 * @param enemyTowersNumber
//	 * @param enemyTowersNumberThreshold
//	 * @param allyMineSites
//	 * @param allyGiants
//	 * @param allyBarracksSites
//	 * @return boolean
//	 */
//	public static boolean isGiantTrainStrategyOk(int enemyTowersNumber, int enemyTowersNumberThreshold,
//			Collection<Site> allyMineSites, Collection<Unit> allyGiants, Collection<Site> allyBarracksSites) {
//		return (enemyTowersNumber > enemyTowersNumberThreshold ||
//        		(enemyTowersNumber > 1 && StructuresUtils.getGoldProduction(allyMineSites) >= 8))
//        		&& allyGiants.size() < 2
//        		&& StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites);
//	}
//
//}
//
//final class GameBoardUtils {
//	
//	private final static int X_LENGTH = 1920;
//	private final static int Y_LENGTH = 1000;
//	
//	/**
//	 * Check if the input Coordinates in the left half side of the game board area.
//	 * 
//	 * @param coordinates
//	 * @return boolean
//	 */
//	public static boolean isLeftSide(Coordinates coordinates) {
//		return coordinates.getX() < X_LENGTH/2;
//	}
//	
//	/**
//	 * Get the Coordinates evaluated to be the safest regarding my starting ally QUEEN Coordinates, the ally TOWER and the enemy KNIGHT.
//	 * 
//	 * @param startingAllyQueenCoordinates
//	 * @param allyTowerSites
//	 * @param allySites
//	 * @return Coordinates
//	 */
//	public static Coordinates getSafestCoordinates(Coordinates startingAllyQueenCoordinates, Collection<Site> allyTowerSites, Collection<Unit> enemyKnights, Coordinates allyQueenCoordinates) {
//		Coordinates safestCoordinates;
//    	System.err.println("---Get safest coordinates---");
//		if (allyTowerSites.size() >= 3) {
//    		Site safestAllyTower = StructuresUtils.getSafestTower(allyTowerSites, startingAllyQueenCoordinates);
//    		System.err.println("Safest TOWER ID : " + safestAllyTower.getId());
//    		
//    		Unit nearestEnemyKnight = UnitsUtils.getNearestUnit(safestAllyTower.getCoordinates(), enemyKnights);
//    		if (nearestEnemyKnight != null) {
//    			System.err.println("Nearest enemy knight X : " + nearestEnemyKnight.getCoordinates().getX());    			
//    			System.err.println("Nearest enemy knight Y : " + nearestEnemyKnight.getCoordinates().getY());    			
//    		}
//    		
//    		safestCoordinates = StructuresUtils.getCoordinatesBehindTowerOppositeToNearestEnemyKnight(nearestEnemyKnight, safestAllyTower, startingAllyQueenCoordinates);
//    		System.err.println("Safest X : " + safestCoordinates.getX());
//    		System.err.println("Safest Y : " + safestCoordinates.getY());
//    	} else {
//    		safestCoordinates = getSafestCoordinatesFromStartingAllyQueen(startingAllyQueenCoordinates);
//    	}
//    	
//    	return safestCoordinates;
//	}
//	
//	/**
//	 * Get the Coordinates evaluated to be the safest regarding the starting ally QUEEN Coordinates.
//	 * If we start at left side, the safest Coordinates is the bottom left corner.
//	 * If we start at right side, the safest Coordinates is the top right corner.
//	 * 
//	 * @param startingAllyQueenCoordinates
//	 * @return Coordinates
//	 */
//	public static Coordinates getSafestCoordinatesFromStartingAllyQueen(Coordinates startingAllyQueenCoordinates) {
//		if (isLeftSide(startingAllyQueenCoordinates)) {
//			return new Coordinates(0, Y_LENGTH);
//		} else {
//			return new Coordinates(X_LENGTH, 0);
//		}
//	}
//	
//	/**
//	 * Check if the input Coordinates is considered as safe.
//	 * It depends on enemy KNIGHT, enemy TOWER and KNIGHT BARRACKS.
//	 * 
//	 * @param coordinates
//	 * @param enemyUnitsByType
//	 * @param enemyTowerSites
//	 * @return boolean
//	 */
//    public static boolean isItSafeAtCoordinates(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType, 
//    		Collection<Site> enemyTowerSites, int safeDistance, Collection<Site> enemyKnightBarracksSites) {
//    	return UnitsUtils.isItSafeAtCoordinatesRegardingEnemyKnights(coordinates, enemyUnitsByType, safeDistance)
//    			&& !StructuresUtils.isCoordinatesInRangeOfTowers(coordinates, enemyTowerSites, 1)
//    			&& !StructuresUtils.isEnemyKnightBarracksDangerous(coordinates, enemyKnightBarracksSites);
//    }
//    
//    /**
//     * a is the Coordinates to evaluate.
//     * b is the Coordinates to go.
//     * c is the QUEEN Coordinates.
//     * 
//     * @param a
//     * @param b
//     * @param c
//     * @return boolean
//     */
//    public static boolean isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(Coordinates a, Coordinates b, Coordinates c) {
//    	int xa = a.getX();
//    	int ya = a.getY();
//    	int xb = b.getX();
//    	int yb = b.getY();
//    	int xc = c.getX();
//    	int yc = c.getY();
//    	
//    	boolean isXcLessThanXb = xc < xb;
//    	boolean isYcLessThanYb = yc < yb;
//    	boolean isXaLessThanXb = xa < xb;
//    	boolean isYaLessThanYb = ya < yb;
//    	boolean isXcLessThanXa = xc < xa;
//    	boolean isYcLessThanYa = yc < ya;
//    	
//    	if (isXcLessThanXb && isYcLessThanYb) {
//    		return isXaLessThanXb && isYaLessThanYb && isXcLessThanXa && isYcLessThanYa;
//    	} else if (isXcLessThanXb && !isYcLessThanYb) {
//    		return isXaLessThanXb && !isYaLessThanYb && isXcLessThanXa && !isYcLessThanYa;
//    	} else if (!isXcLessThanXb && isYcLessThanYb) {
//    		return !isXaLessThanXb && isYaLessThanYb && !isXcLessThanXa && isYcLessThanYa;
//    	} else {
//    		return !isXaLessThanXb && !isYaLessThanYb && !isXcLessThanXa && !isYcLessThanYa;
//    	}
//    }
//    
//    /**
//     * Says in which cardinal corner is the coordinates with respect to the referenceCoordinates.
//     * 
//     * @param coordinates
//     * @param referenceCoordinates
//     * @return GameBoardQuarterEnum
//     */
//    public static GameBoardQuarterEnum getQuarterOfCoordinatesWithRespectToAnotherCoordinates(Coordinates coordinates, Coordinates referenceCoordinates) {
//    	int xCoordinate = coordinates.getX();  
//    	int yCoordinate = coordinates.getY();
//    	int xReferenceCoordinate = referenceCoordinates.getX();
//    	int yReferenceCoordinate = referenceCoordinates.getY();
//    	if (xCoordinate < xReferenceCoordinate && yCoordinate < yReferenceCoordinate) {
//    		return GameBoardQuarterEnum.TOPLEFT;
//    	} else if (xCoordinate < xReferenceCoordinate && yCoordinate >= yReferenceCoordinate) {
//    		return GameBoardQuarterEnum.BOTTOMLEFT;
//    	} else if (xCoordinate >= xReferenceCoordinate && yCoordinate < yReferenceCoordinate) {
//    		return GameBoardQuarterEnum.TOPRIGHT;
//    	} else {
//    		return GameBoardQuarterEnum.BOTTOMRIGHT;
//    	}
//    }
//    
//    /**
//     * Find the best Coordinates that avoid a direct collision with the input siteToAvoid.
//     * 
//     * @param allyQueenCoordinates
//     * @param targetCoordinates
//     * @param siteToAvoid
//     * @return Coordinates
//     */
//    public static Coordinates getCoordinatesToAvoidCollisionWithSite(Coordinates allyQueenCoordinates, Coordinates targetCoordinates, Site siteToAvoid) {
//    	Coordinates coordinates = null;
//    	double intersectionDistance;
//    	double smallestIntersectionDistance = Double.MAX_VALUE;
//    	Coordinates siteCoordinates = siteToAvoid.getCoordinates();
//    	Coordinates closestCoordinatesOfMoveSegmentFromSiteCenter = MathUtils.getClosestCoordinatesOfLineFromPoint(allyQueenCoordinates, targetCoordinates, siteCoordinates);
//    	//System.err.println("closestCoordinatesOfMoveSegmentFromSiteCenter : (" + closestCoordinatesOfMoveSegmentFromSiteCenter.getX() + ", " + closestCoordinatesOfMoveSegmentFromSiteCenter.getY() + ")");
//    	List<Coordinates> intersectionsWithCircle = MathUtils.getIntersectionsOfLineWithCircle(siteCoordinates, closestCoordinatesOfMoveSegmentFromSiteCenter, siteCoordinates, siteToAvoid.getRadius());
//    	for (Coordinates intersection : intersectionsWithCircle) {
//        	//System.err.println("Intersection coordinates : (" + intersection.getX() + ", " + intersection.getY() + ")");
//    		intersectionDistance = MathUtils.getDistanceBetweenTwoCoordinates(intersection, closestCoordinatesOfMoveSegmentFromSiteCenter);
//    		if (intersectionDistance < smallestIntersectionDistance) {
//    			smallestIntersectionDistance = intersectionDistance;
//    			coordinates = intersection;
//    		}
//    	}
//    	return coordinates;
//    }
//    
//    /**
//     * Find the best Coordinates on the path to go from allyQueenCoordinates to targetCoordinates that avoid direct collisions with Sites :
//     * 	- find the closest Site that is on the path (if any, go straight forward)
//     * 	- then evaluate the Coordinates to go
//     * 
//     * @param allyQueenCoordinates
//     * @param targetCoordinates
//     * @param sites
//     * @return Coordinates
//     */
//    public static Coordinates getTargetCoordinatesAvoidingSitesCollisions(Coordinates allyQueenCoordinates, Coordinates targetCoordinates, Collection<Site> sites) {
//    	System.err.println("Coordinates we want to go : (" + targetCoordinates.getX() + ", " + targetCoordinates.getY() + ")");
//    	
//    	Coordinates coordinatesToGo;
//    	Site closestCollidingSite = SitesUtils.getClosestSiteOnPath(allyQueenCoordinates, targetCoordinates, sites);
//    	if (closestCollidingSite == null) {
//    		coordinatesToGo = targetCoordinates;
//    	} else {
//    		System.err.println("Closest Site on path : " + closestCollidingSite.getId());
//    		coordinatesToGo = getCoordinatesToAvoidCollisionWithSite(allyQueenCoordinates, targetCoordinates, closestCollidingSite);
//    	}
//    	
//    	System.err.println("Coordinates to go : (" + coordinatesToGo.getX() + ", " + coordinatesToGo.getY() + ")");
//    	return coordinatesToGo;
//    }
//    	
//}
//
//final class MathUtils {
//	
//	private MathUtils() {}
//	
//	public static double getDistanceBetweenTwoCoordinates(Coordinates a, Coordinates b) {
//		int xa = a.getX();
//		int ya = a.getY();
//		int xb = b.getX();
//		int yb = b.getY();
//		
//		return Math.sqrt(Math.pow(xa - xb, 2) + Math.pow(ya - yb, 2));
//	}
//
//	public static Coordinates getMiddleCoordinatesOfTwoCoordinates(Coordinates a, Coordinates b) {
//		int xa = a.getX();
//		int ya = a.getY();
//		int xb = b.getX();
//		int yb = b.getY();
//		
//		return new Coordinates(Math.abs(xb - xa), Math.abs(yb - ya));
//	}
//	
//	/**
//	 * Calculate the distance between the input point and the closest point of the line drawn by firstLinePoint and secondLinePoint.
//	 * 
//	 * @param firstLinePoint
//	 * @param secondLinePoint
//	 * @param point
//	 * @return double
//	 */
//	public static double getDistanceBetweenLineAndPoint(Coordinates firstLinePoint, Coordinates secondLinePoint, Coordinates point) {
//		int xFirstLinePoint = firstLinePoint.getX();
//		int yFirstLinePoint = firstLinePoint.getY();
//		int xSecondLinePoint = secondLinePoint.getX();
//		int ySecondLinePoint = secondLinePoint.getY();
//		int xPoint = point.getX();
//		int yPoint = point.getY();
//		double distanceBetweenTheTwoLinePoints = getDistanceBetweenTwoCoordinates(firstLinePoint, secondLinePoint);
//		return Math.abs(((xSecondLinePoint - xFirstLinePoint)*(yFirstLinePoint - yPoint)) - ((xFirstLinePoint - xPoint)*(ySecondLinePoint - yFirstLinePoint))) / distanceBetweenTheTwoLinePoints;
//	}
//	
//	/**
//	 * Check if the line between firstLinePoint and secondLinePoint crosses the input circle.
//	 * 
//	 * @param firstLinePoint
//	 * @param secondLinePoint
//	 * @param circleCenter
//	 * @param circleRadius
//	 * @return boolean
//	 */
//	public static boolean isLineCrossingCircle(Coordinates firstLinePoint, Coordinates secondLinePoint, Coordinates circleCenter, int circleRadius) {
//		double distanceBetweenLineAndCircleCenter = getDistanceBetweenLineAndPoint(firstLinePoint, secondLinePoint, circleCenter);
//		if (distanceBetweenLineAndCircleCenter >= circleRadius) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//	
//	/**
//	 * Get the closest Coordinates from p of the line drawn by a and b.
//	 * 
//	 * @param a
//	 * @param b
//	 * @param p
//	 * @return Coordinates
//	 */
//	public static Coordinates getClosestCoordinatesOfLineFromPoint(Coordinates a, Coordinates b, Coordinates p) {
//		int xa = a.getX();
//		int ya = a.getY();
//		int xb = b.getX();
//		int yb = b.getY();
//		int xp = p.getX();
//		int yp = p.getY();
//		
//	    int xpMinusXa = xp - xa;
//	    int ypMinusYa = yp - ya;
//	    int xbMinusXa = xb - xa;
//	    int ybMinusYa = yb - ya;
//
//	    int xCoordinate = xa + (xbMinusXa * ((xpMinusXa * xbMinusXa) + (ypMinusYa * ybMinusYa)) / ((xbMinusXa * xbMinusXa) + (ybMinusYa * ybMinusYa)));
//		int yCoordinate = ya + (ybMinusYa * ((xpMinusXa * xbMinusXa) + (ypMinusYa * ybMinusYa)) / ((xbMinusXa * xbMinusXa) + (ybMinusYa * ybMinusYa)));
//	    return new Coordinates(xCoordinate, yCoordinate);
//	}
//	
//	/**
//	 * Get the two Coordinates of the line drawn by firstLinePoint and secondLinePoint that crosses the input circle.
//	 * It calls a method that I copy and that uses Point2D. I thus convert Point2D into Coordinates.
//	 * 
//	 * @param firstLinePoint
//	 * @param secondLinePoint
//	 * @param circleCenter
//	 * @param circleRadius
//	 * @return List<Coordinates>
//	 */
//	public static List<Coordinates> getIntersectionsOfLineWithCircle(Coordinates firstLinePoint, Coordinates secondLinePoint, Coordinates circleCenter, int circleRadius) {
//		List<Coordinates> intersections = new ArrayList<>();
//		Point2D firstLinePoint2D = new Point2D.Double(firstLinePoint.getX(), firstLinePoint.getY());
//		Point2D secondLinePoint2D = new Point2D.Double(secondLinePoint.getX(), secondLinePoint.getY());
//		Point2D circleCenter2D = new Point2D.Double(circleCenter.getX(), circleCenter.getY());
//		try {
//			List<Point2D> intersections2D = intersection(firstLinePoint2D, secondLinePoint2D, circleCenter2D, circleRadius, false);
//			for (Point2D intersection2D : intersections2D) {
//	        	//System.err.println("Intersection coordinates : (" + intersection2D.getX() + ", " + intersection2D.getY() + ")");
//				intersections.add(new Coordinates((int) Math.round(intersection2D.getX()), (int) Math.round(intersection2D.getY())));
//			}
//		} catch (NoninvertibleTransformException e) {
//			e.printStackTrace();
//		}
//		
//		return intersections;
//	}
//
//	/**
//     * If center of the circle is at the origin and the line is horizontal,
//     * it's easy to calculate the points of intersection, so to handle the
//     * general case, we convert the input to a coordinate system where the
//     * center of the circle is at the origin and the line is horizontal,
//     * then convert the points of intersection back to the original
//     * coordinate system.
//     *
//	 * @param p1
//	 * @param p2
//	 * @param center
//	 * @param radius
//	 * @param isSegment
//	 * @return
//	 * @throws NoninvertibleTransformException
//	 */
//    public static List<Point2D> intersection(Point2D p1, Point2D p2, Point2D center,
//            double radius, boolean isSegment) throws NoninvertibleTransformException {
//        List<Point2D> result = new ArrayList<>();
//        double dx = p2.getX() - p1.getX();
//        double dy = p2.getY() - p1.getY();
//        AffineTransform trans = AffineTransform.getRotateInstance(dx, dy);
//        trans.invert();
//        trans.translate(-center.getX(), -center.getY());
//        Point2D p1a = trans.transform(p1, null);
//        Point2D p2a = trans.transform(p2, null);
//        double y = p1a.getY();
//        double minX = Math.min(p1a.getX(), p2a.getX());
//        double maxX = Math.max(p1a.getX(), p2a.getX());
//        if (y == radius || y == -radius) {
//            if (!isSegment || (0 <= maxX && 0 >= minX)) {
//                p1a.setLocation(0, y);
//                trans.inverseTransform(p1a, p1a);
//                result.add(p1a);
//            }
//        } else if (y < radius && y > -radius) {
//            double x = Math.sqrt(radius * radius - y * y);
//            if (!isSegment || (-x <= maxX && -x >= minX)) {
//                p1a.setLocation(-x, y);
//                trans.inverseTransform(p1a, p1a);
//                result.add(p1a);
//            }
//            if (!isSegment || (x <= maxX && x >= minX)) {
//                p2a.setLocation(x, y);
//                trans.inverseTransform(p2a, p2a);
//                result.add(p2a);
//            }
//        }
//        return result;
//    }
//}
//
//final class InputUtils {
//	
//	private InputUtils() {}
//	
//	/**
//	 * Initialize the Sites at the start of the game thanks to input.
//	 * 
//	 * @param in
//	 * @param numSites
//	 * @return Map<Integer, Site>
//	 */
//	public static Map<Integer, Site> getSitesFromInitialInput(Scanner in, int numSites) {		
//		Map<Integer, Site> sitesById = new HashMap<>();
//        Coordinates coordinates;
//        Site site;
//        
//        for (int i = 0; i < numSites; i++) {
//            int siteId = in.nextInt();
//            int x = in.nextInt();
//            int y = in.nextInt();
//            int radius = in.nextInt();
//            coordinates = new Coordinates(x, y);
//            site = new Site(siteId, coordinates, radius);
//            sitesById.put(siteId, site);
//        }
//        
//        return sitesById;
//	}
//	
//	/**
//	 * Update the Sites of the game start thanks to turn input. Launched at the beginning of each turn.
//	 * It classify the Sites by Site ID, Structure types and Owner.
//	 * 
//	 * @param in
//	 * @param sitesById
//	 * @return Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>>
//	 */
//	public static Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> updateSitesFromTurnInput(Scanner in, Map<Integer, Site> sitesById) {
//		Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = new HashMap<>();
//		
//		Map<StructureEnum, Map<Integer, Site>> emptySitesByIdAndStructure = new HashMap<>();
//		Map<Integer, Site> emptySitesById = new HashMap<>();
//		emptySitesByIdAndStructure.put(StructureEnum.NOTHING, emptySitesById);
//		sitesByIdAndStructureAndOwner.put(OwnerEnum.NOBODY, emptySitesByIdAndStructure);
//		
//		Map<StructureEnum, Map<Integer, Site>> allySitesByIdAndStructure = new HashMap<>();
//		Map<Integer, Site> allyMineSitesById = new HashMap<>();
//		Map<Integer, Site> allyTowerSitesById = new HashMap<>();
//		Map<Integer, Site> allyBarracksSitesById = new HashMap<>();
//		allySitesByIdAndStructure.put(StructureEnum.MINE, allyMineSitesById);
//		allySitesByIdAndStructure.put(StructureEnum.TOWER, allyTowerSitesById);
//		allySitesByIdAndStructure.put(StructureEnum.BARRACKS, allyBarracksSitesById);
//		sitesByIdAndStructureAndOwner.put(OwnerEnum.ALLY, allySitesByIdAndStructure);
//		
//		Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = new HashMap<>();
//		Map<Integer, Site> enemyMineSitesById = new HashMap<>();
//		Map<Integer, Site> enemyTowerSitesById = new HashMap<>();
//		Map<Integer, Site> enemyBarracksSitesById = new HashMap<>();
//		enemySitesByIdAndStructure.put(StructureEnum.MINE, enemyMineSitesById);
//		enemySitesByIdAndStructure.put(StructureEnum.TOWER, enemyTowerSitesById);
//		enemySitesByIdAndStructure.put(StructureEnum.BARRACKS, enemyBarracksSitesById);
//		sitesByIdAndStructureAndOwner.put(OwnerEnum.ENEMY, enemySitesByIdAndStructure);
//		
//		Structure structure;
//		Site site;
//		for (int i = 0; i < sitesById.size(); i++) {
//            int siteId = in.nextInt();
//            int ignore1 = in.nextInt();
//            int ignore2 = in.nextInt();
//            int structureType = in.nextInt();
//            int owner = in.nextInt();
//            int param1 = in.nextInt();
//            int param2 = in.nextInt();
//
//            structure = new Structure(ignore1, ignore2, structureType, owner, param1, param2);
//            site = sitesById.get(siteId);
//            site.setStructure(structure);
//            
//            if (structure.getOwner() == OwnerEnum.NOBODY.getId()) {
//            	emptySitesById.put(siteId, site);
//            } else if (structure.getOwner() == OwnerEnum.ALLY.getId()) {
//            	if (structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
//            		allyMineSitesById.put(siteId, site);
//            	} else if (structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
//            		allyTowerSitesById.put(siteId, site);
//            	} else if (structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()) {
//            		allyBarracksSitesById.put(siteId, site);
//            	}
//            } else if (structure.getOwner() == OwnerEnum.ENEMY.getId()) {
//            	if (structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
//            		enemyMineSitesById.put(siteId, site);
//            	} else if (structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
//            		enemyTowerSitesById.put(siteId, site);
//            	} else if (structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()) {
//            		enemyBarracksSitesById.put(siteId, site);
//            	}
//            }
//        }
//		
//		return sitesByIdAndStructureAndOwner;
//	}
//	
//	/**
//	 * Get all the Units thanks to turn input. Launched at the beginning of each turn.
//	 * It classify the Units by Unit type and Owner.
//	 * 
//	 * @param in
//	 * @param numUnits
//	 * @return Map<OwnerEnum, Map<UnitEnum, List<Unit>>>
//	 */
//	public static Map<OwnerEnum, Map<UnitEnum, List<Unit>>> getUnitsByTypeAndOwnerFromTurnInput(Scanner in, int numUnits) {
//		Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = buildUnitsByTypeAndOwner();
//		
//		Map<UnitEnum, List<Unit>> allyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ALLY);
//		List<Unit> allyQueens = allyUnitsByType.get(UnitEnum.QUEEN);
//		List<Unit> allyKnights = allyUnitsByType.get(UnitEnum.KNIGHT);
//		List<Unit> allyArchers = allyUnitsByType.get(UnitEnum.ARCHER);
//		List<Unit> allyGiants = allyUnitsByType.get(UnitEnum.GIANT);
//		
//		Map<UnitEnum, List<Unit>> enemyUnitsByType = unitsByTypeAndOwner.get(OwnerEnum.ENEMY);
//		List<Unit> enemyQueens = enemyUnitsByType.get(UnitEnum.QUEEN);
//		List<Unit> enemyKnights = enemyUnitsByType.get(UnitEnum.KNIGHT);
//		List<Unit> enemyArchers = enemyUnitsByType.get(UnitEnum.ARCHER);
//		List<Unit> enemyGiants = enemyUnitsByType.get(UnitEnum.GIANT);
//		
//		Coordinates unitCoordinates;
//		Unit unit;
//        for (int i = 0; i < numUnits; i++) {
//            int x = in.nextInt();
//            int y = in.nextInt();
//            unitCoordinates = new Coordinates(x, y);
//            int owner = in.nextInt();
//            int unitType = in.nextInt();
//            int health = in.nextInt();
//
//            unit = new Unit(unitCoordinates, owner, unitType, health);
//            
//            if (unit.getOwner() == OwnerEnum.ALLY.getId()) {
//            	if (unitType == UnitEnum.QUEEN.getId()) {
//            		allyQueens.add(unit);
//            	} else if (unitType == UnitEnum.KNIGHT.getId()) {
//            		allyKnights.add(unit);
//            	} else if (unitType == UnitEnum.ARCHER.getId()) {
//            		allyArchers.add(unit);
//            	} else if (unitType == UnitEnum.GIANT.getId()) {
//            		allyGiants.add(unit);
//            	}            	
//            } else if (unit.getOwner() == OwnerEnum.ENEMY.getId()) {
//            	if (unitType == UnitEnum.QUEEN.getId()) {
//            		enemyQueens.add(unit);
//            	} else if (unitType == UnitEnum.KNIGHT.getId()) {
//            		enemyKnights.add(unit);
//            	} else if (unitType == UnitEnum.ARCHER.getId()) {
//            		enemyArchers.add(unit);
//            	} else if (unitType == UnitEnum.GIANT.getId()) {
//            		enemyGiants.add(unit);
//            	}  
//            }
//        }
//        
//        return unitsByTypeAndOwner;
//	}
//	
//	/**
//	 * Initialize the map used to get the Units from turns input.
//	 * 
//	 * @return
//	 */
//	private static Map<OwnerEnum, Map<UnitEnum, List<Unit>>> buildUnitsByTypeAndOwner() {
//		Map<OwnerEnum, Map<UnitEnum, List<Unit>>> unitsByTypeAndOwner = new HashMap<>();
//		
//		Map<UnitEnum, List<Unit>> allyUnitsByType = new HashMap<>();
//		List<Unit> allyQueens = new ArrayList<>();
//		List<Unit> allyKnights = new ArrayList<>();
//		List<Unit> allyArchers = new ArrayList<>();
//		List<Unit> allyGiants = new ArrayList<>();
//		allyUnitsByType.put(UnitEnum.QUEEN, allyQueens);
//		allyUnitsByType.put(UnitEnum.KNIGHT, allyKnights);
//		allyUnitsByType.put(UnitEnum.ARCHER, allyArchers);
//		allyUnitsByType.put(UnitEnum.GIANT, allyGiants);
//		
//		Map<UnitEnum, List<Unit>> enemyUnitsByType = new HashMap<>();
//		List<Unit> enemyQueens = new ArrayList<>();
//		List<Unit> enemyKnights = new ArrayList<>();
//		List<Unit> enemyArchers = new ArrayList<>();
//		List<Unit> enemyGiants = new ArrayList<>();
//		enemyUnitsByType.put(UnitEnum.QUEEN, enemyQueens);
//		enemyUnitsByType.put(UnitEnum.KNIGHT, enemyKnights);
//		enemyUnitsByType.put(UnitEnum.ARCHER, enemyArchers);
//		enemyUnitsByType.put(UnitEnum.GIANT, enemyGiants);
//		
//		unitsByTypeAndOwner.put(OwnerEnum.ALLY, allyUnitsByType);
//		unitsByTypeAndOwner.put(OwnerEnum.ENEMY, enemyUnitsByType);
//		
//		return unitsByTypeAndOwner;
//	}
//
//}
//
//final class SystemOutUtils {
//	
//	private SystemOutUtils() {}
//	
//	/**
//	 * Print a BUILD action thanks to a Site ID, a Structure type 
//	 * and eventually a Unit type (for BARRACKS Structure).
//	 * 
//	 * @param siteId
//	 * @param structureType
//	 * @param unitType
//	 */
//	public static void printBuildAction(int siteId, StructureEnum structureType, UnitEnum unitType) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("BUILD");
//		sb.append(" ");
//		sb.append(siteId);
//		sb.append(" ");
//		sb.append(structureType.toString());
//		
//		if (unitType != null) {
//			sb.append("-");
//			sb.append(unitType.toString());
//		}
//		
//		System.out.println(sb.toString());
//	}
//	
//	/**
//	 * Print a MOVE action thanks to input Coordinates.
//	 * 
//	 * @param coordinates
//	 */
//	public static void printMoveAction(Coordinates coordinates) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("MOVE");
//		sb.append(" ");
//		sb.append(coordinates.getX());
//		sb.append(" ");
//		sb.append(coordinates.getY());
//		
//		System.out.println(sb.toString());
//	}
//	
//	/**
//	 * Print a TRAIN action thanks to input Site ID.
//	 * If we do not want to TRAIN a Site, the input must be equal to -1;
//	 * 
//	 * @param siteId
//	 */
//	public static void printTrainAction(int siteId) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("TRAIN");
//		
//		if (siteId > -1) {
//			sb.append(" ");
//			sb.append(siteId);			
//		}
//		
//		System.out.println(sb.toString());
//	}
//
//}
//
//final class UnitsUtils {
//	
//	private UnitsUtils() {}
//	
//	/**
//	 * Get the QUEEN from the input map that provides a list of the Unit for each unit type.
//	 * The list of QUEEN must be of size 1 because the input map must be for only one player.
//	 * 
//	 * @param unitsByType
//	 * @return Unit
//	 */
//	public static Unit getQueen(Map<UnitEnum, List<Unit>> unitsByType) {
//		List<Unit> queens = unitsByType.get(UnitEnum.QUEEN);
//		if (queens.size() > 1) {
//			return null;
//		}
//		
//		return queens.get(0);
//	}
//	
//	/**
//	 * Check if the Coordinates is considered as safe regarding enemy KNIGHT.
//	 * Safe distance is defined in a constant.
//	 * 
//	 * @param coordinates
//	 * @param enemyUnitsByType
//	 * @return boolean
//	 */
//	public static boolean isItSafeAtCoordinatesRegardingEnemyKnights(Coordinates coordinates, Map<UnitEnum, List<Unit>> enemyUnitsByType, int safeDistance) {
//		return UnitsUtils.getDistanceBetweenNearestKnightAndCoordinates(coordinates, enemyUnitsByType) > safeDistance;
//	}
//	
//	/**
//	 * Get the distance between the input Coordinates and the nearest KNIGHT form the input map.
//	 * 
//	 * @param coordinates
//	 * @param unitsByType
//	 * @return double
//	 */
//	public static double getDistanceBetweenNearestKnightAndCoordinates(Coordinates coordinates, Map<UnitEnum, List<Unit>> unitsByType) {
//        double distanceToKnight;
//        double distanceToNearestKnight = Double.MAX_VALUE;
//		List<Unit> knights = unitsByType.get(UnitEnum.KNIGHT);
//		Coordinates knightCoordinates;
//		for (Unit knight : knights) {
//			knightCoordinates = knight.getCoordinates();
//			distanceToKnight = MathUtils.getDistanceBetweenTwoCoordinates(coordinates, knightCoordinates);
//			if (distanceToKnight < distanceToNearestKnight) {
//				distanceToNearestKnight = distanceToKnight;
//			}
//		}
//		
//		return distanceToNearestKnight;
//	}
//	
//	public static Unit getNearestUnit(Coordinates coordinates, Collection<Unit> units) {
//        double distanceToUnit;
//        double distanceToNearestUnit = Double.MAX_VALUE;
//		Coordinates unitCoordinates;
//		Unit nearestUnit = null;
//		for (Unit unit : units) {
//			unitCoordinates = unit.getCoordinates();
//			distanceToUnit = MathUtils.getDistanceBetweenTwoCoordinates(coordinates, unitCoordinates);
//			if (distanceToUnit < distanceToNearestUnit) {
//				distanceToNearestUnit = distanceToUnit;
//				nearestUnit = unit;
//			}
//		}
//		
//		return nearestUnit;
//	}
//	
//	/**
//	 * Check if a GIANT is close to the Coordinates. The distance considered as "close to" is defined in a constant.
//	 * 
//	 * @param giants
//	 * @param coordinates
//	 * @return boolean
//	 */
//	public static boolean isGiantCloseToCoordinates(Collection<Unit> giants, Coordinates coordinates) {
//		final int GIANT_SAFE_ZONE = 200;
//		for (Unit giant : giants) {
//			if (MathUtils.getDistanceBetweenTwoCoordinates(coordinates, giant.getCoordinates()) <= GIANT_SAFE_ZONE) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//}
//
//enum GameBoardQuarterEnum {
//	
//	TOPLEFT,
//	TOPRIGHT,
//	BOTTOMLEFT,
//	BOTTOMRIGHT;
//	
//}
//
//enum OwnerEnum {
//	
//	NOBODY(-1),
//	ALLY(0),
//	ENEMY(1);
//	
//	private int id;
//
//	private OwnerEnum(int id) {
//		this.id = id;
//	}
//
//	public int getId() {
//		return id;
//	}
//
//}
//
//enum StructureEnum {
//	
//	NOTHING(-1),
//	MINE(0),
//	TOWER(1),
//	BARRACKS(2);
//	
//	private int id;
//
//	private StructureEnum(int id) {
//		this.id = id;
//	}
//
//	public int getId() {
//		return id;
//	}
//
//}
//
//enum UnitEnum {
//	
//	QUEEN(-1),
//	KNIGHT(0),
//	ARCHER(1),
//	GIANT(2);
//	
//	private int id;
//
//	private UnitEnum(int id) {
//		this.id = id;
//	}
//
//	public int getId() {
//		return id;
//	}
//
//}
//
