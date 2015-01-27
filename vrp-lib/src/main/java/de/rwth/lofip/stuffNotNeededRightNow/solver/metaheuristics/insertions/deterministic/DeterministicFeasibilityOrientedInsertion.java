package de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.deterministic;

import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.DeterministicTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.AbstractFeasibilityOrientedInsertion;
import de.rwth.lofip.stuffNotNeededRightNow.solver.metaheuristics.insertions.AbstractGreedyInsertion;

public class DeterministicFeasibilityOrientedInsertion extends
		AbstractFeasibilityOrientedInsertion {

	@Override
	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new DeterministicTour(depot, new Vehicle(vehicleId,
				vehicleCapacity));
		return tour;
	}

	@Override
	protected AbstractGreedyInsertion getGreedyInsertion() {
		return new DeterministicGreedyInsertion();
	}
}
