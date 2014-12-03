package de.rwth.lofip.library.solver.metaheuristics.insertions.util;

import java.util.Comparator;

import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.CustomerInTour;

public class StochasticTourComparatorWrtFailureProbability implements
		Comparator<Tour> {

	@Override
	public int compare(Tour t1, Tour t2) {
		// calculate the probability, that the demand of each tour
		// is higher than the vehicle capacity
		double failureProbabilityTour1 = 0;
		double failureProbabilityTour2 = 0;
		if (t1.getCustomers().size() > 1) {
			for (int i = 1; i < t1.getCustomers().size(); i++) {
				CustomerInTour c = t1.getCustomersInTour().get(i);
				failureProbabilityTour1 += t1
						.getProbabilityOfFailureAtCustomer(c).doubleValue();
			}
		}
		if (t2.getCustomers().size() > 1) {
			for (int i = 1; i < t2.getCustomers().size(); i++) {
				CustomerInTour c = t2.getCustomersInTour().get(i);
				failureProbabilityTour2 += t2
						.getProbabilityOfFailureAtCustomer(c).doubleValue();
			}
		}

		if (failureProbabilityTour1 < failureProbabilityTour2) {
			return -1;
		}
		if (failureProbabilityTour1 > failureProbabilityTour2) {
			return 1;
		}
		if (t1.getId() < t2.getId())
			return -1;
		if (t1.getId() > t2.getId())
			return 1;
		return 0;
	}

}
