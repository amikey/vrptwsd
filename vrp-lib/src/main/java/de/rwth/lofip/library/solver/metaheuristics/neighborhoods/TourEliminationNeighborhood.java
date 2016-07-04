package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;
import de.rwth.lofip.library.solver.util.TabuList;
import de.rwth.lofip.library.util.math.MathUtils;

public class TourEliminationNeighborhood extends AbstractNeighborhood {
	
	private TabuList tabuList;
	
	public TourEliminationNeighborhood(ElementWithTours solution, TabuList tl) {
		this.elementWithTours = solution;
		tabuList = tl;		
	}
	
	public void resetNeighborhood() {
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
					if (isMoveNewBestMove(move)) {
						if (!isMoveTaboo(move, iteration))
							setRespAddBestNonTabooMove(move);
					}
				}
			}
		}		
		return bestNonTabooMove;
	}
	
	public ElementWithTours actuallyApplyMove(TourEliminationNeighborhoodMove bestMove) {
		elementWithTours = bestMove.getNewSolution();
		elementWithTours.removeEmptyToursAndGots();
		return elementWithTours;
	}
	
	public void updateTabuList(int iteration) {
		tabuList.addSolutionToTabuList(bestNonTabooMove.getCost(), iteration);
	}

	@Override
	protected boolean isMoveTaboo(AbstractNeighborhoodMove move, int iteration) { 
		return tabuList.isMoveTaboo(move.getCost(), iteration);
	}
	
}
