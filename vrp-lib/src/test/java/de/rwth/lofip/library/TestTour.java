package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

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
}
