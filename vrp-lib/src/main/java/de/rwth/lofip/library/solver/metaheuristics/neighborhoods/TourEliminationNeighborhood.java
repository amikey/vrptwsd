package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public class TourEliminationNeighborhood extends AbstractNeighborhood {
	
	public AbstractNeighborhoodMove returnBestMoveOn(SolutionGot solution, int iteration) {
		for (int i = 0; i < solution.getNumberOfTours(); i++) {
			if (solution.getTour(i).getCustomerSize() <= Parameters.getMaximalNumberOfCustomersForDeletionInTourEliminationNeighborhood()) {
				SolutionGot solutionClone = solution.clone();
				List<Customer> customers = solutionClone.deleteTour(i);
				new GreedyInsertion().insertCustomers(solutionClone, customers);
				TourEliminationNeighborhoodMove move = new TourEliminationNeighborhoodMove(solution, solutionClone);
				if (isMoveNewBestMove(move))
					if (!isMoveTaboo(move, iteration))
						setRespAddBestNonTabooMove(move);
			}
		}
	}

}
