package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;
import de.rwth.lofip.library.util.math.MathUtils;

public class AdaptiveMemoryTabuSearch {
	
	private int numberOfDifferentInitialSolutions;
	@SuppressWarnings("unused")
	private int maximalNumberOfCallsToAdaptiveMemory;
	private int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory;
	private int maximalNumberOfIterationsTabuSearch;
	private int maximalNumberOfIterationsWithoutImprovementTabuSearch;
	
	private AdaptiveMemory adaptiveMemory = new AdaptiveMemory();
	
	private SolutionGot solution = null;
	private SolutionGot bestOverallSolution;
	@SuppressWarnings("unused")
	private int callsToAdaptiveMemory = 0;
	private int numberOfAMCallsWithoutImprovement = 0;
	@SuppressWarnings("unused")
	private int numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
	
	public SolutionGot solve(VrpProblem vrpProblem) {
		
		System.out.println("STARTE INITIALISIERUNG AM");
		
		initialiseAdaptiveMemoryWithInitialSolutions(vrpProblem);
		bestOverallSolution = constructInitialSolutionFromAdaptiveMemory();
		
		int iteration = 1;		
		while (!isStoppingCriterionMet()) {
			System.out.println("AM CALL Nr. " + iteration);
			
			solution = constructInitialSolutionFromAdaptiveMemory();
			TabuSearchForElementWithTours tabuSearch = new TabuSearchForElementWithTours();
			tabuSearch.setMaximalNumberOfIterations(maximalNumberOfIterationsTabuSearch);
			tabuSearch.setMaximalNumberOfIterationsWithoutImprovement(maximalNumberOfIterationsWithoutImprovementTabuSearch);
			tabuSearch.improve(solution);
			storeNewToursInAdaptiveMemory(solution);
			
			if (isBestOverallSolutionFoundAgain())
				increaseNumberOfTimesBestOverallSolutionHasBeenFound();
			
			if (isNewSolutionIsNewBestOverallSolution()) {
				setBestOverallSolutionToNewSolution();
				numberOfAMCallsWithoutImprovement = 0;
			} else
				numberOfAMCallsWithoutImprovement++;
			
			iteration++;
		}
		if (solution == null  || bestOverallSolution == null)
			throw new RuntimeException("Solution ist Null. Fehler!");
		return bestOverallSolution;
	}

		private void initialiseAdaptiveMemoryWithInitialSolutions(VrpProblem vrpProblem) {
			for (int i = 1; i <= numberOfDifferentInitialSolutions; i++) {
				System.out.println("Initialising AM " + i);
				RandomI1Solver initialSolver = new RandomI1Solver();
				SolutionGot newSolution = initialSolver.solve(vrpProblem);
				TabuSearchForElementWithTours tabuSearch = new TabuSearchForElementWithTours();
				tabuSearch.setMaximalNumberOfIterations(maximalNumberOfIterationsTabuSearch);
				tabuSearch.setMaximalNumberOfIterationsWithoutImprovement(maximalNumberOfIterationsWithoutImprovementTabuSearch);
				tabuSearch.improve(newSolution);
				adaptiveMemory.addTours(newSolution);
			}
		}
		
		private boolean isStoppingCriterionMet() {
			if (numberOfAMCallsWithoutImprovement >= maximalNumberOfCallsWithoutImprovementToAdaptiveMemory)
				return true;
//			if (callsToAdaptiveMemory >= maximalNumberOfCallsToAdaptiveMemory)
//				return true;
//			if (numberOfTimesSameBestOverallSolutionHasBeenFound >= 3)
//				return true;
			return false;
		}
		
		private SolutionGot constructInitialSolutionFromAdaptiveMemory() {
			callsToAdaptiveMemory++;
			return adaptiveMemory.constructSolutionFromTours();
		}
		
		private void storeNewToursInAdaptiveMemory(SolutionGot solution) {
			adaptiveMemory.addTours(solution);
		}
		
		private boolean isBestOverallSolutionFoundAgain() {
			return solution.equals(bestOverallSolution);
		}
		
		private void increaseNumberOfTimesBestOverallSolutionHasBeenFound() {
			numberOfTimesSameBestOverallSolutionHasBeenFound++;
		}
	
		private boolean isNewSolutionIsNewBestOverallSolution() {
//			assertEquals(false, solution.equals(bestOverallSolution));
			if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactor(),bestOverallSolution.getTotalDistanceWithCostFactor())
				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
					numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
					return true;
			}
			if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
				numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
				return true;
			}
			return false;
		}
		
		private void setBestOverallSolutionToNewSolution() {
			bestOverallSolution = solution.clone();	
		}
	
	
	public void setMaximalNumberOfIterationsTabuSearch(int i) {
		maximalNumberOfIterationsTabuSearch = i;
	}
	
	public void setMaximalNumberOfIterationsWithoutImprovementTabuSerach(int i) {	
		maximalNumberOfIterationsWithoutImprovementTabuSearch = i;
	}

	public void setNumberOfInitialSolutions(
			int numberOfDifferentInitialSolutions2) {
		numberOfDifferentInitialSolutions = numberOfDifferentInitialSolutions2;
	}

	public void setMaximalNumberOfCallsToAdaptiveMemory(
			int maximalNumberOfCallsToAdaptiveMemory2) {
		maximalNumberOfCallsToAdaptiveMemory = maximalNumberOfCallsToAdaptiveMemory2;
	}

	public void setMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory(
			int i) {
		maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = i;
	}
	
	public void resetSeeds() {
		AdaptiveMemory.setSeedTo(0);
		GreedyInsertion.setSeedTo(0);
		RandomI1Solver.setSeedTo(0);
	}

	public static void setSeeds(int seedI1, int seedGI, int seedAM) {
		AdaptiveMemory.setSeedTo(seedAM);
		GreedyInsertion.setSeedTo(seedGI);
		RandomI1Solver.setSeedTo(seedI1);
	}



}