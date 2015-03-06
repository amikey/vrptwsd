package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public class TabuSearch implements MetaSolverInterfaceGot {

	private final int maxNumberIterationsWithoutImprovement = 1000;
	
	private SolutionGot solution;
	private AbstractNeighborhoodMove bestMove;
	private CrossNeighborhoodWithTabooList crossNeighborhood;
	private int iteration = 1;
	private int iterationsWithoutImprovement = 1;
	private SolutionGot bestOverallSolution;	
	
	@Override
	public SolutionGot improve(SolutionGot solutionStart) {
		solution = solutionStart;
		bestOverallSolution = solutionStart;
		crossNeighborhood = new CrossNeighborhoodWithTabooList(solution);
		
		System.out.println("Iteration: " + 0 + "; Solution: " + solution.getSolutionAsTupel());
		while (!isStoppingCriterionMet()) {
			try {
				findBestNonTabooMove();
				printBestMove();
				applyBestNonTabooMove();
				
				System.out.println("Iteration: " + iteration + "; Solution: " + solution.getSolutionAsTupel() + "\n");
				
				if (isNewSolutionIsNewBestOverallSolution()) {
					setBestOverallSolutionToNewSolution();
					updateTabuList();
					iterationsWithoutImprovement = 0;
				} else
					iterationsWithoutImprovement++;
				iteration++;
			} catch (Exception e) {
				if (e.getMessage() == "No feasible move found") {					
					System.out.println("Kein feasible Move gefunden");
				} else 
					throw new RuntimeException(e);
			}
		}
		
		return solution;
	}

	private boolean isStoppingCriterionMet() {
		return iterationsWithoutImprovement >= maxNumberIterationsWithoutImprovement;
	}
	
	private void findBestNonTabooMove() throws Exception {
		bestMove = crossNeighborhood.returnBestMove(iteration);
	}
	
	private void printBestMove() {
		bestMove.print();
	}
	
	private void applyBestNonTabooMove() {
		solution = crossNeighborhood.acctuallyApplyMove(bestMove);
	}
	
	private boolean isNewSolutionIsNewBestOverallSolution() {
		if (solution.getTotalDistance() <= bestOverallSolution.getTotalDistance()
			&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours())
			return true;
		if (solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours())
			return true;
		return false;
	}
	
	private void setBestOverallSolutionToNewSolution() {
		bestOverallSolution = solution;	
	}
	
	private void updateTabuList() {
		crossNeighborhood.updateTabuList(iteration);
	}
}
