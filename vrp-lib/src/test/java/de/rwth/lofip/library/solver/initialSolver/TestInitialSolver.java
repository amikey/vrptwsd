package de.rwth.lofip.library.solver.initialSolver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;

public class TestInitialSolver {
		
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> solutions = new LinkedList<SolutionGot>();	
	
	public void main() throws IOException {		
		problems = ReadAndWriteUtils.readSolomonProblems();
		solveProblemsWithInitialSolver();
		ReadAndWriteUtils.printResultsToFile("initialSolver",solutions);
	}
	
	private void solveProblemsWithInitialSolver() {
		SolverInterfaceGot initialSolver = new GroupPushForwardInsertionSolver();
		for (VrpProblem problem : problems) {
			SolutionGot solution = initialSolver.solve(problem);
			solutions.add(solution);
		}
	}	
}
