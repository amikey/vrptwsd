package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.SetUpUtils;

public class CrossNeighborhoodTest {

	private SolutionGot solutionWithThreeToursAndTwoCustomersEach;
	private SolutionGot solutionWithTwoToursAndTwoCustomersEach;
	private SolutionGot solutionWithOneTourWithCustomers2And3;
	private SolutionGot solutionWithOneTourWithFourCustomersC3BeforeC2;
	private SolutionGot solutionWithOneTourWithFourCustomers;
	private SolutionGot solutionWithTwoToursWithThreeAndOneCustomersResp;
	
	@Before
	public void initialise() {
		solutionWithThreeToursAndTwoCustomersEach = SetUpUtils.getSolutionWithThreeToursAndTwoCustomersEach();
		solutionWithTwoToursAndTwoCustomersEach = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();
		solutionWithOneTourWithCustomers2And3 = SetUpUtils.SetUpSolutionWithOneTourWithCustomer2And3();
		solutionWithOneTourWithFourCustomersC3BeforeC2 = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		solutionWithOneTourWithFourCustomers = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4();		
	}
	
	@Test
	public void performHasNextCombinationOfSegmentsTest() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		assertEquals(crossNeighborhood.isExistsNextCombinationOfSegments(), true);
		int i = 0;
		while (crossNeighborhood.isExistsNextCombinationOfSegments()) {
			crossNeighborhood.generateNextCombinationOfSegements();
			i++;
		};
		assertEquals(3*36, i);
		assertEquals(crossNeighborhood.isExistsNextCombinationOfSegments(), false);
	}
	
	@Test
	public void testIsMovePossibleInnerTourMoves() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithOneTourWithCustomers2And3);
		assertEquals(false,solutionWithOneTourWithCustomers2And3.getGots().get(0).getFirstTour().getRefsFromBeginning().isEmpty());
		for (int i = 1; i <= 9; i++) {
			crossNeighborhood.generateNextCombinationOfSegements();
			assertEquals(false,isMovePossible(crossNeighborhood));
		}	
		assertEquals(false,crossNeighborhood.getTour1().getRefsFromBeginning().isEmpty());
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(false,crossNeighborhood.getTour1().getRefsFromBeginning().isEmpty());
		assertEquals(true,isMovePossible(crossNeighborhood));		
		for (int i = 1; i <= 14; i++) {
			crossNeighborhood.generateNextCombinationOfSegements();
			assertEquals(false,isMovePossible(crossNeighborhood));
		}	
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(true,isMovePossible(crossNeighborhood));
		for (int i = 1; i <= 11; i++) {
			crossNeighborhood.generateNextCombinationOfSegements();
			assertEquals(false,isMovePossible(crossNeighborhood));
		}
		assertEquals(false,crossNeighborhood.isExistsNextCombinationOfSegments());		
	}
	
	private Object isMovePossible(CrossNeighborhood crossNeighborhood) {
		if(crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhood())
			return false;
		else if (crossNeighborhood.isMoveFeasible())
			return true;
		else return false;
	}

	@Test 
	public void testIsMovePossibleWithMovesBetweenTours() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithThreeToursAndTwoCustomersEach);
		//jump over inner-cross-Moves in Tour 1
		for (int i = 1; i <= 36; i++) {
			crossNeighborhood.generateNextCombinationOfSegements();			
		}			
		
		//test moves between tour 1 and 2
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(true,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(true,isMovePossible(crossNeighborhood));	
	}

	@Test
	public void performCalculateCostForCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);		
		double cost;			
		//jump over inner-cross-Moves in Tour 1
		for (int i = 1; i <= 36; i++) {
			crossNeighborhood.generateNextCombinationOfSegements();			
		}
		//moves between tour 1 and 2
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		
		assertEquals(true, crossNeighborhood.isMoveFeasible());
		cost = crossNeighborhood.calculateCost();	
		assertEquals(SetUpUtils.getTourWithCustomer1And4().getTotalDistance() + SetUpUtils.getTourWithCustomer3And2().getTotalDistance(),cost,0.1);
		
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(true, crossNeighborhood.isMoveFeasible());
		cost = crossNeighborhood.calculateCost();	
		
		SolutionGot solution = crossNeighborhood.acctuallyApplyMove(crossNeighborhood.getNeigborhoodMove());
		System.out.print(solution.getSolutionAsTupel());
		
		assertEquals(SetUpUtils.getTourWithFourCustomers().getTotalDistance(),cost,0.1);		
	}
		
	@Test 
	public void findBestCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		solutionWithTwoToursAndTwoCustomersEach.printSolutionAsTupel();
		//generate moves
		List<AbstractNeighborhoodMove> moves = new LinkedList<AbstractNeighborhoodMove>();
		while (crossNeighborhood.isExistsNextCombinationOfSegments()) {
			crossNeighborhood.generateNextCombinationOfSegements();
			if (!crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhood())
				if (crossNeighborhood.isMoveFeasible()) {
					crossNeighborhood.calculateCost();				
					AbstractNeighborhoodMove move = crossNeighborhood.getNeigborhoodMove();
					moves.add(move);
				}
		}		
		assertEquals(false, moves.isEmpty());
		
		//now find best move		
		Collections.sort(moves,
				new Comparator<AbstractNeighborhoodMove>() {
                    @Override
                    public int compare(AbstractNeighborhoodMove o1,
                            AbstractNeighborhoodMove o2) {
                        if (o1.getCost() < o2.getCost()) {
                            return -1;
                        }
                        if (o1.getCost() > o2.getCost()) {
                            return 1;
                        }
                        return 0;
                    }
                }
		);			
		AbstractNeighborhoodMove bestMove = moves.get(0); 
		assertEquals(SetUpUtils.getTourWithFourCustomers().getTotalDistance(), bestMove.getCost(), 0.1);
				
		//now actually apply best move
		solutionWithTwoToursAndTwoCustomersEach = crossNeighborhood.acctuallyApplyMove(bestMove);
		System.out.println(solutionWithTwoToursAndTwoCustomersEach.getSolutionAsTupel());
		Tour tempTour;
		Iterator<Tour> tourIterator = solutionWithTwoToursAndTwoCustomersEach.getTours().iterator();
		tempTour = tourIterator.next();
		assertEquals(true, tempTour.equals(SetUpUtils.getTourWithFourCustomers()));
	}
	
	@Test 
	public void testReturnBestMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		AbstractNeighborhoodMove bestMove = crossNeighborhood.returnBestMove();
		assertEquals(SetUpUtils.getTourWithFourCustomers().getTotalDistance(), bestMove.getCost(), 0.01);
	}
	
	@Test 
	public void testCalculateCost2() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithOneTourWithFourCustomers);		
		AbstractNeighborhoodMove move = crossNeighborhood.returnBestMove();
		assertEquals(SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4().getTotalDistance(), move.getCost(), 0.01);
	}
	
	@Test 
	public void testCalulationOfRefsInNeighborhood() {		
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);					
		//inner-cross-Moves in Tour 1
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(0, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(Double.MAX_VALUE, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(0, crossNeighborhood.getRefSegment1().getEarliestLeavingTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(90, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(345, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(278, crossNeighborhood.getRefSegment1().getEarliestLeavingTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(191.180, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(345, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(475, crossNeighborhood.getRefSegment1().getEarliestLeavingTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(0, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(Double.MAX_VALUE, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(0, crossNeighborhood.getRefSegment1().getEarliestLeavingTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegements();
		assertEquals(90, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(528, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(475, crossNeighborhood.getRefSegment1().getEarliestLeavingTime(), 0.001);
	}
}
