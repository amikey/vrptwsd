package de.rwth.lofip.library.solver.metaheuristics.tabuList;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuListTest {

	private SolutionGot solution;
	
	@Test 
	public void testTabuList() throws Exception {
		givenSolutionWithOneTourWithCustomersC1C3C2C4();
		whenNextNeighborhoodMoveIsTried();
		thenSolutionShouldConsistOfTourWithCustomersC1C2C3C4();
		whenNextNeighborhoodMoveIsTried();
		thenSolutionShouldConsistOfTourWithCustomersC1C3C2C4();
	}

	private void givenSolutionWithOneTourWithCustomersC1C3C2C4() {
		solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();		
	}

	private void whenNextNeighborhoodMoveIsTried() throws Exception {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solution);
		AbstractNeighborhoodMove bestMove = crossNeighborhood.returnBestMove();	
		solution = (SolutionGot) crossNeighborhood.acctuallyApplyMove(bestMove);
	}

	private void thenSolutionShouldConsistOfTourWithCustomersC1C2C3C4() {		
		assertEquals(true, solution.equals(SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4()));
	}

	private void thenSolutionShouldConsistOfTourWithCustomersC1C3C2C4() {
		assertEquals(true, solution.equals(SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4()));
	}
}
