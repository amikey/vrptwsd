package de.rwth.lofip.library.solver.metaheuristics;

import java.io.IOException;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;
import de.rwth.lofip.library.util.math.MathUtils;

public class AdaptiveMemoryTabuSearch {
	
	private int numberOfDifferentInitialSolutions = Parameters.getNumberOfDifferentInitialSolutionsInAM();
	private int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = Parameters.getMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory();
	
	private AdaptiveMemory adaptiveMemory = getAM();
	
	protected SolutionGot solution = null;
	protected SolutionGot bestOverallSolution;
	@SuppressWarnings("unused")
	private int callsToAdaptiveMemory = 0;
	private int numberOfAMCallsWithoutImprovement = 0;
	@SuppressWarnings("unused")
	private int numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
	private long endTime;
	private long timeNeeded;
	private long startTime;
	private int iteration;
	
	protected AdaptiveMemory getAM() {
		return new AdaptiveMemory();
	}
	
	public SolutionGot solve(VrpProblem vrpProblem) throws IOException {
		
		startTime = System.currentTimeMillis();
		
		System.out.println("STARTE INITIALISIERUNG AM");
		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfTabuSearch(vrpProblem);

		initialiseAdaptiveMemoryWithInitialSolutions(vrpProblem);
		bestOverallSolution = constructInitialSolutionFromAdaptiveMemory();
		
		publishSolution(bestOverallSolution);
		
		iteration = 1;		
		while (!isStoppingCriterionMet()) {
			System.out.println("AM CALL Nr. " + iteration);
			
			solution = constructInitialSolutionFromAdaptiveMemory();
			
			publishSolution(solution);
			
			TabuSearchForElementWithTours tabuSearch = getTS(); 
			solution = (SolutionGot) tabuSearch.improve(solution);
			
			publishSolution(solution);
			
			storeNewToursInAdaptiveMemory(solution);
			
			if (isBestOverallSolutionFoundAgain())
				increaseNumberOfTimesBestOverallSolutionHasBeenFound();
			
			if (isNewSolutionIsNewBestOverallSolution()) {
				setBestOverallSolutionToNewSolution();
				numberOfAMCallsWithoutImprovement = 0;
				numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
			} else
				numberOfAMCallsWithoutImprovement++;
			
			publishSolution(bestOverallSolution);
			
			iteration++;
		}
		if (bestOverallSolution == null)
			throw new RuntimeException("bestOverallSolution ist Null. Fehler!");
		
		publishSolution(bestOverallSolution);
		publishSolutionAtEndOfAMTSSearch();
		
		return bestOverallSolution;
	}

		private void publishSolution(SolutionGot solution2) {
			System.out.println(iteration + " " + solution2.getAsTupel());
			System.out.println(solution2.getTotalDistanceWithCostFactor() + "; " + solution2.getNumberOfTours());
		}

		protected TabuSearchForElementWithTours getTS() {
			return new TabuSearchForElementWithTours();		 
		}

		private void initialiseAdaptiveMemoryWithInitialSolutions(VrpProblem vrpProblem) throws IOException {
			for (int i = 0; i < numberOfDifferentInitialSolutions; i++) {
				System.out.println("Initialising AM " + i);
				RandomI1Solver initialSolver = new RandomI1Solver();
				SolutionGot newSolution = initialSolver.solve(vrpProblem);
				//TODO: Ausgabe entfernen
				System.out.println(newSolution.getAsTupel());
				System.out.println(newSolution.getTotalDistanceWithCostFactor());
				TabuSearchForElementWithTours tabuSearch = getTS();
				tabuSearch.improve(newSolution);
				adaptiveMemory.addTours(newSolution);
			}
		}
		
		private boolean isStoppingCriterionMet() {
			if (Parameters.isRunningTimeReached())
				return true;
			if (numberOfAMCallsWithoutImprovement >= maximalNumberOfCallsWithoutImprovementToAdaptiveMemory)
				return true;
//			if (callsToAdaptiveMemory >= maximalNumberOfCallsToAdaptiveMemory)
//				return true;
//			if (numberOfTimesSameBestOverallSolutionHasBeenFound >= 3)
//				return true;
			return false;
		}
		
		private SolutionGot constructInitialSolutionFromAdaptiveMemory() throws IOException {
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
	
		protected boolean isNewSolutionIsNewBestOverallSolution() {
//			assertEquals(false, solution.equals(bestOverallSolution));
			if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactor(),bestOverallSolution.getTotalDistanceWithCostFactor())
				&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) {				
					return true;
			}
			if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) {				
				return true;
			}
			return false;
		}
		
		private void setBestOverallSolutionToNewSolution() {
			bestOverallSolution = solution.clone();	
		}

	public static void setNewRandomWithSeeds(int seedAM, int seedGI, int seedI1) {
		AdaptiveMemory.resetRandomElementWithSeed(seedAM);
		GreedyInsertion.resetRandomElementWithSeed(seedGI);
		RandomI1Solver.resetRandomElementWithSeed(seedI1);
	}
	
	private void publishSolutionAtEndOfAMTSSearch() throws IOException {
		endTime = System.currentTimeMillis();
		timeNeeded = endTime - startTime;
		ReadAndWriteUtils.publishSolutionAtEndOfAMTSSearch(bestOverallSolution, timeNeeded);
	}


}
