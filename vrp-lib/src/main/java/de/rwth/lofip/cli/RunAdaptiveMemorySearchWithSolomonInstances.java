package de.rwth.lofip.cli;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;

public class RunAdaptiveMemorySearchWithSolomonInstances {

	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> solutions = new LinkedList<SolutionGot>();

	@Test
	public void TestAdaptiveMemorySearchOnAllSolomonInstances() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblemRC101();
		solveProblemsWithAdaptiveMemorySolver();		
		ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions);
	}
	
	private void solveProblemsWithAdaptiveMemorySolver() {
		AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
		for (VrpProblem problem : problems) {
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}
	}	
	
}
