package de.rwth.lofip.library.solver.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Depot;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.Vehicle;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.util.TourUtils;
import de.rwth.lofip.library.util.SetUpUtils;


public class TestTourUtilsIsInsertionPossible {
		
	private Customer c1 = SetUpUtils.getC1();
	private Customer c3 = SetUpUtils.getC3();
	private Customer c4 = SetUpUtils.getC4();
	private Tour tourWithCustomer3And2 = SetUpUtils.getTourWithCustomer3And2();
	private Tour tourWithCustomer3 = SetUpUtils.getTourWithCustomer3();
	private VrpProblem problem;
	private Tour tour;
	
	   
	@Test
	public void TryToInsertC1AtPos0(){
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(c1, tourWithCustomer3And2, 0));
	}
	
	@Test
	public void TryToInsertC$AtPos2(){
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(c4, tourWithCustomer3And2, 2));
	}

	@Test
	public void TryToInsertC4AtPos0(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(c4, tourWithCustomer3And2, 0));
	}
	
	@Test
	public void TryToInsertC1AtPos2(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(c1, tourWithCustomer3And2, 2));
	}
    
	@Test
	public void TryToInsertC4AtPos1(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(c4, tourWithCustomer3And2, 1));
	}
	
	@Test
	public void TryToInsertC1AtPos1(){
		assertEquals(false, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(c1, tourWithCustomer3And2, 1));
	}
	
	@Test
	public void TryToInsertC1InTourWithCustomerC3AtPos0() {
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(c3, tourWithCustomer3, 0));
	}
	
	
	@Test 
	public void testFeasibilityChecks() throws IOException {
		whenGivenProblemC101();
		ThenFeasiblityChecksShouldWorkWhenCreatingTourWithCustomers57_55_54_53_56_58_60_59_68_69();
	}

	private void whenGivenProblemC101() throws IOException {
		problem = ReadAndWriteUtils.readSolomonProblemC101().get(0); 	
	}
	
	private void ThenFeasiblityChecksShouldWorkWhenCreatingTourWithCustomers57_55_54_53_56_58_60_59_68_69() {
		tour = new Tour(problem.getDepot(),problem.getVehicles().iterator().next());
		tour.setParentGot(new GroupOfTours());
		Customer customer = problem.getCustomerWithCustomerNo(57);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(55);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(54);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(53);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(56);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(58);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(60);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(59);
		assertEquals(true, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		tour.addCustomer(customer);
		customer = problem.getCustomerWithCustomerNo(68);
		assertEquals(false, TourUtils.isInsertionPossibleWrtDemandAndTWinLinearTime(customer, tour, tour.length()));
		assertEquals(false, TourUtils.isInsertionPossibleWrtDemand(customer, tour));
		assertEquals(false, TourUtils.isInsertionPossibleWrtTW(customer,tour, tour.length()));
		assertEquals(true, TourUtils.isTourFeasibleWrtDemand(tour));
		assertEquals(true, TourUtils.isTourFeasibleWrtTW(tour));
		tour.addCustomer(customer);
		assertEquals(false, TourUtils.isTourFeasibleWrtDemand(tour));
		assertEquals(false, TourUtils.isTourFeasibleWrtTW(tour));
	}
}
