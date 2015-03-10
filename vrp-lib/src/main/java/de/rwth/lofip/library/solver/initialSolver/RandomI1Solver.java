package de.rwth.lofip.library.solver.initialSolver;

import java.util.ArrayList;
import java.util.List;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.util.RandomUtils;

public class RandomI1Solver implements SolverInterfaceGot {
	
	private final int numberOfSeedCustomers = 10;
		
	private VrpProblem vrpProblem;
	private SolutionGot solution;
	
	private List<Customer> remainingCustomers = new ArrayList<Customer>();
	private List<Customer> seedCustomers = new ArrayList<Customer>();

	@Override
	public SolutionGot solve(VrpProblem problem) {
		initialiseSolverWith(problem);		
		randomlySelectSeedCustomers();
		constructToursWithSeedCustomers();
		insertRemainingCustomersIntoTours();
		return solution;
	}
	
	protected void initialiseSolverWith(VrpProblem problem) {
		vrpProblem = problem;
		solution = new SolutionGot(vrpProblem);
		remainingCustomers.addAll(vrpProblem.getCustomers());
	}

	protected List<Customer> randomlySelectSeedCustomers() {
		if (numberOfSeedCustomers > remainingCustomers.size()-1)
			throw new RuntimeException("numberOfSeedCustomers > remainingCustomers.size()-1");
		for (int m = 1; m <= numberOfSeedCustomers; m++) {
			int rand = RandomUtils.generateRandomNumber(remainingCustomers.size()-1,0);
			seedCustomers.add(remainingCustomers.get(rand));
			remainingCustomers.remove(rand);
		}
		return seedCustomers;
	}

	protected SolutionGot constructToursWithSeedCustomers() {
		for (Customer c : seedCustomers) {
			solution.createNewTourWithCustomer(c);
		}
		return solution;			
	}

	protected void insertRemainingCustomersIntoTours() {
		GreedyInsertion gi = new GreedyInsertion();
		solution = gi.insertCustomers(solution, remainingCustomers);
	}

	public int getNumberOfSeedCustomers() {
		return numberOfSeedCustomers;
	}

	public SolutionGot getSolution() {
		return solution;
	}

	public List<Customer> getRemainingCustomers() {
		return remainingCustomers;
	}

	
	
	

}
