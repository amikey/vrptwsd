package de.rwth.lofip.library.solver.metaheuristics.recourse;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooListAndRecourse;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatching;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.math.MathUtils;

public class TabuSearchWithRecourse extends TabuSearchForElementWithTours {
	
	protected int iterationsWithoutRematching = 0;

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
	protected void tryToImproveSolutionWithRematchingPhaseHook() throws IOException {
		if (isRematchingPhaseTurn()) {
			TourMatching tm = getTourMatching();
			solution = tm.matchToursToGots((SolutionGot) solution);
			iterationsWithoutRematching = 0;
		} else 
			iterationsWithoutRematching++;
	}
	
	protected TourMatching getTourMatching() {
		return new TourMatching(){
						@Override
						protected void sortListOfRecourseCostsAccordingToConvexCombinationOfRecourseCostAndNumberOfRecourseActions() {		
							Comparator<RecourseCost> byCombinationOfCostAndNumberRecourseActions = (e1,e2) -> Double.compare(e1.getRecourseCost(),e2.getRecourseCost());		
							Collections.sort(listOfRecourseCosts, byCombinationOfCostAndNumberRecourseActions);						
						}
						@Override
						protected SolutionGot returnEitherNewOrOldSolutionDependingOnWhichHasLessCost() {
							if (newSolution.getTotalDistanceWithCostFactorAndRecourse() <= 
									oldSolution.getTotalDistanceWithCostFactorAndRecourse())	
								return newSolution;
							else
								return oldSolution;
						}
					};
	}

	protected boolean isRematchingPhaseTurn() {
		return (iterationsWithoutRematching >= Parameters.getNumberOfIterationsWithoutRematching());
	}

	@Override
	protected void tryToImproveNewBestSolutionWithIntensificationPhase() {
		// do nothing; intensification phase not applicable for recourse
		// would have to implement intensification phase that caters for recourse
	}

}
