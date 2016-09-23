package de.rwth.lofip.library.solver.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.VrpProblem;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
import de.rwth.lofip.library.solver.initialSolver.RandomI1Solver;
import de.rwth.lofip.library.solver.insertions.GreedyInsertion;
import de.rwth.lofip.library.solver.metaheuristics.TSWithTargetVehicleNumber;
import de.rwth.lofip.library.solver.metaheuristics.TSWithTourElimination;
import de.rwth.lofip.library.solver.metaheuristics.TabuSearchForElementWithTours;
import de.rwth.lofip.library.util.RecourseCost;

public class SolutionGotUtils {
	
	private static Random random = new Random(1000);
	
	public static boolean isSolutionFeasibleWrtDemand(SolutionGot solution){
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtDemand(tour))
				return false;
		}
		return true;
	}
	
	public static boolean isSolutionFeasibleWrtTW(SolutionGot solution){
		for (Tour tour : solution.getTours()) {
			if (!TourUtils.isTourFeasibleWrtTW(tour))
				return false;
		}
		return true;
	}
	
	public static String createStringForCustomersServedByNumberOfVehicles(ElementWithTours bestOverallSolution) {
		String s = "";
//		Iterator<Entry<Integer, Integer>> it = ((SolutionGot) bestOverallSolution).getExpectedRecourseCost().getToursNeededToServeNumberOfCustomers().entrySet().iterator();
		
		RecourseCost rc = ((SolutionGot) bestOverallSolution).getExpectedRecourseCost();
		Iterator<Entry<Integer, Integer>> it = rc.getToursNeededToServeNumberOfCustomers().entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Integer> pair = it.next();
	        //the following line is totally not necessary and just for better readiness 
	        int currentlyExaminedNumberOfVehicles = pair.getKey();
	        int numberOfCustomersServedByCurrentlyExaminedNumberOfVehicles = pair.getValue();
	        s += numberOfCustomersServedByCurrentlyExaminedNumberOfVehicles + ";";
	    }
	    return s;
	}
	
	public static SolutionGot createSolutionWithVehicleGoalNumber(SolutionGot solution, int vehicleGoalNumber) throws IOException {
		int iterations = 0;
		int numberOfToursToBeRemoved = 1;		
		while (!(solution.getNumberOfTours() == vehicleGoalNumber) && iterations < 10) {
			
			iterations++;
			System.out.println("createSolutionWithTargetVehicleNumber " + vehicleGoalNumber + "; iteration = " + iterations);
			
			if (numberOfToursToBeRemoved < solution.getNumberOfTours())
				numberOfToursToBeRemoved++;
			else
				numberOfToursToBeRemoved = 1;
//				solution.addTourToLastOrNewGot(new Tour(solution.getVrpProblem().getDepot(), solution.getVrpProblem().getNewVehicle()));
			System.out.println("Starting perturb. NumberOf Tours that shall be removed: " + numberOfToursToBeRemoved + "; NumberOfToursInSolution: " + solution.getNumberOfTours());
			perturb(solution, numberOfToursToBeRemoved);
			System.out.println("Finished perturb");
			System.out.println("#KFZ nach Perturb:" + solution.getVehicleCount());
			if (solution.getVehicleCount() > vehicleGoalNumber) {
				System.out.println("#KFZ nach Perturb:" + solution.getVehicleCount() + "größer als vehicleTargetNumber " + vehicleGoalNumber);
				solution = findSolutionWithTargetNumber(solution, vehicleGoalNumber);
			}
		}	
		
		// erstelle Lösung mit stichfahrten und finde mit TS targetVehicleNumber		
		if (solution.getVehicleCount() != vehicleGoalNumber) {
			System.out.println("#KFZ " + solution.getVehicleCount() + " ungleich vehicleTargetNumber " + vehicleGoalNumber);
			solution = createSolutionWithSingleTours(solution.getVrpProblem(), vehicleGoalNumber);
			solution = findSolutionWithTargetNumber(solution, vehicleGoalNumber);
		}			
	
		if (solution.getNumberOfTours() != vehicleGoalNumber)
			throw new RuntimeException("No Solution with " + vehicleGoalNumber + " Vehicles found.");
		return solution;
	}
	
	public static SolutionGot createSolutionWithSingleTours(VrpProblem problem, int vehicleGoalNumber) {
		List<Customer> customers = new ArrayList<Customer>(problem.getCustomers());
		Collections.shuffle(customers);
		SolutionGot	solution = new SolutionGot(problem);
		Iterator<Customer> iterator = customers.iterator();
		for (int i = 0; i < vehicleGoalNumber; i++) {
			Customer c = iterator.next();
			iterator.remove();
			Tour t = new Tour(problem.getDepot(), problem.getNewVehicle());
			solution.addTourToLastOrNewGot(t);
			t.addCustomer(c);
		}
		int customersSize = customers.size();
		System.out.println("customers size: " + customersSize);
		int problemCustomerCount = problem.getCustomerCount();
		System.out.println("problemCustomerCount: " + problemCustomerCount);
		if (customersSize != problemCustomerCount - vehicleGoalNumber)
			throw new RuntimeException("Anzahl übrig gebliebener Kunden nicht richtig. Sollte " + (problem.getCustomerCount() - vehicleGoalNumber) + " sein, aber war " + customers.size());
		
		//add remaining customers with Greedy
		GreedyInsertion gi = new GreedyInsertion();
		//make list from set of customers
		solution = gi.insertCustomers(solution,  customers);
		if (solution.getNumberOfTours() < vehicleGoalNumber)
			throw new RuntimeException("createSolutionWithSingleTours created solution with " + solution.getNumberOfTours() + " although it should create a solution with at least " + vehicleGoalNumber);
		return solution;
	}

	private static SolutionGot perturb(SolutionGot solution2, int numberOfToursToBeRemoved) {
		System.out.println("Starting to generate ints");
		int[] ints;
		if (isNumberOfToursInSolutionIsMuchLargerThanNumberOfToursToBeRemoved(solution2, numberOfToursToBeRemoved))
			ints = random.ints(0, solution2.getNumberOfTours()-1).distinct().limit(numberOfToursToBeRemoved).toArray();
		else {
			ints = IntStream.rangeClosed(0, solution2.getNumberOfTours()-1).toArray();
		}
		System.out.println("generated ints" + Arrays.toString(ints));
		Arrays.sort(ints);
		System.out.println("generated ints" + Arrays.toString(ints));
		List<Customer> customers = new ArrayList<Customer>();
		int numberOfToursRemoved = 0;
		for (int i : ints) {
			System.out.println("deleting Tour " + i + "; Solution has " + solution2.getNumberOfTours() + " tours.");
			customers.addAll(solution2.deleteTour(i-numberOfToursRemoved));
			System.out.println("deleted Tour " + i);
			numberOfToursRemoved++;
		}
		new GreedyInsertion().insertCustomers(solution2, customers);
		return solution2;
	}
	
	private static boolean isNumberOfToursInSolutionIsMuchLargerThanNumberOfToursToBeRemoved(SolutionGot solution2, int numberOfToursToBeRemoved) {
		return (solution2.getNumberOfTours() >= 2*numberOfToursToBeRemoved);
	}

	public static SolutionGot findSolutionWithTargetNumber(SolutionGot solution, int targetVehicleNumber2) throws IOException {
		System.out.println("starting TS to find Solution with targetVehicleNumber" + targetVehicleNumber2);
		boolean flag = Parameters.isTourMinimizationPhase();
		boolean flag2 = Parameters.isPublishSolutionAtEndOfTabuSearch();
		
		int vehicleNumberStart = solution.getNumberOfTours();
		Parameters.setTourMinimizationPhase(true);
		Parameters.setPublishSolutionAtEndOfTabuSearch(false);
		TSWithTourElimination ts = new TSWithTargetVehicleNumber(targetVehicleNumber2);
		solution = (SolutionGot) ts.improve(solution);
		System.out.println("finished TS to find Solution with targetVehicleNumber " + targetVehicleNumber2 + "; vehicle number in best solution found by TS: " + solution.getNumberOfTours() + ". Startet with " + vehicleNumberStart + " vehicles in starting solution.");
		Parameters.setTourMinimizationPhase(flag);
		Parameters.setPublishSolutionAtEndOfTabuSearch(flag2);
		return solution;	
	}
	
	public static SolutionGot findSolutionWithLowerOrEqualThanTargetNumber(SolutionGot solution, int targetVehicleNumber2) throws IOException {
		System.out.println("starting TS to find Solution with targetVehicleNumber" + targetVehicleNumber2);
		boolean flag = Parameters.isTourMinimizationPhase();
		boolean flag2 = Parameters.isPublishSolutionAtEndOfTabuSearch();
		
		int vehicleNumberStart = solution.getNumberOfTours();
		Parameters.setTourMinimizationPhase(true);
		Parameters.setPublishSolutionAtEndOfTabuSearch(false);
		TSWithTourElimination ts = new TSWithTargetVehicleNumber(targetVehicleNumber2){
			@Override
			protected boolean isStoppingCriterionMet() {
				if (solution.getNumberOfTours() <= targetVehicleNumber)
					return true;
				if (Parameters.isRunningTimeReached())
					return true;
				return iterationsWithoutImprovement > maxNumberIterationsWithoutImprovement;
			}
		};
		solution = (SolutionGot) ts.improve(solution);
		System.out.println("finished TS to find Solution with targetVehicleNumber " + targetVehicleNumber2 + "; vehicle number in best solution found by TS: " + solution.getNumberOfTours() + ". Startet with " + vehicleNumberStart + " vehicles in starting solution.");
		Parameters.setTourMinimizationPhase(flag);
		Parameters.setPublishSolutionAtEndOfTabuSearch(flag2);
		return solution;	
	}

	
	
}
