package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class CrossNeighborhoodWithTabooListAndRecourseTest {
		
	@Test
	public void testApplyMoveToUnderlyingGots() {
		GroupOfTours got1 = SetUpUtils.getGotWithCustomer1And2();
		GroupOfTours got2 = SetUpUtils.getGotWithCustomer3And4();
		
		//dieser Move generiert eine Tour mit Kunden 1, 2, 3 und eine Tour mit Kunden 4
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 2, 2, 0, 1, 100, 15);
		
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(SetUpUtils.getSomeRandomDummySolution());
		neighborhood.acctuallyApplyMoveAndMaintainNeighborhood(move);
		
		assertEquals(true, got1.getFirstTour().equals(SetUpUtils.getTourWithCustomers1And2And3()));
		assertEquals(true, got2.getFirstTour().equals(SetUpUtils.getTourWithCustomer4()));
	}
	
	@Test
	public void testCalculateRecourseCostForMoves() throws IOException {
		throw new RuntimeException("Implement test for CalculateRecourseCostForMoves()");
		
		SolutionGot solutionRC104 = SetUpUtils.getSomeSolutionFromRC104Problem();
		
		GroupOfTours got1 = solutionRC104.getGots().get(0);
		GroupOfTours got2 = solutionRC104.getGots().get(1);
		
		//dieser Move generiert eine Tour mit Kunden 1, 2, 3 und eine Tour mit Kunden 4
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 2, 2, 0, 1, 100, 15);
		
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(SetUpUtils.getSomeRandomDummySolution());
		neighborhood.setRespAddBestNonTabooMove(move);
		neighborhood.calculateRecourseCostForMoves();
		System.out.println(neighborhood.getFirstMoveInMoveList().getDeterministicAndStochasticCostDifference());
	}
	
	@Test 
	public void testSortMovesWrtToStochasticAspect() {
		throw new RuntimeException("Impelement test for SortMovesWrtToStochasticAspect()");
	}

}
