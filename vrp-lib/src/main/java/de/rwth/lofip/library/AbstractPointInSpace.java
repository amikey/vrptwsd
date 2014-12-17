package de.rwth.lofip.library;

import de.rwth.lofip.library.interfaces.SolutionElement;

public abstract class AbstractPointInSpace implements SolutionElement {

	private double xCoordinate;
	private double yCoordinate;	
    protected String id;

	public AbstractPointInSpace() {
		super();
	}

	public AbstractPointInSpace(double x, double y) {
		xCoordinate = x;
		yCoordinate = y;
		this.setId();
	}

	public double getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
		if(this.yCoordinate != 0)
			setId();
	}

	public double getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(double yCoordinate) {		
		this.yCoordinate = yCoordinate;		
		if(this.xCoordinate != 0)
			setId();	
	}
	
    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }
    
    public void setId() {
    	this.id = "PS"+this.toString();
    }
    
	@Override
	public String toString() {
		return "x: " + xCoordinate + " y: " + yCoordinate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(xCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractPointInSpace other = (AbstractPointInSpace) obj;
		if (Double.doubleToLongBits(xCoordinate) != Double
				.doubleToLongBits(other.xCoordinate))
			return false;
		if (Double.doubleToLongBits(yCoordinate) != Double
				.doubleToLongBits(other.yCoordinate))
			return false;
		return true;
	}

}
