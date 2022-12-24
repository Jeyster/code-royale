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
	public boolean isInForwardDirection(Coordinates startingAllyQueenCoordinates) {
		final int Y_GAP = 150;
        boolean isStartingLeftSide = GameBoardUtils.isLeftSide(startingAllyQueenCoordinates);
		return (isStartingLeftSide && this.getCoordinates().getX() > startingAllyQueenCoordinates.getX() 
				&& this.getCoordinates().getY() < startingAllyQueenCoordinates.getY() + Y_GAP)
    			|| (!isStartingLeftSide && this.getCoordinates().getX() < startingAllyQueenCoordinates.getX()
    			&& this.getCoordinates().getY() > startingAllyQueenCoordinates.getY() - Y_GAP);
	}
	
	/**
	 * Check if there is no gold left in the Site and if a enemy KNIGHT BARRACKS is too close.
	 * 
	 * @param enemyKnightBarrackSites
	 * @param remainingGold
	 * @return boolean
	 */
	public boolean isAllowedToBuildMine(Collection<Site> enemyKnightBarrackSites, Integer remainingGold) {
		return !this.isCloseToNearestEnemyKnightBarracksSite(enemyKnightBarrackSites) 
				&& ((remainingGold == null && this.getStructure().getMineGold() != 0) || (remainingGold != null && remainingGold > 0));
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
    
    public boolean isEnemyKnightBarracksDangerous(Coordinates safestCoordinates, Collection<Site> enemyKnightBarracksSites) {
    	Site nearestEnemyKnightBarracks = SitesUtils.getNearestSiteFromCoordinates(enemyKnightBarracksSites, this.getCoordinates());
    	if (nearestEnemyKnightBarracks == null) {
    		return false;
    	}
    	
    	return nearestEnemyKnightBarracks.getStructure().isBarracksInTraining() 
    			&& MathUtils.getDistanceBetweenTwoCoordinates(this.getCoordinates(), safestCoordinates) >= MathUtils.getDistanceBetweenTwoCoordinates(this.getCoordinates(), nearestEnemyKnightBarracks.getCoordinates());
    }

}
