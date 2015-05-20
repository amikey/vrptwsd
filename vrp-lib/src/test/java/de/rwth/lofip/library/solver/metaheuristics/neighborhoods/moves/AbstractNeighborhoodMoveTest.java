package de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.util.SetUpUtils;

public class AbstractNeighborhoodMoveTest {
	
	@Test
	public void testCloneAbstractNeighborhoodMove() {
		
		GroupOfTours got1 = SetUpUtils.getGotWithCustomer1And2();
		GroupOfTours got2 = SetUpUtils.getGotWithCustomer3And4();
		
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 1, 2, 0, 1, 100);
		
		AbstractNeighborhoodMove cloneOfMove = move.cloneWithCopyOfToursAndGotsAndCustomers();
		cloneOfMove.getTour2().removeCustomerAtPosition(0);
		
		assertEquals(true, move.getTour1().getParentGot().equals(cloneOfMove.getTour1().getParentGot()));
		assertEquals(false, move.getTour2().getParentGot().equals(cloneOfMove.getTour2().getParentGot()));
		assertEquals(true, move.getTour1().equals(cloneOfMove.getTour1()));
		assertEquals(false, move.getTour2().equals(cloneOfMove.getTour2()));		
	}
	
	@Test 
	public void testCloneAbstractNeighborhoodMoveWithOnlyOneGot() {
		GroupOfTours got1 = SetUpUtils.getGotWithCustomer1And2();
		got1.addTour(SetUpUtils.getTourWithCustomer1And4());
		
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got1.getLastTour(), 1, 2, 0, 1, 100);
		
		AbstractNeighborhoodMove cloneOfMove = move.cloneWithCopyOfToursAndGotsAndCustomers();
		cloneOfMove.getTour2().removeCustomerAtPosition(0);
		
		move.getTour1().getParentGot().print();
		cloneOfMove.getTour1().getParentGot().print();
		assertEquals(false, move.getTour1().getParentGot().equals(cloneOfMove.getTour1().getParentGot()));
		
		move.getTour2().getParentGot().print();
		cloneOfMove.getTour2().getParentGot().print();
		assertEquals(false, move.getTour2().getParentGot().equals(cloneOfMove.getTour2().getParentGot()));
		assertEquals(true, move.getTour1().equals(cloneOfMove.getTour1()));
		assertEquals(false, move.getTour2().equals(cloneOfMove.getTour2()));
	}
}
