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
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.CrossNeighborhood;
import de.rwth.lofip.library.solver.metaheuristics.neighborhoods.moves.AbstractNeighborhoodMove;
import de.rwth.lofip.library.util.TestUtils;

public class TestLocalSearchWithSolomonInstances {
	
	private static List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private static List<SolutionGot> initialSolutions = new LinkedList<SolutionGot>();
	private static List<SolutionGot> improvedSolutions = new LinkedList<SolutionGot>();
	
	public static void main(String [ ] args) throws IOException {		
		problems = TestUtils.readSolomonProblems();
		solveProblemsWithInitialSolver();
		improveSolutionsWithLocalSearch();
		TestUtils.printResultsToFile("localSearch",initialSolutions,improvedSolutions);
	}
	
	private static void solveProblemsWithInitialSolver() {
		SolverInterfaceGot initialSolver = new GroupPushForwardInsertionSolver();
		for (VrpProblem problem : problems) {
			SolutionGot solution = initialSolver.solve(problem);
			initialSolutions.add(solution);			
		}
	}	
	
	private static void improveSolutionsWithLocalSearch() {
		LocalSearch localSearch = new LocalSearch();
		for (SolutionGot solution : initialSolutions) {
			System.out.println("SOLVING INSTANCE " + solution.getVrpProblem().getDescription());
			SolutionGot improvedSolution = localSearch.improve(solution.clone());
			improvedSolutions.add(improvedSolution);
		}	
	}
	
	@Test
	public void TestLocalSearchOnC101() throws IOException {		
		problems = TestUtils.readSolomonProblemC101();
		solveProblemsWithInitialSolver();
		improveSolutionsWithLocalSearch();		
	}
	
	@Test
	public void TestLocalSearchOnRC101() throws IOException {		
		problems = TestUtils.readSolomonProblemRC101();
		solveProblemsWithInitialSolver();
		improveSolutionsWithLocalSearch();		
	}

}
