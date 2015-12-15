package de.rwth.lofip.testing.util;

import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;

public class AdaptiveMemoryUtils {
	
	public static void setParametersInAMTSforTesting(AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch) {
		AdaptiveMemoryTabuSearch.setSeeds(1, 1, 1);
		adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsTabuSearch(10);
		adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsWithoutImprovementTabuSerach(10);
		adaptiveMemoryTabuSearch.setNumberOfInitialSolutions(10);
		adaptiveMemoryTabuSearch.setMaximalNumberOfCallsToAdaptiveMemory(10);
		adaptiveMemoryTabuSearch.setMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory(10);
	}

}
