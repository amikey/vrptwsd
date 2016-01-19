package de.rwth.lofip.library.solver.metaheuristics.recourse;

import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;
import de.rwth.lofip.library.util.math.MathUtils;

public class AdaptiveMemoryTabuSearchWithRecourse extends AdaptiveMemoryTabuSearch {
	
	@Override
	protected AdaptiveMemory getAM() {
		return new AdaptiveMemoryWithRecourse();
	}

	protected TabuSearchForElementWithTours getTS() {
		return new TabuSearchWithRecourse();		 
	}
	
	@Override
	protected boolean isNewSolutionIsNewBestOverallSolution() {
//		assertEquals(false, solution.equals(bestOverallSolution));
		if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndRecourse(),bestOverallSolution.getTotalDistanceWithCostFactorAndRecourse())
			&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
				return true;
		}
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
			return true;
		}
		return false;
	}
}
