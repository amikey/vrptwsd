package javaStuff;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooListAndRecourse;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestSortingUsingJava8 {

	@Test
	public void testSortingUsingJava8() {
		SolutionGot solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3();
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(solution);
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(1000));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(900));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(700));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(100));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(800));
		
		neighborhood.sortMovesWrtDeterministicCost();
		
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(0).equals(new AbstractNeighborhoodMove(100)));
		
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(1000));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(900));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(700));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(100));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(800));
		
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(1000));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(900));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(700));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(100));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(800));
		
		neighborhood.takeFirstXNumberOfMoves();
		assertEquals(CrossNeighborhoodWithTabooListAndRecourse.getNumberOfMovesThatRecourseCostAreCalculatedFor(),neighborhood.getListOfNonTabooMoves().size());
	}
	
}
