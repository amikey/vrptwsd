package de.rwth.lofip.library.solver.metaheuristics;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

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
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.math.MathUtils;

public class TabuSearchForElementWithTours implements MetaSolverInterfaceGot {

	@SuppressWarnings("unused")
	private int maximalNumberOfIterations;
	private int maxNumberIterationsWithoutImprovement;
	
	private int iteration = 1;
	protected ElementWithTours solution;
	private AbstractNeighborhoodMove bestMove;
	private CrossNeighborhoodWithTabooList crossNeighborhood;
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
		
		startTime = System.nanoTime();
		
		solution = solutionStart;
		bestOverallSolution = solutionStart.clone();
		crossNeighborhood = getCrossNeighborhood(); 
		
		System.out.println("Starting Tabu Search");		
		while (!isStoppingCriterionMet()) {
			try {				
				System.out.println("Iteration TS: " + iteration);// + "; Solution: " + solution.getAsTupel() + "\n");
				
				findBestNonTabooMove();				
				updateTabuList();								
				applyBestNonTabooMove();								
				
				publishSolution();
				
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
		generateBlankLineForPublishingSolution();
		publishSolutionAtEndOfTabuSearch();
		return bestOverallSolution;
	}

	protected CrossNeighborhoodWithTabooList getCrossNeighborhood() {
		return new CrossNeighborhoodWithTabooList(solution);
	}

	private boolean isStoppingCriterionMet() {
//		return iteration >= maximalNumberOfIterations;
		return iterationsWithoutImprovement >= maxNumberIterationsWithoutImprovement;
	}
	
	private void findBestNonTabooMove() throws Exception {		
		bestMove = crossNeighborhood.returnBestMove(iteration);
		//DESIGN_TODO: Hier TourElimination Nachbarschaft aufrufen, wenn Cross keinen Move gefunden hat, der Tourenanzahl reduziert
	}
	
	private void updateTabuList() {
		crossNeighborhood.updateTabuList(iteration);
	}
	
	private void applyBestNonTabooMove() {
		solution = (SolutionGot) crossNeighborhood.acctuallyApplyMoveAndMaintainNeighborhood(bestMove);
	}
	
	protected boolean isNewSolutionIsNewBestOverallSolution() {
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
	

	private void setBestOverallSolutionToNewSolution() {
		bestOverallSolution = solution.clone();	
	}
	
	// Print Utilities
	
	private void publishSolution() throws IOException {
		if (Parameters.publishSolutionValueProgress())
			IOUtils.write(iteration + ";" + String.format("%.3f",solution.getTotalDistanceWithCostFactor()) + ";" + solution.getNumberOfTours() + "\n", ReadAndWriteUtils.getOutputStreamForPublishingSolutionValueProgress());
	}
	
	private void publishSolutionAtEndOfTabuSearch() throws IOException {
		endTime = System.nanoTime();
		timeNeeded = (endTime - startTime) / 1000 / 1000 / 1000 / 60;
		
		if (Parameters.publishSolutionAtEndOfTabuSearch())			
			if (bestOverallSolution instanceof SolutionGot){
				String s = createStringForCustomersServedByNumberOfVehicles();					
		
				IOUtils.write("Lösung am Ende der TS: " + ";" 
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor()) + ";" 
					+ bestOverallSolution.getNumberOfTours() + ";"
					+ String.format("%.3f",((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
					+ String.format("%.3f",bestOverallSolution.getTotalDistanceWithCostFactor() + ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getRecourseCost()) + ";"
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

	private String createStringForCustomersServedByNumberOfVehicles() {
		String s = "";
	
		Iterator<Entry<Integer, Integer>> it = ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getNumberOfCustomersServedByNumberOfDifferentTours().entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Integer> pair = it.next();
	        //the following line is totally not necessary and just for better readiness 
	        int currentlyExaminedNumberOfVehicles = pair.getKey();
	        int numberOfCustomersServedByCurrentlyExaminedNumberOfVehicles = pair.getValue();
	        s = s + numberOfCustomersServedByCurrentlyExaminedNumberOfVehicles + ";";
	    }
	    
	    return s;
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
	public void tryToImproveNewBestSolutionWithIntensificationPhase(
			SolutionGot solution2) {
		solution = solution2;
		tryToImproveNewBestSolutionWithIntensificationPhase();
	}
	
	

}
