package de.rwth.lofip.library.solver.metaheuristics;

import java.io.IOException;
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
	public void TestTabuSearchOnC101() throws IOException {		
		problems = TestUtils.readSolomonProblemC101();
		solveProblemsWithInitialSolver();
		improveSolutionsWithTabuSearch();		
	}
	
		private void solveProblemsWithInitialSolver() {
			SolverInterfaceGot initialSolver = new GroupPushForwardInsertionSolver();
			for (VrpProblem problem : problems) {
				SolutionGot solution = initialSolver.solve(problem);
				initialSolutions.add(solution);			
			}
		}	
		
		private void improveSolutionsWithTabuSearch() {
			TabuSearch tabuSearch = new TabuSearch();
			for (SolutionGot solution : initialSolutions) {
				System.out.println("SOLVING INSTANCE " + solution.getVrpProblem().getDescription());
				SolutionGot improvedSolution = solution.clone();
				improvedSolution = tabuSearch.improve(improvedSolution);
				improvedSolutions.add(improvedSolution);
			}	
		}
}
