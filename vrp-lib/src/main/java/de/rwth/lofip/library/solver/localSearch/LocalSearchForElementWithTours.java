package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;

import de.rwth.lofip.exceptions.NoSolutionExistsException;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class LocalSearchForElementWithTours implements MetaSolverInterfaceGot {
	
	protected ElementWithTours solution;
	protected AbstractNeighborhoodMove bestMove = null;
	private CrossNeighborhood crossNeighborhood;
	private int iteration = 1;
			
	@Override
	public ElementWithTours improve(ElementWithTours solutionStart) {
		this.solution = solutionStart;
		crossNeighborhood = new CrossNeighborhood(solution);
		boolean isImprovement = false;
		do {
			try{				
				findBestMove();							
				isImprovement = isImprovement();								
				if (isImprovement) {					
					applyBestMove();	
					
//					System.out.println(solution.getAsTupelWithDemand());
					assertEquals(true, ElementWithToursUtils.isElementDemandFeasible(solution));	
//					assertEquals(true, solution.getTours().size() <= 2);
//					System.out.println(iteration);
					assertEquals(true, iteration < 100);
					
					assertEqualsHook();
					iteration++;
				}
			} catch (Exception e) {				
				if (e instanceof NoSolutionExistsException) {
					isImprovement = false;//					
				} else 
					throw new RuntimeException(e);
			}			
		} while (isImprovement);
		return solution;
	}
	
	private void findBestMove() throws Exception {
		bestMove = crossNeighborhood.returnBestMove(iteration);				
	}
		
	@SuppressWarnings("unused")
	private void printBestMove() {
		bestMove.print();
	}
	
	private boolean isImprovement() {
		if (!ElementWithToursUtils.isElementDemandFeasible(solution)) {
			System.out.println("Previous Solution was infeasible: " + solution.getAsTupel());
			System.out.println("Demand Tour 1: " + solution.getTour(0).getDemandOnTour() + "; Capacity 106");
			System.out.println("Demand Tour 2: " + solution.getTour(1).getDemandOnTour() + "; Capacity 103");
			return true;
		}
		if (MathUtils.lessThan(bestMove.getCost(), solution.getTotalDistanceWithCostFactor())) {			
			return true;
		}			
		if (bestMove.reducesNumberOfVehicles()) {		
			return true;
		}		
		return false;
	}	

	private void applyBestMove() {
		solution = crossNeighborhood.acctuallyApplyMove(bestMove);		
	}
	
	protected void assertEqualsHook() {
		//nothing to do here, hook exists for testing only 
	}

}
