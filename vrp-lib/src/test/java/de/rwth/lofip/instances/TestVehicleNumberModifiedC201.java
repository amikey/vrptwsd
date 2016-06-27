package de.rwth.lofip.instances;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;

public class TestVehicleNumberModifiedC201 {
	
	@Test
	public void VehicleNumberAfterI1ModifiedC201Test() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readEigeneModifiedC2SolomonProblems().get(0);
		RandomI1Solver i1 = new RandomI1Solver();
		for (int i = 0; i<10; i++) {
			SolutionGot solution = i1.solve(problem);
			System.out.println("solution has " + solution.getNumberOfTours() + " vehicles.");
			solution.printSolutionAsTupel();
		}	
	}

}
