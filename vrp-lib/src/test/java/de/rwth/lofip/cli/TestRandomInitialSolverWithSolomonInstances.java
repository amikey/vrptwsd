package de.rwth.lofip.cli;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.GroupOfTours;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;

public class TestRandomInitialSolverWithSolomonInstances {

	private List<VrpProblem> problems;
	private int numberOfToursInGot = 2;	

	@Test
	public void TestLocalSearchOnSolomonInstance202() throws IOException {
		GroupOfTours.setNumberOfToursInGot(numberOfToursInGot);
		problems = ReadAndWriteUtils.readSolomonProblemC202();	
		solveProblemsWithInitialSolver();				
	}
	
	private void solveProblemsWithInitialSolver() {			
		for (VrpProblem problem : problems) {
			SolverInterfaceGot initialSolver = new RandomI1Solver();
			SolutionGot solution = initialSolver.solve(problem);
			System.out.println(solution.getAsTupel());			
		}
	}
}
