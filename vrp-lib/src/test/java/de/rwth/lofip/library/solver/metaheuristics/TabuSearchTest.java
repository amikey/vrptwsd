package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchTest {
	
	SolutionGot solution;
	
	@Test
	public void test() {
		//was testet dieser Test?
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
	
	@Test
	public void testTSOnSomeSolutionForR101() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfNonImprovingIterationsInTS(0);
		Parameters.setPublishSolutionValueProgress(true);
		
		GivenSolutionForR101();
		ThenTSShouldHaveMoreThanOneIteration();
	}

	private void GivenSolutionForR101() throws IOException {
		solution = SetUpUtils.getSomeSolutionForR101Problem(); 
		solution.printSolutionCost();
	}

	private void ThenTSShouldHaveMoreThanOneIteration() throws IOException {
		TabuSearchForElementWithTours tabuSearch = new TabuSearchForElementWithTours();
		tabuSearch.improve(solution);
		assertEquals(true, tabuSearch.getNumberOfIterations() > 1);
	}

}
