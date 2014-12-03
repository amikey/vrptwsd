package de.rwth.lofip.library;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.rwth.lofip.library.scenario.Event;
import de.rwth.lofip.library.solver.repair.RepairSolution;
import de.rwth.lofip.library.util.SetUpUtils;

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
	
	
	
	@Test
	public void testThatNumberToursExecutedTheSameDayIsCorrect() {
		givenInfeasibleSolution();
		
		double d = 1;
		thenNumberOfToursExecutedTheSameDayShouldBe(d);
	}
	
    private void thenNumberOfToursExecutedTheSameDayShouldBe(double d) {
    	assertEquals(solution.NumberOfToursExecutedTheSameDay(), d, 0);
	}
    
    
    @Test
    public void testThatNumberToursExecutedTheSameDayIsCorrectNegative() {
    	solution = SetUpUtils.SetUpFeasibleSolutionWhereSingleTourIsExecutedTheNextDay();
    	
    	double d = 0;
		thenNumberOfToursExecutedTheSameDayShouldBe(d);
    }
    
    
    
    @Test
    public void testThatNumberOfCustomersServedTheSameDayIsCorrect() {
    	givenSolutionWhereSingleTourIsExecutedTheSameDay();    	
    	
    	double d = 4;
    	thenNumberOfCustomersServedTheSameDayShouldBe(d);
    }
     

	private void givenSolutionWhereSingleTourIsExecutedTheSameDay() {
    	solution = SetUpUtils.SetUpFeasibleSolutionWhereSingleTourIsExecutedTheSameDay();
	}

	@Test
    public void testThatNumberOfCustomersServedTheSameDayIsCorrectNegative() {
    	solution = SetUpUtils.SetUpFeasibleSolutionWhereSingleTourIsExecutedTheNextDay();
    	
    	double d = 0;
    	thenNumberOfCustomersServedTheSameDayShouldBe(d);
    }

	private void thenNumberOfCustomersServedTheSameDayShouldBe(double d) {
		assertEquals(solution.NumberOfCustomersServedTheSameDay(), d, 0); 
	}

	@Test
    public void testPercentageOfToursExecutedTheSameDay() {
    	givenInfeasibleSolutionWithEqualDemandsAtEachCustomer();
    	
    	whenSolutionIsRepaired();
    	
    	double d = 0.5;
    	thenPercentageOfToursExecutedTheSameDayShouldBe(d);
    }       
    
	private void thenPercentageOfToursExecutedTheSameDayShouldBe(double d) {
		System.out.println("Kostenfaktoren:");
		for (Tour t : solution.getTours())
			System.out.println(t.getCostFactor());
		double percentageOfToursExecutedTheSameDay = solution.percentageOfToursExecutedTheSameDay();
		assertEquals(percentageOfToursExecutedTheSameDay,d,0);	
	}
	
	
    
    @Test 
    public void testPercentageOfCustomersServedTheSameDay() {
    	givenInfeasibleSolutionWithEqualDemandsAtEachCustomer();
    	
    	whenSolutionIsRepaired();
    	
    	double d1 = 3;
    	double d2 = 4;
    	double d3 = d1 / d2;
    	thenPercentageOfCustomersServedTheSameDayShouldBe(d3);
    }
    
	private void thenPercentageOfCustomersServedTheSameDayShouldBe(double d) {		
		assertEquals(solution.percentageOfCustomersServedTheSameDay(),d,0);			
	}
	
	
    
    @Test
    public void testPercentageOfParcelsCollectedTheSameDay() {
    	givenInfeasibleSolutionWithEqualDemandsAtEachCustomer();
    	
    	whenSolutionIsRepaired();
    	
    	double d1 = 3;
    	double d2 = 4;
    	double d3 = d1 / d2;
    	thenPercentageOfParcelsCollectedTheSameDayShouldBe(d3);    	
    }
               
	private void thenPercentageOfParcelsCollectedTheSameDayShouldBe(double d) {		
		assertEquals(solution.percentageOfParcelsCollectedTheSameDay(),d,0);
	}



	private void whenSolutionIsRepaired() {		
		try {
			repairSolution();			
		} catch (Exception e1) {			
			e1.printStackTrace();
		}		
	}

	private void repairSolution() throws Exception {
		Event e = createEventThatDoesNotChangeAnything();	
		solution = new RepairSolution().repair(solution, e).get(0).getNewSolution();
	}

	private Event createEventThatDoesNotChangeAnything() {
		return new Event(11,20,10);
	}
	
	private void givenInfeasibleSolution() {
		solution = SetUpUtils.SetUpInfeasibleSolution();
    }
	
	private void givenFeasibleSolution() 
    {
    	solution = SetUpUtils.SetUpFeasibleSolution();
    }
	
	
	
	private void givenInfeasibleSolutionWithEqualDemandsAtEachCustomer() {
		//set up
        Customer c11 = new Customer();
        c11.setxCoordinate(15);
        c11.setyCoordinate(80);
        c11.setCustomerNo(11);
        c11.setDemand(20);
        c11.setTimeWindowOpen(278);
        c11.setTimeWindowClose(345);
        c11.setServiceTime(90);
        
        Customer c12 = new Customer();
        c12.setCustomerNo(12);
        c12.setxCoordinate(20);
        c12.setyCoordinate(85);
        c12.setDemand(20);
        c12.setTimeWindowOpen(475);
        c12.setTimeWindowClose(528);
        c12.setServiceTime(90);

        Customer c13 = new Customer();
        c13.setCustomerNo(13);
        c13.setxCoordinate(25);
        c13.setyCoordinate(85);
        c13.setDemand(20);
        c13.setTimeWindowOpen(625);
        c13.setTimeWindowClose(721);
        c13.setServiceTime(90);
        
        Customer c14 = new Customer();
        c14.setCustomerNo(14);
        c14.setxCoordinate(35);
        c14.setyCoordinate(70);
        c14.setDemand(20);
        c14.setTimeWindowOpen(873);
        c14.setTimeWindowClose(921);
        c14.setServiceTime(90);
        
        Depot depot = new Depot();
        depot.setxCoordinate(40);
        depot.setyCoordinate(50);
        
        Vehicle vehicle = new Vehicle(1, 65);
        Set<Vehicle> vehicles = new HashSet<Vehicle>();
        vehicles.add(vehicle);
        
        DeterministicTour tour = new DeterministicTour(depot, vehicle);
        tour.addCustomer(c11);
        tour.addCustomer(c12);
        tour.addCustomer(c13);
        tour.addCustomer(c14);
        
        //create vprProblem
        VrpProblem vrpProblem = new VrpProblem();
        vrpProblem.addCustomer(c11);
        vrpProblem.addCustomer(c12);
        vrpProblem.addCustomer(c13);
        vrpProblem.addCustomer(c14);
        vrpProblem.addDepot(depot);
        vrpProblem.setVehicles(vehicles);
        vrpProblem.setMaxTime(10000);
                
        //create solution
        solution = new Solution(vrpProblem);
        solution.addTour(tour);
	}
	

    

    

    
}
