package de.rwth.lofip.library.solver.util;

import static org.junit.Assert.assertEquals;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;

public class GreedyInsertionWithLinearTimeChecks extends GreedyInsertion {
	
	@Override
	protected boolean isInsertionPossible(Customer customerToBeInserted,
			Tour tour, int i) {
		boolean testTWLinearTime = TourUtils.
        		isInsertionPossibleWrtDemandAndTWinLinearTime(
                        customerToBeInserted,
                        tour, i);
		boolean testTWRef = TourUtils.isInsertionOfCustomerFeasibleTestWithRef(tour, customerToBeInserted, i);
		assertEquals(testTWLinearTime, testTWRef);
		return TourUtils. //isInsertionOfCustomerFeasibleTestWihtRef(tour, customerToBeInserted, i)) {
        		isInsertionPossibleWrtDemandAndTWinLinearTime(
                        customerToBeInserted,
                        tour, i);        		
	}


}
