package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public class TabuSearch implements MetaSolverInterfaceGot {

	private final int maxNumberIterationsWithoutImprovement = 1000;
	
	private SolutionGot solution;
	private AbstractNeighborhoodMove bestMove;
	private CrossNeighborhood crossNeighborhood;
	private int iteration = 1;
	private int iterationsWithoutImprovement = 1;
	private SolutionGot bestOverallSolution;	
	
	@Override
	public SolutionGot improve(SolutionGot solutionStart) {
		solution = solutionStart;
		bestOverallSolution = solutionStart;
		crossNeighborhood = new CrossNeighborhood(solution);
		
		while (!isStoppingCriterionMet()) {
			findBestNonTabooMove();
			printBestMove();
			applyBestNonTabooMove();
			if (isNewSolutionIsNewBestOverallSolution()) {
				setBestOverallSolutionToNewSolution();
				iterationsWithoutImprovement = 0;
			} else
				iterationsWithoutImprovement++;
			iteration++;
		}
	}

	private boolean isStoppingCriterionMet() {
		return iterationsWithoutImprovement >= maxNumberIterationsWithoutImprovement;
	}
	
	private void findBestNonTabooMove() {
		bestMove = crossNeighborhood.returnBestNonTabuMove(iteration);
	}
}
