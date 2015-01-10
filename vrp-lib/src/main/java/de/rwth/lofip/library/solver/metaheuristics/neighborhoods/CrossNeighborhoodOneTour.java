package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.LinkedList;
import java.util.List;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;

public class CrossNeighborhoodOneTour {

	//this neighborhood performs the cross neighborhood where no customers are removed from one tour
	
	private static Tour tour1;
	private static Tour tour2;
	private SolutionGot solution;
	
	@Override
	public CrossNeighborhoodMove returnBestMove() {
		List<CrossNeighborhoodMove> moves = new LinkedList<CrossNeighborhoodMove>();
		for (int tourCounter1 = 0; tourCounter1 < solution.getNumberOfTours()-1; tourCounter1++)
			for (int tourCounter2 = tourCounter1+1; tourCounter2 < solution.getNumberOfTours()-1; tourCounter2++)
				
	}
}
