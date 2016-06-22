package de.rwth.lofip.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.CreateSolutionsFromFile;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.recourse.TabuSearchWithRecourse;
import de.rwth.lofip.library.util.PrintUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class TestCreateSolutionsFromFile {
	
	protected List<VrpProblem> problems = new ArrayList<VrpProblem>();
	protected List<SolutionGot> solutions = new ArrayList<SolutionGot>();
	
	@Test
	public void testCreateSolutionsFromFile() throws IOException {			
		Parameters.setAllParametersToNewBestValuesAfterParameterTesting();
		Parameters.setNumberOfIterationsWithoutRematching(30);
		Parameters.setTourMinimizationPhase(false);
		Parameters.setMinimumNumberOfIterationsWithoutTourElemination(0);
		Parameters.setMaximalNumberOfCustomersConsideredInSegment(7);
		Parameters.setNumberOfInitialSolutions(20);
		Parameters.setNumberOfIntensificationTries(0);
		
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setPublishSolutionAtEndOfAMTSSearch(true);
		Parameters.setOutputDirectory("\\SolutionsFromExistingFiles\\R1\\RecourseCost\\2Touren");
		
		problems = ReadAndWriteUtils.readEigeneModifiedR1SolomonProblems();
		for (VrpProblem problem : problems) {		
			solutions = CreateSolutionsFromFile.createSolutionsFromFileForProblem(problem);
			long startTime = System.currentTimeMillis();
//			PrintUtils.printListOfSolutions(solutions);	
			SolutionGot solution = improveSolutionsWithTSRecourseSolver(solutions);
//			solution.printSolutionAsTupel();
			long endTime = System.currentTimeMillis();
			publishSolutionAtEndOfImprovementTS(solution, endTime - startTime);
		}
	}
	
	private SolutionGot improveSolutionsWithTSRecourseSolver(List<SolutionGot> solutions2) throws IOException {
		SolutionGot bestOverallSolution = solutions2.get(0);
		Parameters.setTourMinimizationPhase(false);
		for (SolutionGot solution : solutions2) {
			solution = (SolutionGot) new TabuSearchWithRecourse().improve(solution);			
			if (isNewSolutionIsBestOverallSolution(solution, bestOverallSolution))
				bestOverallSolution = solution.clone();		
		}
		return bestOverallSolution;
	}

	private boolean isNewSolutionIsBestOverallSolution(SolutionGot solution, SolutionGot bestOverallSolution) {
		if (MathUtils.lessThan(solution.getTotalDistanceWithCostFactorAndRecourse(),bestOverallSolution.getTotalDistanceWithCostFactorAndRecourse()))			 				
			return true;
		return false;
	}
	
	private void publishSolutionAtEndOfImprovementTS(SolutionGot solution, long timeNeeded) throws IOException {		
		ReadAndWriteUtils.publishSolutionAtEndOfAMTSSearch(solution, timeNeeded);
	}

}
