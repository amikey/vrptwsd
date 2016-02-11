package de.rwth.lofip.library.solver.metaheuristics;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.exceptions.NoSolutionExistsException;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.localSearch.LocalSearchForElementWithTours;
import de.rwth.lofip.library.solver.metaheuristics.interfaces.MetaSolverInterfaceGot;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhoodWithTabooList;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.TourEliminationNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.solver.util.SolutionGotUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class TabuSearchForElementWithTours implements MetaSolverInterfaceGot {

	@SuppressWarnings("unused")
	private int maximalNumberOfIterations;
	private int maxNumberIterationsWithoutImprovement;
	
	protected int iteration = 0;
	protected ElementWithTours solution;
	protected AbstractNeighborhoodMove bestMove;
	protected CrossNeighborhoodWithTabooList crossNeighborhood;
	protected TourEliminationNeighborhood tourEliminationNeighborhood;
	protected ElementWithTours bestOverallSolution;
	private int iterationsWithoutImprovement = 0;
	
	private long timeNeeded;
	private long startTime;
	private long endTime;
	
	public TabuSearchForElementWithTours() {
		maximalNumberOfIterations = Parameters.getMaximalNumberOfIterationsTabuSearch();
		maxNumberIterationsWithoutImprovement = Parameters.getMaximalNumberOfIterationsWithoutImprovementTabuSearch();
	}

	@Override
	public ElementWithTours improve(ElementWithTours solutionStart) throws IOException {
		
		startTime = System.currentTimeMillis();
		
		solution = solutionStart;
		bestOverallSolution = solutionStart.clone();
		
		publishSolution(solution);
		publishSolution(bestOverallSolution);
		
		crossNeighborhood = getCrossNeighborhood(); 
		tourEliminationNeighborhood = getTourEliminationNeighborhood();
		
		System.out.println("Starting Tabu Search");		
		while (!isStoppingCriterionMet()) {
			try {				
				System.out.println("Iteration TS: " + iteration);// + "; Solution: " + solution.getAsTupel() + "\n");
				
				System.out.println(iteration + " " + solution.getAsTupel());
				System.out.println(solution.getTotalDistanceWithCostFactor() + "; " + solution.getNumberOfTours() + "; " + (solution.getTotalDistanceWithCostFactorAndRecourse() - solution.getTotalDistanceWithCostFactor()) + "; " + solution.getTotalDistanceWithCostFactorAndRecourse());
				bestOverallSolution.getTotalDistanceWithCostFactor();
				
				findBestNonTabooMove();				
				updateTabuList();								
				applyBestNonTabooMove();	
				
				tryToImproveSolutionWithRematchingPhaseHook();
				
				publishSolution(solution);
				
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
		publishSolution(bestOverallSolution);
		generateBlankLineForPublishingSolution();
		publishSolutionAtEndOfTabuSearch();
		return bestOverallSolution;
	}

	protected TourEliminationNeighborhood getTourEliminationNeighborhood() {
		return null;
	}

	protected CrossNeighborhoodWithTabooList getCrossNeighborhood() {
		return new CrossNeighborhoodWithTabooList(solution);
	}

	private boolean isStoppingCriterionMet() {
		if (Parameters.isRunningTimeReached())
			return true;
		return iterationsWithoutImprovement > maxNumberIterationsWithoutImprovement;
	}
	
	protected void findBestNonTabooMove() throws Exception {		
		bestMove = crossNeighborhood.returnBestMove(iteration);
		//DESIGN_TODO: Hier TourElimination Nachbarschaft aufrufen, wenn Cross keinen Move gefunden hat, der Tourenanzahl reduziert
	}
	
	protected void updateTabuList() {
		crossNeighborhood.updateTabuList(iteration);
	}
	
	protected void applyBestNonTabooMove() {
		solution = (SolutionGot) crossNeighborhood.actuallyApplyMoveAndMaintainNeighborhood(bestMove);
	}
	
	protected void tryToImproveSolutionWithRematchingPhaseHook() throws IOException {
		//do nothing
	}
	
	protected boolean isNewSolutionIsNewBestOverallSolution() {
//		assertEquals(false, solution.equals(bestOverallSolution));
		double costOldSolution = solution.getTotalDistanceWithCostFactor();
		double costBestOverallSolution = bestOverallSolution.getTotalDistanceWithCostFactor();
		
		if (MathUtils.lessThan(costOldSolution, costBestOverallSolution)
			&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours())
			return true;
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours())
			return true;
		return false;
	}
	
	protected void tryToImproveNewBestSolutionWithIntensificationPhase() {
		if (solution instanceof GroupOfTours)
			throw new RuntimeException("tryToImproveNewBestSolutionWithIntensificationPhase ist nicht für Gots implementiert");		
		//CODE_SMELL_TODO: Fallunterscheidung zwischen Solution und GroupOfTours; Cast zu Solution ist ein Hack
		SolutionGot solutionTemp = (SolutionGot) solution;
		for (GroupOfTours got : solutionTemp.getGots()) {
			for (int j = 0; j < got.getTours().size(); j++) {				
				Tour tour = got.getTour(j);
				//RUNTIME_TODO: versuche hier weniger Iterationen
				for (int i = 0; i < Parameters.getNumberOfIntensificationTries(); i++) { 					
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

	private void setBestOverallSolutionToNewSolution() {
		solution.getTotalDistanceWithCostFactor();
		bestOverallSolution = solution.clone();	
	}

	// Print Utilities
	
	private void publishSolution(ElementWithTours solution2) throws IOException {
		if (Parameters.publishSolutionValueProgress()) {
			System.out.println(iteration + " " + solution2.getAsTupel());
			System.out.println(solution2.getTotalDistanceWithCostFactor() + "; " + solution2.getNumberOfTours());
//			IOUtils.write(iteration + ";" + String.format("%.3f",solution.getTotalDistanceWithCostFactor()) + ";" + solution.getNumberOfTours() + "\n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionValueProgress());
		}
	}
	
	private void publishSolutionAtEndOfTabuSearch() throws IOException {
		endTime = System.currentTimeMillis();
		timeNeeded = (endTime - startTime);
		
		if (Parameters.publishSolutionAtEndOfTabuSearch())			
			if (bestOverallSolution instanceof SolutionGot){
				String s = SolutionGotUtils.createStringForCustomersServedByNumberOfVehicles(bestOverallSolution);					
		
				IOUtils.write("Lösung am Ende der TS: " + ";" 
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor()) + ";" 
					+ bestOverallSolution.getNumberOfTours() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActions()) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getConvexCombinationOfCostAndNumberRecourseActions()) + ";"
					+ ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfRouteFailures() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfAdditionalTours()) + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfDifferentRecourseActions()) + ";"
					+ timeNeeded + ";"
					+ bestOverallSolution.getUseOfCapacityInTours() + ";"	
					+ bestOverallSolution.getAsTupel() + ";"
					+ s 
					+ "\n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionAtEndOfTabuSearch(solution));
			} else {
				throw new RuntimeException("bestOverallSolution ist nicht vom Typ SolutionGot");
			}
	}
	
	private void generateBlankLineForPublishingSolution() throws IOException {
		if (Parameters.publishSolutionValueProgress())
			IOUtils.write("\n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionValueProgress());
	}

	@SuppressWarnings("unused")
	private void printBestMove() {
		bestMove.print();
	}
	
	// Test Utilities
	
	//this exists only for testing
	public void tryToImproveNewBestSolutionWithIntensificationPhase(SolutionGot solution2) {
		solution = solution2;
		tryToImproveNewBestSolutionWithIntensificationPhase();
	}

	//this exists only for testing
	public int getNumberOfIterations() {
		return iteration;
	}
}
