package de.rwth.lofip.library.solver.metaheuristics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.initialSolver.GroupPushForwardInsertionSolver;
import de.rwth.lofip.library.util.TestUtils;

public class TestTabuSearchWithSolomonInstances {
	
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> initialSolutions = new LinkedList<SolutionGot>();
	private List<SolutionGot> improvedSolutions = new LinkedList<SolutionGot>();

	@Test
	public void TestTabuSearchOnAllSolomonInstances() throws IOException {	
//		PrintStream out = new PrintStream(new FileOutputStream("C:/Users/abraun/Dropbox/Uni/Diss/Code/output/output.txt"));
//		System.setOut(out);
//		
		problems = TestUtils.readSolomonProblems();
		solveProblemsWithInitialSolver();
		improveSolutionsWithTabuSearch();
		TestUtils.printResultsToFile("TabuSearch",initialSolutions,improvedSolutions);
	}	
	
//	@Test
//	public void TestTabuSearchOnC101() throws IOException {		
////		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
////		System.setOut(out);
//		
//		problems = TestUtils.readSolomonProblemC101();
//		solveProblemsWithInitialSolver();
//		improveSolutionsWithTabuSearch();		
//	}
	
		private void solveProblemsWithInitialSolver() {
			SolverInterfaceGot initialSolver = new GroupPushForwardInsertionSolver();
			for (VrpProblem problem : problems) {
				SolutionGot solution = initialSolver.solve(problem);
				initialSolutions.add(solution);			
			}
		}	
		
		private void improveSolutionsWithTabuSearch() {			
			for (SolutionGot solution : initialSolutions) {
				TabuSearch tabuSearch = new TabuSearch();
				System.out.println("SOLVING INSTANCE " + solution.getVrpProblem().getDescription());
				SolutionGot improvedSolution = solution.clone();
				improvedSolution = tabuSearch.improve(improvedSolution);
				improvedSolutions.add(improvedSolution);
			}	
		}
		
		private static String getOutputFile() {
			String s = System.getenv("USERPROFILE");
			s += "\\Dropbox\\Uni\\Diss\\Code\\output\\output.txt";					
			System.out.println(s);
			return s;		
		}
}
