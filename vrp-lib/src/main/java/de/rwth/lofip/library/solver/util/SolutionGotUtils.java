package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;

public class SolutionGotUtils {

	public static boolean isSolutionDemandFeasible(SolutionGot solution) {
		boolean feasible = true;
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtDemand(tour))
				feasible = false;			
		}
		return feasible;
	}
	
	public static boolean isSolutionTWFeasible(SolutionGot solution) {
		boolean feasible = true;
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtTW(tour))
				feasible = false;			
		}
		return feasible;
	}	

}
