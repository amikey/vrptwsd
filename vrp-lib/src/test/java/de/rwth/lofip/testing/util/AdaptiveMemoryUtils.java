package de.rwth.lofip.testing.util;

import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;

public class AdaptiveMemoryUtils {
	
	public static void setParametersInAMTSforTesting(AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch) {
		AdaptiveMemoryTabuSearch.setSeeds(1, 1, 1);
		Parameters.setNumberOfIterationsInTS(10);
		Parameters.setNumberOfImprovingIterationsInTS(10);
		adaptiveMemoryTabuSearch.setNumberOfInitialSolutions(10);
		adaptiveMemoryTabuSearch.setMaximalNumberOfCallsToAdaptiveMemory(10);
		adaptiveMemoryTabuSearch.setMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory(10);
	}

}
