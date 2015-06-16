package de.rwth.lofip.library.solver.metaheuristics.interfaces;

import de.rwth.lofip.library.SolutionGot;

public interface NeighborhoodMoveInterface {
	
	public double getCost();
	
	public double getCostDifference();
	
	public boolean reducesNumberOfVehicles();
	
	public void applyMoveToSolution(SolutionGot solution);

}
