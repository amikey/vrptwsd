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
// @XmlRootElement(name = "Edge")
public class Edge {
	// @XmlElement(required = true)
	private AbstractPointInSpace pointOne;
	private AbstractPointInSpace pointTwo;
	// @XmlElement(required = true)
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
			length = Math.sqrt(Math.pow(
					Double.valueOf(pointTwo.getxCoordinate()
							- pointOne.getxCoordinate()), 2)
					+ Math.pow(
							Double.valueOf(pointTwo.getyCoordinate()
									- pointOne.getyCoordinate()), 2));
		} else {
			length = Double.MAX_VALUE;
		}
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}

	public double getLength() {
		return length;
	}

}
