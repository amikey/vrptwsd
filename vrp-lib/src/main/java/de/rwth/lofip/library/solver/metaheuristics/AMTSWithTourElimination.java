package de.rwth.lofip.library.solver.metaheuristics;

public class AMTSWithTourElimination extends AdaptiveMemoryTabuSearch {

	@Override
	protected TabuSearchForElementWithTours getTS() {
		return new TSWithTourElimination();		 
	}	
}
