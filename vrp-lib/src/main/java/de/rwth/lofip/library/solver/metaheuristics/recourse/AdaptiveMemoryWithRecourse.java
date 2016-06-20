package de.rwth.lofip.library.solver.metaheuristics.recourse;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatchingWithNumberOfRecourseActions;

public class AdaptiveMemoryWithRecourse extends AdaptiveMemory {
	
	@Override
	protected void labelToursWithSolutionValueAndNumberOfTours(SolutionGot solution) {
		for (Tour tour :  solution.getTours()) {
			tour.setSolutionValue(solution.getTotalDistanceWithCostFactorAndRecourse());
			tour.setSolutionNumberOfTours(solution.getNumberOfTours());
		}	
	}
	
	@Override
	protected void rematchToursAccordingToRecourseCost() {
		currentNewSolution = new TourMatchingWithNumberOfRecourseActions().matchToursToGots(currentNewSolution);
	}
}

