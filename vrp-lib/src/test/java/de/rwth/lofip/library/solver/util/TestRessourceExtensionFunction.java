package de.rwth.lofip.library.solver.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;

public class TestRessourceExtensionFunction {
	
	@Test
	public void testThatConcatenatingEmptyRefAndAnotherRefAreFeasible() {
		ResourceExtensionFunction emptyRef = new ResourceExtensionFunction(); 
		ResourceExtensionFunction anotherRef = new ResourceExtensionFunction(0,195.81138830084188, 345.0, 425.0,20);
		
		assertEquals(true, TourUtils.isConcatenationOfRefsTWFeasible(emptyRef,anotherRef));
	}
	
	@Test 
	public void testThatGIWithRefsProducesSameResultsAsWithLinearTimeTWCheck() throws IOException {
		List<VrpProblem> problems = ReadAndWriteUtils.readSolomonProblemC202();
		AdaptiveMemoryTabuSearch.setSeeds(0, 0, 0);
		SolutionGot solutionLinearTime = new RandomI1Solver().solve(problems.get(0));
		
		AdaptiveMemoryTabuSearch.setSeeds(0, 0, 0);
		SolutionGot solutionRef = new RandomI1SolverWithLinearTimeChecks().solve(problems.get(0));
		
		assertEquals(true, solutionLinearTime.equals(solutionRef)); 
	}

}
