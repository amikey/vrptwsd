package de.rwth.lofip.library.solver.localSearch;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public class LocalSearch implements MetaSolverInterfaceGot {
	
	private SolutionGot solution;
	private AbstractNeighborhoodMove bestMove;
	private CrossNeighborhood crossNeighborhood;
			
	@Override
	public SolutionGot improve(SolutionGot solutionStart) {
		this.solution = solutionStart;
		crossNeighborhood = new CrossNeighborhood(solution);
		System.out.println("Iteration: " + 0 + "; Solution: " + solution.getSolutionAsTupel());
		int iteration = 1;
		boolean isImprovement;
		do {
			findBestMove();
			printBestMove();
			isImprovement = isImprovement();
			System.out.println("bestMove.getCost() < solution.getTotalDistance(): " + bestMove.getCost() +"; " + solution.getTotalDistance());
			if (isImprovement) {
				applyBestMove();	
				System.out.println("Iteration: " + iteration + "; Solution: " + solution.getSolutionAsTupel() + "\n");
				iteration++;
			}
		} while (isImprovement);
		System.out.println("");
		return solution;
	}
	
	private void findBestMove() {
		bestMove = crossNeighborhood.returnBestMove();				
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
