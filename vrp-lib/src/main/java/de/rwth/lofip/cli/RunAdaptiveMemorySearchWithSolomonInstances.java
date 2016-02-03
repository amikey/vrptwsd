package de.rwth.lofip.cli;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.util.TourMatching;

public class RunAdaptiveMemorySearchWithSolomonInstances {

	protected List<VrpProblem> problems = new LinkedList<VrpProblem>();
	protected List<SolutionGot> solutions = new LinkedList<SolutionGot>();
	protected long timeNeeded;
	
	protected int numberOfExperiments = 100;
	
	@Test
	public void TestAdaptiveMemorySearchOnAllSolomonInstances() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(ReadAndWriteUtils.getOutputFile()));
//		System.setOut(out);
		Parameters.setOutputDirectory("\\ErgebnisseSolomon100\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		problems = ReadAndWriteUtils.readOriginalSolomonProblems();
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnAllModifiedSolomonInstances() throws IOException {
		Parameters.setOutputDirectory("\\ErgebnisseSolomonModified\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		problems = ReadAndWriteUtils.readModifiedSolomonProblems();
		processProblems();
		printProblemsModifiedSolomonInstances();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnAllSolomon200Instances() throws IOException {
		Parameters.setOutputDirectory("\\ErgebnisseSolomon200\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		problems = ReadAndWriteUtils.readSolomonProblems200();
		processProblems();
		printProblemsModifiedSolomonInstances();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnAllGering400Instances() throws IOException {
		Parameters.setOutputDirectory("\\ErgebnisseGehring400\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		File dir = new File(ReadAndWriteUtils.getInputDirectoryForGehring400Files());	
		problems = ReadAndWriteUtils.createListOfProblemsFromInputDirectory(dir);
		processProblems();
		printProblemsModifiedSolomonInstances();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnAllGering600Instances() throws IOException {
		Parameters.setOutputDirectory("\\ErgebnisseGehring600\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		File dir = new File(ReadAndWriteUtils.getInputDirectoryForGehring600Files());	
		problems = ReadAndWriteUtils.createListOfProblemsFromInputDirectory(dir);
		processProblems();
		printProblemsModifiedSolomonInstances();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnAllGering800Instances() throws IOException {
		Parameters.setOutputDirectory("\\ErgebnisseGehring800\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		File dir = new File(ReadAndWriteUtils.getInputDirectoryForGehring800Files());	
		problems = ReadAndWriteUtils.createListOfProblemsFromInputDirectory(dir);
		processProblems();
		printProblemsModifiedSolomonInstances();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnAllGering1000Instances() throws IOException {
		Parameters.setOutputDirectory("\\ErgebnisseGehring1000\\");
		Parameters.setPublishSolutionAtEndOfTabuSearch(true);
		File dir = new File(ReadAndWriteUtils.getInputDirectoryForGehring1000Files());	
		problems = ReadAndWriteUtils.createListOfProblemsFromInputDirectory(dir);
		processProblems();
		printProblemsModifiedSolomonInstances();
	}
		
	
	@Test
	public void TestAdaptiveMemorySearchOnAllSolomon200_1000Instances() throws IOException {
		problems = ReadAndWriteUtils.readSolomonProblems200_1000();
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
	public void TestAdaptiveMemorySearchOnSolomonInstanceC101() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemC101();	
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
	public void TestAdaptiveMemorySearchOnSolomonInstanceR201() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemR201();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceR208() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemR208();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceR205() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemR205();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceR209() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemR209();	
		processProblems();
		printProblems();
	}
	
	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceR210() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemR210();	
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
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC106() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC106AsList();	
		processProblems();
		printProblems();
	}

	@Test
	public void TestAdaptiveMemorySearchOnSolomonInstanceRC2XX() throws IOException {			
		problems = ReadAndWriteUtils.readSolomonProblemRC2XX();	
		processProblems();
		postProcessProblemsWithTourMatchingAlgorithm();
		printProblems();
	}
	
	protected void processProblems() throws IOException {
		for (int i = 1; i <= numberOfExperiments; i++) {
			if (!Parameters.isRunningTimeReached()) {
//				increaseParameters();
				solveProblemsWithAdaptiveMemorySolver();
			}
		}		
	}
	
	public List<SolutionGot> solveProblemsWithAdaptiveMemorySolver(List<VrpProblem> vrpProblems, List<SolutionGot> solutionsTemp) throws IOException {
		this.problems = vrpProblems;
		this.solutions = solutionsTemp;
		
		solveProblemsWithAdaptiveMemorySolver();
		return solutions;
	}
	
	private void solveProblemsWithAdaptiveMemorySolver() throws IOException {
		long startTime = System.nanoTime();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());						
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutions.add(solution);			
		}
		long endTime = System.nanoTime();
		timeNeeded = (endTime - startTime) / 1000 / 1000 / 1000 / 60;
	}	
	
	private void postProcessProblemsWithTourMatchingAlgorithm() {
		for (int i = 0; i < solutions.size(); i++) {
			SolutionGot solution = solutions.get(i);
			SolutionGot solution2 = new TourMatching().matchToursToGots(solution);
			solutions.set(i, solution2);
		}
	}
	
	protected void setNumberOfExperimentsTo(int i) {
		numberOfExperiments = i;
	}


	// not used any more
	
	protected void printProblems() throws IOException {
//		ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch",solutions, timeNeeded,				
//				Parameters.getMaximalNumberOfIterationsTabuSearch(), 
//				Parameters.getMaximalNumberOfIterationsWithoutImprovementTabuSearch(), 
//				seedI1, seedGI, seedAM);
	}
	
	private void printProblemsModifiedSolomonInstances() throws IOException {
//		ReadAndWriteUtils.printResultsToFile("AdaptiveMemorySearch - ModifiedProblems",solutions, timeNeeded,
//				numberOfDifferentInitialSolutions, 
//				Parameters.getMaximalNumberOfIterationsTabuSearch(), 
//				Parameters.getMaximalNumberOfIterationsWithoutImprovementTabuSearch(),
//				maximalNumberOfCallsToAdaptiveMemory,
//				maximalNumberOfCallsWithoutImprovementToAdaptiveMemory,
//				seedI1, seedGI, seedAM);
	}
	
}
