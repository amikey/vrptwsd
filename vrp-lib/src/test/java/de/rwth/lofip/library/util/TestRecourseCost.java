package de.rwth.lofip.library.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.util.testing.AdaptiveMemoryUtils;

public class TestRecourseCost {
	
	SolutionGot solution;
	
	@Test
	public void testRecourseCostOfModifiedC101Solution() throws IOException {
		givenC101Solution();
		thenRecourseCostShouldNotBeEqualToZero();
	}

	private void givenC101Solution() throws IOException {
		VrpProblem problem = ReadAndWriteUtils.readModifiedSolomonProblems().get(0);
		AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
		AdaptiveMemoryUtils.setParametersInAMTSforTesting(adaptiveMemoryTabuSearch);
		solution = adaptiveMemoryTabuSearch.solve(problem);
	}

	private void thenRecourseCostShouldNotBeEqualToZero() {
		System.out.println(solution.getAsTupel());
		if (solution.getExpectedRecourseCost().getRecourseCost() == 0.0)
			throw new RuntimeException("Recourse Cost ist 0. Sollte nicht sein");
	}
	
	@Test
	public void testeKennzahlen() {
		throw new RuntimeException("Implementiere in Diss / Bericht aufgeführte Kennzahlen in Recourse");
	}

}
