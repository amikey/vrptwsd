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
	private long timeNeeded;
	
	private int numberOfDifferentInitialSolutions = 20;
	private int maximalNumberOfIterationsTabuSearch = 0;
	private int maximalNumberOfIterationsWithoutImprovementTabuSearch = 50;
	private int maximalNumberOfCallsToAdaptiveMemory = 0;
	private int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = 50;
	
	private int seedI1 = 14;
	private int seedGI = 14;
	private int seedAM = 14;
	
	private int numberOfExperiments = 100;
	
	@Test
	public void TestAdaptiveMemorySearchOnAllSolomonInstances() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(ReadAndWriteUtils.getOutputFile()));
//		System.setOut(out);
		problems = ReadAndWriteUtils.readSolomonProblems();
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnAllModifiedSolomonInstances() throws IOException {
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		processProblems();
		printProblemsModifiedSolomonInstances();
	}

	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC1XX() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemC1XX();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC2XX() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemC2XX();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC202() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemC202();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceC204() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemC204();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceR1XX() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemR1XX();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceR2XX() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemR2XX();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC1XX() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC1XX();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC101() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC101AsList();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC102() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC102AsList();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC103() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC103AsList();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC104() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC104AsList();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC105() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC105AsList();	
		processProblems();
		printProblems();
	}

	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC2XX() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC2XX();	
		processProblems();
		printProblems();
	}
	
	private void increaseParameters() {
		seedI1++;
		seedGI++;
		seedAM++;
	}
	
	private void processProblems() throws IOException {
		for (int i = 1; i < numberOfExperiments; i++) {
			increaseParameters();
			solveProblemsWithAdaptiveMemorySolver();	
		}		
	}
	
	private void printProblems() throws IOException {
		ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions, timeNeeded,
				numberOfDifferentInitialSolutions, 
				maximalNumberOfIterationsTabuSearch, 
				maximalNumberOfIterationsWithoutImprovementTabuSearch, 
				maximalNumberOfCallsToAdaptiveMemory,
				maximalNumberOfCallsWithoutImprovementToAdaptiveMemory,
				seedI1, seedGI, seedAM);
	}
	
	private void printProblemsModifiedSolomonInstances() throws IOException {
		ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch - ModifiedProblems",solutions, timeNeeded,
				numberOfDifferentInitialSolutions, 
				maximalNumberOfIterationsTabuSearch, 
				maximalNumberOfIterationsWithoutImprovementTabuSearch, 
				maximalNumberOfCallsToAdaptiveMemory,
				maximalNumberOfCallsWithoutImprovementToAdaptiveMemory,
				seedI1, seedGI, seedAM);
	}
	
	public List<SolutionGot> solveProblemsWithAdaptiveMemorySolver(
			List<VrpProblem> vrpProblems,
			List<SolutionGot> solutionsTemp, 
			int numberOfDifferentInitialSolutions,
			int maximalNumberOfIterationsTabuSearch,
			int maximalNumberOfIterationsWithoutImprovementTabuSearch,
			int maximalNumberOfCallsToAdaptiveMemory,
			int maximalNumberOfCallsWithoutImprovementToAdaptiveMemory,
			int seedI1,
			int seedGI,
			int seedAM) {
		
		this.problems = vrpProblems;
		this.solutions = solutionsTemp;
		this.numberOfDifferentInitialSolutions = numberOfDifferentInitialSolutions;
		this.maximalNumberOfIterationsTabuSearch = maximalNumberOfIterationsTabuSearch;
		this.maximalNumberOfIterationsWithoutImprovementTabuSearch = maximalNumberOfIterationsWithoutImprovementTabuSearch; 
		this.maximalNumberOfCallsToAdaptiveMemory = maximalNumberOfCallsToAdaptiveMemory;
		this.maximalNumberOfCallsWithoutImprovementToAdaptiveMemory = maximalNumberOfCallsWithoutImprovementToAdaptiveMemory;
		this.seedI1 = seedI1;
		this.seedGI = seedGI;
		this.seedAM = seedAM;
		
		solveProblemsWithAdaptiveMemorySolver();
		return solutions;
	}
	
	private void solveProblemsWithAdaptiveMemorySolver() {
		long startTime = System.nanoTime();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());
						
			AdaptiveMemoryTabuSearch.setSeeds(seedI1, seedGI, seedAM);
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
			adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsTabuSearch(maximalNumberOfIterationsTabuSearch);
			adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsWithoutImprovementTabuSerach(maximalNumberOfIterationsWithoutImprovementTabuSearch);
			adaptiveMemoryTabuSearch.setNumberOfInitialSolutions(numberOfDifferentInitialSolutions);
			adaptiveMemoryTabuSearch.setMaximalNumberOfCallsToAdaptiveMemory(maximalNumberOfCallsToAdaptiveMemory);
			adaptiveMemoryTabuSearch.setMaximalNumberOfCallsWithoutImprovementToAdaptiveMemory(maximalNumberOfCallsWithoutImprovementToAdaptiveMemory);
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}
		long endTime = System.nanoTime();
		timeNeeded = (endTime - startTime) / 1000 / 1000 / 1000 / 60;
	}	
	
}
