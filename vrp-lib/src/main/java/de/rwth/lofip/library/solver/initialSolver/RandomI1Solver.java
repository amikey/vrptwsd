package de.rwth.lofip.library.solver.initialSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.SolverInterfaceGot;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.util.VrpUtils;

public class RandomI1Solver implements SolverInterfaceGot {
	
	private static int seed = 0;
	private static Random rand = new Random(seed); 
	
	private int numberOfSeedCustomers = 0;
		
	private VrpProblem vrpProblem = null;
	protected SolutionGot solution = null;
	
	protected List<Customer> remainingCustomers = new ArrayList<Customer>();
	private List<Customer> seedCustomers = new ArrayList<Customer>();
	
	public SolutionGot solve(Tour tour) {
		VrpProblem problem = VrpUtils.constructVrpProblemFromTour(tour);
		return solve(problem);
	}
	
	@Override
	public SolutionGot solve(VrpProblem problem) {
		initialiseSolverWith(problem);		
		randomlySelectSeedCustomers();
		constructToursWithSeedCustomers();
		insertRemainingCustomersIntoTours();
		if (solution == null) 
			throw new RuntimeException("solution ist null");
		if (solution.getNumberOfTours() > problem.getCustomerCount())
			throw new RuntimeException("solution verwendet mehr Touren als Kunden da sind.");
		return solution;
	}
	
	protected void initialiseSolverWith(VrpProblem problem) {
		vrpProblem = problem;		
		solution = new SolutionGot(vrpProblem);
		if (vrpProblem == null)
			throw new RuntimeException("vrpProblem = null");
		remainingCustomers.addAll(vrpProblem.getCustomers());
	}

	protected List<Customer> randomlySelectSeedCustomers() {
		numberOfSeedCustomers = vrpProblem.getMinimalNumberOfVehiclesWrtDemand();
		
		if (numberOfSeedCustomers == 0)
			throw new RuntimeException("numberOfSeedCustomers ist 0; VrpProblem: " + vrpProblem.print());
		if (numberOfSeedCustomers > remainingCustomers.size()) 
			throw new RuntimeException("numberOfSeedCustomers > remainingCustomers.size()-1");
		
		for (int m = 1; m <= numberOfSeedCustomers; m++) {
			int rand = generateRandomNumber(remainingCustomers.size()-1,0);
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

	private int generateRandomNumber(int max, int min) {
	    int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;	    	
	}

	public static void setSeedTo(int i) {
		seed = i;
	}
	
	public static void resetRandomElementWithSeed(int i) {
		rand = new Random(i);  
	}

	@Override
	public String getDescriptiveName() {
		return "RandomI1Solver";
	}
	
	//UTILITIES
	
	private String printRemainingCustomers() {
		String s = "(";
		for (Customer c : getRemainingCustomers())
			s += "" + c.getCustomerNo() + " ";
		s += ")";
		return s;
	}


}
