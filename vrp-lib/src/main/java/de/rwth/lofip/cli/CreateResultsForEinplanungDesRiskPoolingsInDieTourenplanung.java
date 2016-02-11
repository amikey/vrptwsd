package de.rwth.lofip.cli;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.recourse.AdaptiveMemoryTabuSearchWithRecourse;

public class CreateResultsForEinplanungDesRiskPoolingsInDieTourenplanung extends RunAdaptiveMemorySearchWithSolomonInstances {

	//--- Eigene Modified Solomon Instances - 22% relative standardabweichung
	
	@Test
	public void AllEigeneModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(1);
		Parameters.setNumberOfToursInGot(2);		
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
		processProblems();
	}
	
	@Test
	public void R1XXEigeneModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfToursInGot(2);		
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readEigeneModifiedSolomonR1XXProblems();
		processProblems();
	}
	
	//--- Modified Solomon Instances - 22% relative standardabweichung
	
	@Test
	public void AllModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfToursInGot(2);		
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\ModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		processProblems();
	}
	
	
	//--- Original Solomon Instances - 22% relative standardabweichung
	
	@Test
	public void AllOriginalSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfToursInGot(2);		
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OriginalSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		processProblems();
	}
	
	//-------------
	
	@Override
	protected void processProblems() throws IOException {
		while(!Parameters.isRunningTimeReached())
				solveProblemsWithAdaptiveMemoryWithRecourseSolver();		
	}

	private void solveProblemsWithAdaptiveMemoryWithRecourseSolver() throws IOException {		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearchWithRecourse();
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}		
	}	
		
}
