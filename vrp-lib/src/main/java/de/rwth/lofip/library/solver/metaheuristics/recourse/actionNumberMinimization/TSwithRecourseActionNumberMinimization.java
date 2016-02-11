package de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization;

import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooListAndRecourseWithNumberOfRecourseActions;
import de.rwth.lofip.library.solver.metaheuristics.recourse.TabuSearchWithRecourse;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatching;
import de.rwth.lofip.library.util.math.MathUtils;

public class TSwithRecourseActionNumberMinimization extends TabuSearchWithRecourse {
		
	@Override
	protected CrossNeighborhoodWithTabooList getCrossNeighborhood() {
		return new CrossNeighborhoodWithTabooListAndRecourseWithNumberOfRecourseActions(solution);
	}
	
	@Override
	protected boolean isNewSolutionIsNewBestOverallSolution() {
		if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse(),
				bestOverallSolution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse())
				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours())
			return true;
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours())
			return true;
		return false;	
	}

	@Override
	protected TourMatching getTourMatching() {
		return new TourMatching();
	}
}
