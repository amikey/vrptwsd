package de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooListAndRecourse;
import de.rwth.lofip.library.util.SetUpUtils;

public class AbstractNeighborhoodMoveTest {
	
	@Test
	public void testCloneAbstractNeighborhoodMove() {
		
		GroupOfTours got1 = SetUpUtils.getGotWithCustomer1And2();
		GroupOfTours got2 = SetUpUtils.getGotWithCustomer3And4();
		
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 1, 2, 0, 1, 100, 15);
		
		AbstractNeighborhoodMove cloneOfMove = move.cloneWithCopyOfToursAndGotsAndCustomers();
		cloneOfMove.getTour2().removeCustomerAtPosition(0);
		
		assertEquals(true, move.getTour1().getParentGot().equals(cloneOfMove.getTour1().getParentGot()));
		assertEquals(false, move.getTour2().getParentGot().equals(cloneOfMove.getTour2().getParentGot()));
		assertEquals(true, move.getTour1().equals(cloneOfMove.getTour1()));
		assertEquals(false, move.getTour2().equals(cloneOfMove.getTour2()));
		
		assertEquals(move.getCost(), cloneOfMove.getCost(), 0.00001);
		assertEquals(move.getCostDifferenceToPreviousSolution(), cloneOfMove.getCostDifferenceToPreviousSolution(), 0.00001);
	}
	
	@Test 
	public void testCloneAbstractNeighborhoodMoveWithOnlyOneGot() {
		GroupOfTours got1 = SetUpUtils.getGotWithCustomer1And2();
		got1.addTour(SetUpUtils.getTourWithCustomer1And4());
		
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got1.getLastTour(), 1, 2, 0, 1, 100, 15);
		
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
		
		assertEquals(move.getCost(), cloneOfMove.getCost(), 0.00001);
		assertEquals(move.getCostDifferenceToPreviousSolution(), cloneOfMove.getCostDifferenceToPreviousSolution(), 0.00001);
	}
	
	@Test
	public void testGetDeterministicAndStochasticCostDifference() throws IOException {
		SolutionGot solutionRC104 = SetUpUtils.getSomeSolutionFromRC104Problem();
		
		GroupOfTours got1 = solutionRC104.getGots().get(0);
		GroupOfTours got2 = solutionRC104.getGots().get(1);
		
		//todo : ist dieser move überhaupt feasible? Ist das wichtig?
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 2, 2, 0, 1, Double.MAX_VALUE, Double.MAX_VALUE);
		
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(SetUpUtils.getSomeRandomDummySolution());
		neighborhood.setRespAddBestNonTabooMove(move);

		neighborhood.calculateRecourseCostForMoves();
		
		//manually calculate old deterministic Cost
		double oldDeterministicCost = got1.getTotalDistanceWithCostFactor() + got2.getTotalDistanceWithCostFactor();
		//manually calculate old Recourse Cost
		double oldRecourseCost = got1.getExpectedRecourse().getRecourseCost() + got2.getExpectedRecourse().getRecourseCost();
		
		neighborhood.acctuallyApplyMoveAndMaintainNeighborhood(move);
		
		//manually calculate new deterministic cost
		double newDeterministicCost = got1.getTotalDistanceWithCostFactor() + got2.getTotalDistanceWithCostFactor();
		//manually calculate new Recourse Cost
		double newRecourseCost = got1.getExpectedRecourse().getRecourseCost() + got2.getExpectedRecourse().getRecourseCost();
	
		double oldCost = oldDeterministicCost + oldRecourseCost;
		double newCost = newDeterministicCost + newRecourseCost;
		double costDifference = newCost - oldCost;
		
		move.setCostOfCompleteSolutionThatResultsFromMove(newDeterministicCost);
		move.setCostDifferenceToPreviousSolution(newDeterministicCost - oldDeterministicCost);
		
		assertEquals(costDifference, move.getDeterministicAndStochasticCostDifference(), 0.00001);
	}
}
