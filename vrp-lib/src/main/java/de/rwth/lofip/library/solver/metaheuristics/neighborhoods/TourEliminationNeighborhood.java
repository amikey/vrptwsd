package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;

public class TourEliminationNeighborhood extends AbstractNeighborhood {
	
	public TourEliminationNeighborhood(ElementWithTours solution) {
		this.elementWithTours = solution;
		bestNonTabooMove = null;
		//bestMoveThatMightBeTaboo existiert nur, um unterscheiden zu können, ob es keine zulässingen Moves gibt, oder ob alle Moves tabu sind.
		bestMoveThatMightBeTaboo = null;		
	}

	public AbstractNeighborhoodMove returnBestMove(int iteration) {
		for (int i = 0; i < elementWithTours.getNumberOfTours(); i++) {
			if (elementWithTours.getTour(i).getCustomerSize() <= Parameters.getMaximalNumberOfCustomersForDeletionInTourEliminationNeighborhood()) {
				for (int j = 0; j  < Parameters.getNumberOfInsertionTries(); j++) {
					SolutionGot solutionClone = (SolutionGot) elementWithTours.clone();
					List<Customer> customers = solutionClone.deleteTour(i);
					new GreedyInsertion().insertCustomers(solutionClone, customers);
					TourEliminationNeighborhoodMove move = new TourEliminationNeighborhoodMove((SolutionGot) elementWithTours, solutionClone);
					if (isMoveNewBestMove(move))
						if (!isMoveTaboo(move, iteration))
							setRespAddBestNonTabooMove(move);
				}
			}
		}
		if (bestNonTabooMove == null)
			// no move possible because there is no tour that has at most Parameters.getMaximalNumberOfCustomersForDeletion customers => no move found
			throw new RuntimeException("no TourEliminationMove found because every tour has more Customers than Parameters.getMaximalNumberOfCustomersForDeletion(). Should not happen.");
		return bestNonTabooMove;
	}
	
	public ElementWithTours actuallyApplyMove(TourEliminationNeighborhoodMove bestMove) {
		elementWithTours = bestMove.getNewSolution();
		return elementWithTours;
	}
	
}
