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
	
	@Before
	public void initialise() {
		solutionWithThreeToursAndTwoCustomersEach = SetUpUtils.getSolutionWithThreeToursAndTwoCustomersEach();
		solutionWithTwoToursAndTwoCustomersEach = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();
		solutionWithOneTourWithCustomers2And3 = SetUpUtils.SetUpSolutionWithOneTourWithCustomer2And3();
		solutionWithOneTourWithFourCustomersC3BeforeC2 = SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		solutionWithOneTourWithFourCustomers = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4();
	}
	
	@Test
	public void performHasNextNeighborhoodStepTest() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		assertEquals(crossNeighborhood.hasNextNeighborhoodMove(), true);
		int i = 0;
		while (crossNeighborhood.hasNextNeighborhoodMove()) {
			crossNeighborhood.generateNextNeigborhoodMove();
			i++;
		};
		assertEquals(3*36, i);
		assertEquals(crossNeighborhood.hasNextNeighborhoodMove(), false);
	}

	@Test
	public void performCalculateCostForCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);		
		double cost;			
		//jump over inner-cross-Moves in Tour 1
		for (int i = 1; i <= 36; i++) {
			crossNeighborhood.generateNextNeigborhoodMove();			
		}
		//moves between tour 1 and 2
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		
		assertEquals(true, crossNeighborhood.isMovePossible());
		cost = crossNeighborhood.calculateCost();	
		assertEquals(SetUpUtils.getTourWithCustomer1And4().getTotalDistance() + SetUpUtils.getTourWithCustomer3And2().getTotalDistance(),cost,0.1);
		
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true, crossNeighborhood.isMovePossible());
		cost = crossNeighborhood.calculateCost();	
		
		SolutionGot solution = crossNeighborhood.acctuallyApplyMove(crossNeighborhood.getNeigborhoodMove());
		System.out.print(solution.getSolutionAsTupel());
		
		assertEquals(SetUpUtils.getTourWithFourCustomers().getTotalDistance(),cost,0.1);		
	}
	
	
	@Test
	public void testIsMovePossibleInnerTourMoves() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithOneTourWithCustomers2And3);
		for (int i = 1; i <= 9; i++) {
			crossNeighborhood.generateNextNeigborhoodMove();
			assertEquals(false,crossNeighborhood.isMovePossible());
		}	
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());		
		for (int i = 1; i <= 14; i++) {
			crossNeighborhood.generateNextNeigborhoodMove();
			assertEquals(false,crossNeighborhood.isMovePossible());
		}	
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());
		for (int i = 1; i <= 11; i++) {
			crossNeighborhood.generateNextNeigborhoodMove();
			assertEquals(false,crossNeighborhood.isMovePossible());
		}
		assertEquals(false,crossNeighborhood.hasNextNeighborhoodMove());
		
	}
	
	@Test 
	public void testIsMovePossible() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithThreeToursAndTwoCustomersEach);
		//jump over inner-cross-Moves in Tour 1
		for (int i = 1; i <= 36; i++) {
			crossNeighborhood.generateNextNeigborhoodMove();			
		}			
		
		//test moves between tour 1 and 2
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(false,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());
		crossNeighborhood.generateNextNeigborhoodMove();
		assertEquals(true,crossNeighborhood.isMovePossible());	
	}

	@Test 
	public void findBestCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		//generate moves
		List<AbstractNeighborhoodMove> moves = new LinkedList<AbstractNeighborhoodMove>();
		while (crossNeighborhood.hasNextNeighborhoodMove()) {
			crossNeighborhood.generateNextNeigborhoodMove();
			if (crossNeighborhood.isMovePossible()) {
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
//		System.out.println("Cost: " + solutionWithTwoToursAndTwoCustomersEach.getTotalDistance());
//		System.out.println("Cost best move: " + bestMove.getCost());
//		System.out.println("Tour with 4 Customers Cost: " + SetUpUtils.setUpFeasibleTourWithFourCustomers().getTotalDistance());

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

}
