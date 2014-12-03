package de.rwth.lofip.library.solver.metaheuristics.insertions.util;

import java.util.Comparator;

import de.rwth.lofip.library.Tour;

public class DeterministicTourComparatorWrtDemand implements Comparator<Tour> {

	@Override
	public int compare(Tour t1, Tour t2) {
		if (t1.getDemandOnTour() < t2.getDemandOnTour())
			return -1;
		if (t1.getDemandOnTour() > t2.getDemandOnTour())
			return 1;
		if (t1.getId() < t2.getId())
			return -1;
		if (t1.getId() > t2.getId())
			return 1;
		return 0;
	}

}
