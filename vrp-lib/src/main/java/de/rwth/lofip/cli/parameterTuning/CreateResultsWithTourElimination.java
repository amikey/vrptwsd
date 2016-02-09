package de.rwth.lofip.cli.parameterTuning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AMTSWithTourElimination;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;

public class CreateResultsWithTourElimination {
	
	//Includes new Random and no Error in AM
	
	private List<VrpProblem> problems;
	private List<SolutionGot> solutions = new ArrayList<SolutionGot>();

	@Test
	public void TestPublishSolutionsAtEndOfAMTSSearch() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);		
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\VerfiziereDassErgebnisseWiederGutSind\\MitCostMinimizationPhase\\MitKorrigierterCostMinimization\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();		
		processProblems();
	}

	private void processProblems() throws IOException {					
		while (!Parameters.isRunningTimeReached()) {			
				solveProblemsWithAMTSWithTourEliminationSolver();
		}
	}

	private void solveProblemsWithAMTSWithTourEliminationSolver() throws IOException {
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AMTSWithTourElimination();
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}	
	}
	
	@Test
	public void TestPublishAllSolutionsAtEndOfAMTSSearch() throws IOException {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		//Set Parameters for Scenario
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);	
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);		
		Parameters.setRunningTimeInHours(36);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\TestPublishSolutionAtEndOfAMTSAndTS2AndNewRandom\\TestTourEliminationAlleInstanzen\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		
		problems = ReadAndWriteUtils.readOriginalSolomonProblems();		
		processProblems();
	}


}
