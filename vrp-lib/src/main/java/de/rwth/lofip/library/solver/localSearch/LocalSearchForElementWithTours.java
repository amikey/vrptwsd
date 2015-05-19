package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;
import de.rwth.lofip.exceptions.NoSolutionExistsException;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.ElementWithToursUtils;

public class LocalSearchForElementWithTours implements MetaSolverInterfaceGot {
	
	protected ElementWithTours solution;
	protected AbstractNeighborhoodMove bestMove = null;
	private CrossNeighborhood crossNeighborhood;
	private int iteration = 1;
			
	@Override
	public ElementWithTours improve(ElementWithTours solutionStart) {
		this.solution = solutionStart;
		crossNeighborhood = new CrossNeighborhood(solution);
//		System.out.println("Iteration: " + 0 + "; Solution: " + solution.getAsTupel());		
		boolean isImprovement = false;
		do {
			try{
				findBestMove();			
//				printBestMove();				
				isImprovement = isImprovement();
//				System.out.println("bestMove.getCost() < solution.getTotalDistance(): " + bestMove.getCost() +"; " + solution.getTotalDistance());
				if (isImprovement) {
					applyBestMove();					
//					System.out.println("Move applied! Iteration: " + iteration + "; Solution: " + solution.getSolutionAsTupel() + "\n");
					assertEqualsHook();
					iteration++;
				}
			} catch (Exception e) {				
				if (e instanceof NoSolutionExistsException) {
					isImprovement = false;
//					System.out.println(e.getMessage() + " Iteration: " + iteration + "; Solution: " + solution);
				} else 
					throw new RuntimeException(e);
			}			
		} while (isImprovement);
//		System.out.println("");
		return solution;
	}
	
	private void findBestMove() throws Exception {
		bestMove = crossNeighborhood.returnBestMove(iteration);				
	}
	
	private void printBestMove() {
		bestMove.print();
	}
	
	private boolean isImprovement() {
		if (bestMove.getCost() < solution.getTotalDistance())
			return true;
		if (bestMove.reducesNumberOfVehicles())
			return true;
		return false;
	}	

	private void applyBestMove() {
		solution = (SolutionGot) crossNeighborhood.acctuallyApplyMove(bestMove);		
	}
	
	protected void assertEqualsHook() {
		//nothing to do here, hook exists for testing only 
	}

}
