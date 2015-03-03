package de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves;

import de.rwth.lofip.library.Tour;

public class AbstractNeighborhoodMove {
	
	//helper class to store a cross move including its cost
	
	private Tour tour1;
	private Tour tour2;
	private int positionStartOfSegmentTour1;
	private int positionEndOfSegmentTour1;
	private int positionStartOfSegmentTour2;
	private int positionEndOfSegmentTour2;
	private double costOfMove;
	
	public AbstractNeighborhoodMove(Tour tour1, Tour tour2, int posStart1, int posEnd1, int posStart2, int posEnd2, double cost) {
		this.tour1 = tour1;
		this.tour2 = tour2;
		this.positionStartOfSegmentTour1 = posStart1;
		this.positionEndOfSegmentTour1 = posEnd1;
		this.positionStartOfSegmentTour2 = posStart2;
		this.positionEndOfSegmentTour2 = posEnd2;
		this.costOfMove = cost;
	}

	public double getCost() {
		return costOfMove;
	}

	public Tour getTour1() {
		return tour1;
	}

	public Tour getTour2() {
		return tour2;
	}

	public int getStartPositionTour1() {
		return positionStartOfSegmentTour1;
	}

	public int getEndPositionTour1() {
		return positionEndOfSegmentTour1;
	}

	public int getStartPositionTour2() {
		return positionStartOfSegmentTour2;	
	}

	public int getEndPositionTour2() {
		return positionEndOfSegmentTour2;
	}

	public void print() {
		System.out.println("Tour1: " + tour1.getId());
		System.out.println("Positionen: " + positionStartOfSegmentTour1 + ", " + positionEndOfSegmentTour1);
		System.out.println("Tour2: " + tour2.getId());
		System.out.println("Positionen: " + positionStartOfSegmentTour2 + ", " + positionEndOfSegmentTour2);		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractNeighborhoodMove other = (AbstractNeighborhoodMove) obj;
		if (Double.doubleToLongBits(costOfMove) != Double
				.doubleToLongBits(other.costOfMove))
			return false;
		if (positionEndOfSegmentTour1 != other.positionEndOfSegmentTour1)
			return false;
		if (positionEndOfSegmentTour2 != other.positionEndOfSegmentTour2)
			return false;
		if (positionStartOfSegmentTour1 != other.positionStartOfSegmentTour1)
			return false;
		if (positionStartOfSegmentTour2 != other.positionStartOfSegmentTour2)
			return false;
		if (tour1 == null) {
			if (other.tour1 != null)
				return false;
		} else if (!tour1.equals(other.tour1))
			return false;
		if (tour2 == null) {
			if (other.tour2 != null)
				return false;
		} else if (!tour2.equals(other.tour2))
			return false;
		return true;
	}

	public boolean isInnerTourMove() {
		return tour1.equals(tour2);
	}
}
