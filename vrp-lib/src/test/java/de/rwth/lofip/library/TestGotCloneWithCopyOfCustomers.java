package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;

public class TestGotCloneWithCopyOfCustomers {
	
	private GroupOfTours got1, got2;

	@Test
	public void testCloneOfTourWithCopyOfCustomers() {
		givenOneGot();
		andCloningThatGotWithCopyOfCustomers();
		whenCustomersInClonedGotAreAltered();
		ThenCustomersInOriginalGotAndClonedGotShouldBeDifferent();		
	}

	private void givenOneGot() {
		got1 = SetUpUtils.getGotWithCustomer1And2();
	}

	private void andCloningThatGotWithCopyOfCustomers() {
		got2 = got1.cloneWithCopyOfTourAndCustomers();
		
	}

	private void whenCustomersInClonedGotAreAltered() {
		got1.getFirstTour().getCustomerAtPosition(1).getCustomer().setDemand(100000);
		got1.getFirstTour().getCustomerAtPosition(1).getCustomer().setOriginalDemand(100000);
	}

	private void ThenCustomersInOriginalGotAndClonedGotShouldBeDifferent() {
		assertEquals(true, got1.getFirstTour().getCustomerAtPosition(1).getCustomer().getDemand() != got2.getFirstTour().getCustomerAtPosition(1).getCustomer().getDemand());		
	}
	
	@Test
	public void showCustomersInGot() {
		givenOneGot();
		System.out.println(got1.getCustomers());
	}
	
}
