package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchTest {
	
	SolutionGot solution;
	
	@Test
	public void test() {
		solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		Tour tour = solution.getTour(0);
		tour.removeCustomerAtPosition(0);
		tour = SetUpUtils.getTourWithCustomer1();
		solution.printSolutionAsTupel();
	}
	
	@Test
	public void testTryToImproveRoutesWithIntensificationPhase() {
		solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		solution.printSolutionAsTupel();
		
		SolutionGot solution1324 = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		System.out.println("Cost Solution1324: " + solution1324.getTotalDistanceWithCostFactor());
		new TabuSearchForElementWithTours().tryToImproveNewBestSolutionWithIntensificationPhase(solution);
		solution.printSolutionAsTupel();
		assertEquals(true, SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4().equals(solution));
	}
	
	@Test
	public void testTryToImproveRoutesWithIntensificationPhaseTestCaseWithoutImprovement() {
		solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4();
		new TabuSearchForElementWithTours().tryToImproveNewBestSolutionWithIntensificationPhase(solution);
		assertEquals(true, SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4().equals(solution));
	}

}
