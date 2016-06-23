package de.rwth.lofip.library.solver.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import de.rwth.lofip.library.Customer;
import de.rwth.lofip.library.SolutionGot;
import de.rwth.lofip.library.Tour;
import de.rwth.lofip.library.interfaces.ElementWithTours;
import de.rwth.lofip.library.parameters.Parameters;
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
		while (!(solution.getNumberOfTours() == vehicleGoalNumber)) {
			
			iterations++;
			System.out.println("createSolutionWithTargetVehicleNumber " + vehicleGoalNumber + "; iteration = " + iterations);
			if (iterations > 1000)
				throw new RuntimeException("No Solution with " + vehicleGoalNumber + "found.");
			
			if (numberOfToursToBeRemoved < solution.getNumberOfTours())
				numberOfToursToBeRemoved++;
			else
				numberOfToursToBeRemoved = 1;
//				solution.addTourToLastOrNewGot(new Tour(solution.getVrpProblem().getDepot(), solution.getVrpProblem().getNewVehicle()));				
			perturb(solution, numberOfToursToBeRemoved);
			if (solution.getVehicleCount() > vehicleGoalNumber)
				solution = findSolutionWithTargetNumber(solution, vehicleGoalNumber);
		}		
		System.out.println("#KFZ nach Perturb:" + solution.getVehicleCount());
		return solution;
	}
	
	private static SolutionGot perturb(SolutionGot solution2, int numberOfToursToBeRemoved) {
		final int[] ints = random.ints(0, solution2.getNumberOfTours()-1).distinct().limit(numberOfToursToBeRemoved).toArray();
		Arrays.sort(ints);
		List<Customer> customers = new ArrayList<Customer>();
		int numberOfToursRemoved = 0;
		for (int i : ints) {
			customers.addAll(solution2.deleteTour(i-numberOfToursRemoved));
			numberOfToursRemoved++;
		}
		new GreedyInsertion().insertCustomers(solution2, customers);
		return solution2;
	}
	
	private static SolutionGot findSolutionWithTargetNumber(SolutionGot solution, int targetVehicleNumber2) throws IOException {
		boolean flag = Parameters.isTourMinimizationPhase();		
		Parameters.setTourMinimizationPhase(true);
		TSWithTourElimination ts = new TSWithTargetVehicleNumber(targetVehicleNumber2);
		solution = (SolutionGot) ts.improve(solution);
		Parameters.setTourMinimizationPhase(flag);
		return solution;	
	}
	
	
}
