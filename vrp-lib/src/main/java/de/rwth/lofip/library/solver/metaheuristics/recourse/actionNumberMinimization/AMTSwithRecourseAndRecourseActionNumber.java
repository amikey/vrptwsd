package de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization;

import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;
import de.rwth.lofip.library.util.math.MathUtils;

public class AMTSwithRecourseAndRecourseActionNumber extends AdaptiveMemoryTabuSearch {
	
	@Override
	protected AdaptiveMemory getAM() {
		return new AdaptiveMemoryWithRecourseAndRecourseActionNumber();
	}

	protected TabuSearchForElementWithTours getTS() {
		return new TSwithRecourseActionNumberMinimization();		 
	}
	
	@Override
	protected boolean isNewSolutionIsNewBestOverallSolution() {
		if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse(),
				bestOverallSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse())
			&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
				return true;
		}
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
			return true;
		}
		return false;
	}

}
