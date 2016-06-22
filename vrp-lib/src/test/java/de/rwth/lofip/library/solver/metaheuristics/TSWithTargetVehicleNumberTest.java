package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;

public class TSWithTargetVehicleNumberTest {

	private List<VrpProblem> problems;

	@Test
	public void TestTabuSearchWithTargetVehicleNumber() throws IOException {
		problems = ReadAndWriteUtils.readSolomonProblemC101();	
		RandomI1Solver initialSolver = new RandomI1Solver();
		SolutionGot newSolution = initialSolver.solve(problems.get(0));
		TSWithTargetVehicleNumber tabuSearch = new TSWithTargetVehicleNumber(11);
		SolutionGot endSolution = (SolutionGot) tabuSearch.improve(newSolution);
		endSolution.printVehicleCount();
		endSolution.printSolutionAsTupel();
		assertEquals(11,endSolution.getVehicleCount());		
	}
	
}

