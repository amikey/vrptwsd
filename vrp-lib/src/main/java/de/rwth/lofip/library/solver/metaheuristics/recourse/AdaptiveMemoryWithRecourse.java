package de.rwth.lofip.library.solver.metaheuristics.recourse;

import java.util.Collections;
import java.util.Comparator;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatching;
		
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
		currentNewSolution = new TourMatching().matchToursToGots(currentNewSolution);
	}
}

