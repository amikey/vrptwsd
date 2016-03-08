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
import de.rwth.lofip.library.util.VrpUtils;

public class CreateResultsForMinimierungDerRecourseActions extends RunAdaptiveMemorySearchWithSolomonInstances {
	
	//--- Eigene Modified Solomon Instances - 22% relative standardabweichung
	
	@Test
	public void MinimierungRecourseActionsOnC1R1EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\90ProzentMaxAuslastung\\C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		
		Parameters.setPercentageOfCapacity(0.9);
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}

	@Test
	public void MinimierungRecourseActionsOnC2R2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\90ProzentMaxAuslastung\\C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
				
		Parameters.setPercentageOfCapacity(0.9);
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void MinimierungRecourseActionsOnRC1RC2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\90ProzentMaxAuslastung\\RC1CRC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();
				
		Parameters.setPercentageOfCapacity(0.9);
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void AllEigeneModifiedSolomonInstances() throws IOException {			
		setParameters();
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\");
		
		problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
		processProblems();
	}
	
	private void setParameters() {
		//Set Parameters for Scenario
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);		
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
	}
		
	@Override
	protected void processProblems() throws IOException {
		while(!Parameters.isRunningTimeReached())
				solveProblemsWithAdaptiveMemoryWithRecourseSolver();		
	}

	private void solveProblemsWithAdaptiveMemoryWithRecourseSolver() throws IOException {		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AMTSwithRecourseAndRecourseActionNumber();
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}		
	}	
	
}
