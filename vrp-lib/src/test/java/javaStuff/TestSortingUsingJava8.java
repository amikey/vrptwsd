package javaStuff;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooListAndRecourse;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestSortingUsingJava8 {

	@Test
	public void testSortingUsingJava8() {
		SolutionGot solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3();
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(solution);
		//Fictitious previous solution cost = 1100 
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(1000, -100));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(900, -200));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(700, -400));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(100, -1000));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(800, -300));
		
		neighborhood.sortMovesWrtDeterministicCost();
		
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(0).equals(new AbstractNeighborhoodMove(100, -1000)));
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(1).equals(new AbstractNeighborhoodMove(700, -400)));
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(2).equals(new AbstractNeighborhoodMove(800, -300)));
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(3).equals(new AbstractNeighborhoodMove(900, -200)));
		
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
		assertEquals(Parameters.getNumberOfMovesThatRecourseCostAreCalculatedFor(),neighborhood.getListOfNonTabooMoves().size());
	}
	
	@Test
	public void testSortingUsingJava8SortByCostDifference() {
		SolutionGot solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3();
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(solution);
		//Fictitious previous solution cost = 1100 
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(1000, -100));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(900, -200));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(700, -400));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(100, -1000));
		neighborhood.setRespAddBestNonTabooMove(new AbstractNeighborhoodMove(800, -300));
	
		
		neighborhood.sortMovesWrtCostDifference();
		
		System.out.println(neighborhood.getListOfNonTabooMoves().get(0).getCostDifferenceToPreviousSolution());
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(0).equals(new AbstractNeighborhoodMove(100, -1000)));
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(1).equals(new AbstractNeighborhoodMove(700, -400)));
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(2).equals(new AbstractNeighborhoodMove(800, -300)));
		assertEquals(true, neighborhood.getListOfNonTabooMoves().get(3).equals(new AbstractNeighborhoodMove(900, -200)));
	}
	
}
