package de.rwth.lofip.cli;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.initialSolver.GroupPushForwardInsertionSolver;
import de.rwth.lofip.library.solver.localSearch.LocalSearchForElementWithTours;

public class RunLocalSearchWithSolomonInstances {
	
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> initialSolutions = new LinkedList<SolutionGot>();
	private List<SolutionGot> improvedSolutions = new LinkedList<SolutionGot>();
	
	@Test
	public void TestLocalSearchOnAllSolomonInstances() throws IOException {	
//		PrintStream out = new PrintStream(new FileOutputStream("C:/Users/abraun/Dropbox/Uni/Diss/Code/output/output.txt"));
//		System.setOut(out);
//		
		problems = ReadAndWriteUtils.readSolomonProblems();
		solveProblemsWithInitialSolver();
		improveSolutionsWithLocalSearch();
		ReadAndWriteUtils.printResultsToFile("localSearch",initialSolutions,improvedSolutions);
	}
	
		private void solveProblemsWithInitialSolver() {			
			for (VrpProblem problem : problems) {
				SolverInterfaceGot initialSolver = new GroupPushForwardInsertionSolver();
				SolutionGot solution = initialSolver.solve(problem);
				initialSolutions.add(solution);			
			}
		}	
		
		private void improveSolutionsWithLocalSearch() {			
			for (SolutionGot solution : initialSolutions) {
				LocalSearchForElementWithTours localSearch = new LocalSearchForElementWithTours();
				System.out.println("SOLVING INSTANCE " + solution.getVrpProblem().getDescription());
				SolutionGot improvedSolution = localSearch.improve(solution.clone());
				improvedSolutions.add(improvedSolution);
			}	
		}
	
//	@Test
//	public void TestLocalSearchOnC101() throws IOException {		
//		problems = TestUtils.readSolomonProblemC101();
//		solveProblemsWithInitialSolver();
//		improveSolutionsWithLocalSearch();		
//	}
//	
//	@Test
//	public void TestLocalSearchOnC102() throws IOException {		
//		problems = TestUtils.readSolomonProblemC102();
//		solveProblemsWithInitialSolver();
//		improveSolutionsWithLocalSearch();		
//	}
//	
//	@Test
//	public void TestLocalSearchOnC103() throws IOException {		
//		problems = TestUtils.readSolomonProblemC103();
//		solveProblemsWithInitialSolver();
//		improveSolutionsWithLocalSearch();		
//	}
//	
//	@Test
//	public void TestLocalSearchOnC106() throws IOException {		
//		problems = TestUtils.readSolomonProblemC106();
//		solveProblemsWithInitialSolver();
//		improveSolutionsWithLocalSearch();		
//	}
//	
//	@Test
//	public void TestLocalSearchOnRC101() throws IOException {		
//		problems = TestUtils.readSolomonProblemRC101();
//		solveProblemsWithInitialSolver();
//		improveSolutionsWithLocalSearch();		
//	}

}
