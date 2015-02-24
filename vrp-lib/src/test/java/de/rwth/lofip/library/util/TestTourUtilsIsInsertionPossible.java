package de.rwth.lofip.library.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.util.TourUtils;


public class TestTourUtilsIsInsertionPossible {
		
	private Customer c1 = SetUpUtils.getC1();
	private Customer c3 = SetUpUtils.getC3();
	private Customer c4 = SetUpUtils.getC4();
	private Tour tourWithCustomer3And2 = SetUpUtils.getTourWithCustomer3And2();
	private Tour tourWithCustomer3 = SetUpUtils.getTourWithCustomer3();
	private VrpProblem problem;
	private Tour tour;
	
	
    @Test
    public void testInsertionPossible() {

        Customer c6 = new Customer(25, 30);
        c6.setDemand(3);
        c6.setTimeWindowOpen(89);
        c6.setTimeWindowClose(119);
        c6.setServiceTime(10);

        Customer c2 = new Customer(35, 17);
        c2.setDemand(7);
        c2.setTimeWindowOpen(40);
        c2.setTimeWindowClose(70);
        c2.setServiceTime(10);

        Depot depot = new Depot();
        depot.setxCoordinate(35);
        depot.setyCoordinate(35);

        Tour tour = new Tour(depot, new Vehicle(1, 50));
        tour.addCustomer(c2);

        boolean insertionPossible;
        tour = new Tour(depot, new Vehicle(1, 50));
        tour.addCustomer(c6);
        insertionPossible = TourUtils.isInsertionPossibleWrtStochasticDemandAndTW(c2, tour, 1, 0.99999);
        // should fail because the time window of c2 is no longer valid
        assert (insertionPossible == false);
    }
    
	@Test
	public void TryToInsertC1AtPos0(){
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(c1, tourWithCustomer3And2, 0));
	}
	
	@Test
	public void TryToInsertC$AtPos2(){
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(c4, tourWithCustomer3And2, 2));
	}

	@Test
	public void TryToInsertC4AtPos0(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(c4, tourWithCustomer3And2, 0));
	}
	
	@Test
	public void TryToInsertC1AtPos2(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(c1, tourWithCustomer3And2, 2));
	}
    
	@Test
	public void TryToInsertC4AtPos1(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(c4, tourWithCustomer3And2, 1));
	}
	
	@Test
	public void TryToInsertC1AtPos1(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(c1, tourWithCustomer3And2, 1));
	}
	
	@Test
	public void TryToInsertC1InTourWithCustomerC3AtPos0() {
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(c3, tourWithCustomer3, 0));
	}
	
	
	@Test 
	public void testFeasibilityChecks() throws IOException {
		whenGivenProblemC101();
		ThenFeasiblityChecksShouldWorkWhenCreatingTourWithCustomers57_55_54_53_56_58_60_59_68_69();
	}

	private void whenGivenProblemC101() throws IOException {
		problem = TestUtils.readSolomonProblemC101().get(0); 	
	}
	
	private void ThenFeasiblityChecksShouldWorkWhenCreatingTourWithCustomers57_55_54_53_56_58_60_59_68_69() {
		tour = new Tour(problem.getDepot(),problem.getVehicles().iterator().next());
		Customer customer = problem.getCustomerWithCustomerNo(57);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(55);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(54);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(53);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(56);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(58);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(60);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(59);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(68);
		assertEquals(false, TourUtils.isInsertionPossibleWrtDeterministicDemandAndTW(customer, tour, tour.length()));
		assertEquals(false, TourUtils.isInsertionPossibleWrtDemand(customer, tour));
		assertEquals(false, TourUtils.isInsertionPossibleWrtTW(customer,tour, tour.length()));
		assertEquals(true, TourUtils.isTourFeasibleWrtDemand(tour));
		assertEquals(true, TourUtils.isTourFeasibleWrtTW(tour));
		tour.addCustomer(customer);
		assertEquals(false, TourUtils.isTourFeasibleWrtDemand(tour));
		assertEquals(false, TourUtils.isTourFeasibleWrtTW(tour));
	}
}
