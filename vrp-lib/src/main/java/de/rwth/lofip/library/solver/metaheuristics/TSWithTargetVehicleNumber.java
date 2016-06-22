package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.parameters.Parameters;

public class TSWithTargetVehicleNumber extends TSWithTourElimination {
	
	private int targetVehicleNumber;
	
	public TSWithTargetVehicleNumber(int tvn) {
		super();
		targetVehicleNumber = tvn;
	}
	
	@Override
	protected boolean isStoppingCriterionMet() {
		if (solution.getNumberOfTours() == targetVehicleNumber)
			return true;
		if (Parameters.isRunningTimeReached())
			return true;
		return iterationsWithoutImprovement > maxNumberIterationsWithoutImprovement;
	}

}
