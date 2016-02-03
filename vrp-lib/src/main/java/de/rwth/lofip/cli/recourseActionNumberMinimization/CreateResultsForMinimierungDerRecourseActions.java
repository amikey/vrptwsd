package de.rwth.lofip.cli.recourseActionNumberMinimization;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization.AMTSwithRecourseAndRecourseActionNumber;

public class CreateResultsForMinimierungDerRecourseActions extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	//--- Eigene Modified Solomon Instances - 15% relative standardabweichung
	
	@Test
	public void AllEigeneModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfToursInGot(2);
		Parameters.setRelativeStandardDeviationTo(0.15);
		Parameters.setRecourseActionNumberMinimization(true);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\StatbilitaetBzwKonsistenzDerBenoetigtenUmplanungen\\EigeneModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
		processProblems();
	}
	
	//--- Modified Solomon Instances - 15% relative standardabweichung
	
	@Test
	public void AllModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfToursInGot(2);
		Parameters.setRelativeStandardDeviationTo(0.15);
		Parameters.setRecourseActionNumberMinimization(true);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\StatbilitaetBzwKonsistenzDerBenoetigtenUmplanungen\\ModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		processProblems();
	}
	
	
	//--- Original Solomon Instances - 15% relative standardabweichung
	
	@Test
	public void AllOriginalSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfToursInGot(2);
		Parameters.setRelativeStandardDeviationTo(0.15);
		Parameters.setRecourseActionNumberMinimization(true);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\StatbilitaetBzwKonsistenzDerBenoetigtenUmplanungen\\OriginalSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		processProblems();
	}
	
	//-------------
	
	@Override
	protected void processProblems() throws IOException {
		for (int i = 1; i <= numberOfExperiments; i++) {
			if (!Parameters.isRunningTimeReached()) {
//				increaseParameters();
				solveProblemsWithAdaptiveMemoryWithRecourseSolver();
			}
		}		
	}

	private void solveProblemsWithAdaptiveMemoryWithRecourseSolver() throws IOException {
		long startTime = System.nanoTime();
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());
						
			AdaptiveMemoryTabuSearch.setSeeds(seedI1, seedGI, seedAM);
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AMTSwithRecourseAndRecourseActionNumber();
			adaptiveMemoryTabuSearch.setNumberOfInitialSolutions(numberOfDifferentInitialSolutions);
			adaptiveMemoryTabuSearch.setMaximalNumberOfCallsToAdaptiveMemory(maximalNumberOfCallsToAdaptiveMemory);
			adaptiveMemoryTabuSearch.setMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory(maximalNumberOfCallsWithoutImprovementToAdaptiveMemory);
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}
		long endTime = System.nanoTime();
		timeNeeded = (endTime - startTime) / 1000 / 1000 / 1000 / 60;
	}
	
}
