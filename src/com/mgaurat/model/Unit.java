package com.mgaurat.model;

import com.mgaurat.enums.UnitEnum;
import com.mgaurat.utils.MathUtils;

/**
 * Class for a game Unit.
 * It is a bean that is instanciated from the game turn input.
 * 
 * @author mgaurat
 *
 */
public class Unit {
	
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
	private int speed;

	public Unit(Coordinates coordinates, int owner, int unitType, int health) {
		super();
		this.coordinates = coordinates;
		this.owner = owner;
		this.unitType = unitType;
		this.health = health;
		this.speed = evaluateSpeed();
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
	
	public int evaluateSpeed() {
		if (this.unitType == UnitEnum.QUEEN.getId()) {
			return 60;
		} else if (this.unitType == UnitEnum.KNIGHT.getId()) {
			return 100;
		} else if (this.unitType == UnitEnum.GIANT.getId()) {
			return 50;
		} else if (this.unitType == UnitEnum.ARCHER.getId()) {
			return 75;
		}
		return 0;
	}
	
	public int getSpeed() {
		return this.speed;
	}
	
	public boolean canReachSiteSomeTurnsBeforeUnit(Site site, Unit unit, double turnsNumber) {
		if (unit == null) {
			return true;
		}
		
		double distanceFromSite = MathUtils.getDistanceBetweenTwoCoordinates(this.getCoordinates(), site.getCoordinates());
		double distanceFromUnit = MathUtils.getDistanceBetweenTwoCoordinates(this.getCoordinates(), unit.getCoordinates());
		double timeToGo = distanceFromSite / this.getSpeed();
		double timeToGoForUnit = distanceFromUnit / unit.getSpeed();
		return timeToGoForUnit - timeToGo > turnsNumber;
	}
 
}
