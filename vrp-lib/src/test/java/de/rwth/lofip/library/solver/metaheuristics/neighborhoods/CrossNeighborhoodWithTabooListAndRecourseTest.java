package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.*;

import org.junit.Test;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.CrossNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class CrossNeighborhoodWithTabooListAndRecourseTest {
	
	@Test
	public void testSortingUsingJava8() {
		SolutionGot solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3();
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(solution);
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(1000));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(900));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(700));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(100));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(800));
		
		neighborhood.sortMovesWrtDeterministicCost();
		
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(0).equals(new CrossNeighborhoodMove(100)));
		
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(1000));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(900));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(700));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(100));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(800));
		
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(1000));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(900));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(700));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(100));
		neighborhood.setRespAddBestNonTabooMove(new CrossNeighborhoodMove(800));
		
		neighborhood.takeFirstXNumberOfMoves();
		assertEquals(neighborhood.numberOfMovesThatRecourseCostAreCalculatedFor,neighborhood.getListOfNonTabooMoves().size());
	}
	
	@Test
	public void testApplyMoveToUnderlyingGots() {
		GroupOfTours got1 = SetUpUtils.getGotWithCustomer1And2();
		GroupOfTours got2 = SetUpUtils.getGotWithCustomer3And4();
		
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 2, 2, 0, 1, 100);
		
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3());
		neighborhood.acctuallyApplyMove(move);
		
		assertEquals(true, got1.getFirstTour().equals(SetUpUtils.getTourWithCustomers1And2And3()));
		assertEquals(true, got2.getFirstTour().equals(SetUpUtils.getTourWithCustomer4()));
	}

}
