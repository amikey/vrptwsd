package de.rwth.lofip.library.solver.localSearch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.initialSolver.GroupPushForwardInsertionSolver;
import de.rwth.lofip.library.util.TestUtils;

public class TestLocalSearchWithSolomonInstances {
	
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> initialSolutions = new LinkedList<SolutionGot>();
	private List<SolutionGot> improvedSolutions = new LinkedList<SolutionGot>();
	
//	@Test
//	public void TestLocalSearchOnAllSolomonInstancesSolveSequentially() throws IOException {
//		problems = TestUtils.readSolomonProblems();		
//		for (VrpProblem problem : problems) {
//			System.out.println("SOLVING INSTANCE " + problem.getDescription());
//			SolverInterfaceGot initialSolver = new GroupPushForwardInsertionSolver();
//			SolutionGot solution = initialSolver.solve(problem);
//			LocalSearch localSearch = new LocalSearch();
//			SolutionGot improvedSolution = localSearch.improve(solution.clone());
//			improvedSolutions.add(improvedSolution);
//		}
//	}
//	
//	@Test
//	public void TestLocalSearchOnAllSolomonInstances() throws IOException {	
////		PrintStream out = new PrintStream(new FileOutputStream("C:/Users/Andreas/Dropbox/Uni/Diss/Code/output/output.txt"));
////		System.setOut(out);
////		
//		problems = TestUtils.readSolomonProblems();
//		solveProblemsWithInitialSolver();
//		improveSolutionsWithLocalSearch();
//		TestUtils.printResultsToFile("localSearch",initialSolutions,improvedSolutions);
//	}
	
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
	public void TestLocalSearchOnC102() throws IOException {		
		problems = TestUtils.readSolomonProblemC102();
		solveProblemsWithInitialSolver();
		improveSolutionsWithLocalSearch();		
	}
	
	@Test
	public void TestLocalSearchOnC103() throws IOException {		
		problems = TestUtils.readSolomonProblemC103();
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
