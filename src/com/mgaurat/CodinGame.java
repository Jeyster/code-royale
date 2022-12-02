import java.util.*;

import com.mgaurat.enums.Owner;
import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;
import com.mgaurat.model.Unit;
import com.mgaurat.utils.GameBoardUtil;
import com.mgaurat.utils.MathUtils;

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
            } else if (GameBoardUtil.getNumberOfTowerOwnedByMe(sitesById) == 4) {
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
	
    // Not used yet
    private int ignore1;
    private int ignore2;
    
    // -1 : nothing is built
    // 1 : Tower 
    // 2 : Barrack
    int structureType;
    
    // -1 : nothing is built
    // 0 : ally structure
    // 1 : enemy structure
    int owner;
    
    // if nothing is built = -1
    // if Tower = life points
    // if Barrack = turns left before TRAIN ending (0 if ready to TRAIN)
    int param1;
    
    // if nothing is built = -1
    // if Tower = range radius
    // if Barrack = 0 if it produces KNIGHT
    //            = 1 if it produces ARCHER
    //            = 2 if it produces GIANT
    int param2;

	public Structure(int ignore1, int ignore2, int structureType, int owner, int param1, int param2) {
		this.ignore1 = ignore1;
		this.ignore2 = ignore2;
		this.structureType = structureType;
		this.owner = owner;
		this.param1 = param1;
		this.param2 = param2;
	}

	public int getIgnore1() {
		return ignore1;
	}

	public int getIgnore2() {
		return ignore2;
	}

	public int getStructureType() {
		return structureType;
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
		return getOwner() == Owner.ALLY.getOwnerId();
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

enum UnitType {
	
	QUEEN(-1),
	KNIGHT(0),
	ARCHER(1),
	GIANT(2);
	
	private int unitTypeId;

	private UnitType(int unitTypeId) {
		this.unitTypeId = unitTypeId;
	}

	public int getUnitTypeId() {
		return unitTypeId;
	}

}

enum StructureType {
	
	TOWER(1),
	BARRACK(2);
	
	private int structureType;

	private StructureType(int structureType) {
		this.structureType = structureType;
	}

	public int getStructureType() {
		return structureType;
	}

}

enum Owner {
	
	ALLY(0),
	ENEMY(1);
	
	private int ownerId;

	private Owner(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getOwnerId() {
		return ownerId;
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

final class GameBoardUtil {
	
	private GameBoardUtil() {
	}
	
	public static Map<Integer, Site> getSitesFromInitialInput(Scanner in, int numSites) {
		Map<Integer, Site> sitesById = new HashMap<>();
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
	
	public static Map<UnitType, List<Unit>> getUnitsByType(Scanner in, int numUnits) {
		Map<UnitType, List<Unit>> unitsByType = buildUnitsByType();
		List<Unit> queens = unitsByType.get(UnitType.QUEEN);
		List<Unit> knights = unitsByType.get(UnitType.KNIGHT);
		List<Unit> archers = unitsByType.get(UnitType.ARCHER);
		List<Unit> giants = unitsByType.get(UnitType.GIANT);
		
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
            
            if (unitType == UnitType.QUEEN.getUnitTypeId()) {
				queens.add(unit);
			} else if (unitType == UnitType.KNIGHT.getUnitTypeId()) {
				knights.add(unit);
			} else if (unitType == UnitType.ARCHER.getUnitTypeId()) {
				archers.add(unit);
			} else if (unitType == UnitType.GIANT.getUnitTypeId()) {
				giants.add(unit);
			}
        }
        
        return unitsByType;
	}
	
	private static Map<UnitType, List<Unit>> buildUnitsByType() {
		Map<UnitType, List<Unit>> unitsByType = new HashMap<>();
		List<Unit> queens = new ArrayList<>();
		List<Unit> knights = new ArrayList<>();
		List<Unit> archers = new ArrayList<>();
		List<Unit> giants = new ArrayList<>();
		
		unitsByType.put(UnitType.QUEEN, queens);
		unitsByType.put(UnitType.KNIGHT, knights);
		unitsByType.put(UnitType.ARCHER, archers);
		unitsByType.put(UnitType.GIANT, giants);
		
		return unitsByType;
	}
	
	public static Unit getMyQueen(Map<UnitType, List<Unit>> unitsByType) {
		List<Unit> queens = unitsByType.get(UnitType.QUEEN);
		for (Unit queen : queens) {
			if (queen.getOwner() == Owner.ALLY.getOwnerId()) {
				return queen;
			}
		}
		
		return null;
	}
	
    public static Site getNearestSite(Map<Integer, Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Site site;
        Coordinates siteCoordinates;
        for (int i = 0; i < sites.size(); i++) {
            site = sites.get(i);
            siteCoordinates = site.getCoordinates();
            distanceToSite = MathUtils.getDistanceBetweenTwoCoordinates(myQueenCoordinates, siteCoordinates);
            if (distanceToSite < distanceToNearestSite) {
                distanceToNearestSite = distanceToSite;
                nearestSite = site;
            }
        }
        return nearestSite;
    }
    
    public static Site getNearestSiteNotOwned(Map<Integer, Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Site site;
        Coordinates siteCoordinates;
        for (int i = 0; i < sites.size(); i++) {
            site = sites.get(i);
            
            if (site.getStructure().getOwner() != Owner.ALLY.getOwnerId()) {
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
    
    public static boolean isAtLeastOneSiteOwned(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe()) {
        		return true;
        	}
        }
        return false;

    }
    
    public static Site getASiteToTrain(Map<Integer, Site> sites) {
    	Site site = null;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getParam1() == 0 
        			&& site.getStructure().getStructureType() == StructureType.BARRACK.getStructureType()) {
        		return site;
        	}
        }
        return site;
    }
    
    public static boolean isAtLeastOneKnightBarrackOwnedByMe(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.BARRACK.getStructureType()
        			&& site.getStructure().getParam2() == UnitType.KNIGHT.getUnitTypeId()) {
        		return true;
        	}
        }
        return false;
    }
    
    public static Site getAKnightBarrackOwnedByMe(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.BARRACK.getStructureType()
        			&& site.getStructure().getParam2() == UnitType.KNIGHT.getUnitTypeId()) {
        		return site;
        	}
        }
        return null;
    }
    
    public static int getNumberOfTowerOwnedByMe(Map<Integer, Site> sites) {
    	int towerNumber = 0;
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.TOWER.getStructureType()) {
        		towerNumber++;
        	}
        }
        
        return towerNumber;
    }

}

