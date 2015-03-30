package de.rwth.lofip.library.solver.metaheuristics;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;

public class AdaptiveMemoryTabuSearchTest {
	
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> solutions = new LinkedList<SolutionGot>();
	private List<SolutionGot> solutions2 = new LinkedList<SolutionGot>();
	private long timeNeeded;
	
	private int maximalNumberOfIterationsTabuSearch = 7;
	private int numberOfDifferentInitialSolutions = 3;
	private int maximalNumberOfCallsToAdaptiveMemory = 15;
	
	@Test
	public void testThatTwoRunsOfAMSearchProduceTheSameResults() throws IOException {
			
		problems = ReadAndWriteUtils.readSolomonProblemRC101();		
				
		solveProblemsWithAdaptiveMemoryTabuSeach(solutions);		

		maximalNumberOfIterationsTabuSearch = 7;
		numberOfDifferentInitialSolutions = 3;
		maximalNumberOfCallsToAdaptiveMemory = 20;
		
		solveProblemsWithAdaptiveMemoryTabuSeach(solutions2);
		
		for (int i = 0; i < solutions.size(); i++) {
			solutions.get(i).printSolutionCost();
			solutions.get(i).printVehicleCount();
			solutions2.get(i).printSolutionCost();
			solutions2.get(i).printVehicleCount();
			assertEquals(true,isSolutionOneWorseThanSolutionTwo(solutions.get(i),solutions2.get(i)));
		}
	}
	
	private void solveProblemsWithAdaptiveMemoryTabuSeach(List<SolutionGot> solutionsTemp) {
		long startTime = System.nanoTime();
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());
			
			AdaptiveMemoryTabuSearch adaptiveMemoryTabuSearch = new AdaptiveMemoryTabuSearch();
			adaptiveMemoryTabuSearch.resetSeeds();
			adaptiveMemoryTabuSearch.setMaximalNumberOfIterationsTabuSearch(maximalNumberOfIterationsTabuSearch);
			adaptiveMemoryTabuSearch.setNumberOfInitialSolutions(numberOfDifferentInitialSolutions);
			adaptiveMemoryTabuSearch.setMaximalNumberOfCallsToAdaptiveMemory(maximalNumberOfCallsToAdaptiveMemory);
			SolutionGot solution = adaptiveMemoryTabuSearch.solve(problem);
			solutionsTemp.add(solution);			
		}
		long endTime = System.nanoTime();
		timeNeeded = (endTime - startTime) / 1000 / 1000 / 1000 / 60;
	}	
	
	private Object isSolutionOneWorseThanSolutionTwo(SolutionGot solutionGot, SolutionGot solutionGot2) {
		if (solutionGot.getTotalDistance() >= solutionGot2.getTotalDistance())
			return true;
		if (solutionGot.getVehicleCount() > solutionGot2.getVehicleCount())
			return true;
		return false;
	}
	
}
