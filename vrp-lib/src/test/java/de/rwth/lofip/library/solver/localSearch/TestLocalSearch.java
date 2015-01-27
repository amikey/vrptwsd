package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestLocalSearch {
	
	private SolutionGot solution;
	
	@Test
	public void testLocalSearch() {		
		givenSolutionWithTwoToursAndTwoCustomersEach();
		whenSolutionIsImprovedWithLocalSearch();
		thenSolutionShouldHaveOneTourWithFourCustomers();
	}

	private void givenSolutionWithTwoToursAndTwoCustomersEach() {
		solution = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();
	}
	
	private void whenSolutionIsImprovedWithLocalSearch() {
		LocalSearch localSearch = new LocalSearch();
		solution = localSearch.improve(solution);
	}

	private void thenSolutionShouldHaveOneTourWithFourCustomers() {
		System.out.println(SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4().getSolutionAsTupel());
		System.out.println(SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4().getTotalDistance());
		System.out.println(SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4().getSolutionAsTupel());
		System.out.println(SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4().getTotalDistance());
		assertEquals(true, solution.equals(SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4()));
	}
	
	@Test
	public void testLocalSearch2() {		
		givenSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer();
		whenSolutionIsImprovedWithLocalSearch();
		thenSolutionShouldHaveOneTourWithFourCustomers();
	}

	private void givenSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer() {
		solution = SetUpUtils.getSolutionWithThreeToursWithTwoCustomersAndTwoTimesOneCustomer();
	}

}
