package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.RecourseCost;
import de.rwth.lofip.library.util.SetUpUtils;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

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
	
	@Test
	public void testThatGotIsAlwaysFeasibleAfterImrpovingWithLocalSearch() throws IOException {
		Parameters.setMaximalNumberOfToursInGot(3);
		
		solution = SetUpSolutionFromString.SetUpSolution("( ( 6 18 16 12 ) ( 32 31 84 85 ) )", ReadAndWriteUtils.readEigeneModifiedSolomonProblems().get(9));				
		//SetCustomerDemands
		solution.getTour(0).getCustomerWithId(6).getCustomer().setDemand(19);
		solution.getTour(0).getCustomerWithId(18).getCustomer().setDemand(27);
		solution.getTour(0).getCustomerWithId(16).getCustomer().setDemand(56);
		solution.getTour(0).getCustomerWithId(12).getCustomer().setDemand(27);
		
		solution.getTour(1).getCustomerWithId(32).getCustomer().setDemand(38);
		solution.getTour(1).getCustomerWithId(31).getCustomer().setDemand(22);
		solution.getTour(1).getCustomerWithId(84).getCustomer().setDemand(19);
		solution.getTour(1).getCustomerWithId(85).getCustomer().setDemand(34);
		System.out.println(solution.getAsTupelWithDemand());
		System.out.println(solution.getTour(0).getVehicle().getCapacity());
		
		//baue touren neu auf, damit refs richtig sind
		List<Customer> customers = solution.getTour(0).removeCustomersBetween(0, solution.getTour(0).getCustomerSize());
		solution.getTour(0).insertCustomersAtPosition(customers, 0);			
		
		List<Customer> customers2 = solution.getTour(1).removeCustomersBetween(0, solution.getTour(1).getCustomerSize());
		solution.getTour(1).insertCustomersAtPosition(customers2, 0);

		System.out.println(solution.getAsTupelWithDemand());
		
		assertEquals(false, TourUtils.isTourFeasibleWrtDemandCheckWithRef(solution.getTour(0)));
		assertEquals(false, TourUtils.isTourFeasibleWrtDemandCheckWithRef(solution.getTour(1)));
		
//		List<Customer> customerList = solution.getTour(1).removeCustomersBetween(0, 2);
//		Tour tour = new Tour(solution.getVrpProblem(), new GroupOfTours(solution));
//		tour.addCustomer(customerList.get(0));
//		tour.addCustomer(customerList.get(1));
//		solution.addTourToLastOrNewGot(tour);
//
//		System.out.println(solution.getAsTupelWithDemand());
		
		//baue got mit touren
		GroupOfTours got = new GroupOfTours(solution);
		got.addTour(solution.getTour(0));
		got.addTour(solution.getTour(1));
		
//		System.out.println(got.getAsTupelWithDemand());
				
//		Tour tour = new Tour(solution.getVrpProblem(), got);
//		tour.addCustomer(solution.getTour(1).getCustomerWithId(31).getCustomer());
//		tour.addCustomer(solution.getTour(0).getCustomerWithId(16).getCustomer());
//		tour.addCustomer(solution.getTour(0).getCustomerWithId(12).getCustomer());
//		assertEquals(true, TourUtils.isTourFeasibleWrtDemandCheckWithRef(tour));
//		assertEquals(false, TourUtils.isTourFeasibleWrtTW(tour));
		
		GroupOfTours gotAfter = new RecourseCost(0, 0, 0, 0).makeSolutionFeasibleAgainWithCostMinimizationForTesting(got);
		
//		TabuSearchForElementWithTours ts = new TabuSearchForElementWithTours();
//		System.out.println(solution.getAsTupel());
//		
//		SolutionGot solutionAfter = new SolutionGot();
//		try {
//			solutionAfter = (SolutionGot) ts.improve(solution);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new RuntimeException("TS Failed because of IOException");
//		}    					
//		
//		System.out.println(solutionAfter.getAsTupel());
		System.out.println(gotAfter.getAsTupel());
		
		assertEquals(true, TourUtils.isTourFeasibleWrtDemandCheckWithRef(gotAfter.getTour(0)));
		assertEquals(true, TourUtils.isTourFeasibleWrtDemandCheckWithRef(gotAfter.getTour(1)));
		assertEquals(true, TourUtils.isTourFeasibleWrtDemandCheckWithRef(gotAfter.getTour(2)));
	}

}
