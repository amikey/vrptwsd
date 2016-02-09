package de.rwth.lofip.library.solver.metaheuristics.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.util.SetUpUtils;

public class TestAdaptiveMemory {

	private AdaptiveMemory adaptiveMemory;
	
	@Test
	public void testAddTour() throws IOException {
		adaptiveMemory = new AdaptiveMemory();
		
		SolutionGot solution = SetUpUtils.getSolutionWithOneTourWithCustomersC1C2C3C4();
		adaptiveMemory.addTours(solution);
		
		SolutionGot newSolution = adaptiveMemory.constructSolutionFromTours();
		assertEquals(true, solution.equals(newSolution));
		
		SolutionGot newSolution2 = adaptiveMemory.constructSolutionFromTours();
	}
}
