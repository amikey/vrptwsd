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
	@SuppressWarnings("unused")
	private int maximalNumberOfCallsToAdaptiveMemory;
	private int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory;
	
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
	
	protected AdaptiveMemory getAM() {
		return new AdaptiveMemory();
	}
	
	public SolutionGot solve(VrpProblem vrpProblem) throws IOException {
		
		startTime = System.currentTimeMillis();
		
		System.out.println("STARTE INITIALISIERUNG AM");
		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfTabuSearch(vrpProblem);
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();

		initialiseAdaptiveMemoryWithInitialSolutions(vrpProblem);
		bestOverallSolution = constructInitialSolutionFromAdaptiveMemory();			
		
		int iteration = 1;		
		while (!isStoppingCriterionMet()) {
			System.out.println("AM CALL Nr. " + iteration);
			
			solution = constructInitialSolutionFromAdaptiveMemory();
			TabuSearchForElementWithTours tabuSearch = getTS(); 
			tabuSearch.improve(solution);
			storeNewToursInAdaptiveMemory(solution);
			
			if (isBestOverallSolutionFoundAgain())
				increaseNumberOfTimesBestOverallSolutionHasBeenFound();
			
			if (isNewSolutionIsNewBestOverallSolution()) {
				setBestOverallSolutionToNewSolution();
				numberOfAMCallsWithoutImprovement = 0;
				numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
			} else
				numberOfAMCallsWithoutImprovement++;
			
			iteration++;
		}
		if (solution == null  || bestOverallSolution == null)
			throw new RuntimeException("Solution ist Null. Fehler!");
		
		publishSolutionAtEndOfAMTSSearch();
		
		return bestOverallSolution;
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
