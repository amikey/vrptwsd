package de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberAndAddTours;

import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.recourse.TabuSearchWithRecourse;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatchingWithNumberOfRecourseActions;
import de.rwth.lofip.library.util.math.MathUtils;

public class TSwithRecourseAndRecActNumAndAddTours extends TabuSearchWithRecourse {

	@Override
	protected CrossNeighborhoodWithTabooList getCrossNeighborhood() {
		return new CrossNieghborhoodWithTabooListAndRecourseWithRecActNumAndAddTours();
	}
	
	@Override 
	protected boolean isNewSolutionIsNewBestOverallSolution() {
		if (MathUtils.lessThan(solution.getTotalDistanceWithRecourseWithRecActNumAndAddTours(), bestOverallSolution.getTotalDistanceWithRecourseWithRecActNumAndAddTours()))
			return true;
		return false;
	}
	
	@Override
	protected TourMatchingWithNumberOfRecourseActions getTourMatching() {
		return new TourMatchingWithRecActNumAndAddTours();
	}
}
