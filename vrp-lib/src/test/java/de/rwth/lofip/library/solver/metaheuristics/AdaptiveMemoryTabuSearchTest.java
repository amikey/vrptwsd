package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.RunAdaptiveMemorySearchWithSolomonInstances;
import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.util.SolutionGotUtils;
import de.rwth.lofip.library.util.math.MathUtils;

public class AdaptiveMemoryTabuSearchTest {
	
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> solutions = new LinkedList<SolutionGot>();
	private List<SolutionGot> solutions2 = new LinkedList<SolutionGot>();
	private List<SolutionGot> solutions3 = new LinkedList<SolutionGot>();
	
	@Test
	public void testThatRunOfAMTSproducesTheSameSolutionEveryTime() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.resetSeedsAndRandomElements();
		SolutionGot solution;
		VrpProblem problem = ReadAndWriteUtils.readModifiedSolomonProblems().get(0);
		AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
		solution = adaptiveMemoryTabuSearch.solve(problem);
		System.out.println(solution.getAsTupel());
		assertEquals("( ( 8 6 2 ) ) ( ( 5 3 7 11 12 ) ) ( ( 16 9 4 1 ) ) ( ( 13 17 19 10 ) ) ( ( 18 15 14 ) ) ", solution.getAsTupel());		
	}
	
	@Test
	public void testThatTwoRunsOfAMSearchProduceTheSameResultsAllowingPermutationOfTours() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		problems = ReadAndWriteUtils.readSolomonProblemRC101AsList();	
		
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(problems, solutions);
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(problems, solutions2);
		
		Parameters.setNumberOfNonImprovingAMCalls(1);
		
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(problems, solutions3);
				
		for (int i = 0; i < solutions.size(); i++) {
			solutions.get(i).printSolutionCost();
			solutions.get(i).printVehicleCount();
			solutions2.get(i).printSolutionCost();
			solutions2.get(i).printVehicleCount();
			solutions3.get(i).printSolutionCost();
			solutions3.get(i).printVehicleCount();
			assertEquals(true,solutions.get(i).equals(solutions2.get(i)));
			assertEquals(true,isSolutionOneWorseThanOrEqualToSolutionTwo(solutions.get(i),solutions3.get(i)));
		}
	}
	
	private boolean isSolutionOneWorseThanOrEqualToSolutionTwo(SolutionGot solutionGot, SolutionGot solutionGot2) {
		if (MathUtils.greaterThan(solutionGot.getTotalDistanceWithCostFactor(), solutionGot2.getTotalDistanceWithCostFactor()) ||
				MathUtils.equals(solutionGot.getTotalDistanceWithCostFactor(), solutionGot2.getTotalDistanceWithCostFactor()))
			return true;
		if (solutionGot.getVehicleCount() > solutionGot2.getVehicleCount())
			return true;
		return false;
	}
	
	@Test
	public void testFeasibilityOfC101Solution() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		problems = ReadAndWriteUtils.readSolomonProblemC101();		
		solutions = new ArrayList<SolutionGot>();
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(problems, solutions);
		
		SolutionGot solutionC101 = solutions.get(0);
		
		solutionC101.printSolutionCost();
		solutionC101.printSolutionAsTupel();
		assertEquals(true,SolutionGotUtils.isSolutionFeasibleWrtDemand(solutionC101));
		assertEquals(true,SolutionGotUtils.isSolutionFeasibleWrtTW(solutionC101));
	}
	
}
