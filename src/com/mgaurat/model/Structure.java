package com.mgaurat.model;

/**
 * Structure that is built on a Site.
 * 
 * @author mgaurat
 *
 */
public class Structure {
	
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

}
