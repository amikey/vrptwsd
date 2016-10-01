package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.parameters.Parameters;


public class AMTSWithTourElimination extends AdaptiveMemoryTabuSearch {

	@Override
	protected TabuSearchForElementWithTours getTS() {
		if (Parameters.isTourMinimizationPhase())
			return new TSWithTargetVehicleNumber(solution.getVrpProblem().getMinimalNumberOfVehiclesWrtDemand());
		else
			return new TabuSearchForElementWithTours();
	}	
	
	@Override
	protected boolean isStoppingCriterionMet() {
		if (hasBestOverallSolutionMinimalVehicleNumberWrtCapacity())
			return true;
		if (Parameters.isRunningTimeReached())
			return true;
		if (numberOfAMCallsWithoutImprovement > maximalNumberOfCallsWithoutImprovementToAdaptiveMemory)
			return true;
//		if (callsToAdaptiveMemory >= maximalNumberOfCallsToAdaptiveMemory)
//			return true;
//		if (numberOfTimesSameBestOverallSolutionHasBeenFound >= 3)
//			return true;
		return false;
	}

	private boolean hasBestOverallSolutionMinimalVehicleNumberWrtCapacity() {
		return (bestOverallSolution.getVrpProblem().getMinimalNumberOfVehiclesWrtDemand() == bestOverallSolution.getNumberOfTours());
	}
}
