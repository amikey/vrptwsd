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
		assertEquals("( ( 8 11 9 6 ) ) ( ( 5 3 4 2 1 ) ) ( ( 16 14 12 ) ) ( ( 13 17 10 ) ) ( ( 18 19 15 ) ) ( ( 7 ) ) ", solution.getAsTupel());		
	}
	
	@Test
	public void testThatTwoRunsOfAMSearchProduceTheSameResultsAllowingPermutationOfTours() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		problems = ReadAndWriteUtils.readSolomonProblemRC101AsList();	
		
		Parameters.resetSeedsAndRandomElements();
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(problems, solutions);
		
		Parameters.resetSeedsAndRandomElements();
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(problems, solutions2);
		
		Parameters.setNumberOfNonImprovingAMCalls(1);
		
		Parameters.resetSeedsAndRandomElements();
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
	
	@Test 
	public void testCostOptimiziationPostProcessingPhase() throws IOException {
		givenRC104Problem();
		whenSolvingProblemWithoutCostOptimizationPhase();
		andSolvingProblemWithCostOptimizationPhase();
		thenSecondSolutionShouldNotBeWorseThanFirstSolution();
	}

	private void givenRC104Problem() throws IOException {
		problems = ReadAndWriteUtils.readSolomonProblemRC104AsList();
	}

	private void whenSolvingProblemWithoutCostOptimizationPhase() throws IOException {
		System.out.println("Ohne Cost Optimization");
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.resetSeedsAndRandomElements();
		solutions = new ArrayList<SolutionGot>();		
		AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch() {
			@Override
			protected void improveBestSolutionsWithCostMinimizationPhase() {
				//do nothing;
			}
		};	
		solutions.add(adaptiveMemoryTabuSearch.solve(problems.get(0)));		
	}

	private void andSolvingProblemWithCostOptimizationPhase() throws IOException {
		System.out.println("Mit Cost Optimization");
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.resetSeedsAndRandomElements();
		solutions2 = new ArrayList<SolutionGot>();		
		AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
		solutions2.add(adaptiveMemoryTabuSearch.solve(problems.get(0)));		
	}

	private void thenSecondSolutionShouldNotBeWorseThanFirstSolution() {
		assertEquals(true, MathUtils.lessThanOrEqual(solutions2.get(0).getTotalDistanceWithCostFactor(), solutions.get(0).getTotalDistanceWithCostFactor()));
		assertEquals(true, MathUtils.lessThanOrEqual(solutions2.get(0).getNumberOfTours(), solutions.get(0).getNumberOfTours()));
	}
	
}
