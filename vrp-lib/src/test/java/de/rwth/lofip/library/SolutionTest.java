package de.rwth.lofip.library;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

public class SolutionTest {
	Solution solution;
	Solution solutionOther;

	@Test
	public void testThatFeasibleSolutionIsFeasible() {
		givenFeasibleSolution();
		thenSolutionShouldBeFeasible();
	}

	private void givenFeasibleSolution() {
		solution = SetUpUtils.SetUpFeasibleSolution();
	}
	
	private void thenSolutionShouldBeFeasible() {
		assertTrue(solution.isSolutionFeasibleWrtDemand());
	}


	@Test
	public void testThatInfeasibleSolutionIsInfeasible() {
		givenInfeasibleSolution();
		thenSolutionShouldBeInfeasible();
	}
	
	private void givenInfeasibleSolution() {
		solution = SetUpUtils.SetUpInfeasibleSolution();
	}

	private void thenSolutionShouldBeInfeasible() {
		assertFalse(solution.isSolutionFeasibleWrtDemand());
	}

}
