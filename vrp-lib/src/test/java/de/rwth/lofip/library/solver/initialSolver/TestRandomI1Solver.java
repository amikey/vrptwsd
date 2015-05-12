package de.rwth.lofip.library.solver.initialSolver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.rwth.lofip.cli.util.ReadAndWriteUtils;
import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.AdaptiveMemoryTabuSearch;
import de.rwth.lofip.library.solver.metaheuristics.util.AdaptiveMemory;

public class TestRandomI1Solver {
	
	private RandomI1Solver solver = new RandomI1Solver();
	private List<VrpProblem> problems = new LinkedList<VrpProblem>();
	private List<SolutionGot> solutions = new LinkedList<SolutionGot>();
	private List<SolutionGot> solutions2 = new LinkedList<SolutionGot>();
	
	@Test
	public void testRandomI1Solver() throws IOException {
		solver = new RandomI1Solver();
		solver.initialiseSolverWith(ReadAndWriteUtils.readSolomonProblemC101().get(0));		
				
		List<Customer> seedCustomers = solver.randomlySelectSeedCustomers();		
		assertEquals(solver.getNumberOfSeedCustomers(), seedCustomers.size());
		
		assertEquals(solver.getNumberOfSeedCustomers(), solver.constructToursWithSeedCustomers().getNumberOfTours());
		
		solver.insertRemainingCustomersIntoTours();
		
		assertEquals(100, solver.getSolution().getCustomersInTours().size());
	}
	
	
	@Test
	public void testThatTwoRunsOfRandomI1SolverProduceTheSameResults() throws IOException {		
		problems = ReadAndWriteUtils.readSolomonProblemRC101();		
		
		RandomI1Solver.setSeedTo(1);
		GreedyInsertion.setSeedTo(1);
		solveProblemsWithRandomI1Solver(solutions);
		
		RandomI1Solver.setSeedTo(1);
		GreedyInsertion.setSeedTo(1);
		solveProblemsWithRandomI1Solver(solutions2);
		
		for (int i = 0; i < solutions.size(); i++) {
			System.out.println("Solution: " + solutions.get(i).getAsTupel());
			System.out.println("Solution2: " + solutions2.get(i).getAsTupel());
			assertEquals(true,solutions.get(i).equals(solutions2.get(i)));
		}
	}
	
	private void solveProblemsWithRandomI1Solver(List<SolutionGot> solutionsTemp) {		
		for (VrpProblem problem : problems) {
			System.out.println("SOLVING PROBLEM " + problem.getDescription());
			
			RandomI1Solver i1Solver = new RandomI1Solver();			
			SolutionGot solution = i1Solver.solve(problem);
			solutionsTemp.add(solution);			
		}				
	}	
	
}
