package de.rwth.lofip.library.solver.localSearch;

import static org.junit.Assert.assertEquals;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.SolutionUtils;

public class LocalSearch implements MetaSolverInterfaceGot {
	
	private SolutionGot solution;
	private AbstractNeighborhoodMove bestMove;
	private CrossNeighborhood crossNeighborhood;
	private int iteration = 1;
			
	@Override
	public SolutionGot improve(SolutionGot solutionStart) {
		this.solution = solutionStart;
		crossNeighborhood = new CrossNeighborhood(solution);
		System.out.println("Iteration: " + 0 + "; Solution: " + solution.getSolutionAsTupel());		
		boolean isImprovement;
		do {
			try{
				findBestMove();			
				printBestMove();
				isImprovement = isImprovement();
				System.out.println("bestMove.getCost() < solution.getTotalDistance(): " + bestMove.getCost() +"; " + solution.getTotalDistance());
				if (isImprovement) {
					applyBestMove();					
					System.out.println("Iteration: " + iteration + "; Solution: " + solution.getSolutionAsTupel() + "\n");
					//TODO: Remove because takes time (O(n))
					assertEquals(true, SolutionUtils.isSolutionDemandFeasible(solution));
					assertEquals(true, SolutionUtils.isSolutionTWFeasible(solution));
					iteration++;
				}
			} catch (Exception e) {				
				if (e.getMessage() == "no move found") {
					isImprovement = false;
					System.out.println("Kein feasible Move gefunden");
				} else 
					throw new RuntimeException(e);
			}			
		} while (isImprovement);
		System.out.println("");
		return solution;
	}
	
	private void findBestMove() throws Exception {
		bestMove = crossNeighborhood.returnBestMove(iteration);				
	}
	
	private void printBestMove() {
		bestMove.print();
	}
	
	private boolean isImprovement() {
		return (bestMove.getCost() < solution.getTotalDistance());
	}	

	private void applyBestMove() {
		solution = crossNeighborhood.acctuallyApplyMove(bestMove);		
	}
}
