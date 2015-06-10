package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class CrossNeighborhoodWithTabooListAndRecourseTest {
		
	@Test
	public void testApplyMoveToUnderlyingGots() {
		GroupOfTours got1 = SetUpUtils.getGotWithCustomer1And2();
		GroupOfTours got2 = SetUpUtils.getGotWithCustomer3And4();
		
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 2, 2, 0, 1, 100, 15);
		
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3());
		neighborhood.acctuallyApplyMove(move);
		
		assertEquals(true, got1.getFirstTour().equals(SetUpUtils.getTourWithCustomers1And2And3()));
		assertEquals(true, got2.getFirstTour().equals(SetUpUtils.getTourWithCustomer4()));
	}
	
	@Test
	public void testCalculateRecourseCostForMoves() {
		throw new RuntimeException("Impelement test for CalculateRecourseCostForMoves()");
	}
	
	@Test 
	public void testSortMovesWrtToStochasticAspect() {
		throw new RuntimeException("Impelement test for SortMovesWrtToStochasticAspect()");
	}

}
