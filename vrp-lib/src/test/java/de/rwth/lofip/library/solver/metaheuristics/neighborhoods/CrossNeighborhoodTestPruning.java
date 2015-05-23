package de.rwth.lofip.library.solver.metaheuristics.neighborhoods;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.util.SetUpUtils;

public class CrossNeighborhoodTestPruning {
	
	private SolutionGot solutionWithTwoToursAndTwoCustomersEach;
	
	@Before
	public void initialise() {
		solutionWithTwoToursAndTwoCustomersEach = SetUpUtils.getSolutionWithTwoToursAndTwoCustomersEach();
	}

	@Test
	public void testPruningBecauseOfViolationOfCapacityConstraint() {	
		initialise();
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		//jump over inner-cross-moves in Tour 1
		for (int i = 1; i <= 36; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();			
		}
		
		//test moves between tour 1 and 2
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true, crossNeighborhood.isNeighborhoodStepEquals(0, 0, 0, 0));
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true, crossNeighborhood.isNeighborhoodStepEquals(0, 0, 0, 1));
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true, crossNeighborhood.isNeighborhoodStepEquals(0, 0, 1, 1));
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true, crossNeighborhood.isNeighborhoodStepEquals(0, 0, 1, 2));
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true, crossNeighborhood.isNeighborhoodStepEquals(0, 0, 2, 2));
		assertEquals(false,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegments();		
		
		assertEquals(true, crossNeighborhood.isNeighborhoodStepEquals(0, 1, 0, 0));
		assertEquals(true,crossNeighborhood.endOfSegmentToBeRemovedCanBeIncreasedInTour2());
		assertEquals(true,isMovePossible(crossNeighborhood));
		crossNeighborhood.generateNextCombinationOfSegments();
		assertEquals(true, crossNeighborhood.isNeighborhoodStepEquals(0, 1, 0, 1));
		assertEquals(true,isMovePossible(crossNeighborhood));
		assertEquals(true,crossNeighborhood.endOfSegmentToBeRemovedCanBeIncreasedInTour2());	
	}
	
	@Test 
	public void testIncreaseTour2NoPruning() {	
		CrossNeighborhood crossNeighborhood = new CrossNeighborhood(solutionWithTwoToursAndTwoCustomersEach);
		//jump over inner-cross-moves in Tour 1
		for (int i = 1; i <= 36; i++) {
			crossNeighborhood.generateNextCombinationOfSegments();			
		}		
		assertEquals(true, crossNeighborhood.tour2CanBeIncreased());
	}
		
	private boolean isMovePossible(CrossNeighborhood crossNeighborhood) {
		if(crossNeighborhood.segmentsToBeSwapedAreNotInNeighborhoodRefPositions())
			return false;
		else if (crossNeighborhood.isMoveFeasibleCheckWithRef())
			return true;
		else return false;
	}
	
	
	
}
