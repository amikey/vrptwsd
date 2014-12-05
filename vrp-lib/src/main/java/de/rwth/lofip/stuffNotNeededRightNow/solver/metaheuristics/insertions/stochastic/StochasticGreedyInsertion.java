package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.metaheuristics.insertions.AbstractGreedyInsertion;
import de.rwth.lofip.library.solver.util.TourUtils;

/**
 * Implement the Greedy Insertion as described by Lei et al. in section 3.3.5 of
 * their paper.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class StochasticGreedyInsertion extends AbstractGreedyInsertion {

	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new Tour(depot, new Vehicle(vehicleId, vehicleCapacity));
		return tour;
	}

	public boolean isInsertionPossibleHook(Customer customer, Tour tour, int i,
			double approximateEquality) {
		// stochastic variant
		return TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(customer,
				tour, i, approximateEquality);
	}
}
