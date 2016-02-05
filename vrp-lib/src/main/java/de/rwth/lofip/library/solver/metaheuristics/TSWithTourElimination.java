package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.AbstractNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.TourEliminationNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;
import de.rwth.lofip.library.solver.util.TabuList;

public class TSWithTourElimination extends TabuSearchForElementWithTours {
	
//	private int numberOfIterationsWithoutTourElimination = 0;
	private TabuList tabuList = new TabuList();

	@Override
	protected void findBestNonTabooMove() throws Exception {
		AbstractNeighborhoodMove crossMove = crossNeighborhood.returnBestMove(iteration); 
		AbstractNeighborhoodMove tourEliminationMove = tourEliminationNeighborhood.returnBestMove(iteration);
		if (AbstractNeighborhood.isSecondMoveBetterThanFirstMove(tourEliminationMove, crossMove))
			bestMove = crossMove;
		else
			bestMove = crossMove;

//		if (!bestMove.reducesNumberOfVehicles()) {
//			//try tourEliminationNeighborhood but only if at least Parameters.getNumberOfIterationsWithoutTE iterations without TE were run
//			if (numberOfIterationsWithoutTourElimination >= Parameters.getMinimumNumberOfIterationsWithoutTourElemination()) {
//				numberOfIterationsWithoutTourElimination = 0;
//				System.out.println("Tour Elimination Neighborhood wird aufgerufen!");
//				AbstractNeighborhoodMove tourEliminationMove = tourEliminationNeighborhood.returnBestMove(iteration);
//				if (isTourEliminationMoveBetterThanCrossNeighborhoodMove(tourEliminationMove)) {
////					IOUtils.write("JUHUU; TOUR ELIMINATION NEIGHBORHOOD WAR ERFOLGREICH! \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(solution));									
//					System.out.println("JUHUU; TOUR ELIMINATION NEIGHBORHOOD WAR ERFOLGREICH!");
//					System.out.println("Kosten best CrossNeighborhood Move: " + bestMove.getCost());
//					System.out.println("Kosten best TourEliminationNeighborhood Move: " + tourEliminationMove.getCost());
//					bestMove = tourEliminationMove;
//				}					
//			} else
//				numberOfIterationsWithoutTourElimination++;
//		} else 
//			numberOfIterationsWithoutTourElimination++;
	}
	
//	// Tour Elimination also works as some kind of Large Neighborhood Search
//	private boolean isTourEliminationMoveBetterThanCrossNeighborhoodMove(AbstractNeighborhoodMove tourEliminationMove) {
//		//CODE_SMELL_TODO: returning null for tourEliminationMove is bad practice
//		if (tourEliminationMove == null)
//			//no move was found in tourEliminationNeighborhood
//			return false;
//		if (tourEliminationMove.reducesNumberOfVehicles())
//			return true;
//		if (bestMove.shortensShorterRespShortestTour())
//			return false;
//		if (tourEliminationMove.getCost() < bestMove.getCost())
//			return true;
//		return false;
//	}
	
	@Override 
	protected void updateTabuList() {
		if (bestMove instanceof TourEliminationNeighborhoodMove)
			tourEliminationNeighborhood.updateTabuList(iteration);
		else
			crossNeighborhood.updateTabuList(iteration);
	}

	@Override
	protected void applyBestNonTabooMove() {	
		if (bestMove instanceof TourEliminationNeighborhoodMove)
			solution = (SolutionGot) tourEliminationNeighborhood.actuallyApplyMove((TourEliminationNeighborhoodMove) bestMove);
		else
			solution = (SolutionGot) crossNeighborhood.actuallyApplyMoveAndMaintainNeighborhood(bestMove);
		
		crossNeighborhood.updateNeighborhoodWithSolution(solution);
		tourEliminationNeighborhood.updateNeighborhoodWithSolution(solution);
		crossNeighborhood.resetNeighborhood();
		tourEliminationNeighborhood.resetNeighborhood();	
	}

	@Override
	protected CrossNeighborhoodWithTabooList getCrossNeighborhood() {
		return new CrossNeighborhoodWithTabooList(solution, tabuList);
	}

	@Override
	protected TourEliminationNeighborhood getTourEliminationNeighborhood() {
		return new TourEliminationNeighborhood(solution, tabuList);
	}


}
