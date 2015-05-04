package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.util.SetUpUtils;

	//Starts Adaptive Memory Tabu Search (AMTS) several times in case that AMTS finds same solution three times

public class UberAdaptiveMemoryTabuSearch {
		
	private long maximalTime;	
	private SolutionGot bestOverallSolution = SetUpUtils.getDummySolutionThatIsWorseThanEveryPossibleSolution();
	private int numberOfTimesSameBestOverallSolutionHasBeenFound = 0;
	
	private int initialNumberOfDifferentInitialSolutions;
	private int initialNumberOfIterationsTabuSearch;
	private int seedI1;
	private int seedGI;
	private int seedAM;
	
	public UberAdaptiveMemoryTabuSearch(long maximalTime) {
		this.maximalTime = maximalTime;
	}

	public SolutionGot solve(VrpProblem vrpProblem) {
		
		AdaptiveMemoryTabuSearch.setSeeds(seedI1, seedGI, seedAM);
		AdaptiveMemoryTabuSearch.setParameters(initialNumberOfDifferentInitialSolutions, initialNumberOfIterationsTabuSearch);
		
		while (!isStoppingCriterionMet()) {			
			AdaptiveMemoryTabuSearch amts = new AdaptiveMemoryTabuSearch(maximalTime);
			amts.increaseSeeds();
			amts.increaseParameters();
			SolutionGot solutionTemp = amts.solve(vrpProblem);
			
			if (isBestOverallSolutionFoundAgain(solutionTemp))
				increaseNumberOfTimesBestOverallSolutionHasBeenFound();
			
			if (isSolutionIsNewBestOverallSolution(solutionTemp)) {
				setBestOverallSolutionTo(solutionTemp);
				resetNumberOfTimesSameBestOverallSolutionHasBeenFound();
			}
		}
		
		
		return bestOverallSolution;
	}

	private boolean isStoppingCriterionMet() {
		if (System.currentTimeMillis() >= maximalTime)
			return true;
		if (numberOfTimesSameBestOverallSolutionHasBeenFound >= 3) 			
			return true;
		return false;
	}
	
	private boolean isBestOverallSolutionFoundAgain(SolutionGot solution) {
		return solution.equals(bestOverallSolution);
	}
	
	private void increaseNumberOfTimesBestOverallSolutionHasBeenFound() {
		numberOfTimesSameBestOverallSolutionHasBeenFound++;
	}
	
	private boolean isSolutionIsNewBestOverallSolution(SolutionGot solution) {
		if (solution.getTotalDistance() < bestOverallSolution.getTotalDistance()
			&& solution.getNumberOfTours() <= bestOverallSolution.getNumberOfTours()) 									
				return true;			
		if (solution.getNumberOfTours() < bestOverallSolution.getNumberOfTours()) 								
			return true;			
		return false;
	}
	
	private void setBestOverallSolutionTo(SolutionGot solutionTemp) {
		bestOverallSolution = solutionTemp;	
	}
	
	private void resetNumberOfTimesSameBestOverallSolutionHasBeenFound() {
		numberOfTimesSameBestOverallSolutionHasBeenFound = 0;	
	}
	
	public void setParameters(int initialNumberOfDifferentInitialSolutions, int initialNumberOfIterationsTabuSearch) {
		this.initialNumberOfDifferentInitialSolutions = initialNumberOfDifferentInitialSolutions;
		this.initialNumberOfIterationsTabuSearch = initialNumberOfIterationsTabuSearch;
	}

	public void setSeeds(int seedI1, int seedGI, int seedAM) {
		this.seedI1 = seedI1;
		this.seedGI = seedGI;
		this.seedAM = seedAM;
	}
	
}
