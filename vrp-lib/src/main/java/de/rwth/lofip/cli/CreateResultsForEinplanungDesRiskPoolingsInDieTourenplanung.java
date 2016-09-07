package de.rwth.lofip.cli;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.recourse.AdaptiveMemoryTabuSearchWithRecourse;
import de.rwth.lofip.library.util.VrpUtils;

public class CreateResultsForEinplanungDesRiskPoolingsInDieTourenplanung extends RunAdaptiveMemorySearchWithSolomonInstances {

	//--- Eigene Modified Solomon Instances - 22% relative standardabweichung
	
	double percentageOfCapacity = 1.0;
	private int numberOfToursInGots = 3;
	String ordnerPfad = "\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\3Touren\\";
	
	
	@Test
	public void AlleOhneVorgegebeneAuslastung() throws IOException {
		setParameters();	
		
		Parameters.setOutputDirectory(ordnerPfad);
		problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
		
		problems = problems.subList(48, problems.size());
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C1R1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();	
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setOutputDirectory(ordnerPfad + "C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();	
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setAdditionalNumberOfVehicles(2);		
		Parameters.setOutputDirectory(ordnerPfad + "C1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void R1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();		
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setOutputDirectory(ordnerPfad + "R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedR1SolomonProblems();		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C2R2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setOutputDirectory(ordnerPfad + "C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setOutputDirectory(ordnerPfad + "C2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void R2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setOutputDirectory(ordnerPfad + "R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedR2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void RC1RC2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setOutputDirectory(ordnerPfad + "RC1RC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void RC1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setAdditionalNumberOfVehicles(3);		
		Parameters.setOutputDirectory(ordnerPfad + "RC1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void RC2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setOutputDirectory(ordnerPfad + "RC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	
	
	
	//---------
	
	@Test
	public void C1R1() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(0.9);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\90ProzentAuslastung\\C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C2R2() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(0.9);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\90ProzentAuslastung\\C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C2() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(0.999);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\95ProzentAuslastung\\C2999Prozent\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void RC1RC2() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(0.9);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\90ProzentAuslastung\\RC1RC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void AllEigeneModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(2);		
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		
		problems = ReadAndWriteUtils.readEigeneModifiedSolomonProblems();
		processProblems();
	}
	
	@Test
	public void R2EigeneModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(1);
		Parameters.setMaximalNumberOfToursInGot(2);		
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\EigeneModifiedSolomon\\Hack\\R2\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		
		problems = ReadAndWriteUtils.readEigeneModifiedR2SolomonProblems();
		System.out.println(problems.size());
		problems = problems.subList(problems.size()-4, problems.size());
		System.out.println(problems.size());
		processProblems();
	}
	
	
	//--- Modified Solomon Instances - 22% relative standardabweichung
	
	@Test
	public void AllModifiedSolomonInstances() throws IOException {			
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMaximalNumberOfToursInGot(2);		
		
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
		Parameters.setMaximalNumberOfToursInGot(2);		
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OriginalSolomon\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		processProblems();
	}
	
	//-------------
	
	
	protected void setParameters() {
		//Set Parameters for Scenario
		//Set Parameters for Algorithm
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(percentageOfCapacity);
		Parameters.setMaximalNumberOfToursInGot(numberOfToursInGots);
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
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearchWithRecourse();
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}		
	}	
		
}
