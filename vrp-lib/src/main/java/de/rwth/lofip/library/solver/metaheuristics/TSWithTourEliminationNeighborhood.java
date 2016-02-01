package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.TourEliminationNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;

public class TSWithTourEliminationNeighborhood extends TabuSearchForElementWithTours {
	
	@Override
	protected void findBestNonTabooMove() throws Exception {		
		bestMove = crossNeighborhood.returnBestMove(iteration);
		if (!bestMove.reducesNumberOfVehicles()) {
			//try tourEliminationNeighborhood
			AbstractNeighborhoodMove tourEliminationMove = tourEliminationNeighborhood.returnBestMove();
			if (tourEliminationMove.reducesNumberOfVehicles())
				bestMove = tourEliminationMove;
		}
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
