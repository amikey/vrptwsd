package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.stochastic;

import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.AbstractFeasibilityOrientedInsertion;

public class StochasticFeasibilityOrientedInsertion extends
		AbstractFeasibilityOrientedInsertion {

	@Override
	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new Tour(depot, new Vehicle(vehicleId, vehicleCapacity));
		return tour;
	}

	@Override
	protected StochasticGreedyInsertion getGreedyInsertion() {
		return new StochasticGreedyInsertion();
	}
}
