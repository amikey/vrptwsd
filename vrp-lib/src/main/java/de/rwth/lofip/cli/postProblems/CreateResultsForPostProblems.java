package de.rwth.lofip.cli.postProblems;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.recourse.AdaptiveMemoryTabuSearchWithRecourse;
import de.rwth.lofip.library.solver.metaheuristics.recourse.actionNumberMinimization.AMTSwithRecourseAndRecourseActionNumber;
import de.rwth.lofip.library.util.VrpUtils;

public class CreateResultsForPostProblems {

	private List<VrpProblem> problems = new ArrayList<VrpProblem>();
	private List<SolutionGot> solutions = new ArrayList<SolutionGot>();

	@Test
	public void CreateResultsForIstSituation() throws IOException {
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setPercentageOfCapacity(0.8);
		Parameters.setIsPostScenario(true);
		
		Parameters.setOutputDirectory("\\PostSzenarien\\IstSituation\\");
		problems = ReadAndWriteUtils.readPostProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblemsWithDeterministicSolver();
	}
	
	@Test
	public void CreateResultsForIstSituationMitTourkombination() throws IOException {
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setPercentageOfCapacity(0.8);
		Parameters.setIsPostScenario(true);
		
		Parameters.setOutputDirectory("\\PostSzenarien\\IstSituation\\MitTourkombination\\");
		problems = ReadAndWriteUtils.readPostProblems();
		
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblemsWithDeterministicSolver();
	}
	
	@Test
	public void HundredPercentOfCapacity() throws IOException {			
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(1);
		Parameters.setIsPostScenario(true);
		
		Parameters.setOutputDirectory("\\PostSzenarien\\100%Auslastung\\");
		
		problems = ReadAndWriteUtils.readPostProblems();		
		processProblemsWithDeterministicSolver();
	}
	
	@Test
	public void HundredPercentOfCapacityMitTourkombination() throws IOException {			
		setParameters();
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setIsPostScenario(true);
		
		Parameters.setOutputDirectory("\\PostSzenarien\\100%Auslastung\\MitTourkombination\\");
		
		problems = ReadAndWriteUtils.readPostProblems();		
		processProblemsWithDeterministicSolver();
	}
	
	@Test
	public void G_2Equals0() throws IOException {			
		setParameters();
		Parameters.setIsPostScenario(true);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\PostSzenarien\\100ProzentAuslastungMinRecourseCost\\");		
		
		problems = ReadAndWriteUtils.readPostProblems();
		processProblemsWithStochasticSolver();
	}
	
	@Test
	public void G_2Equals0Point3() throws IOException {			
		setParameters();
		Parameters.setPercentageOfCapacity(0.95);
		Parameters.setIsPostScenario(true);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\PostSzenarien\\95ProzentAuslastungMinAnzRecourseActions\\");		
		
		problems = ReadAndWriteUtils.readPostProblems();	
		problems = problems.subList(problems.size()-2, problems.size());
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblemsWithStochasticAnzRecActMinSolver();
	}
	
	@Test
	public void G_2Equals0Point7() throws IOException {			
		setParameters();
		Parameters.setPercentageOfCapacity(0.90);
		Parameters.setIsPostScenario(true);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\PostSzenarien\\90ProzentAuslastungMinAnzRecourseActions\\");		
		
		problems = ReadAndWriteUtils.readPostProblems();	
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblemsWithStochasticAnzRecActMinSolver();
	}
	
	@Test
	public void G_2Equals1() throws IOException {			
		setParameters();
		Parameters.setPercentageOfCapacity(0.85);
		Parameters.setIsPostScenario(true);
		
		//Set Output parameters
		Parameters.setOutputDirectory("\\PostSzenarien\\85ProzentAuslastungMinAnzRecourseActions\\");		
		
		problems = ReadAndWriteUtils.readPostProblems();	
		VrpUtils.reduceCapacityOfVehiclesInProblems(problems);
		processProblemsWithStochasticAnzRecActMinSolver();
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
		
	private void processProblemsWithDeterministicSolver() throws IOException {
		while (!Parameters.isRunningTimeReached())
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
	
	private void processProblemsWithStochasticSolver() throws IOException {
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
	
	private void processProblemsWithStochasticAnzRecActMinSolver() throws IOException {
		while(!Parameters.isRunningTimeReached())
				solveProblemsWithAdaptiveMemoryWithRecourseActMinSolver();		
	}

	private void solveProblemsWithAdaptiveMemoryWithRecourseActMinSolver() throws IOException {		
		ReadAndWriteUtils.createHeaderForPublishingSolutionAtEndOfAMTSSearch();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AMTSwithRecourseAndRecourseActionNumber();
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}		
	}	
	
}
