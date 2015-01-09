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
import de.rwth.lofip.library.util.SetUpUtils;

public class CrossNeighborhoodTest {

	private SolutionGot solutionWithThreeToursAndTwoCustomersEach;
	private SolutionGot solutionWithTwoToursAndTwoCustomersEach;
	
	@Before
	public void initialise() {
//		tour1 = SetUpUtils.setUpFeasibleTourWithThreeCustomers();
//		tour2 = SetUpUtils.setUpFeasibleTourWithThreeCustomers();
		solutionWithThreeToursAndTwoCustomersEach = SetUpUtils.SetUpSolutionWithThreeToursAndTwoCustomersEach();
		solutionWithTwoToursAndTwoCustomersEach = SetUpUtils.SetUpSolutionWithTwoToursAndTwoCustomersEach();
	}
	
	@Test
	public void performHasNextNeighborhoodStepTest() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithThreeToursAndTwoCustomersEach);
		assertEquals(crossNeighborhood.hasNextNeighborhoodMove(), true);
		int i = 0;
		while (crossNeighborhood.hasNextNeighborhoodMove()) {
			crossNeighborhood.generateNextNeigborhoodMove();
			i++;
		};
		assertEquals(i, 27);
		assertEquals(crossNeighborhood.hasNextNeighborhoodMove(), false);
	}

	@Test
	public void performCalculateCostForCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithThreeToursAndTwoCustomersEach);		
		double cost;	
		assertEquals(true, crossNeighborhood.isMovePossible());
		if (crossNeighborhood.isMovePossible()) {
			cost = crossNeighborhood.calculateCost();	
			assertEquals(SetUpUtils.getTourWithCustomer1And4().getTotalDistance() + SetUpUtils.getTourWithCustomer3And2().getTotalDistance() + SetUpUtils.getTourWithCustomer1And2().getTotalDistance(),cost,0.1);			
		}
	}
	
	@Test 
	public void testIsMovePossible() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithThreeToursAndTwoCustomersEach);
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());	
	}

	@Test 
	public void findBestCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		//generate moves
		List<CrossNeighborhoodMove> moves = new LinkedList<CrossNeighborhoodMove>();
		while (crossNeighborhood.hasNextNeighborhoodMove()) {
			crossNeighborhood.generateNextNeigborhoodMove();
			if (crossNeighborhood.isMovePossible()) {
				crossNeighborhood.calculateCost();				
				CrossNeighborhoodMove move = crossNeighborhood.getNeigborhoodMove();
				moves.add(move);
			}
		}		
		assertEquals(false, moves.isEmpty());
		
		//now find best move		
		Collections.sort(moves,
				new Comparator<CrossNeighborhoodMove>() {
                    @Override
                    public int compare(CrossNeighborhoodMove o1,
                            CrossNeighborhoodMove o2) {
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
		CrossNeighborhoodMove bestMove = moves.get(0); 
		assertEquals(171.6, bestMove.getCost(), 0.1);
				
		//now actually apply best move
		solutionWithTwoToursAndTwoCustomersEach = crossNeighborhood.acctuallyApplyMove(bestMove);
		Tour tempTour;
		Iterator<Tour> tourIterator = solutionWithTwoToursAndTwoCustomersEach.getTours().iterator();
		tempTour = tourIterator.next();
		assertEquals(true, tempTour.equals(SetUpUtils.getTourWithCustomer3()));
		tempTour = tourIterator.next();
		assertEquals(true, tempTour.equals(SetUpUtils.getTourWithCustomer1And2And4()));
	}
	
	@Test 
	public void testReturnBestMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		CrossNeighborhoodMove bestMove = crossNeighborhood.returnBestCrossMove();
		assertEquals(171.6, bestMove.getCost(), 0.01);
	}

}
