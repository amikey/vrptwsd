package de.rwth.lofip.stuffNotNeededRightNow.solver.initialSolver;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.stuffNotNeededRightNow.solver.util.CustomerWithCost;

public class PushForwardInsertionSolver extends
		AbstractPushForwardInsertionSolver {

	@Override
	protected CustomerWithCost calculateCustomerHook(Customer customer,
			Tour tour, double approximateEquality) {
		return TourUtils.calculateCost(customer, tour);
	}

	@Override
	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new Tour(depot, new Vehicle(vehicleId, vehicleCapacity));
		return tour;
	}
}
