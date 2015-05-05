package de.rwth.lofip.library.solver.util;

import de.rwth.lofip.library.Tour;

public class SimpleTourUtils {

	public static Tour getEmptyTourWithDoubleCostFactor(Tour firstTour) {
		Tour newTour = new Tour(firstTour.getDepot(), firstTour.getVehicle());
		newTour.setCostFactor(2);
		return newTour;
	}

}
