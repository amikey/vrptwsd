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
	
	private double weight = 0.01;

	@Test
	public void C1R1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();		
		Parameters.setWeightForConvexcombination(weight);
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt1\\C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();		
		problems = problems.subList(4, problems.size());
		processProblems();
	}
	
	@Test
	public void C1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setWeightForConvexcombination(weight);
		Parameters.setAdditionalNumberOfVehicles(2);		
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt01\\C1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1SolomonProblems();
		problems = problems.subList(4, problems.size());
		processProblems();
	}
	
	@Test
	public void R1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(weight);		
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt03\\R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedR1SolomonProblems();		
		processProblems();
	}
	
	@Test
	public void C2R2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(weight);		
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt03\\C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();		
		processProblems();
	}
	
	@Test
	public void C2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(weight);
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt03\\C2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2SolomonProblems();		
		processProblems();
	}
	
	@Test
	public void R2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(weight);
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt03\\R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedR2SolomonProblems();		
		processProblems();
	}
	
	@Test
	public void RC1RC2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(weight);		
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt03\\RC1RC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();		
		processProblems();
	}
	
	@Test
	public void RC1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(weight);
		Parameters.setAdditionalNumberOfVehicles(3);		
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt03\\RC1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1SolomonProblems();
		processProblems();
	}
	
	@Test
	public void RC2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(weight);		
		Parameters.setOutputDirectory("\\MinimierungRecourseActions\\OhneVorgegebeneAuslastung\\0Punkt03\\RC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC2SolomonProblems();		
		processProblems();
	}
	
	//------------------
	
	@Test
	public void MinimierungRecourseActionsOnC1EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(0.1);
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\85ProzentMaxAuslastung\\NachRevert\\C1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		
		Parameters.setPercentageOfCapacity(0.9);
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void MinimierungRecourseActionsOnR1EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(0.1);
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\85ProzentMaxAuslastung\\NachRevert\\R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		problems = problems.subList(9, problems.size());
		
		Parameters.setPercentageOfCapacity(0.9);
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}

	@Test
	public void MinimierungRecourseActionsOnC2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(0.1);
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\85ProzentMaxAuslastung\\NachRevert\\C2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
				
		Parameters.setPercentageOfCapacity(0.9);
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void MinimierungRecourseActionsOnR2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(0.1);
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\85ProzentMaxAuslastung\\NachRevert\\R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		problems = problems.subList(8, problems.size());
				
		Parameters.setPercentageOfCapacity(0.9);
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void MinimierungRecourseActionsOnRC1RC2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setWeightForConvexcombination(0.1);
		
		Parameters.setOutputDirectory("\\AnzahlRecourseActionsInZielfunktion\\85ProzentMaxAuslastung\\NachRevert\\RC1CRC2\\");
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
