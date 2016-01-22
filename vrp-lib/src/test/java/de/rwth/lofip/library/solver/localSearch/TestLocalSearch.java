package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestLocalSearch {
	
	private SolutionGot solution;
	
	@Test
	public void testLocalSearchTestCase1() {			
		givenSolutionWithTwoToursAndTwoCustomersEach();
		whenSolutionIsImprovedWithLocalSearch();
		thenSolutionShouldHaveOneTourWithFourCustomers();
	}

	private void givenSolutionWithTwoToursAndTwoCustomersEach() {
		solution = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();
	}
	
	private void whenSolutionIsImprovedWithLocalSearch() {
		LocalSearchForElementWithTours localSearch = new LocalSearchForTesting();
		solution = (SolutionGot) localSearch.improve(solution);
	}

	private void thenSolutionShouldHaveOneTourWithFourCustomers() {
		assertEquals(true, solution.equals(SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4()));
	}
	
	@Test
	public void testLocalSearchTestCase2() {		
		givenSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer();
		whenSolutionIsImprovedWithLocalSearch();
		thenSolutionShouldHaveOneTourWithFourCustomers();
	}

	private void givenSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer() {
		solution = SetUpUtils.getSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer();
	}
	
	@Test
	public void testLocalSearchOnSolomonInstance() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readSolomonProblemRC101();
		SolutionGot solution = new RandomI1Solver().solve(problem);
		LocalSearchForElementWithTours localSearch = new LocalSearchForTesting();
		//asserts are in LocalSearchForTesting
		solution = (SolutionGot) localSearch.improve(solution);		
	}

}
