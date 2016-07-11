package de.rwth.lofip.library.solver.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;

public class TestSolutionGotUtils {
	
	@Test
	public void testCreateSolutionWithTargetVehicleNumber() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readEigeneModifiedC2SolomonProblems().get(0);
		SolutionGot solution = new SolutionGot(problem); 
		solution = SolutionGotUtils.createSolutionWithSingleTours(solution,problem.getCustomerCount());
		assertEquals(true, solution.getNumberOfTours() == problem.getCustomerCount());
	}
}
