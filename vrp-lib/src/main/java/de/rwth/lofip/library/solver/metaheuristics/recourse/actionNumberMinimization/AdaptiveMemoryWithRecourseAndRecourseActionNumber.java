package de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.metaheuristics.recourse.AdaptiveMemoryWithRecourse;

public class AdaptiveMemoryWithRecourseAndRecourseActionNumber extends AdaptiveMemoryWithRecourse {
	
	@Override
	protected void labelToursWithSolutionValueAndNumberOfTours(SolutionGot solution) {
		for (Tour tour :  solution.getTours()) {
			tour.setSolutionValue(solution.getTotalDistanceWithCostFactorAndConvexcombinationOfRecourse());
			tour.setSolutionNumberOfTours(solution.getNumberOfTours());
		}	
	}

}
