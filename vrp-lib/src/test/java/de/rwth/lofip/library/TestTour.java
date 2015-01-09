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
		System.out.println(tour1.getTourAsTupel());
		System.out.println(SetUpUtils.getTourWithCustomer1And4().getTourAsTupel());
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

}
