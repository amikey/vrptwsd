package de.rwth.lofip.library.solver.localSearch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.initialSolver.GroupPushForwardInsertionSolver;
import de.rwth.lofip.library.util.TestUtils;

public class TestLocalSearchWithSolomonInstances {
	
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> initialSolutions = new LinkedList<SolutionGot>();
	private List<SolutionGot> improvedSolutions = new LinkedList<SolutionGot>();
	
	@Test
	public void testLocalSearch() throws IOException {		
		problems = TestUtils.readSolomonProblems();
		solveProblemsWithInitialSolver();
		improveSolutionsWithLocalSearch();
		TestUtils.printResultsToFile("localSearch",initialSolutions,improvedSolutions);
	}
	
	private void solveProblemsWithInitialSolver() {
		SolverInterfaceGot initialSolver = new GroupPushForwardInsertionSolver();
		for (VrpProblem problem : problems) {
			SolutionGot solution = initialSolver.solve(problem);
			initialSolutions.add(solution);			
		}
	}	
	
	private void improveSolutionsWithLocalSearch() {
		LocalSearch localSearch = new LocalSearch();
		for (SolutionGot solution : initialSolutions) {
			SolutionGot improvedSolution = localSearch.improve(solution.clone());
			improvedSolutions.add(improvedSolution);
		}	
	}

}
