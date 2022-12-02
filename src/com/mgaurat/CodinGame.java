import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mgaurat.enums.Owner;
import com.mgaurat.enums.StructureType;
import com.mgaurat.enums.UnitType;
import com.mgaurat.model.Coordinates;
import com.mgaurat.model.Site;
import com.mgaurat.model.Structure;
import com.mgaurat.model.Unit;
import com.mgaurat.utils.MathUtils;
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
            Map<UnitType, List<Unit>> unitsByType = UnitsUtils.getUnitsByType(in, numUnits);
            Unit myQueen = UnitsUtils.getMyQueen(unitsByType);
            Coordinates myQueenCordinates = myQueen.getCoordinates();

            // Choose a target Site :
            //	- if no Site owned by me, choose the nearest
            //	- else choose the nearest not owned by me
            Site targetedSite;
            if (SitesUtils.isAtLeastOneSiteOwnedByMe(sites)) {
            	targetedSite = SitesUtils.getNearestSiteNotOwnedByMe(sites, myQueenCordinates);
            } else {
            	targetedSite = SitesUtils.getNearestSite(sites, myQueenCordinates);            	
            }
            int targetedSiteId = targetedSite.getId();
            
            // 1) First turn action is to BUILD if possible. Else is MOVE.
            //		a) BUILD when touching the targeted Site :
            //			- if I don't owned a Knight Barrack, build it
            //			- else build a Tower
            //		b) else MOVE :
            //			- if already build a sufficient number of Structures, move back to the Knight Barrack
            //			- else move to the targeted site
            if (touchedSite == targetedSiteId) {
            	if (!StructuresUtils.isAtLeastOneKnightBarrackOwnedByMe(sitesById)) {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureType.BARRACKS, UnitType.KNIGHT);
            	} else {
            		SystemOutUtils.printBuildAction(targetedSiteId, StructureType.TOWER, null);
            	}
            } else if (StructuresUtils.getNumberOfTowerOwnedByMe(sitesById) == 6) {
            	Site knightBarrack = StructuresUtils.getAKnightBarrackOwnedByMe(sitesById);
            	SystemOutUtils.printMoveAction(knightBarrack.getCoordinates());
            } else {	
            	SystemOutUtils.printMoveAction(targetedSite.getCoordinates());
            }

            // 2) Second turn action is to TRAIN :
            //		- if there is a site available for training, do it
            //		- else train nothing
            Site siteToTrain = SitesUtils.getASiteToTrain(sites);
            if (siteToTrain != null) {
                SystemOutUtils.printTrainAction(targetedSiteId);
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
    
    public static Site getNearestSiteNotOwnedByMe(Collection<Site> sites, Coordinates myQueenCoordinates) {
        Site nearestSite = null;
        double distanceToSite;
        double distanceToNearestSite = Double.MAX_VALUE;
        Coordinates siteCoordinates;
        for (Site site : sites) {            
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
        			&& site.getStructure().getStructureType() == StructureType.BARRACKS.getStructureType()) {
        		return site;
        	}
        }
        return null;
    }

}

final class StructuresUtils {
	
	private StructuresUtils() {
	}
	
    public static boolean isAtLeastOneKnightBarrackOwnedByMe(Map<Integer, Site> sites) {
    	Site site;
        for (int i = 0; i < sites.size(); i++) {
        	site = sites.get(i);
        	if (site.getStructure().isOwnedByMe() 
        			&& site.getStructure().getStructureType() == StructureType.BARRACKS.getStructureType()
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
        			&& site.getStructure().getStructureType() == StructureType.BARRACKS.getStructureType()
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

final class UnitsUtils {
	
	private UnitsUtils() {
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

}

final class SystemOutUtils {
	
	private SystemOutUtils() {
	}
	
	public static void printBuildAction(int targetedSiteId, StructureType structureType, UnitType unitType) {
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
		sb.append(" ");
		sb.append(siteId);
		
		System.out.println(sb.toString());
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

enum StructureType {
	
	TOWER(1),
	BARRACKS(2);
	
	private int structureTypeId;

	private StructureType(int structureTypeId) {
		this.structureTypeId = structureTypeId;
	}

	public int getStructureType() {
		return structureTypeId;
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

