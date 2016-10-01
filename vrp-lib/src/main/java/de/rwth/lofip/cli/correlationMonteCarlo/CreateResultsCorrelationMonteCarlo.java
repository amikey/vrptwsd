package de.rwth.lofip.cli.correlationMonteCarlo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization.AMTSwithRecourseAndRecourseActionNumber;
import de.rwth.lofip.library.util.VrpUtils;

public class CreateResultsCorrelationMonteCarlo {

	private List<VrpProblem> problems = new ArrayList<VrpProblem>();
	private List<SolutionGot> solutions = new ArrayList<SolutionGot>();
	
	private String path = "\\Ab20160720\\CorrelationMonteCarlo\\PostSzenarien\\";
	private double weight = 0.015;

	@Test
	public void CreateResultsForIstSituation() throws IOException {
		setParameters();
		Parameters.setSeeds(700);
		Parameters.setWeightForConvexcombination(weight);
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setNumberOfCorrelatedCustomerGroups(10);
		Parameters.setIsPostScenario(true);
		
		Parameters.setOutputDirectory(path + "10Gruppen\\ZweiteRechnung\\");
		problems = ReadAndWriteUtils.readPostProblems();
		
		solveProblemsWithAdaptiveMemoryWithRecourseActMinSolverNewAlgo();
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
	
	private void solveProblemsWithAdaptiveMemoryWithRecourseActMinSolverNewAlgo() throws IOException {		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			int customerCount = problem.getCustomerCount();
			int numberOfCorrelatedCustomerGroups = Parameters.getNumberOfCorrelatedCustomerGroups();
			int numberOfCustomersInCorrelatedGroup = (int) Math.ceil((double) customerCount / (double) numberOfCorrelatedCustomerGroups);
			
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AMTSwithRecourseAndRecourseActionNumber adaptiveMemoryTabuSearch = new AMTSwithRecourseAndRecourseActionNumber();
			SolutionGot solution = adaptiveMemoryTabuSearch.solveWithNewAlgo(problem);
			solutions.add(solution);			
		}		
	}
	
}