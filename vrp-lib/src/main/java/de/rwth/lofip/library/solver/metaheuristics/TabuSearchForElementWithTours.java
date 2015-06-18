package de.rwth.lofip.library.solver.metaheuristics;

import org.hamcrest.core.IsInstanceOf;

import de.rwth.lofip.exceptions.NoSolutionExistsException;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.localSearch.LocalSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.math.MathUtils;

public class TabuSearchForElementWithTours implements MetaSolverInterfaceGot {

	@SuppressWarnings("unused")
	private int maximalNumberOfIterations;
	private int maxNumberIterationsWithoutImprovement;
	
	private int iteration = 1;
	private ElementWithTours solution;
	private AbstractNeighborhoodMove bestMove;
	private CrossNeighborhoodWithTabooList crossNeighborhood;
	private ElementWithTours bestOverallSolution;
	private int iterationsWithoutImprovement = 0;

	@Override
	public ElementWithTours improve(ElementWithTours solutionStart) {
		
		solution = solutionStart;
		bestOverallSolution = solutionStart.clone();
		crossNeighborhood = new CrossNeighborhoodWithTabooList(solution);
		
		System.out.println("Starting Tabu Search");		
		while (!isStoppingCriterionMet()) {
			try {				
				System.out.println("Iteration TS: " + iteration);// + "; Solution: " + solution.getAsTupel() + "\n");
				
				findBestNonTabooMove();				
				updateTabuList();								
				applyBestNonTabooMove();								
				
				if (isNewSolutionIsNewBestOverallSolution()) { 					
					tryToImproveNewBestSolutionWithIntensificationPhase();
					setBestOverallSolutionToNewSolution();	
 					iterationsWithoutImprovement = 0;
				} else
					iterationsWithoutImprovement++;			
			} catch (Exception e) {
				if (e instanceof NoSolutionExistsException) {													
					iterationsWithoutImprovement++;
				} else {
					StackTraceElement[] arr = e.getStackTrace();
			     	for(int i=0; i<arr.length; i++)
			     		System.out.println(arr[i].toString());			     	
					throw new RuntimeException(e);
				}
			}			
			iteration++;
		}		
		System.out.println("Anzahl Iterationen Tabu Suche : " + iteration + "\n");
		return bestOverallSolution;
	}

	private boolean isStoppingCriterionMet() {
//		return iteration >= maximalNumberOfIterations;
		return iterationsWithoutImprovement >= maxNumberIterationsWithoutImprovement;
	}
	
	private void findBestNonTabooMove() throws Exception {		
		bestMove = crossNeighborhood.returnBestMove(iteration);
		//DESIGN_TODO: Hier TourElimination Nachbarschaft aufrufen, wenn Cross keinen Move gefunden hat, der Tourenanzahl reduziert
	}
	
	@SuppressWarnings("unused")
	private void printBestMove() {
		bestMove.print();
	}
	
	private void applyBestNonTabooMove() {
		solution = (SolutionGot) crossNeighborhood.acctuallyApplyMove(bestMove);
	}
	
	private boolean isNewSolutionIsNewBestOverallSolution() {
//		assertEquals(false, solution.equals(bestOverallSolution));
		if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactor(), bestOverallSolution.getTotalDistanceWithCostFactor())
			&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours())
			return true;
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours())
			return true;
		return false;
	}
	
	protected void tryToImproveNewBestSolutionWithIntensificationPhase() {
		if (solution instanceof GroupOfTours)
			throw new RuntimeException("tryToImproveNewBestSolutionWithIntensificationPhase ist nicht für Gots implementiert");		
		//IMPORTANT_TODO: Fallunterscheidung zwischen Solution und GroupOfTours; Cast zu Solution ist ein Hack
		SolutionGot solutionTemp = (SolutionGot) solution;
		for (GroupOfTours got : solutionTemp.getGots()) {
			for (int j = 0; j < got.getTours().size(); j++) {				
				Tour tour = got.getTour(j);
				//RUNTIME_TODO: versuche hier weniger Iterationen
				for (int i = 0; i < 10; i++) { // 10 Verbesserungsversuche pro Tour					
					//CODE_SMELL_TODO: construct incomplete vrpProblem first and start I1Solver with vrpProblem
					SolutionGot newSolution = new RandomI1Solver().solve(tour);					
					new LocalSearchForElementWithTours().improve(newSolution);					
					if (newSolution.getNumberOfTours() == 1)
						if (MathUtils.lessThan(newSolution.getTotalDistanceWithCostFactor(), tour.getTotalDistanceWithCostFactor())) {							
							tour = newSolution.getTour(0);
							got.setTour(j, newSolution.getTour(0));
	//						break;
						}
				}
			}	
		}
	}
	
	public void tryToImproveNewBestSolutionWithIntensificationPhase(
			SolutionGot solution2) {
		solution = solution2;
		tryToImproveNewBestSolutionWithIntensificationPhase();
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
	
	public void setMaximalNumberOfIterationsWithoutImprovement(int i) {
		maxNumberIterationsWithoutImprovement = i;
	}

}
