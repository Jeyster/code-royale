package com.mgaurat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.StructureEnum;
import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitEnum;
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
            Map<UnitEnum, List<Unit>> unitsByType = UnitsUtils.getUnitsByTypeFromTurnInput(in, numUnits);
            Unit myQueen = UnitsUtils.getMyQueen(unitsByType);
            Coordinates myQueenCoordinates = myQueen.getCoordinates();
            
            /* 1) First turn action is to MOVE or BUILD.
            *		a) If i have no KNIGHT BARRACKS :
            *			- get the nearest free site and MOVE to it
            *			- if touching this free site, build a KNIGHT BARRACKS
            *		b) else if my gold production is less than MIN_GOLD_PRODUCTION :
            *			- if I touch a MINE of mine that is not in full production, increase the production
            *			- else get the nearest site where a MINE can be built (gold not depleted) and MOVE on it or BUILD a MINE
            *		c) else if my TOWER number is less than MIN_TOWER_NUMBER :
            *			- if I touch a TOWER of mine that is not full life, increase its life
            *			- else get the nearest free site and MOVE on it or BUILD a TOWER
            *		d) else if my gold production is less than MAX_GOLD_PRODUCTION :
            *			- if I touch a MINE of mine that is not in full production, increase the production
            *			- else get the nearest site where a MINE can be built (gold not depleted) and MOVE on it or BUILD a MINE
            *		e) else if my TOWER number is less than MAX_TOWER_NUMBER :
            *			- if I touch a TOWER of mine that is not full life, increase its life
            *			- else get the nearest free site and MOVE on it or BUILD a TOWER
            */
            final int MIN_GOLD_PRODUCTION = 5;
            final int MAX_GOLD_PRODUCTION = 8;
            final int MIN_TOWER_NUMBER = 3;
            final int MAX_TOWER_NUMBER = 4;
            Site targetedSite;
            int targetedSiteId;
            if (!StructuresUtils.isAtLeastOneKnightBarrackOwnedByMe(sites)) {
            	targetedSite = SitesUtils.getNearestFreeSite(sites, myQueenCoordinates);
            	targetedSiteId = targetedSite.getId();
            	if (touchedSite != targetedSiteId) {
                	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.BARRACKS, UnitEnum.KNIGHT);
            	}
        	} else if (StructuresUtils.getCurrentGoldProduction(sites) < MIN_GOLD_PRODUCTION) {
        		if (touchedSite != -1 
        			&& StructuresUtils.isMineOwnedByMeNotInFullProduction(sitesById.get(touchedSite).getStructure())) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSiteNotOwnedToBuildAMine(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        			}        			
        		}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sites) < MIN_TOWER_NUMBER) {
        		if (touchedSite != -1 
            			&& StructuresUtils.isTowerOwnedByMeNotFullLife(sitesById.get(touchedSite).getStructure())) {
                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        		} else {
        			targetedSite = SitesUtils.getNearestFreeSite(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        			}        			
        		}
        	} else if (StructuresUtils.getCurrentGoldProduction(sites) < MAX_GOLD_PRODUCTION) {
        		if (touchedSite != -1 
        			&& StructuresUtils.isMineOwnedByMeNotInFullProduction(sitesById.get(touchedSite).getStructure())) {
            		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.MINE, null);
        		} else {
        			targetedSite = SitesUtils.getNearestSiteNotOwnedToBuildAMine(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.MINE, null);
        			}        			
        		}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sites) < MAX_TOWER_NUMBER) {
        		if (touchedSite != -1 
            			&& StructuresUtils.isTowerOwnedByMeNotFullLife(sitesById.get(touchedSite).getStructure())) {
                		SystemOutUtils.printBuildAction(touchedSite, StructureEnum.TOWER, null);
        		} else {
        			targetedSite = SitesUtils.getNearestFreeSite(sites, myQueenCoordinates);
        			targetedSiteId = targetedSite.getId();
        			if (touchedSite != targetedSiteId) {
        				SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
        			} else {
        				SystemOutUtils.printBuildAction(targetedSiteId, StructureEnum.TOWER, null);
        			}        			
        		}
        	} else {
            	Site knightBarrack = StructuresUtils.getAKnightBarrackOwnedByMe(sites);
            	SystemOutUtils.printMoveAction(knightBarrack.getCoordinates());
        	}

            /* 2) Second turn action is to TRAIN :
            *		- if there is a site available for training, do it
            *		- else train nothing
            */
            Site siteToTrain = SitesUtils.getASiteToTrain(sites);
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(siteToTrain.getId());
            } else {
                SystemOutUtils.printTrainAction(0);
            }
        }
    }

}


class Coordinates {
	
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
}

class Site {
	
	private int id;
    private Coordinates coordinates;
    private int radius;
    private Structure structure;
    
	public Site(int id, Coordinates coordinates, int radius) {
		this.id = id;
		this.coordinates = coordinates;
		this.radius = radius;
	}
	
	public int getId() {
		return id;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	public int getRadius() {
		return radius;
	}

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

}

class Structure {
	
    // Gold left in a MINE (-1 if unknown or N/A)
    private int mineGold;
    
    // Maximum MINE gold production (-1 if unknown or N/A)
    private int maxMineProduction;
    
    // -1 : nothing is built
    // 0 : MINE
    // 1 : TOWER 
    // 2 : BARRACKS
    int structureTypeId;
    
    // -1 : nothing is built
    // 0 : ALLY structure
    // 1 : ENEMY structure
    int owner;
    
    // if nothing is built = -1
    // if MINE = current gold production (-1 if ENEMY MINE)
    // if TOWER = life points
    // if BARRACKS = turns left before TRAIN ending (0 if ready to TRAIN)
    int param1;
    
    // if nothing or a MINE is built = -1
    // if TOWER = range radius
    // if BARRACKS = 0 if it produces KNIGHT
    //            = 1 if it produces ARCHER
    //            = 2 if it produces GIANT
    int param2;

	public Structure(int mineGold, int maxMineProduction, int structureType, int owner, int param1, int param2) {
		this.mineGold = mineGold;
		this.maxMineProduction = maxMineProduction;
		this.structureTypeId = structureType;
		this.owner = owner;
		this.param1 = param1;
		this.param2 = param2;
	}

	public int getMineGold() {
		return mineGold;
	}

	public int getMaxMineProduction() {
		return maxMineProduction;
	}

	public int getStructureTypeId() {
		return structureTypeId;
	}

	public int getOwner() {
		return owner;
	}

	public int getParam1() {
		return param1;
	}

	public int getParam2() {
		return param2;
	}
	
	public boolean isOwnedByMe() {
		return getOwner() == OwnerEnum.ALLY.getId();
	}

}

class Unit {
	
	private Coordinates coordinates;
	
    // 0 : ally
    // 1 : enemy	
	private int owner;
	
	// -1 : QUEEN
	// 0 : KNIGHT
	// 1 : ARCHER
	// 2 : GIANT
	private int unitType;
	
	private int health;

	public Unit(Coordinates coordinates, int owner, int unitType, int health) {
		super();
		this.coordinates = coordinates;
		this.owner = owner;
		this.unitType = unitType;
		this.health = health;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public int getOwner() {
		return owner;
	}

	public int getUnitType() {
		return unitType;
	}

	public int getHealth() {
		return health;
	}
 
}

enum OwnerEnum {
	
	NOBODY(-1),
	ALLY(0),
	ENEMY(1);
	
	private int id;

	private OwnerEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}


enum StructureEnum {
	
	NOTHING(-1),
	MINE(0),
	TOWER(1),
	BARRACKS(2);
	
	private int id;

	private StructureEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}


enum UnitEnum {
	
	QUEEN(-1),
	KNIGHT(0),
	ARCHER(1),
	GIANT(2);
	
	private int id;

	private UnitEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}

final class MathUtils {
	
	private MathUtils() {
	}
	
	public static double getDistanceBetweenTwoCoordinates(Coordinates a, Coordinates b) {
		int xa = a.getX();
		int ya = a.getY();
		int xb = b.getX();
		int yb = b.getY();
		
		return Math.sqrt(Math.pow(xa - xb, 2) + Math.pow(ya - yb, 2));
	}

}

final class SitesUtils {
	
	private SitesUtils() {
	}
	
	public static Map<Integer, Site> getSitesFromInitialInput(Scanner in, int numSites) {
		Map<Integer, Site> sitesById = new HashMap();
        Coordinates coordinates;
        Site site;
        
        for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int radius = in.nextInt();
            coordinates = new Coordinates(x, y);
            site = new Site(siteId, coordinates, radius);
            sitesById.put(siteId, site);
        }
        
        return sitesById;
	}
	
	public static void updateSitesFromTurnInput(Scanner in, int numSites, Map<Integer, Site> sitesById) {
		Structure structure;
		Site site;
		for (int i = 0; i < numSites; i++) {
            int siteId = in.nextInt();
            int ignore1 = in.nextInt();
            int ignore2 = in.nextInt();
            int structureType = in.nextInt();
            int owner = in.nextInt();
            int param1 = in.nextInt();
            int param2 = in.nextInt();

            structure = new Structure(ignore1, ignore2, structureType, owner, param1, param2);
            site = sitesById.get(siteId);
            site.setStructure(structure);
        }
	}
	
	public static Collection<Site> getSitesCollection(Map<Integer, Site> sitesById) {
		return sitesById.values();
	}
	
    public static Site getNearestSite(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {
            siteCoordinates = site.getCoordinates();
            distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            if (distanceToSite < distanceToNearestSite) {
                distanceToNearestSite = distanceToSite;
                nearestSite = site;
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestFreeSite(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
            if (site.getStructure().getOwner() == OwnerEnum.NOBODY.getId()) {
            	siteCoordinates = site.getCoordinates();
            	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteNotOwnedToBuildAMine(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
            if (site.getStructure().getOwner() == OwnerEnum.NOBODY.getId()
            		&& site.getStructure().getMineGold() != 0) {
            	siteCoordinates = site.getCoordinates();
            	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteNotOwnedByMe(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
            if (site.getStructure().getOwner() != OwnerEnum.ALLY.getId()) {
            	siteCoordinates = site.getCoordinates();
            	distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            	if (distanceToSite < distanceToNearestSite) {
            		distanceToNearestSite = distanceToSite;
            		nearestSite = site;
            	}            	
            }
        }
        return nearestSite;
    }
    
    public static boolean isAtLeastOneSiteOwnedByMe(Collection<Site> sites) {
        for (Site site : sites) {            
        	if (site.getStructure().isOwnedByMe()) {
        		return true;
        	}
        }
        return false;

    }
    
    public static Site getASiteToTrain(Collection<Site> sites) {
        for (Site site : sites) {            
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getParam1() == 0 
        			&& site.getStructure().getStructureTypeId() == StructureEnum.BARRACKS.getId()) {
        		return site;
        	}
        }
        return null;
    }

}

final class StructuresUtils {
	
	private StructuresUtils() {
	}
	
    public static boolean isAtLeastOneKnightBarrackOwnedByMe(Collection<Site> sites) {
    	Structure structure;
        for (Site site : sites) {
        	structure = site.getStructure();
        	if (structure.isOwnedByMe() 
        			&& structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()
        			&& structure.getParam2() == UnitEnum.KNIGHT.getId()) {
        		return true;
        	}
        }
        return false;
    }
    
    public static Site getAKnightBarrackOwnedByMe(Collection<Site> sites) {
    	Structure structure;
        for (Site site : sites) {
        	structure = site.getStructure();
        	if (structure.isOwnedByMe() 
        			&& structure.getStructureTypeId() == StructureEnum.BARRACKS.getId()
        			&& structure.getParam2() == UnitEnum.KNIGHT.getId()) {
        		return site;
        	}
        }
        return null;
    }
    
    public static int getNumberOfTowerOwnedByMe(Collection<Site> sites) {
    	int towerNumber = 0;
    	Structure structure;
        for (Site site : sites) {
        	structure = site.getStructure();
        	if (structure.isOwnedByMe() 
        			&& structure.getStructureTypeId() == StructureEnum.TOWER.getId()) {
        		towerNumber++;
        	}
        }
        
        return towerNumber;
    }
    
    public static int getCurrentGoldProduction(Collection<Site> sites) {
    	int currentGoldProduction = 0;
    	Structure structure;
    	for (Site site : sites) {
        	structure = site.getStructure();
    		if (structure.isOwnedByMe()
    			&& structure.getStructureTypeId() == StructureEnum.MINE.getId()) {
    			currentGoldProduction += structure.getParam1();
    		}
    	}
    	
    	return currentGoldProduction;
    }
    
    public static boolean isMineOwnedByMeNotInFullProduction(Structure structure) {
    	return structure.getStructureTypeId() == StructureEnum.MINE.getId()
    			&& structure.getParam1() < structure.getMaxMineProduction();
    }
    
    public static boolean isTowerOwnedByMeNotFullLife(Structure structure) {
    	final int MAX_TOWER_LIFE = 750;
    	return structure.getStructureTypeId() == StructureEnum.TOWER.getId()
    			&& structure.getParam1() < MAX_TOWER_LIFE;
    }

}

final class UnitsUtils {
	
	private UnitsUtils() {
	}
	
	public static Map<UnitEnum, List<Unit>> getUnitsByTypeFromTurnInput(Scanner in, int numUnits) {
		Map<UnitEnum, List<Unit>> unitsByType = buildUnitsByType();
		List<Unit> queens = unitsByType.get(UnitEnum.QUEEN);
		List<Unit> knights = unitsByType.get(UnitEnum.KNIGHT);
		List<Unit> archers = unitsByType.get(UnitEnum.ARCHER);
		List<Unit> giants = unitsByType.get(UnitEnum.GIANT);
		
		Coordinates unitCoordinates;
		Unit unit;
        for (int i = 0; i < numUnits; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            unitCoordinates = new Coordinates(x, y);
            int owner = in.nextInt();
            int unitType = in.nextInt();
            int health = in.nextInt();

            unit = new Unit(unitCoordinates, owner, unitType, health);
            
            if (unitType == UnitEnum.QUEEN.getId()) {
				queens.add(unit);
			} else if (unitType == UnitEnum.KNIGHT.getId()) {
				knights.add(unit);
			} else if (unitType == UnitEnum.ARCHER.getId()) {
				archers.add(unit);
			} else if (unitType == UnitEnum.GIANT.getId()) {
				giants.add(unit);
			}
        }
        
        return unitsByType;
	}
	
	private static Map<UnitEnum, List<Unit>> buildUnitsByType() {
		Map<UnitEnum, List<Unit>> unitsByType = new HashMap<>();
		List<Unit> queens = new ArrayList();
		List<Unit> knights = new ArrayList<>();
		List<Unit> archers = new ArrayList<>();
		List<Unit> giants = new ArrayList<>();
		
		unitsByType.put(UnitEnum.QUEEN, queens);
		unitsByType.put(UnitEnum.KNIGHT, knights);
		unitsByType.put(UnitEnum.ARCHER, archers);
		unitsByType.put(UnitEnum.GIANT, giants);
		
		return unitsByType;
	}
	
	public static Unit getMyQueen(Map<UnitEnum, List<Unit>> unitsByType) {
		List<Unit> queens = unitsByType.get(UnitEnum.QUEEN);
		for (Unit queen : queens) {
			if (queen.getOwner() == OwnerEnum.ALLY.getId()) {
				return queen;
			}
		}
		
		return null;
	}

}

final class SystemOutUtils {
	
	private SystemOutUtils() {
	}
	
	public static void printBuildAction(int targetedSiteId, StructureEnum structureType, UnitEnum unitType) {
		StringBuilder sb = new StringBuilder();
		sb.append("BUILD");
		sb.append(" ");
		sb.append(targetedSiteId);
		sb.append(" ");
		sb.append(structureType.toString());
		
		if (unitType != null) {
			sb.append("-");
			sb.append(unitType.toString());
		}
		
		System.out.println(sb.toString());
	}
	
	public static void printMoveAction(Coordinates coordinates) {
		StringBuilder sb = new StringBuilder();
		sb.append("MOVE");
		sb.append(" ");
		sb.append(coordinates.getX());
		sb.append(" ");
		sb.append(coordinates.getY());
		
		System.out.println(sb.toString());
	}
	
	public static void printTrainAction(int siteId) {
		StringBuilder sb = new StringBuilder();
		sb.append("TRAIN");
		
		if (siteId > 0) {
			sb.append(" ");
			sb.append(siteId);			
		}
		
		System.out.println(sb.toString());
	}

}

