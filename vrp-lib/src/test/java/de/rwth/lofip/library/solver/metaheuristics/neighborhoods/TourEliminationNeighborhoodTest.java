package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;
import de.rwth.lofip.library.solver.util.TabuList;
import de.rwth.lofip.library.util.SetUpUtils;

public class TourEliminationNeighborhoodTest {
	
	private SolutionGot solution;
	int j = 30; 

	@Test
	public void testTourEliminationNeighborhood() throws Exception {		
//		for (j = 1; j < 20; j++) {
			whenGivenSolution();
			thenPerformingTourEliminationNeighborhoodShouldNotThrowAnyErrors();
			System.out.println();
//		}
	}

	private void whenGivenSolution() throws IOException {
		for (int i = 0; i < 10; i++)
			solution = SetUpUtils.getSomeSolutionForC101Problem();
	}
		
	private void thenPerformingTourEliminationNeighborhoodShouldNotThrowAnyErrors() throws Exception {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setMaximalNumberOfCustomersForDeletionInTourEliminationNeighborhood(7);
		solution.printSolutionAsTupel();
		solution.printSolutionCost();
		
//		CrossNeighborhood cn = new CrossNeighborhood(solution);
//		AbstractNeighborhoodMove moveCN = cn.returnBestMove();
//		cn.acctuallyApplyMoveAndMaintainNeighborhood(moveCN);
//		solution.printSolutionAsTupel();
//		solution.printSolutionCost();

		TourEliminationNeighborhood ten = new TourEliminationNeighborhood(solution, new TabuList());
		TourEliminationNeighborhoodMove move = (TourEliminationNeighborhoodMove) ten.returnBestMove(100);
		ten.actuallyApplyMove(move);
		solution.printSolutionAsTupel();
		solution.printSolutionCost();
	}	

}
