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
	
	private int maximalNumberOfIterationsTabuSearch = 50;
	private int maximalNumberOfIterationsWithoutImprovementTabuSearch = 50;
	private int numberOfDifferentInitialSolutions = 10;
	private int maximalNumberOfCallsToAdaptiveMemory = 50;
	
	private int seedI1 = 1;
	private int seedGI = 1;
	private int seedAM = 1;
	
	private int numberOfExperiments = 100;
	
	@Test
	public void TestAdaptiveMemorySearchOnAllSolomonInstances() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(ReadAndWriteUtils.getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblems();
		for (int i = 1; i < numberOfExperiments; i++) {
			increaseParameters();
			solveProblemsWithAdaptiveMemorySolver();		
			ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions, timeNeeded,
					numberOfDifferentInitialSolutions, maximalNumberOfIterationsTabuSearch, seedI1, seedGI, seedAM);
		}		
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC202() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblemC202();	
		for (int i = 1; i < numberOfExperiments; i++) {
			increaseParameters();
			solveProblemsWithAdaptiveMemorySolver();		
			ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions, timeNeeded,
					numberOfDifferentInitialSolutions, maximalNumberOfIterationsTabuSearch, seedI1, seedGI, seedAM);
		}
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC204() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblemC204();	
		for (int i = 1; i < numberOfExperiments; i++) {
			increaseParameters();
			solveProblemsWithAdaptiveMemorySolver();		
			ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions, timeNeeded,
					numberOfDifferentInitialSolutions, maximalNumberOfIterationsTabuSearch, seedI1, seedGI, seedAM);
		}
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC206() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblemC206();	
		for (int i = 1; i < numberOfExperiments; i++) {
			increaseParameters();
			solveProblemsWithAdaptiveMemorySolver();		
			ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions, timeNeeded,
					numberOfDifferentInitialSolutions, maximalNumberOfIterationsTabuSearch, seedI1, seedGI, seedAM);
		}
	}
	
	private void increaseParameters() {
		seedI1++;
		seedGI++;
		seedAM++;
	}
	
	private void solveProblemsWithAdaptiveMemorySolver() {
		long startTime = System.nanoTime();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());
			
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
			adaptiveMemoryTabuSearch.setSeeds(seedI1, seedGI, seedAM);
			adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsTabuSearch(maximalNumberOfIterationsTabuSearch);
			adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsWithoutImprovementTabuSerach(maximalNumberOfIterationsWithoutImprovementTabuSearch);
			adaptiveMemoryTabuSearch.setNumberOfInitialSolutions(numberOfDifferentInitialSolutions);
			adaptiveMemoryTabuSearch.setMaximalNumberOfCallsToAdaptiveMemory(maximalNumberOfCallsToAdaptiveMemory);
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}
		long endTime = System.nanoTime();
		timeNeeded = (endTime - startTime) / 1000 / 1000 / 1000 / 60;
	}	
	
}
