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
	
	double capacity = 1.1;	
	String pfad = "\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\";
	
	@Test
	public void C1R1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();		
		Parameters.setOutputDirectory(pfad + "C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setAdditionalNumberOfVehicles(2);		
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\C1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void R1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(1);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedR1SolomonProblems();		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C2R2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setPercentageOfCapacity(1);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void C2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\C2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void R2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setNumberOfInitialSolutions(10);
		Parameters.setNumberOfNonImprovingAMCalls(1);
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedR2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void RC1RC2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();		
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\RC1RC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void RC1OhneVorgegebeneAuslastung() throws IOException {
		setParameters();
		Parameters.setAdditionalNumberOfVehicles(3);		
		Parameters.setOutputDirectory("\\ErgebnisseEinplanungDesRiskPoolings\\OhneVorgegebeneAuslastung\\ErhoehteKapazitaet1Punkt1\\RC1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void RC2OhneVorgegebeneAuslastung() throws IOException {
		setParameters();		
		Parameters.setOutputDirectory(pfad + "RC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC2SolomonProblems();
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	//-------
	
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
	
	private void setParameters() {
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfNonImprovingAMCalls(30);
		Parameters.setNumberOfMovesThatStochasticCostIsCalculatedFor(20);
		Parameters.setNumberOfIntensificationTries(0);
		Parameters.setPercentageOfCapacity(capacity);
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
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
