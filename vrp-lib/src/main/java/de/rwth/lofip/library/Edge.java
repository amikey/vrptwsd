package de.rwth.lofip.library;

import de.rwth.lofip.library.util.CustomerInTour;

/**
 * An {@code Edge} represents the distance between two points. These two points
 * may either be two customers or it may be an Edge between a customer and the
 * depot.
 * 
 * The purpose of this class is to calculate the distance between two customers
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class Edge {
	private AbstractPointInSpace pointOne;
	private AbstractPointInSpace pointTwo;
	private double length;

	/**
	 * Konstruktoren
	 */
	public Edge() {
		super();
	}

	public Edge(CustomerInTour pointOne, CustomerInTour pointTwo) {
		this(pointOne.getCustomer(), pointTwo.getCustomer());
	}

	public Edge(CustomerInTour pointOne, AbstractPointInSpace pointTwo) {
		this(pointOne.getCustomer(), pointTwo);
	}

	public Edge(AbstractPointInSpace pointOne, CustomerInTour pointTwo) {
		this(pointOne, pointTwo.getCustomer());
	}

	public Edge(AbstractPointInSpace pointOne, AbstractPointInSpace pointTwo) {
		this.pointOne = pointOne;
		this.pointTwo = pointTwo;
		recalculateLength();
	}

	/**
	 * Konstruktoren ENDE
	 */

	public AbstractPointInSpace getPointOne() {
		return pointOne;
	}

	public void setPointOne(AbstractPointInSpace pointOne) {
		this.pointOne = pointOne;
		recalculateLength();
	}

	public AbstractPointInSpace getPointTwo() {
		return pointTwo;
	}

	public void setPointTwo(AbstractPointInSpace pointTwo) {
		this.pointTwo = pointTwo;
		recalculateLength();
	}

	/**
	 * Calculate the length of this edge. This is just the Euclidian distance
	 * between the two points.
	 */
	private void recalculateLength() {
		if (pointOne != null && pointTwo != null) {
			double xCoordinatePoint1 = pointOne.getxCoordinate();
			double xCoordinatePoint2 = pointTwo.getxCoordinate();
			double xDifference = Double.valueOf(xCoordinatePoint2 - xCoordinatePoint1);
			
			double yDifference = Double.valueOf(pointTwo.getyCoordinate()- pointOne.getyCoordinate());
		
			length = Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
		} else {
			length = Double.MAX_VALUE;
		}
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getLength() {
		return length;
	}

}
