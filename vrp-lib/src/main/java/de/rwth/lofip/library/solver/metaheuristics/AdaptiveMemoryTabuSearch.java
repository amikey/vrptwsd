package de.rwth.lofip.library.solver.metaheuristics;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;

public class AdaptiveMemoryTabuSearch {
	
	private int numberOfDifferentInitialSolutions = 20;
	private int callsToAdaptiveMemory = 100;
	private int maximalNumberOfCallsToAdaptiveMemory = 20;
	private AdaptiveMemory adaptiveMemory = new AdaptiveMemory();
	
	public SolutionGot solve(VrpProblem vrpProblem) {
		
		SolutionGot solution = null;
		
		initialiseAdaptiveMemoryWithInitialSolutions(vrpProblem);
			
		while (!isStoppingCriterionMet()) {
			solution = constructInitialSolutionFromAdaptiveMemory();
			TabuSearch tabuSearch = new TabuSearch();
			tabuSearch.improve(solution);
			storeNewToursInAdaptiveMemory(solution);
		}
		if (solution == null)
			throw new RuntimeException("Solution ist Null. Fehler!");
		return solution;
	}

	private void initialiseAdaptiveMemoryWithInitialSolutions(VrpProblem vrpProblem) {
		for (int i = 1; i <= numberOfDifferentInitialSolutions; i++) {
			RandomI1Solver initialSolver = new RandomI1Solver();
			SolutionGot newSolution = initialSolver.solve(vrpProblem);
			TabuSearch tabuSearch = new TabuSearch();
			tabuSearch.improve(newSolution);
			adaptiveMemory.addTours(newSolution);
		}
	}
	
	private boolean isStoppingCriterionMet() {
		return (callsToAdaptiveMemory == maximalNumberOfCallsToAdaptiveMemory);
	}
	
	private SolutionGot constructInitialSolutionFromAdaptiveMemory() {
		callsToAdaptiveMemory++;
		return adaptiveMemory.constructSolutionFromTours();
	}
	
	private void storeNewToursInAdaptiveMemory(SolutionGot solution) {
		adaptiveMemory.addTours(solution);
	}

}
