package de.rwth.lofip.cli;

import static org.junit.Assert.assertFalse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.metaheuristics.UberAdaptiveMemoryTabuSearch;

public class RunUberAdaptiveMemorySearchWithSolomonInstances {

	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> solutions = new LinkedList<SolutionGot>();
	private long timeNeeded = 0;
	
	private List<List<SolutionGot>> solutionsOfAllRuns = new LinkedList<List<SolutionGot>>();	
	
	private int maximalNumberOfIterationsTabuSearch = 50;
	private int numberOfDifferentInitialSolutions = 5;
	
	private int seedI1 = 1;
	private int seedGI = 1;
	private int seedAM = 1;
	
	private double minutesToRunPerInstance = 20;
	private int numberOfExperiments = 30;
	
	@Test
	public void RunAdaptiveMemorySearchSeveralTimesWithDifferentParameters() throws IOException {
		problems = ReadAndWriteUtils.readSolomonProblems();
		FileOutputStream outputStream = ReadAndWriteUtils.openOutputFile("ResultsOverAllRuns");
		FileOutputStream outputStreamCluster = ReadAndWriteUtils.openOutputFile("ResultsAllRunsCluster");
		
		for (int i = 1; i < numberOfExperiments; i++) {
			increaseParameters();
			solutions = new LinkedList<SolutionGot>();
			solveProblemsWithUberAdaptiveMemorySolver();
			List<SolutionGot> dummySolutions = new LinkedList<SolutionGot>(); //bad hack, improve! siehe eine Zeile weiter unten
			ReadAndWriteUtils.printResultsToFile("UberAdaptiveMemorySearch",solutions, dummySolutions, timeNeeded,
					numberOfDifferentInitialSolutions, maximalNumberOfIterationsTabuSearch, seedI1, seedGI, seedAM);
			solutionsOfAllRuns.add(solutions);			
			ReadAndWriteUtils.printResultsOverSeveralRunsToFile(solutionsOfAllRuns, outputStream);
			
			ReadAndWriteUtils.printAverageValues(solutions, outputStreamCluster);
			IOUtils.write("Start-Parameter: " + 
					"initialNumberOfDifferentInitialSolutions " + numberOfDifferentInitialSolutions + " " +
					"initialNumberOfIterationsTabuSearch " + maximalNumberOfIterationsTabuSearch + " " +
					"seedI1 " + seedI1 + " " +
					"seedGI " + seedGI + " " +
					"seedAM " + seedAM + "; "
					, outputStreamCluster);
		}
	}
	
	private void increaseParameters() {
		maximalNumberOfIterationsTabuSearch += 10;
		numberOfDifferentInitialSolutions++;
		seedI1++;
		seedGI++;
		seedAM++;
	}

	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void TestAdaptiveMemorySearchOnAllSolomonInstances() throws IOException {			
		PrintStream out = new PrintStream(new FileOutputStream(ReadAndWriteUtils.getOutputFile()));
		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblems();
		solveProblemsWithUberAdaptiveMemorySolver();		
		ReadAndWriteUtils.printResultsToFile("UberAdaptiveMemorySearch",solutions, timeNeeded);
	}
	
	@Test
	public void TestUberAdaptiveMemorySearchOnSolomonInstanceRC101() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblemRC101();		
		solveProblemsWithUberAdaptiveMemorySolver();	
		assertFalse(solutions.get(0) == null);
		ReadAndWriteUtils.printResultsToFile("UberAdaptiveMemorySearch",solutions,timeNeeded);
	}
	
	@Test
	public void TestUberAdaptiveMemorySearchOnSolomonInstanceC101() throws IOException {			
//		PrintStream out = new PrintStream(new FileOutputStream(getOutputFile()));
//		System.setOut(out);
		
		problems = ReadAndWriteUtils.readSolomonProblemC101();		
		solveProblemsWithUberAdaptiveMemorySolver();	
		assertFalse(solutions.get(0) == null);
//		ReadAndWriteUtils.printResultsToFile("UberAdaptiveMemorySearch",solutions,timeNeeded);
	}
	
	private void solveProblemsWithUberAdaptiveMemorySolver() {				
		for (VrpProblem problem : problems) {
			long startTime = System.currentTimeMillis();
			long endTime = (long) (startTime + minutesToRunPerInstance * 60 * 1000);
			timeNeeded += minutesToRunPerInstance;
			
			System.out.println("SOLVING PROBLEM " + problem.getDescription());			
			
			UberAdaptiveMemoryTabuSearch uamts = new UberAdaptiveMemoryTabuSearch(endTime);
			uamts.setParameters(numberOfDifferentInitialSolutions, maximalNumberOfIterationsTabuSearch);
			uamts.setSeeds(seedI1, seedGI, seedAM);
			SolutionGot solution = uamts.solve(problem);
			solutions.add(solution);								
		}		
	}	
}
