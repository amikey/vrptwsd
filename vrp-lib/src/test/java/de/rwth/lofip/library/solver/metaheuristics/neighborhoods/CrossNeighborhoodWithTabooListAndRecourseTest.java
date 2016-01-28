package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;
import de.rwth.lofip.testing.util.SetUpSolutionFromString;

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
	public void testApplyMoveToUnderlyingGots2() throws IOException {
		SolutionGot solutionRC104 = SetUpUtils.getSomeSolutionFromRC104Problem();
		
		GroupOfTours got1 = solutionRC104.getGots().get(2);
		GroupOfTours got2 = solutionRC104.getGots().get(3);
		
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 3, 4, 1, 1, 100, 15);
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(SetUpUtils.getSomeRandomDummySolution());
		
		neighborhood.acctuallyApplyMoveAndMaintainNeighborhood(move);
				
		SolutionGot solutionToCompareAgainst = SetUpSolutionFromString.SetUpSolution("( ( 73 17 16 13 58 ) )", ReadAndWriteUtils.readSolomonProblemRC104AsList().get(0));
		assertEquals(true, got1.getFirstTour().equals(solutionToCompareAgainst.getLastTour()));
		assertEquals(true, got2.getFirstTour().equals(SetUpSolutionFromString.SetUpSolution("( ( 99 53 55 ) )", ReadAndWriteUtils.readSolomonProblemRC104AsList().get(0)).getLastTour()));
		
	}
	
	@Test
	public void testCalculateRecourseCostForMoves() throws IOException {
		Parameters.setAllParametersToDefaultValues();
		SolutionGot solutionRC104 = SetUpUtils.getSomeSolutionFromRC104Problem();
		
		GroupOfTours got1 = solutionRC104.getGots().get(0);
		GroupOfTours got2 = solutionRC104.getGots().get(1);
		
		//todo : ist dieser move überhaupt feasible? Ist das wichtig?
		AbstractNeighborhoodMove move = new AbstractNeighborhoodMove(got1.getFirstTour(), got2.getFirstTour(), 2, 2, 0, 1, Double.MAX_VALUE, Double.MAX_VALUE);
		
		CrossNeighborhoodWithTabooListAndRecourse neighborhood = new CrossNeighborhoodWithTabooListAndRecourse(SetUpUtils.getSomeRandomDummySolution());
		neighborhood.setRespAddBestNonTabooMove(move);

		neighborhood.calculateRecourseCostForMoves();
		
		//manually calculate old Recourse Cost
		double oldRecourseCost = got1.getExpectedRecourse().getRecourseCost() + got2.getExpectedRecourse().getRecourseCost();
		
		neighborhood.acctuallyApplyMoveAndMaintainNeighborhood(move);
		
		//manually calculate new Recourse Cost
		double newRecourseCost = got1.getExpectedRecourse().getRecourseCost() + got2.getExpectedRecourse().getRecourseCost();
	
		System.out.println(oldRecourseCost);
		System.out.println(newRecourseCost);
		
		assertEquals(oldRecourseCost,move.getOldRecourseCost().getRecourseCost(),0.0001);
		assertEquals(newRecourseCost,move.getNewRecourseCost().getRecourseCost(),0.0001);	
	}

}
