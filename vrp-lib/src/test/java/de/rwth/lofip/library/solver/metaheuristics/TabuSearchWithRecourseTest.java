package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.metaheuristics.recourse.TabuSearchWithRecourse;
import de.rwth.lofip.library.solver.util.SolutionGotUtils;
import de.rwth.lofip.library.util.SetUpUtils;

public class TabuSearchWithRecourseTest {
	
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	
	@Test
	public void TestTabuSearchWithRecourse() throws IOException {
		//Set Parameters for Algorithm
		Parameters.setAllParametersToMinimalTestingValues();
//		Parameters.setNumberOfNonImprovingIterationsInTS(5);
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setRelativeStandardDeviationTo(0.15);
		Parameters.setSeeds(3001);
		
		problems = ReadAndWriteUtils.readSolomonProblemC101();
		
		solveProblemsWithTabuSearchWithRecourseSolver();	
	}

	private void solveProblemsWithTabuSearchWithRecourseSolver() throws IOException {		
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());							
			RandomI1Solver initialSolver = new RandomI1Solver();
			SolutionGot newSolution = initialSolver.solve(problem);
			TabuSearchForElementWithTours tabuSearch = new TabuSearchWithRecourse();
			tabuSearch.improve(newSolution);
		}	
	}	
	
	@Test
	public void assertThatSolutionStillHasOneMoreTourAfterTSWithRecourseImprovement() throws IOException {
		Parameters.setAllParametersToMinimalTestingValues();
		Parameters.setMaximalNumberOfToursInGot(2);
		Parameters.setRelativeStandardDeviationTo(0.15);
		Parameters.setSeeds(3001);
		
		SolutionGot solution = SetUpUtils.getSomeSolutionForRC104Problem();
		solution.printVehicleCount();
				
		TabuSearchForElementWithTours tabuSearch = new TabuSearchWithRecourse();
		int vehicleGoalNumber = solution.getNumberOfTours()+1;
		solution = SolutionGotUtils.createSolutionWithVehicleGoalNumber(solution, vehicleGoalNumber);

		assertEquals(vehicleGoalNumber, solution.getNumberOfTours());
		
//		solution = (SolutionGot) tabuSearch.improve(solution);
//		solution.printVehicleCount();
//		assertEquals(vehicleGoalNumber, solution.getNumberOfTours());
	}
	
}
