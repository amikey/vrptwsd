package de.rwth.lofip.library.solver.localSearch;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodMove;

public class LocalSearch implements MetaSolverInterfaceGot {
	
	private SolutionGot solution;
	private CrossNeighborhoodMove bestMove;
	private CrossNeighborhood crossNeighborhood;
			
	@Override
	public SolutionGot improve(SolutionGot solutionStart) {
		this.solution = solutionStart;
		crossNeighborhood = new CrossNeighborhood(solution);
		do {
			findBestMove();
			if (isImprovement());
				applyBestMove();
		} while (isImprovement());
		return solution;
	}
	
	private void findBestMove() {
		bestMove = crossNeighborhood.returnBestMove();				
	}
	
	private boolean isImprovement() {
		return (bestMove.getCost() < solution.getTotalDistance());
	}	

	private void applyBestMove() {
		solution = crossNeighborhood.acctuallyApplyMove(bestMove);
	}
}
