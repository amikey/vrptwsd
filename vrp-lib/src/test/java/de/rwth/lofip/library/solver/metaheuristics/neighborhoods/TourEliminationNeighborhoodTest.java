package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.TourEliminationNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class TourEliminationNeighborhoodTest {
	
	private SolutionGot solution;

	@Test
	public void testTourEliminationNeighborhood() throws Exception {
		whenGivenSolution();
		thenPerformingTourEliminationNeighborhoodShouldNotThrowAnyErrors();
	}

	private void whenGivenSolution() throws IOException {
		solution = SetUpUtils.getSomeSolutionForC101Problem();
	}
		
	private void thenPerformingTourEliminationNeighborhoodShouldNotThrowAnyErrors() throws Exception {
		solution.printSolutionAsTupel();
		
//		CrossNeighborhood cn = new CrossNeighborhood(solution);
//		AbstractNeighborhoodMove moveCN = cn.returnBestMove();
//		cn.acctuallyApplyMoveAndMaintainNeighborhood(moveCN);
//		solution.printSolutionAsTupel();
//		
		TourEliminationNeighborhood ten = new TourEliminationNeighborhood(solution);
		TourEliminationNeighborhoodMove move = (TourEliminationNeighborhoodMove) ten.returnBestMove();
		ten.actuallyApplyMove(move);
		solution.printSolutionAsTupel();
		
		
	}
	
	

}
