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
	public void testThatTwoRunsOfAMSearchProduceTheSameResults1() throws IOException {
	
		problems = ReadAndWriteUtils.readSolomonProblemRC101AsList();	
		
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(
				problems, solutions, 2, 5, 5, 5, 5, 1, 1, 1);	
		
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(
				problems, solutions2, 2, 5, 5, 5, 5, 1, 1, 1);
		
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(
				problems, solutions3, 2, 5, 5, 7, 7, 1, 1, 1);
				
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
		
		Parameters.setPublishSolutionAtEndOfTabuSearch(false);
		
		problems = ReadAndWriteUtils.readSolomonProblemC101();	
		
		solutions = new ArrayList<SolutionGot>();
		
		new RunAdaptiveMemorySearchWithSolomonInstances().solveProblemsWithAdaptiveMemorySolver(
				problems, solutions, 1, 0, 5, 0, 5, 1, 1, 1);
		
		SolutionGot solutionC101 = solutions.get(0);
		
		solutionC101.printSolutionCost();
		solutionC101.printSolutionAsTupel();
		assertEquals(true,SolutionGotUtils.isSolutionFeasibleWrtDemand(solutionC101));
		assertEquals(true,SolutionGotUtils.isSolutionFeasibleWrtTW(solutionC101));
	}
	
}
