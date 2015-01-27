package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.deterministic;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.AbstractGreedyInsertion;

/**
 * Implement a deterministic version of the Greedy Insertion.
 * 
 * @author Andreas Braun <braun@dpor.rwth-aachen.de>
 */
public class DeterministicGreedyInsertion extends AbstractGreedyInsertion {

	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new DeterministicTour(depot, new Vehicle(vehicleId,
				vehicleCapacity));
		return tour;
	}

	public boolean isInsertionPossibleHook(Customer customer, Tour tour, int i,
			double approximateEquality) {
		// stochastic variant
		return TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(
				customer, tour, i);
	}

}
