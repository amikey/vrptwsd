package de.rwth.lofip.library.solver.metaheuristics.recourse;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooListAndRecourse;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatching;
import de.rwth.lofip.library.util.math.MathUtils;

public class TabuSearchWithRecourse extends TabuSearchForElementWithTours {
	
	private int iterationsWithoutRematching = 0;

	@Override
	protected CrossNeighborhoodWithTabooList getCrossNeighborhood() {
		return new CrossNeighborhoodWithTabooListAndRecourse(solution);
	}
	
	@Override
	protected boolean isNewSolutionIsNewBestOverallSolution() {
		if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndRecourse(), bestOverallSolution.getTotalDistanceWithCostFactorAndRecourse())
				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours())
			return true;
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours())
			return true;
		return false;	
	}
	
	@Override
	protected void tryToImproveSolutionWithRematchingPhaseHook() {
		if (isRematchingPhaseTurn()) {
			solution = new TourMatching().matchToursToGots((SolutionGot) solution);
			iterationsWithoutRematching = 0;
		} else 
			iterationsWithoutRematching++;
	}
	
	private boolean isRematchingPhaseTurn() {
		return (iterationsWithoutRematching >= Parameters.getNumberOfIterationsWithoutRematching());
	}

	@Override
	protected void tryToImproveNewBestSolutionWithIntensificationPhase() {
		// do nothing; intensification phase not applicable for recourse
		// would have to implement intensification phase that caters for recourse
	}

}
