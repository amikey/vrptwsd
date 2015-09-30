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
	private Random rand = new Random(seed); 
	
	private int numberOfSeedCustomers;
		
	private VrpProblem vrpProblem;
	protected SolutionGot solution;
	
	protected List<Customer> remainingCustomers = new ArrayList<Customer>();
	private List<Customer> seedCustomers = new ArrayList<Customer>();

	public RandomI1Solver() {
		seed++;
		rand = new Random(seed); 
	}
	
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
		return solution;
	}
	
	protected void initialiseSolverWith(VrpProblem problem) {
		vrpProblem = problem;
		solution = new SolutionGot(vrpProblem);
		remainingCustomers.addAll(vrpProblem.getCustomers());
	}

	protected List<Customer> randomlySelectSeedCustomers() {
		numberOfSeedCustomers = vrpProblem.getMinimalNumberOfVehiclesWrtDemand();
		if (numberOfSeedCustomers > remainingCustomers.size()) {
			System.out.println("VrpProblem: " + vrpProblem.print());
			System.out.println("vrpProblem.getMinimalNumberOfVehiclesWrtDemand(): " + vrpProblem.getMinimalNumberOfVehiclesWrtDemand());
			System.out.println(printRemainingCustomers());
			throw new RuntimeException("numberOfSeedCustomers > remainingCustomers.size()-1");
		}
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
//		System.out.println("Lösung: " + solution.getAsTupel());
//		List<Long> listCustomers = new ArrayList<Long>();
//		listCustomers.add((long) 16);
//		listCustomers.add((long) 12);
//		listCustomers.add((long) 14);
//		listCustomers.add((long) 87);
//		listCustomers.add((long) 97);
//		
//		boolean debugging = true;
//		if (remainingCustomers.size() > listCustomers.size())
//			debugging = false;
//		else {
//			for(int i = 0; i < remainingCustomers.size(); i++) {			
//				System.out.print(remainingCustomers.get(i).getCustomerNo() + ";");
//				if (remainingCustomers.get(i).getCustomerNo() != listCustomers.get(i))
//					debugging = false;
//			}
//		}
//		System.out.print("\n");
//		
//		if (debugging)
//			System.out.println("DEBUGGING!");
		
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
