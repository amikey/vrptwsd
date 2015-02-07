package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.rwth.lofip.library.solver.util.RessourceExtensionFunction;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestTour {
	
	private Tour tour1, tour2;
	
	@Test 
	public void testInsertCustomersAtPostion() {
		whenGivenOneEmptyTour();
		andInsertingTwoCustomers();
		thenTourShouldContainThoseTwoCustomers();
	}
	
	private void whenGivenOneEmptyTour() {
		tour1 = SetUpUtils.getEmptyTour();		
	}

	private void andInsertingTwoCustomers() {
		List<Customer> customers = new LinkedList<Customer>();
		customers.add(SetUpUtils.getC1());
		customers.add(SetUpUtils.getC4());
		tour1.insertCustomersAtPosition(customers, 0);
	}

	private void thenTourShouldContainThoseTwoCustomers() {
		assertEquals(true, tour1.equals(SetUpUtils.getTourWithCustomer1And4()));	
	}

	@Test
	public void testEqualsMethodWithPositiveOutcome() {
		givenTwoEqualToursInTermsOfCustomers();
		toursShouldBeTheSameInTermsOfCustomers();
	}

	private void givenTwoEqualToursInTermsOfCustomers() {
		tour1 = SetUpUtils.getTourWithCustomer1And2();
		tour2 = SetUpUtils.getTourWithCustomer1And2();
	}

	private void toursShouldBeTheSameInTermsOfCustomers() {
		assertEquals(true, tour1.equals(tour2));
	}
	
	@Test
	public void testEqualsMethodWithNegativeOutcome() {
		givenTwoDifferentToursInTermsOfCustomers();
		toursShouldBeDifferentInTermsOfCustomers();
	}

	private void givenTwoDifferentToursInTermsOfCustomers() {
		tour1 = SetUpUtils.getTourWithCustomer1And2();
		tour2 = SetUpUtils.getTourWithCustomer1And4();
	}

	private void toursShouldBeDifferentInTermsOfCustomers() {
		assertEquals(false, tour1.equals(tour2));
	}

	
	@Test
	public void testGetEdgesBoundaryCaseWithZeroCustomers() {
		whenGivenOneEmptyTour();
		thenGetEdgesShouldReturnEmptyList();
	}
	
	private void thenGetEdgesShouldReturnEmptyList() {
		assertEquals(0,tour1.getEdges().size());
	}

	
	@Test
	public void testGetEdgesInTourWith2Customers() {
		givenTourWith2Customers();
		thenEdgesShouldBeTheVeryEdgesInThisTour();
	}

	private void givenTourWith2Customers() {
		tour1 = SetUpUtils.getTourWithCustomer1And2();		
	}

	private void thenEdgesShouldBeTheVeryEdgesInThisTour() {
		Depot depot = SetUpUtils.getDepot();
		Customer c1 = SetUpUtils.getC1();
		Customer c2 = SetUpUtils.getC2();
		assertEquals(new Edge(depot,c1).getLength(),tour1.getEdges().get(0).getLength(),0.001);		
	}
	
	
	@Test
	public void testRecalculateTimes() {
		givenTourWithCustomerC1();
		whenInsertingCustomerC2AtTheEndOfTheTour();
		thenArrivalTimesAtCustomersShouldBeCorrect();
	}

	private void givenTourWithCustomerC1() {
		tour1 = SetUpUtils.getTourWithCustomer1();
	}

	private void whenInsertingCustomerC2AtTheEndOfTheTour() {
		tour1.addCustomer(SetUpUtils.getC2WithEarlierTimeWindowOpening());
	}

	private void thenArrivalTimesAtCustomersShouldBeCorrect() {
		assertEquals(42.42641,tour1.getCustomersInTour().get(0).getArrivalTime(),0.001);
		assertEquals((278+90),tour1.getCustomersInTour().get(0).getEarliestLeavingTime(),0.001);
		assertEquals((278+90+11.18034),tour1.getCustomersInTour().get(1).getArrivalTime(),0.001);
		assertEquals((278+90+11.18034+90),tour1.getCustomersInTour().get(1).getEarliestLeavingTime(),0.001);
	}
	
	@Test
	public void testRecalculateDemand() {
		whenGivenOneEmptyTour();
		andInsertingTwoCustomers();
		thenDemandShouldBeThatOfThoseTwoCustomers();
		andRemovingCustomer1();
		thenDemandShouldBeThatOfCustomer4();
	}

	private void thenDemandShouldBeThatOfThoseTwoCustomers() {
		assertEquals(SetUpUtils.getC1().getDemand() + SetUpUtils.getC4().getDemand() , tour1.getDemandOnTour(), 0.001);
	}	

	private void andRemovingCustomer1() {
		tour1.removeCustomerAtPosition(0);	
	}

	private void thenDemandShouldBeThatOfCustomer4() {
		assertEquals(SetUpUtils.getC4().getDemand() , tour1.getDemandOnTour(), 0.001);
	}
	
	@Test 
	public void testCalculationOfRefsFromBeginning() {
		whenGivenOneEmptyTour();
		thenListsOfRefsInTourShouldBeEmpty();
		whenAddingCustomer2();
		thenListsOfRefsShouldContainCorrectRefs();
		whenInsertingCustomer1AtPosition0();
		thenListOfRefsShouldEqualThatOfTourWithCustomersC1C2();
		whenAddingCustomer3();
		thenListOfRefsShouldEqualThatOfTourWithCustomersC1C2C3();
		whenCustomer2IsDeleted();
		thenListOfRefsShouldEqualThatOfTourWithCustomersC1C3();		
	}

	private void thenListsOfRefsInTourShouldBeEmpty() {
		assertEquals(0,tour1.getRefsFromBeginning().size());
		assertEquals(0,tour1.getRefsToEnd().size());
	}
	
	private void whenAddingCustomer2() {
		tour1.addCustomer(SetUpUtils.getC2());
	}
	
	private void thenListsOfRefsShouldContainCorrectRefs() {		
		assertEquals(90, tour1.getRefsFromBeginning().get(0).getDuration(), 0.001);
		assertEquals(528, tour1.getRefsFromBeginning().get(0).getLatestArrivalTime(), 0.001);
		assertEquals(475, tour1.getRefsFromBeginning().get(0).getEarliestLeavingTime(), 0.001);		
	}
	
	private void whenInsertingCustomer1AtPosition0() {
		tour1.insertCustomerAtPosition(SetUpUtils.getC1(), 0);
	}
	
	private void thenListOfRefsShouldEqualThatOfTourWithCustomersC1C2() {		
		assertEquals(true, SetUpUtils.getTourWithCustomer1And2().getRefsFromBeginning().get(0).equals(tour1.getRefsFromBeginning().get(0)));		
		assertEquals(true, SetUpUtils.getTourWithCustomer1And2().getRefsFromBeginning().get(1).equals(tour1.getRefsFromBeginning().get(1)));
	}
	
	private void whenAddingCustomer3() {
		tour1.addCustomer(SetUpUtils.getC3());
	}
	
	private void thenListOfRefsShouldEqualThatOfTourWithCustomersC1C2C3() {
		assertEquals(true, SetUpUtils.getTourWithCustomers1And2And3().getRefsFromBeginning().get(0).equals(tour1.getRefsFromBeginning().get(0)));		
		assertEquals(true, SetUpUtils.getTourWithCustomers1And2And3().getRefsFromBeginning().get(1).equals(tour1.getRefsFromBeginning().get(1)));		
		assertEquals(true, tour1.getRefsFromBeginning().get(2).equals(new RessourceExtensionFunction(286.1803398874989, 345.0, 570.0)));
		assertEquals(true, SetUpUtils.getTourWithCustomers1And2And3().getRefsFromBeginning().get(2).equals(tour1.getRefsFromBeginning().get(2)));
	}
		
	private void whenCustomer2IsDeleted() {
		tour1.removeCustomerAtPosition(1);	
	}
	
	private void thenListOfRefsShouldEqualThatOfTourWithCustomersC1C3() {
		assertEquals(true, SetUpUtils.getTourWithCustomers1And3().getRefsFromBeginning().get(0).equals(tour1.getRefsFromBeginning().get(0)));
		assertEquals(true, tour1.getRefsFromBeginning().get(1).equals(new RessourceExtensionFunction(195.81138830084188, 345.0, 425.0)));	
	}
	
}
