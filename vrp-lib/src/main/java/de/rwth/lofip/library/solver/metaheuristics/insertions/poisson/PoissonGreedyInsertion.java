package de.rwth.lofip.library.solver.metaheuristics.insertions.poisson;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.PoissonTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.metaheuristics.insertions.AbstractGreedyInsertion;
import de.rwth.lofip.library.solver.util.TourUtils;

public class PoissonGreedyInsertion extends AbstractGreedyInsertion {
	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new PoissonTour(depot, new Vehicle(vehicleId,
				vehicleCapacity));
		return tour;
	}

	public boolean isInsertionPossibleHook(Customer customer, Tour tour, int i,
			double approximateEquality) {
		// stochastic variant
		return TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer,
				tour, i, approximateEquality);
	}
}
