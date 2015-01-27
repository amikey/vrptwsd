package de.rwth.lofip.library;

public class Depot extends AbstractPointInSpace {

	public Depot() {}
	
	public Depot(double d, double e) {
        this.setxCoordinate(d);
        this.setyCoordinate(e);
        this.setId();
	}

	@Override
	public void setId() {
    	this.id ="D"+Math.round(Math.random()*1000)+" PS"+this.toString();
    }

}
