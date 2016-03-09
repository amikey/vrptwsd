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
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatching;
import de.rwth.lofip.library.util.VrpUtils;

public class AnalyseIstSituation {
	
	private List<VrpProblem> problems = new ArrayList<VrpProblem>();
	private List<SolutionGot> solutions = new ArrayList<SolutionGot>();

	@Test
	public void HundredPercentOfCapacityOnC1R1EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\MitTourKombination\\C1R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void HundredPercentOfCapacityOnR1EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\MitTourKombination\\R1\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC1R1SolomonProblems();
		problems = problems.subList(9, problems.size());
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}

	@Test
	public void HundredPercentOfCapacityOnC2R2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\MitTourKombination\\C2R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void HundredPercentOfCapacityOnR2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\MitTourKombination\\R2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedC2R2SolomonProblems();
		problems = problems.subList(8, problems.size());
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblems();
	}
	
	@Test
	public void HundredPercentOfCapacityOnRC1RC2EigeneModifiedSolomonInstances() throws IOException {
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(2);
		
		Parameters.setOutputDirectory("\\AnalyseIstSituation\\MitTourKombination\\RC1CRC2\\");
		problems = ReadAndWriteUtils.readEigeneModifiedRC1RC2SolomonProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
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
		Parameters.setPercentageOfCapacity(0.8);
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
	}
	

	private void processProblems() throws IOException {
		while (!Parameters.isRunningTimeReached())
			solveProblemsWithAdaptiveMemorySolver();
	}

	private void solveProblemsWithAdaptiveMemorySolver() throws IOException {
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			AdaptiveMemoryTabuSearch amts = new AdaptiveMemoryTabuSearch();
			solutions.add(amts.solve(problem));
		}
		postProcessProblemsWithTourMatchingAlgorithm();
	}
	
	private void postProcessProblemsWithTourMatchingAlgorithm() throws IOException {
		for (int i = 0; i < solutions.size(); i++) {
			SolutionGot solution = solutions.get(i);
			SolutionGot solution2 = new TourMatching().matchToursToGots(solution);
			solutions.set(i, solution2);
		}
	}

}
