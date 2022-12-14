package com.mgaurat.model;

import java.util.Collection;

import com.mgaurat.enums.GameBoardQuarterEnum;
import com.mgaurat.utils.GameBoardUtils;
import com.mgaurat.utils.MathUtils;
import com.mgaurat.utils.SitesUtils;

/**
 * Site from the game board.
 * Geometrical properties from the inital input (do not change during the game).
 * Hold a Structure (does change during the game).
 * 
 * @author mgaurat
 *
 */
public class Site {
	
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
	
	public boolean isEmpty() {
		return this.structure.getParam1() == -1;
	}
	
	public boolean isItsCoordinates(Coordinates coordinates) {
		return this.getCoordinates().equals(coordinates);
	}
	
	/**
	 * Check if this is in a direction towards the enemy camp.
	 * We consider that startingAllyQueenCoordinates determined the camps side.
	 * We stay in a narrow Y band defined by Y_GAP.
	 * 
	 * @param startingAllyQueenCoordinates
	 * @return
	 */
	public boolean isInBandForwardDirection(Coordinates startingAllyQueenCoordinates) {
		final int Y_GAP = 100;
        boolean isStartingLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
		return (isStartingLeftSide && this.getCoordinates().getX() > startingAllyQueenCoordinates.getX() 
				&& this.getCoordinates().getY() < startingAllyQueenCoordinates.getY() + Y_GAP)
    			|| (!isStartingLeftSide && this.getCoordinates().getX() < startingAllyQueenCoordinates.getX()
    			&& this.getCoordinates().getY() > startingAllyQueenCoordinates.getY() - Y_GAP);
	}
	
	/**
	 * Check if this is in a direction towards the enemy camp.
	 * We consider that startingAllyQueenCoordinates determined the camps side.
	 * 
	 * @param allyQueenCoordinates
	 * @param startingAllyQueenCoordinates
	 * @return boolean
	 */
	public boolean isInForwardDirection(Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
        boolean isStartingLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
		return (isStartingLeftSide && this.getCoordinates().getX() > allyQueenCoordinates.getX())
    			|| (!isStartingLeftSide && this.getCoordinates().getX() < allyQueenCoordinates.getX());
	}
	
	/**
	 * Check if there is no gold left in the Site and if a enemy KNIGHT BARRACKS is too close.
	 * 
	 * @param enemyKnightBarrackSites
	 * @param remainingGold
	 * @return boolean
	 */
	public boolean isAllowedToBuildMine(Collection<Site> enemyKnightBarrackSites, Integer remainingGold, Collection<Site> allyTowers, Collection<Site> enemyMines) {
		return (this.isAllyTowerProtection(enemyKnightBarrackSites, allyTowers) || enemyMines.isEmpty())
				&& ((remainingGold == null && this.getStructure().getMineGold() != 0) || (remainingGold != null && remainingGold > 5));
	}
	
	/**
	 * Check if there is there is ally TOWER between this and enemy KNIGHT BARRACKS.
	 * 
	 * @param enemyKnightBarrackSites
	 * @param allyTowers
	 * @return boolean
	 */
	public boolean isAllyTowerProtection(Collection<Site> enemyKnightBarrackSites, Collection<Site> allyTowers) {
		Site nearestEnemyKnightBarracks = SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarrackSites, this.getCoordinates());
		if (nearestEnemyKnightBarracks == null) {
			return true;
		}
		
		return allyTowers
				.stream()
				.filter(tower -> tower.getCoordinates().isBetweenTwoXCoordinates(this.getCoordinates(), nearestEnemyKnightBarracks.getCoordinates()))
				.count() >= 3;
	}
		
    /**
     * Check if site is close (distance <= SAFE_DISTANCE) to the nearest enemy KNIGHT BARRACKS.
     * 
     * @param enemyKnightBarracksSites
     * @return boolean
     */
    public boolean isCloseToNearestEnemyKnightBarracksSite(Collection<Site> enemyKnightBarracksSites) {
    	if (enemyKnightBarracksSites.isEmpty()) {
    		return false;
    	}
    	
    	return this.isCloseToAnotherSite(SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarracksSites, this.getCoordinates()));
    }
    
    /**
     * Check if the distance between 2 Sites is less or equal to constant SAFE_DISTANCE.
     * 
     * @param site
     * @return boolean
     */
    public boolean isCloseToAnotherSite(Site site) {
    	final double SAFE_DISTANCE = 300;
    	if (site == null) {
    		return false;
    	}
    	
    	return MathUtils.getDistanceBetweenTwoCoordinates(this.getCoordinates(), site.getCoordinates()) <= SAFE_DISTANCE;
    }
    
    /**
     * Check if this is in the corner we want to build the firsts TOWER.
     * If my QUEEN starts left, it is the bottom left corner.
     * If my QUEEN starts right, it is the top right corner.
     * 
     * @param startingAllyQueenCoordinates
     * @return boolean
     */
    public boolean isInFirstsTowersBuildCorner(Coordinates startingAllyQueenCoordinates, Coordinates boardGameCenter) {
    	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
		GameBoardQuarterEnum siteBoardGameQuarter = GameBoardUtils.getQuarterOfCoordinatesWithRespectToAnotherCoordinates(this.getCoordinates(), boardGameCenter);
		return (isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.BOTTOMLEFT))
				|| (!isLeftSide && siteBoardGameQuarter.equals(GameBoardQuarterEnum.TOPRIGHT));
    }
    
    public boolean isBehindEnemyLine(Unit allyQueen, Collection<Site> enemySites) {
    	boolean output =  enemySites
    			.stream()
    			.anyMatch(site -> site.getCoordinates().isBetweenTwoXCoordinates(this.getCoordinates(), allyQueen.getCoordinates()));
    	
    	if (this.getId() == 10) {
    		System.err.println(output);
    	}
    	
    	return output;
    }
    
    public boolean isGoodAngleToBuildFirstTower(Coordinates allyQueenCoordinates, Coordinates startingAllyQueenCoordinates) {
    	final int X_LENGTH = 1920;
    	final int Y_LENGTH = 1000;
    	boolean isLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
    	Coordinates siteInRefOfQueen = MathUtils.galileanTransformation(coordinates, allyQueenCoordinates);
    	return (isLeftSide && siteInRefOfQueen.getY() >= 0 
    			&& (siteInRefOfQueen.getX() == 0 || (siteInRefOfQueen.getY() / siteInRefOfQueen.getX() >= Y_LENGTH / X_LENGTH)))
    			|| (!isLeftSide && siteInRefOfQueen.getY() <= 0
    			&& (siteInRefOfQueen.getX() == 0 || (siteInRefOfQueen.getY() / siteInRefOfQueen.getX() >= Y_LENGTH / X_LENGTH)));
    }

}
