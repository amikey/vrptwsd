package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.TourEliminationNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;

public class TSWithTourEliminationNeighborhood extends TabuSearchForElementWithTours {
	
	private int numberOfIterationsWithoutTourElimination = 0;

	@Override
	protected void findBestNonTabooMove() throws Exception {		
		bestMove = crossNeighborhood.returnBestMove(iteration);
		if (!bestMove.reducesNumberOfVehicles()) {
			//try tourEliminationNeighborhood but only if at least Parameters.getNumberOfIterationsWithoutTE iterations without TE were run
			if (numberOfIterationsWithoutTourElimination > Parameters.getMinimumNumberOfIterationsWithoutTourElemination()) {
				numberOfIterationsWithoutTourElimination = 0;
				System.out.println("Tour Elimination Neighborhood wird aufgerufen!");
				AbstractNeighborhoodMove tourEliminationMove = tourEliminationNeighborhood.returnBestMove(iteration);
				if (isTourEliminationMoveBetterThanCrossNeighborhoodMove(tourEliminationMove)) {
					bestMove = tourEliminationMove;	
					System.out.println("JUHUU; TOUR ELIMINATION NEIGHBORHOOD WAR ERFOLGREICH!");
				}					
			} else
				numberOfIterationsWithoutTourElimination++;
		} else 
			numberOfIterationsWithoutTourElimination++;
	}
	
	// Tour Elimination also works as some kind of Large Neighborhood Search
	private boolean isTourEliminationMoveBetterThanCrossNeighborhoodMove(AbstractNeighborhoodMove tourEliminationMove) {
		if (tourEliminationMove.reducesNumberOfVehicles())
			return true;
		if (bestMove.shortensShorterTour())
			return false;
		if (tourEliminationMove.getCost() < bestMove.getCost())
			return true;
		return false;
	}
	
	@Override 
	protected void updateTabuList() {
		if (bestMove instanceof TourEliminationNeighborhoodMove)
			crossNeighborhood.updateTabuListWithTourEliminationNeighborhoodMove(bestMove, iteration);
		else
			crossNeighborhood.updateTabuList(iteration);
	}

	@Override
	protected void applyBestNonTabooMove() {
		if (bestMove instanceof TourEliminationNeighborhoodMove)
			solution = (SolutionGot) tourEliminationNeighborhood.actuallyApplyMove((TourEliminationNeighborhoodMove) bestMove);
		else
			solution = (SolutionGot) crossNeighborhood.acctuallyApplyMoveAndMaintainNeighborhood(bestMove);
	}
	
	@Override
	protected TourEliminationNeighborhood getTourEliminationNeighborhood() {
		return new TourEliminationNeighborhood(solution);
	}


}
