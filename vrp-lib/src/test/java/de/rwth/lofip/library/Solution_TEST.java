package de.rwth.lofip.library;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.rwth.lofip.library.util.SetUpUtils;
import de.rwth.lofip.stuffNotNeededRightNow.Solution;

public class Solution_TEST {	
	Solution solution;

    @Test
    public void testThatFeasibleSolutionIsFeasible() {
      givenFeasibleSolution();
      
      thenSolutionShouldBeFeasible();
    }
    
    private void thenSolutionShouldBeFeasible() {
        assertTrue(solution.isSolutionFeasibleWrtDemand());		
	}
    

		
	@Test
    public void testThatInfeasibleSolutionIsInfeasible() {
      givenInfeasibleSolution();
      
      thenSolutionShouldBeInfeasible();           
    }
	
	private void thenSolutionShouldBeInfeasible() {
		assertFalse(solution.isSolutionFeasibleWrtDemand());		
	}
    
    
    @Test
    public void testThatInfeasibleSolutionHasOneTourWithCostFactorOne() {
    	givenInfeasibleSolution();
    	
    	double d = 1.0;
    	thenCostFactorOfAllToursShouldBe(d);
    }
        
	private void thenCostFactorOfAllToursShouldBe(double d) {
    	System.out.println("Kostenfaktoren:");
    	for (Tour t : solution.getTours())	
    	{
    		System.out.println(t.getCostFactor());
    		assertEquals(t.getCostFactor(),d,0);
    	}
	}
	
	
	private void givenInfeasibleSolution() {
		solution = SetUpUtils.SetUpInfeasibleSolution();
    }
	
	private void givenFeasibleSolution() 
    {
    	solution = SetUpUtils.SetUpFeasibleSolution();
    }
	
	
	
	

    

    

    
}
