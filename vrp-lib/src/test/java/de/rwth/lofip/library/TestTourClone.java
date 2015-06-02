package de.rwth.lofip.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;

public class TestTourClone {
	
	private Tour tour1, tour2;

	@Test
	public void testCloneOfTourWithCopyOfCustomers() {
		givenOneTour();
		andCloningThatTourWithCopyOfCustomers();
		whenCustomersInClonedTourAreAltered();
		ThenCustomersInOriginalTourAndClonedTourShouldBeDifferent();		
	}

	private void givenOneTour() {
		tour1 = SetUpUtils.getTourWithCustomer1And2();
	}

	private void andCloningThatTourWithCopyOfCustomers() {
		tour2 = tour1.cloneWithCopyOfCustomersAndVehicleAndSetParentGot(new GroupOfTours());
	}

	private void whenCustomersInClonedTourAreAltered() {
		tour2.getCustomerAtPosition(1).getCustomer().setDemand(100000);
	}

	private void ThenCustomersInOriginalTourAndClonedTourShouldBeDifferent() {
		assertEquals(true, tour1.getCustomerAtPosition(1).getCustomer().getDemand() != tour2.getCustomerAtPosition(1).getCustomer().getDemand());	
	}
	
}