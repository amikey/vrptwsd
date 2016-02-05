package de.rwth.lofip.library.solver.metaheuristics;

import java.io.IOException;

import de.rwth.lofip.library.parameters.Parameters;

public class AMTSWithTourElimination extends AdaptiveMemoryTabuSearch {

	@Override
	protected TabuSearchForElementWithTours getTS() {
		return new TSWithTourElimination();		 
	}	
	
	@Override
	protected void improveBestOverallSolutionWithCostMinimizationPhaseHook() throws IOException {
		System.out.println("Starte Cost MinimizationPhase");
		
		TabuSearchForElementWithTours ts = getTS();
		Parameters.setTourMinimization(false);
		ts.improve(bestOverallSolution);
		Parameters.setTourMinimization(true);
		
		publishSolution(bestOverallSolution);
	}

}
