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
import de.rwth.lofip.library.solver.util.ResourceExtensionFunction;
import de.rwth.lofip.library.util.SetUpUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class CrossNeighborhoodTest {

	private SolutionGot solutionWithTwoToursAndTwoCustomersEach;
	private SolutionGot solutionWithOneTourWithCustomers2And3;
	private SolutionGot solutionWithOneTourWithFourCustomers;
	private SolutionGot solutionWithOneTourWithCustomers1And2And3;
	
	@Before
	public void initialise() {
		SetUpUtils.getSolutionWithThreeToursAndTwoCustomersEach();
		solutionWithTwoToursAndTwoCustomersEach = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();
		solutionWithOneTourWithCustomers2And3 = SetUpUtils.getSolutionWithOneTourWithCustomer2And3();
		SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4();
		solutionWithOneTourWithFourCustomers = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4();
		solutionWithOneTourWithCustomers1And2And3 = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3();
	}
	
	@Test
	public void performHasNextCombinationOfSegmentsTest() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		assertEquals(crossNeighborhood.isExistsNextCombinationOfSegments(), true);
		int i = 0;
		while (crossNeighborhood.isExistsNextCombinationOfSegments()) {
			crossNeighborhood.generateNextCombinationOfSegments();
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
			crossNeighborhood.generateNextCombinationOfSegments();			
		}	
		assertEquals(false,crossNeighborhood.getTour1().getRefsFromBeginning().isEmpty());
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(false,crossNeighborhood.getTour1().getRefsFromBeginning().isEmpty());		
		for (int i = 1; i <= 14; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();			
		}	
		crossNeighborhood.generateNextCombinationOfSegments();		
		for (int i = 1; i <= 11; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();			
		}
		assertEquals(false,crossNeighborhood.isExistsNextCombinationOfSegments());		
	}	
	
	@Test
	public void testIsMovePossibleInnerTourMovesTestWithRef() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithOneTourWithCustomers1And2And3);
		for (int i = 1; i <= 17; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();
			assertEquals(true,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		}
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(false,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		assertEquals(false,crossNeighborhood.isMoveFeasibleCheckWithRef());
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(false,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		assertEquals(false,crossNeighborhood.isMoveFeasibleCheckWithRef());
		for (int i = 1; i <= 9; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();
			assertEquals(true,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		}		
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(false,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		assertEquals(false,crossNeighborhood.isMoveFeasibleCheckWithRef());
		for (int i = 1; i <= 20; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();
			assertEquals(true,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		}		
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(false,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		assertEquals(false,crossNeighborhood.isMoveFeasibleCheckWithRef());
		for (int i = 1; i <= 8; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();
			assertEquals(true,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		}		
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(false,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		assertEquals(true,crossNeighborhood.isMoveFeasibleCheckWithRef());
		for (int i = 1; i <= 25; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();		
		}
		assertEquals(false,crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions());
		assertEquals(true,crossNeighborhood.isMoveFeasibleCheckWithRef());
		crossNeighborhood.printNeighborhoodStep();
		crossNeighborhood.actuallyApplyMoveAndMaintainNeighborhood();
		solutionWithOneTourWithCustomers1And2And3.printSolutionAsTupel();
		assertEquals(true, solutionWithOneTourWithCustomers1And2And3.equals(SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2()));
	}	

	@Test
	public void performCalculateCostForCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);		
		double cost;			
		//jump over inner-cross-Moves in Tour 1
		for (int i = 1; i <= 36; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();
			crossNeighborhood.getRefSegment2().print();
		}
		//moves between tour 1 and 2
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		
		assertEquals(true, crossNeighborhood.isMoveFeasibleCheckWithRef());
		assertEquals(true, crossNeighborhood.getRefSegment1().equals(new ResourceExtensionFunction(SetUpUtils.getC1())));
		crossNeighborhood.getRefSegment2().print();
		assertEquals(true, crossNeighborhood.getRefSegment2().equals(new ResourceExtensionFunction(SetUpUtils.getC3())));	
		assertEquals(true, crossNeighborhood.isMoveFeasibleCheckWithRef());
		cost = crossNeighborhood.calculateCostUsingRefs();
//		cost = crossNeighborhood.calculateCost();	
		assertEquals(SetUpUtils.getTourWithCustomer1And4().getTotalDistanceWithCostFactor() + SetUpUtils.getTourWithCustomer3And2().getTotalDistanceWithCostFactor(),cost,0.1);
		
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true, crossNeighborhood.isMoveFeasibleCheckWithRef());
		cost = crossNeighborhood.calculateCostUsingRefs();	
		
		SolutionGot solution = (SolutionGot) crossNeighborhood.actuallyApplyMoveAndMaintainNeighborhood(crossNeighborhood.getNeigborhoodMove());
		System.out.print(solution.getAsTupel());
		
		assertEquals(SetUpUtils.getTourWithFourCustomers().getTotalDistanceWithCostFactor(),cost,0.1);		
	}
		
	@Test 
	public void findBestCrossMove() {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		solutionWithTwoToursAndTwoCustomersEach.printSolutionAsTupel();
		//generate moves
		List<AbstractNeighborhoodMove> moves = new LinkedList<AbstractNeighborhoodMove>();
		while (crossNeighborhood.isExistsNextCombinationOfSegments()) {
			crossNeighborhood.generateNextCombinationOfSegments();
			if (!crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions())
				if (crossNeighborhood.isMoveFeasibleCheckWithRef()) {
					crossNeighborhood.calculateCostUsingRefs();				
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
                        if (MathUtils.lessThan(o1.getCost(), o2.getCost())) {
                            return -1;
                        }
                        if (MathUtils.greaterThan(o1.getCost(), o2.getCost())) {
                            return 1;
                        }
                        return 0;
                    }
                }
		);			
		AbstractNeighborhoodMove bestMove = moves.get(0); 
		assertEquals(SetUpUtils.getTourWithFourCustomers().getTotalDistanceWithCostFactor(), bestMove.getCost(), 0.1);
				
		//now actually apply best move
		solutionWithTwoToursAndTwoCustomersEach = (SolutionGot) crossNeighborhood.actuallyApplyMoveAndMaintainNeighborhood(bestMove);
		System.out.println(solutionWithTwoToursAndTwoCustomersEach.getAsTupel());
		Tour tempTour;
		Iterator<Tour> tourIterator = solutionWithTwoToursAndTwoCustomersEach.getTours().iterator();
		tempTour = tourIterator.next();
		assertEquals(true, tempTour.equals(SetUpUtils.getTourWithFourCustomers()));
	}
	
	@Test 
	public void testReturnBestMove() throws Exception {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		AbstractNeighborhoodMove bestMove = crossNeighborhood.returnBestMove();
		assertEquals(SetUpUtils.getTourWithFourCustomers().getTotalDistanceWithCostFactor(), bestMove.getCost(), 0.01);
	}
	
	@Test 
	public void testCalculateCost2() throws Exception {
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithOneTourWithFourCustomers);		
		AbstractNeighborhoodMove move = crossNeighborhood.returnBestMove();
		assertEquals(SetUpUtils.getSolutionWithOneTourWithCustomersC1C3C2C4().getTotalDistanceWithCostFactor(), move.getCost(), 0.01);
	}
	
	@Test 
	public void testCalulationOfRefsInNeighborhood() {		
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);					
		//inner-cross-Moves in Tour 1
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(0, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(Double.MAX_VALUE, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(0, crossNeighborhood.getRefSegment1().getEarliestDepartureTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(90, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(345, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(368, crossNeighborhood.getRefSegment1().getEarliestDepartureTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(191.180, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(345, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(565, crossNeighborhood.getRefSegment1().getEarliestDepartureTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(0, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(Double.MAX_VALUE, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(0, crossNeighborhood.getRefSegment1().getEarliestDepartureTime(), 0.001);
		for (int i = 1; i <= 6; i++) 
			crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(90, crossNeighborhood.getRefSegment1().getDuration(), 0.001);
		assertEquals(528, crossNeighborhood.getRefSegment1().getLatestArrivalTime(), 0.001);
		assertEquals(565, crossNeighborhood.getRefSegment1().getEarliestDepartureTime(), 0.001);
	}
}
