package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import de.rwth.lofip.library.Tour;

public class CrossNeighborhoodMove {
	
	//helper class to store a cross move including its cost
	
	private Tour tour1;
	private Tour tour2;
	private int positionStartOfSegmentTour1;
	private int positionEndOfSegmentTour1;
	private int positionStartOfSegmentTour2;
	private int positionEndOfSegmentTour2;
	private double costOfMove;
	
	public CrossNeighborhoodMove(Tour tour1, Tour tour2, int posStart1, int posEnd1, int posStart2, int posEnd2, double cost) {
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
}
