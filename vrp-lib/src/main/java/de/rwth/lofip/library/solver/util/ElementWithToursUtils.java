package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.ElementWithTours;

public class ElementWithToursUtils {

	public static boolean isElementDemandFeasible(ElementWithTours solution) {
		boolean feasible = true;
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtDemand(tour))
				feasible = false;			
		}
		return feasible;
	}
	
	public static boolean isElementDemandFeasibleCheckWithRef(ElementWithTours solution) {
		boolean feasible = true;
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtDemandCheckWithRef(tour))
				feasible = false;			
		}
		return feasible;
	}
	
	public static boolean isElementTWFeasible(ElementWithTours solution) {
		boolean feasible = true;
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtTW(tour))
				feasible = false;			
		}
		return feasible;
	}	

}
