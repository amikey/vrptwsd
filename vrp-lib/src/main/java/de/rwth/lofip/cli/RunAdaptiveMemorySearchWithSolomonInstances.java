package de.rwth.lofip.cli;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
	private long timeNeeded;
	
	private int maximalNumberOfIterationsTabuSearch = 20;
	private int numberOfDifferentInitialSolutions = 5;
	private int maximalNumberOfCallsToAdaptiveMemory = 20;
	
	@Test
	public void TestAdaptiveMemorySearchOnAllSolomonInstances() throws IOException {			
		PrintStream out = new PrintStream(new FileOutputStream(ReadAndWriteUtils.getOutputFile()));
		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblems();
		solveProblemsWithAdaptiveMemorySolver();		
		ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions, timeNeeded);
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC101() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblemC101();		
		solveProblemsWithAdaptiveMemorySolver();		
		ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions,timeNeeded);
	}
	
	private void solveProblemsWithAdaptiveMemorySolver() {
		long startTime = System.nanoTime();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());
			
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
			adaptiveMemoryTabuSearch.resetSeeds();
			adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsTabuSearch(maximalNumberOfIterationsTabuSearch);
			adaptiveMemoryTabuSearch.setNumberOfInitialSolutions(numberOfDifferentInitialSolutions);
			adaptiveMemoryTabuSearch.setMaximalNumberOfCallsToAdaptiveMemory(maximalNumberOfCallsToAdaptiveMemory);
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}
		long endTime = System.nanoTime();
		timeNeeded = (endTime - startTime) / 1000 / 1000 / 1000 / 60;
	}	
	
}
