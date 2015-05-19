package de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves;

import de.rwth.lofip.library.Tour;

public class CrossNeighborhoodMove extends AbstractNeighborhoodMove {

	public CrossNeighborhoodMove(Tour tour1, Tour tour2, int posStart1,
			int posEnd1, int posStart2, int posEnd2, double cost) {
		super(tour1, tour2, posStart1, posEnd1, posStart2, posEnd2, cost);
	}

	public CrossNeighborhoodMove(double i) {
		super(i);
	}

}
