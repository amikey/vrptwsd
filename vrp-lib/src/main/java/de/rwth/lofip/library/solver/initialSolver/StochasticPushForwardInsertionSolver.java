package de.rwth.lofip.library.solver.initialSolver;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.util.CustomerWithCost;
import de.rwth.lofip.library.solver.util.TourUtils;

/**
 * This is a stochastic version of the PFI heuristic that respects the condition
 * demand on tour < twice vehicle capacity with probability approximateEquality.
 * 
 * @author Andreas Braun <dominik@dadadom.de>
 */
public class StochasticPushForwardInsertionSolver extends
		AbstractPushForwardInsertionSolver {

	@Override
	protected CustomerWithCost calculateCustomerHook(Customer customer,
			Tour tour, double approximateEquality) {
		return TourUtils.calculateCostStochasticSolver(customer, tour,
				approximateEquality);
	}

	@Override
	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new Tour(depot, new Vehicle(vehicleId, vehicleCapacity));
		return tour;
	}
}
