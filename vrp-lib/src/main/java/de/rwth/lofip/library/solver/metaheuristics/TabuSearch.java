package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;

public class TabuSearch implements MetaSolverInterfaceGot {

	private int maximalNumberOfIterations = 100;
	
	private int iteration = 1;
	private SolutionGot solution;
	private AbstractNeighborhoodMove bestMove;
	private CrossNeighborhoodWithTabooList crossNeighborhood;
	private SolutionGot bestOverallSolution;

	@Override
	public SolutionGot improve(SolutionGot solutionStart) {
		System.out.println("STARTE TABU SUCHE");
		
		solution = solutionStart;
		bestOverallSolution = solutionStart.clone();
		crossNeighborhood = new CrossNeighborhoodWithTabooList(solution);
		
		System.out.println("Iteration: " + 0 + "; Solution: " + solution.getSolutionAsTupel());
		while (!isStoppingCriterionMet()) {
			try {
				findBestNonTabooMove();
				//printBestMove();
				
//				System.out.println("bestMove.getCost() < solution.getTotalDistance(): " + bestMove.getCost() +"; " + solution.getTotalDistance());
				
				updateTabuList();
				applyBestNonTabooMove();
								
//				System.out.println("Iteration: " + iteration + "; Solution: " + solution.getSolutionAsTupel() + "\n");
				
//				if (iteration == 83)
//					System.out.println("DEBUGGING!");
				
				if (isNewSolutionIsNewBestOverallSolution()) 
					setBestOverallSolutionToNewSolution();	
				
			} catch (Exception e) {
				if (e.getMessage() == "No feasible move found.") {					
					System.out.println("Kein feasible Move gefunden. Iteration: " + iteration);
				} else {
					StackTraceElement[] arr = e.getStackTrace();
			     	for(int i=0; i<arr.length; i++)
			     		System.out.println(arr[i].toString());			     	
					throw new RuntimeException(e);
				}
			}
			iteration++;
		}
		
		return bestOverallSolution;
	}

	private boolean isStoppingCriterionMet() {
		return iteration >= maximalNumberOfIterations;
//		return iterationsWithoutImprovement >= maxNumberIterationsWithoutImprovement;
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
//		assertEquals(false, solution.equals(bestOverallSolution));
		if (solution.getTotalDistance() < bestOverallSolution.getTotalDistance()
			&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours())
			return true;
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours())
			return true;
		return false;
	}
	
	private void setBestOverallSolutionToNewSolution() {
		bestOverallSolution = solution.clone();	
	}
	
	private void updateTabuList() {
		crossNeighborhood.updateTabuList(iteration);
	}
	
	public void setMaximalNumberOfIterations(int i) {
		maximalNumberOfIterations = i;
	}
}
