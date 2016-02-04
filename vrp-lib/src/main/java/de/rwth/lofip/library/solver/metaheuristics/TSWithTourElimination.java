package de.rwth.lofip.library.solver.metaheuristics;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.TourEliminationNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;
import de.rwth.lofip.library.solver.util.TabuList;

public class TSWithTourElimination extends TabuSearchForElementWithTours {
	
	private int numberOfIterationsWithoutTourElimination = 0;
	private TabuList tabuList = new TabuList();

	@Override
	protected void findBestNonTabooMove() throws Exception {		
		bestMove = crossNeighborhood.returnBestMove(iteration);
		if (!bestMove.reducesNumberOfVehicles()) {
			//try tourEliminationNeighborhood but only if at least Parameters.getNumberOfIterationsWithoutTE iterations without TE were run
			if (numberOfIterationsWithoutTourElimination >= Parameters.getMinimumNumberOfIterationsWithoutTourElemination()) {
				numberOfIterationsWithoutTourElimination = 0;
				System.out.println("Tour Elimination Neighborhood wird aufgerufen!");
				AbstractNeighborhoodMove tourEliminationMove = tourEliminationNeighborhood.returnBestMove(iteration);
				if (isTourEliminationMoveBetterThanCrossNeighborhoodMove(tourEliminationMove)) {
					bestMove = tourEliminationMove;
					IOUtils.write("JUHUU; TOUR ELIMINATION NEIGHBORHOOD WAR ERFOLGREICH! \n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(solution));									
					System.out.println("JUHUU; TOUR ELIMINATION NEIGHBORHOOD WAR ERFOLGREICH!");
				}					
			} else
				numberOfIterationsWithoutTourElimination++;
		} else 
			numberOfIterationsWithoutTourElimination++;
	}
	
	// Tour Elimination also works as some kind of Large Neighborhood Search
	private boolean isTourEliminationMoveBetterThanCrossNeighborhoodMove(AbstractNeighborhoodMove tourEliminationMove) {
		//CODE_SMELL_TODO: returning null for tourEliminationMove is bad practice
		if (tourEliminationMove == null)
			//no move was found in tourEliminationNeighborhood
			return false;
		if (tourEliminationMove.reducesNumberOfVehicles())
			return true;
//		if (bestMove.shortensShorterTour())
//			return false;
		if (tourEliminationMove.getCost() < bestMove.getCost())
			return true;
		return false;
	}
	
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
