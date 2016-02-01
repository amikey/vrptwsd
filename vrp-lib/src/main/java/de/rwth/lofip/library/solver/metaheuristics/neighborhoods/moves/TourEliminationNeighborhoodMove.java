package de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves;

import de.rwth.lofip.library.SolutionGot;

public class TourEliminationNeighborhoodMove extends AbstractNeighborhoodMove {

	private static final long serialVersionUID = -5353530482363579099L;
	
	SolutionGot oldSolution;
	SolutionGot newSolution;

	public TourEliminationNeighborhoodMove(SolutionGot originalSolution,SolutionGot newSolution) {
		super(newSolution.getTotalDistanceWithCostFactor());
		this.oldSolution = originalSolution;
		this.newSolution = newSolution;
	}
	
	@Override
	public boolean reducesNumberOfVehicles() {
		return (newSolution.getNumberOfTours() < oldSolution.getNumberOfTours());
	}
	
	@Override 
	public boolean shortensShorterTour() {
		return false;
	}
	
	@Override
	public int shorterTourResultsInNumberOfCustomers() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean makesInfeasibleToursFeasible() {
		return false;
	}

	public SolutionGot getNewSolution() {
		return newSolution;
	}
	

}
