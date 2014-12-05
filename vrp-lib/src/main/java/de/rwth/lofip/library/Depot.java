package de.rwth.lofip.library;

public class Depot extends AbstractPointInSpace {

	@Override
	public void setId() {
    	this.id ="D"+Math.round(Math.random()*1000)+" PS"+this.toString();
    }

}
