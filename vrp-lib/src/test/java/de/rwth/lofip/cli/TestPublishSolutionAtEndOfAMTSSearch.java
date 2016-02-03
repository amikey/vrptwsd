package de.rwth.lofip.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;

public class TestPublishSolutionAtEndOfAMTSSearch {
	
	private List<VrpProblem> problems;
	private List<SolutionGot> solutions = new ArrayList<SolutionGot>();
	
	@Test
	public void TestPublishSolutionsAtEndOfAMTSSearch() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setPublishSolutionValueProgress(true);
		//Set Parameters for Scenario
		Parameters.setNumberOfNonImprovingIterationsInTS(0);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(1);		
		Parameters.setRunningTimeInHours(6);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\TestPublishSolutionAtEndOfAMTSAndTS2AndNewRandom\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems().subList(0, 10);		
		solveProblemsWithAdaptiveMemorySolver();
	}
	
	private void solveProblemsWithAdaptiveMemorySolver() throws IOException {		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}		
	}	

}
