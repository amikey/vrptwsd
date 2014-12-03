package de.rwth.lofip.library.solver.metaheuristics.insertions.poisson;

import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.PoissonTour;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.solver.metaheuristics.insertions.AbstractFeasibilityOrientedInsertion;
import de.rwth.lofip.library.solver.metaheuristics.insertions.AbstractGreedyInsertion;

public class PoissonFeasibilityOrientedInsertion extends
		AbstractFeasibilityOrientedInsertion {
	@Override
	protected Tour createTourHook(Depot depot, int vehicleId,
			double vehicleCapacity) {
		Tour tour = new PoissonTour(depot, new Vehicle(vehicleId,
				vehicleCapacity));
		return tour;
	}

	@Override
	protected AbstractGreedyInsertion getGreedyInsertion() {
		return new PoissonGreedyInsertion();
	}
}