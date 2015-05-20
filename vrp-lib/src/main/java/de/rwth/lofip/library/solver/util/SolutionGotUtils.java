package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;

public class SolutionGotUtils {
	
	public static boolean isSolutionFeasibleWrtDemand(SolutionGot solution){
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtDemand(tour))
				return false;
		}
		return true;
	}
	
	public static boolean isSolutionFeasibleWrtTW(SolutionGot solution){
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtTW(tour))
				return false;
		}
		return true;
	}
	
}
