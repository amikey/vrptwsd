package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestLocalSearch {
	
	private SolutionGot solution;
	private CrossNeighborhoodMove bestMove;
	private CrossNeighborhood crossNeighborhood;

	@Before
	public void initialize() {
		solution = SetUpUtils.SetUpSolutionWithTwoToursAndTwoCustomersEach();
		crossNeighborhood = new CrossNeighborhood(solution);
	}
	
	@Test
	public void testLocalSearch() {		
		localSearch();
		assertEquals(true, solution.equals(SetUpUtils.SetUpSolutionWithTwoToursWithOneAndThreeCustomersRespectively()));
	}
	
	private void localSearch() {
		do {
			findBestMove();
			if (isImprovement());
				applyBestMove();
		} while (isImprovement());		
	}

	private void findBestMove() {
		bestMove = crossNeighborhood.returnBestCrossMove();				
	}
	
	private boolean isImprovement() {
		return (bestMove.getCost() < solution.getTotalDistance());
	}	

	private void applyBestMove() {
		solution = crossNeighborhood.acctuallyApplyMove(bestMove);
		crossNeighborhood.resetNeighborhood();
	}
}
