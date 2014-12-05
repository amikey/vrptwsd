package de.rwth.lofip.library.solver.initialSolver;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;

/**
 * A start heuristic, based on the Push Forward Insertion by Solomon (1987).
 * This is a slightly modified version by Lei et al. (2011).
 * 
 * This heurisic respects time window and capacity constrains but cannot
 * guarantee the use of a certain number of vehicles.
 * 
 * @author Dominik Sandjaja <dominik@dadadom.de>
 * 
 */
public class DeterministicPushForwardInsertionSolver extends
		AbstractPushForwardInsertionSolver {

	@Override
	protected CustomerWithCost calculateCustomerHook(Customer customer,
			Tour tour, double approximateEquality) {
		return TourUtils.calculateCostDeterministicSolver(customer, tour);
	}

	@Override
	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new DeterministicTour(depot, new Vehicle(vehicleId,
				vehicleCapacity));
		return tour;
	}

}
