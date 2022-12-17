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
//	private static Integer startingQueenHealth = null;
//	private static Coordinates startingAllyQueenCoordinates = null;
//	private static Map<Integer, Integer> remainingGoldBySiteId = new HashMap<>();
//	private static boolean isFirstBuildDone = false;
//	private static boolean isTwoFirstMinesBuild = false;
//	private static boolean isFirstKnightBarracksBuilt = false;
//	private static int towersBuilt = 0;
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
//            // Update Sites by creating Structure thanks to the the turn input
//            Map<OwnerEnum, Map<StructureEnum, Map<Integer, Site>>> sitesByIdAndStructureAndOwner = InputUtils.updateSitesFromTurnInput(in, sitesById);
//            
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
//            Collection<Site> allyMineAndNotTrainingBarracksAndTowerSites = StructuresUtils.getMineAndNotTrainingBarracksAndTowerSites(allySites);
//            
//            Collection<Site> allyAndEmptySites = new ArrayList<>();
//            allyAndEmptySites.addAll(emptySites);
//            allyAndEmptySites.addAll(allySites);
//            
//            Collection<Site> emptyAndAllyMineAndNotInTraingBarracksSites = StructuresUtils.getEmptyAndMineAndNotTrainingBarracks(allyAndEmptySites);
//            
//    		Map<StructureEnum, Map<Integer, Site>> enemySitesByIdAndStructure = sitesByIdAndStructureAndOwner.get(OwnerEnum.ENEMY);
//    		Map<Integer, Site> enemyMineSitesById = enemySitesByIdAndStructure.get(StructureEnum.MINE);
//            Collection<Site> enemyMineSites = enemyMineSitesById.values();
//    		Map<Integer, Site> enemyTowerSitesById = enemySitesByIdAndStructure.get(StructureEnum.TOWER);
//    		Collection<Site> enemyTowerSites = enemyTowerSitesById.values();
//    		int enemyTowersNumber = enemyTowerSites.size();
//    		Map<Integer, Site> enemyBarracksSitesById = enemySitesByIdAndStructure.get(StructureEnum.BARRACKS);
//            Collection<Site> enemyBarracksSites = enemyBarracksSitesById.values();
//            Collection<Site> enemyKnightBarracksSites = StructuresUtils.getKnightBarracksSites(enemyBarracksSites);
//            Collection<Site> enemySites = new ArrayList<>();
//            enemySites.addAll(enemyMineSites);
//            enemySites.addAll(enemyTowerSites);
//            enemySites.addAll(enemyBarracksSites);
//            
//            Collection<Site> enemyAndEmptySites = new ArrayList<>();
//            enemyAndEmptySites.addAll(emptySites);
//            enemyAndEmptySites.addAll(enemySites);
//            
//            Collection<Site> emptyAndEnemyMineAndNotInTraingBarracksSites = StructuresUtils.getEmptyAndMineAndNotTrainingBarracks(enemyAndEmptySites);
//            
//            Collection<Site> allSites = new ArrayList<>();
//            allSites.addAll(emptySites);
//            allSites.addAll(allySites);
//            allSites.addAll(enemySites);
//            
//            StructuresUtils.updateRemaingGoldBySiteId(remainingGoldBySiteId, allSites);
//
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
//            // Initialize start of game parameters
//            if (startingQueenHealth == null) {
//            	startingQueenHealth = allyQueenHealth;
//            }
//            if (startingAllyQueenCoordinates == null) {
//            	startingAllyQueenCoordinates = allyQueenCoordinates;
//            }
//            
//            // Constants
//            final int MIN_ALLY_TOWERS_NUMBER = 3;
//            final int MAX_ALLY_TOWERS_NUMBER = 4;
//            final int MAX_ALLY_GOLD_PRODUCTION = 8;
//            final int ENEMY_TOWERS_NUMBER_THRESHOLD = 3;
//    		final int SAFE_DISTANCE = 500;
//            
//            // Depending on ally QUEEN health, choose the best values
//            int minAllyGoldProduction = 2;
////            if (allyQueen.getHealth() < 50) {
////            	minAllyGoldProduction = 3;
////            } else {
////            	minAllyGoldProduction = 4;            	
////            }
//            
//            int minAllyFirstMines;
//            if (startingQueenHealth < 50) {
//            	minAllyFirstMines = 1;
//            } else {
//            	minAllyFirstMines = 2;            	
//            }
//            
//            
//            if (!isTwoFirstMinesBuild) {
//            	isTwoFirstMinesBuild = allyMineSites.size() == minAllyFirstMines;
//            }
//            
//            // Possible Site to MOVE or to BUILD
//            Site targetedSite;
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
//            // Booleans that could be use to choose what to do during this turn
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
//            *		d) else if MOVE to a free Site and BUILD a MINE until minAllyGoldProduction is reached
//            *		e) else if MOVE to a free Site and BUILD a TOWER until minAllyTowerNumber is reached
//            *		f) else if MOVE to a free Site and BUILD an only one KNIGHT BARRACKS
//            *		g) else if MOVE to a Site not in training and BUILD an only one KNIGHT BARRACKS
//            *		h) else if MOVE to a free Site and BUILD an only one GIANT BARRACKS
//            *		i) else if MOVE to a free Site and BUILD a MINE until MAX_ALLY_GOLD_PRODUCTION is reached
//            *		j) else if MOVE to a free Site and BUILD a TOWER until MAX_ALLY_TOWERS_NUMBER is reached
//            *		k) else if MOVE to a free Site and BUILD a MINE
//            *		l) else if MOVE to a free Site and BUILD a TOWER
//            *		m) else if MOVE to the nearest ally TOWER with not enough life points
//            *		n) else MOVE to a safe place
//            */
//            if (TurnStrategyUtils.isRunAwayStrategyOk(allyQueenHealth, allyQueenCoordinates, enemyUnitsByType, enemyTowerSites, emptySitesNumber, enemyKnightsNumber, SAFE_DISTANCE, enemyKnightBarracksSites)
//            		&& towersBuilt > 0) {
//            	Coordinates safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, enemyKnights); 
//            	if (TurnStrategyUtils.isBuildTowerWhenRunningAwayStrategyOk(allyQueenCoordinates, safestCoordinates, nearestSiteToBuildATowerWhenRunningAway, enemyGiants)) {
//            		if (touchedSite == nearestSiteToBuildATowerWhenRunningAway.getId()) {
//            			towersBuilt++;
//                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
//            		} else {
//            			safestCoordinates = nearestSiteToBuildATowerWhenRunningAway.getCoordinates();
//            			SystemOutUtils.printMoveAction(safestCoordinates);            			
//            		}
//            	} else {
//            		SystemOutUtils.printMoveAction(safestCoordinates);
//            	}
//            } else if (isTouchingAMineToImprove) {
//            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
//        	} else if (isTouchingATowerToImprove) {
//            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
//        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, MAX_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)
//        			&& !isTwoFirstMinesBuild) {
//        		targetedSiteId = nearestSiteToBuildAMine.getId();
//        		if (touchedSite != targetedSiteId) {
//        			SystemOutUtils.printMoveAction(nearestSiteToBuildAMine.getCoordinates());
//        		} else {
//        			if (!isFirstBuildDone) {
//        				isFirstBuildDone = true;
//        			}
//        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//        		}   
//        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, minAllyGoldProduction, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//        		targetedSiteId = nearestSiteToBuildAMine.getId();
//        		if (touchedSite != targetedSiteId) {
//        			SystemOutUtils.printMoveAction(nearestSiteToBuildAMine.getCoordinates());
//        		} else {
//        			if (!isFirstBuildDone) {
//        				isFirstBuildDone = true;
//        			}
//        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//        		}   
//            } else if (TurnStrategyUtils.isKnightBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyKnightBarracksSites, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	targetedSite = nearestEmptySite;
//            	targetedSiteId = targetedSite.getId();
//            	if (touchedSite != targetedSiteId) {
//                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
//            	} else {
//            		if (!isFirstKnightBarracksBuilt) {
//            			isFirstKnightBarracksBuilt = true;
//            		}
//            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
//            	}
//            } else if (TurnStrategyUtils.isKnightBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestAllySiteNotInTraining, allyKnightBarracksSites, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//            	targetedSite = nearestAllySiteNotInTraining;
//            	targetedSiteId = targetedSite.getId();
//            	if (touchedSite != targetedSiteId) {
//                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
//            	} else {
//            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
//            	}
//            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, MIN_ALLY_TOWERS_NUMBER, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//        		targetedSite = nearestEmptySite;
//        		targetedSiteId = targetedSite.getId();
//        		if (touchedSite != targetedSiteId) {
//        			SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
//        		} else {
//        			towersBuilt++;
//        			SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
//        		}   
//            } else if (TurnStrategyUtils.isGiantBarracksMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, enemyTowersNumber, allyBarracksSites, enemyUnitsByType, enemyTowerSites, ENEMY_TOWERS_NUMBER_THRESHOLD, SAFE_DISTANCE, enemyKnightBarracksSites, enemyMineSites, allyMineSites)) {
//            	targetedSite = nearestEmptySite;
//            	targetedSiteId = targetedSite.getId();
//            	if (touchedSite != targetedSiteId) {
//                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
//            	} else {
//            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.GIANT);
//            	}
//        	} else if (TurnStrategyUtils.isMineMoveOrBuildStrategyOk(allyQueenHealth, nearestSiteToBuildAMine, allyMineSites, MAX_ALLY_GOLD_PRODUCTION, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//    			targetedSiteId = nearestSiteToBuildAMine.getId();
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(nearestSiteToBuildAMine.getCoordinates());
//    			} else {
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//    			}        			
//            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, MAX_ALLY_TOWERS_NUMBER, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//    			targetedSite = nearestEmptySite;
//    			targetedSiteId = targetedSite.getId();
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
//    			} else {
//        			towersBuilt++;
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
//    			}        			
//        	} else if (nearestSiteToBuildAMine != null && GameBoardUtils.isItSafeAtCoordinates(nearestSiteToBuildAMine.getCoordinates(), enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//    			targetedSiteId = nearestSiteToBuildAMine.getId();
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(nearestSiteToBuildAMine.getCoordinates());
//    			} else {
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
//    			}  
//        	} else if (nearestAllyTowerSiteWithNotSufficientLife != null 
//        			&& (nearestSiteToBuildAMine == null || MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestAllyTowerSiteWithNotSufficientLife.getCoordinates()) < MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestSiteToBuildAMine.getCoordinates()))
//        			&& (nearestEmptySite == null || MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestAllyTowerSiteWithNotSufficientLife.getCoordinates()) < MathUtils.getDistanceBetweenTwoCoordinates(allyQueenCoordinates, nearestEmptySite.getCoordinates()))) {
//        		SystemOutUtils.printMoveAction(nearestAllyTowerSiteWithNotSufficientLife.getCoordinates()); 
//            } else if (TurnStrategyUtils.isTowerMoveOrBuildStrategyOk(allyQueenHealth, nearestEmptySite, allyTowersNumber, Integer.MAX_VALUE, enemyUnitsByType, enemyTowerSites, SAFE_DISTANCE, enemyKnightBarracksSites)) {
//    			targetedSite = nearestEmptySite;
//    			targetedSiteId = targetedSite.getId();
//    			if (touchedSite != targetedSiteId) {
//    				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
//    			} else {
//        			towersBuilt++;
//    				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
//    			} 
//            } else {
//            	Coordinates safestCoordinates = GameBoardUtils.getSafestCoordinates(startingAllyQueenCoordinates, allyTowerSites, enemyKnights);
//            	SystemOutUtils.printMoveAction(safestCoordinates);
//        	}
//
//            /* 2) Second turn action is to TRAIN.
//            *		a) if enemy TOWER number is more than ENEMY_TOWER_NUMBER_THRESHOLD and I owned a GIANT BARRACKS, TRAIN a GIANT
//            *		b) else TRAIN a KNIGHT
//            */
//            Site siteToTrain = null;
//            if ((enemyTowerSitesById.size() > (ENEMY_TOWERS_NUMBER_THRESHOLD + 2) ||
//            		(enemyTowersNumber > 1 && StructuresUtils.getGoldProduction(allyMineSites) >= 8))
//            		&& allyGiants.size() < 2
//            		&& StructuresUtils.isAtLeastOneGiantBarracks(allyBarracksSites)) {
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
//		
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
//    public static Site getFirstSiteToBuildTowerInCorner(Collection<Site> sites, Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
//		GameBoardQuarterEnum siteBoardGameQuarter;
//        boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
//    	Site chosenSite = null;
//        double distanceToSite;
//        double distanceToChosenSite = Double.MAX_VALUE;
//        Coordinates siteCoordinates;
//        for (Site site : sites) {
//        	siteBoardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(site.getCoordinates(), new Coordinates(960, 500));
//        	if ((isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.BOTTOMLEFT))
//        			|| (!isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.TOPRIGHT))) {
//        		siteCoordinates = site.getCoordinates();
//        		distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(new Coordinates(960, 500), siteCoordinates);
//        		if (distanceToSite < distanceToChosenSite) {
//        			distanceToChosenSite = distanceToSite;
//        			chosenSite = site;
//        		}        		
//        	}
//        }
//        return chosenSite;
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
//     * Get a Coordinates to hide from enemies behind a tower :
//     * 	- first evaluate the nearest enemy position with respect to the towerSite
//     * 	- then adapt the Coordinates close to the towerSite
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
//    public static int getSafeDistanceWithRespectToKnightBarracks(Site knightBarracksSite) {
//    	if (knightBarracksSite == null) {
//    		return 0;
//    	}
//    	
//    	int trainingTurnsRemaining = knightBarracksSite.getStructure().getParam1();
//    	if (trainingTurnsRemaining == 0) {
//    		return 0;
//    	} else {
//    		return (6 - trainingTurnsRemaining) * 100;
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
//		int xNearestSiteToBuildATowerCoordinates = nearestSiteToBuildATowerCoordinates.getX();
//		int yNearestSiteToBuildATowerCoordinates = nearestSiteToBuildATowerCoordinates.getY();
//		int xAllyQueenCoordinates = allyQueenCoordinates.getX();
//		int yAllyQueenCoordinates = allyQueenCoordinates.getY();
//		if (Math.abs(xNearestSiteToBuildATowerCoordinates - xAllyQueenCoordinates) <= X_RANGE
//				&& Math.abs(yNearestSiteToBuildATowerCoordinates - yAllyQueenCoordinates) <= Y_RANGE
//				&& GameBoardUtils.isCoordinatesOnTheWayOfTrajectoryBetweenTwoCoordinates(nearestSiteToBuildATowerCoordinates, allyQueenCoordinates, safestCoordinates)) {
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
//	public static Coordinates getSafestCoordinates(Coordinates startingAllyQueenCoordinates, Collection<Site> allyTowerSites, Collection<Unit> enemyKnights) {
//		Coordinates safestCoordinates;
//    	if (allyTowerSites.size() >= 3) {
//    		Site safestAllyTower = StructuresUtils.getSafestTower(allyTowerSites, startingAllyQueenCoordinates);
//    		safestCoordinates = StructuresUtils.getCoordinatesBehindTower(UnitsUtils.getNearestUnit(safestAllyTower.getCoordinates(), enemyKnights), safestAllyTower, startingAllyQueenCoordinates);
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
//    	
//    	if (isXcLessThanXb && isYcLessThanYb) {
//    		return isXaLessThanXb && isYaLessThanYb;
//    	} else if (isXcLessThanXb && !isYcLessThanYb) {
//    		return isXaLessThanXb && !isYaLessThanYb;
//    	} else if (!isXcLessThanXb && isYcLessThanYb) {
//    		return !isXaLessThanXb && isYaLessThanYb;
//    	} else {
//    		return !isXaLessThanXb && !isYaLessThanYb;
//    	}
//    }
//    
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
//	public static Coordinates getCoordinatesBetweenTwoCoordinates(Coordinates a, Coordinates b) {
//		int xa = a.getX();
//		int ya = a.getY();
//		int xb = b.getX();
//		int yb = b.getY();
//		
//		return new Coordinates(Math.abs(xb - xa), Math.abs(yb - ya));
//	}
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
